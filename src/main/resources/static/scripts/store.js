let addProductForm;
let increaseForm;
let reduceForm;

document.addEventListener("DOMContentLoaded", () => {
    addProductForm = document.getElementById('add-product-form');
    addProductForm.addEventListener('submit', handleFormSubmit);
    increaseForm = document.getElementById('increase-product-form');
    increaseForm.addEventListener('submit', handleFormSubmit);
    reduceForm = document.getElementById('reduce-product-form');
    reduceForm.addEventListener('submit', handleFormSubmit);
});

const setInputColor = function(select) {
    const color = select.value !== '' ? 'white' : '#999';
    select.style.color = color;
};

const showModal = function(dom, show, type) {
    const modal = document.getElementById(`modal-${type}`);
    modal.style.display = show ? '' : 'none';
    if (show) {
        const str = dom.parentNode.parentNode;
        if (type === 'del') {
            const idBlock = modal.querySelector('[id="id-block"]');
            idBlock.innerText = str.children[0].innerText;
            return;
        }
        const form = type === 'increase' ? increaseForm : reduceForm;
        form[0].value = str.children[0].innerText;
        form[1].value = type === 'increase' ? 'additional' : 'write-off';
    }
};

const deleteProduct = async function() {
    const modalDel = document.getElementById('modal-del');
    const productId = modalDel.querySelector('[id="id-block"]').innerText;
    const response = await fetch(`http://localhost:9090/del-product/${productId}`, {
        method: 'POST',
    });
    if (response.status === 200) document.location.reload();
};

const useProductFilter = async function() {
    await fetch(`http://localhost:9090/use-product-filter/1`, {
        method: 'POST',
    });
    // if (response.status === 200) document.location.reload();
};
