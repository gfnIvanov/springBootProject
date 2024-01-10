package com.example.javaspringlearn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/")
    public String viewHomePage(Model model, @Param("login") String login) {
        Users user = userService.getWithLogin("admin");
        model.addAttribute("user", user);
        model.addAttribute("login", login);
        return "index";
    }

    @GetMapping("/home")
    public String viewHomePage() {
        return "home";
    }

    @GetMapping("/store")
    public String viewStorePage(Model model) {
        Products product = new Products();
        ProductData productData = new ProductData();
        ArrayList<ProductData> productDataList = new ArrayList<>();
        List<Products> listProducts = productsService.listAll();
        for (int i = 0; i < listProducts.size(); i++) {
            Long id = listProducts.get(i).getId();
            productData.setId(id);
            productData.setName(listProducts.get(i).getName());
            productData.setCategory(listProducts.get(i).getCategory());
            productData.setDateCreate(listProducts.get(i).getDateCreate());
            productData.setActualQuant(productsService.getActualQuant(id));
            productDataList.add(i, productData);
        }
        List<Categories> listCategories = categoriesService.listAll();
        model.addAttribute("product", product);
        model.addAttribute("listProducts", productDataList);
        model.addAttribute("listCategories", listCategories);
        return "store";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product") Products product) {
        productsService.save(product);
        return "redirect:store";
    }

    @GetMapping("/users/{page}")
    public String viewUsersPage(Model model, @PathVariable(name="page") int page) {
        double slice = 5.0;
        Users user = new Users();
        List<Users> listUsers = userService.listAll();
        int pagesCount = (int)Math.ceil((float)listUsers.size() / slice);
        Page[] pages = new Page[pagesCount];
        for (int i = 0; i < pagesCount; i++) {
            int current = i + 1;
            pages[i] = new Page(current, current == page ? "active" : "");
        }
        double remainder = listUsers.size() % slice;
        remainder = remainder == 0 ? 5 : remainder;
        double sliceLength = page == pagesCount ? remainder : slice;
        Users[] listUsersSlice = new Users[(int)sliceLength];
        double stopInd = page == pagesCount ? listUsers.size() : slice * page;
        double startInd = page == pagesCount ? stopInd - remainder : stopInd - slice;
        int ind = 0;
        for (int i = (int)startInd; i < stopInd; i++) {
            listUsersSlice[ind] = listUsers.get(i);
            ind++;
        }
        model.addAttribute("user", user);
        model.addAttribute("listUsers", listUsersSlice);
        model.addAttribute("pages", pages);
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

    @PostMapping("/increase-quant")
    public String increaseQuant(@ModelAttribute("user") Users user) {
        userService.save(user);
        return "redirect:users/1";
    }

    @PostMapping("/reduce-quant")
    public String reduceQuant(@ModelAttribute("user") Users user) {
        userService.save(user);
        return "redirect:users/1";
    }
}
