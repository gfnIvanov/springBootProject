let addForm;

document.addEventListener("DOMContentLoaded", () => {
    addForm = document.getElementById('add-form');
    addForm.addEventListener('submit', handleFormSubmit);
    increaseForm = document.getElementById('increase-product-form');
    reduceForm = document.getElementById('reduce-product-form');
});

const setInputColor = function(select) {
    const color = select.value !== '' ? 'white' : '#999';
    select.style.color = color;
};

const showModal = function(dom, show, reduce) {
    if (reduce) {
        const modalReduce = document.getElementById('modal-reduce');
        modalReduce.style.display = show ? '' : 'none';
        if (show) {
            const str = dom.parentNode.parentNode;
        }
        return;
    }
    const modalIncrease = document.getElementById('modal-increase');
    modalIncrease.style.display = show ? '' : 'none';
    if (show) {
        const str = dom.parentNode.parentNode;
        increaseForm[0].value = str.children[0].innerText;
    }
};