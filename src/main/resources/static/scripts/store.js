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

const showModal = function(dom, show, type) {
    if (type === 'del') {
        const modalDel = document.getElementById('modal-del');
        modalDel.style.display = show ? '' : 'none';
        if (show) {
            const str = dom.parentNode.parentNode;
            const idBlock = modalDel.querySelector('[id="id-block"]');
            idBlock.innerText = str.children[0].innerText;
        }
        return;
    }
    const modal = document.getElementById(`modal-${type}`);
    modal.style.display = show ? '' : 'none';
    if (show) {
        const str = dom.parentNode.parentNode;
        increaseForm[0].value = str.children[0].innerText;
    }
};