package com.example.javaspringlearn;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.persistence.Tuple;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class AppControllers {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private UsersService userService;
    @Autowired
    private OperationsService operationsService;

    private Pagination pagination(int page, float dataSize) {
        double slice = 5.0;
        int pagesCount = (int)Math.ceil(dataSize / slice);
        Page[] pages = new Page[pagesCount];
        for (int i = 0; i < pagesCount; i++) {
            int current = i + 1;
            pages[i] = new Page(current, current == page ? "active" : "");
        }
        double remainder = dataSize % slice;
        remainder = remainder == 0 ? 5 : remainder;
        double sliceLength = page == pagesCount ? remainder : slice;
        double stopInd = page == pagesCount ? dataSize : slice * page;
        double startInd = page == pagesCount ? stopInd - remainder : stopInd - slice;
        return new Pagination(sliceLength, stopInd, startInd, pages);
    }

    @GetMapping("/")
    public String viewHomePage(Model model, @Param("login") String login) {
        Boolean status = Authorization.getInstance().getStatus();
        if (!status) return "redirect:login";
        Users user = userService.getWithLogin("admin");
        model.addAttribute("user", user);
        model.addAttribute("login", login);
        return "index";
    }

    @GetMapping("/login")
    public String viewLoginPage(Model model) {
        model.addAttribute("authForm", new AuthForm());
        return "login";
    }

    @GetMapping("/home")
    public String viewHomePage() {
        return "home";
    }

    @GetMapping(value = {"/store/{page}", "/store/{page}/{category}"})
    public String viewStorePage(Model model,
                                @PathVariable(name="page") int page,
                                @PathVariable(name="category", required = false) Long category) {
        ArrayList<ProductData> productDataList = new ArrayList<>();
        List<Products> listProducts = productsService.listAll();
        int skip = 0;
        for (int i = 0; i < listProducts.size(); i++) {
            Categories currentCategory = listProducts.get(i).getCategory();
            if (category != null && currentCategory.getId() != category) {
                skip = skip + 1;
                continue;
            }
            Long id = listProducts.get(i).getId();
            ProductData productData = new ProductData();
            productData.setId(id);
            productData.setName(listProducts.get(i).getName());
            productData.setCategory(currentCategory);
            productData.setDateCreate(listProducts.get(i).getDateCreate());
            productData.setActualQuant(productsService.getActualQuant(id));
            System.out.println(skip);
            productDataList.add(i - skip, productData);
        }
        Pagination pagination = pagination(page, (float)productDataList.size());
        int ind = 0;
        ProductData[] productDataListSlice = new ProductData[(int)pagination.getSliceLength()];
        if (listProducts.size() > 0 && productDataList.size() > 0) {
            for (int i = (int)pagination.getStartInd(); i < pagination.getStopInd(); i++) {
                productDataListSlice[ind] = productDataList.get(i);
                ind++;
            }
        } else {
            productDataListSlice = new ProductData[0];
        }
        List<Categories> listCategories = categoriesService.listAll();
        model.addAttribute("product", new Products());
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("operationForm", new OperationForm());
        model.addAttribute("listProducts", productDataListSlice);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("productFilterForm", new ProductFilterForm());
        model.addAttribute("pages", pagination.getPages());
        model.addAttribute("noproducts", listProducts.size() == 0 ? "Товары не найдены" : "");
        return "store";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productForm") ProductForm productForm) {
        Products product = new Products();
        product.setName(productForm.getProductName());
        product.setCategory(categoriesService.get(productForm.getCategoryId()));
        System.out.println(product);
        productsService.save(product);
        return "redirect:store/1";
    }

    @GetMapping("/users/{page}")
    public String viewUsersPage(Model model, @PathVariable(name="page") int page) {
        List<Users> listUsers = userService.listAll();
        Pagination pagination = pagination(page, (float)listUsers.size());
        int ind = 0;
        Users[] listUsersSlice = new Users[(int)pagination.getSliceLength()];
        if (listUsers.size() > 0) {
            for (int i = (int)pagination.getStartInd(); i < pagination.getStopInd(); i++) {
                listUsersSlice[ind] = listUsers.get(i);
                ind++;
            }
        } else {
            listUsersSlice = new Users[0];
        }
        model.addAttribute("user", new Users());
        model.addAttribute("listUsers", listUsersSlice);
        model.addAttribute("pages", pagination.getPages());
        model.addAttribute("nousers", listUsers.size() == 0 ? "Пользователи не найдены" : "");
        return "users";
    }

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute("user") Users user) {
        userService.save(user);
        return "redirect:users/1";
    }

    @PostMapping("/del-user/{id}")
    public ResponseEntity<HttpStatus> deleteUser(Model model, @PathVariable(name="id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update-quant")
    public String increaseQuant(@ModelAttribute("operationForm") OperationForm operationForm) {
        Operations operation = new Operations();
        operation.setProduct(productsService.get(operationForm.getProductId()));
        operation.setAction(operationForm.getAction());
        operation.setQuant(operationForm.getQuant());
        operationsService.save(operation);
        return "redirect:store/1";
    }

    @PostMapping("/del-product/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(Model model, @PathVariable(name="id") Long id) {
        operationsService.delByProduct(id);
        productsService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add-categories")
    public ResponseEntity<HttpStatus> addCategories() {
        List<Categories> categories = categoriesService.listAll();
        if (categories.size() == 0) {
            String[] catNames  = {"электроника", "одежда", "продукты"};
            for (int i = 0; i < catNames.length; i++) {
                Categories category = new Categories();
                category.setName(catNames[i]);
                categoriesService.save(category);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/use-product-filter")
    public String useProductFilter(@ModelAttribute("productFilterForm") ProductFilterForm productFilterForm) {
        Long categoryId = productFilterForm.getCategoryId();
        if (categoryId == 0) {
            return "redirect:store/1";
        }
        return "redirect:store/1/" + categoryId;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("authForm") AuthForm authForm) {
        Users user = userService.getWithLogin(authForm.getLogin());
        if (user != null && user.getPassword().equals(authForm.getPassword())) {
            Authorization auth = Authorization.getInstance();
            auth.setStatus(true);
            return "redirect:/";
        }
        return "redirect:login";
    }

    @GetMapping("/get-data-for-graphs")
    public ResponseEntity<String> getDataForGraphs() {
        ArrayList<String> result = new ArrayList<String>();
        List<Tuple> graphData = operationsService.getDataForGraphs();
        for (int i = 0; i < graphData.size(); i++) {
            String str = "{\"name\": \"" + graphData.get(i).get(0) + "\", \"action\": \""
                         + graphData.get(i).get(1) + "\", \"quant\": "
                         + graphData.get(i).get(2) + "}";
            result.add(str);
        }
        JSONArray jresult = new JSONArray(result);
        return new ResponseEntity<>(jresult.toString(), HttpStatus.OK);
    }
}
