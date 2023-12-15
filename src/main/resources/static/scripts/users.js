let addForm;
let editForm;

document.addEventListener("DOMContentLoaded", () => {
    addForm = document.getElementById('add-user-form');
    editForm = document.getElementById('edit-user-form');
    addForm.addEventListener('submit', handleFormSubmit);
    editForm.addEventListener('submit', handleFormSubmit);
});

const showAddForm = function(show) {
    const addFormContainer = document.querySelector('.add-user-form');
    const addButton = document.querySelector('.add-btn');
    addFormContainer.style.display = show ? '' : 'none';
    addButton.style.display = show ? 'none' : '';
    if (!show) {
        Array.from(addForm.elements).forEach(elem => {
            elem.value = '';
            elem.classList.remove('required');
        });
    }
};

const handleFormSubmit = function(event) {
    const form = event.target.id === 'add-user-form' ? addForm: editForm;
    event.preventDefault();
    let validForm = true;
    Array.from(form.elements).forEach(elem => {
        if (elem.nodeName !== 'INPUT') return;
        if (elem.value === '') {
            elem.classList.add('required');
            validForm = false;
        }
    });
    if (validForm) form.submit();
};

const showModal = function(dom, show, del) {
    if (del) {
        const modalDel = document.getElementById('modal-del');
        modalDel.style.display = show ? '' : 'none';
        if (show) {
            const str = dom.parentNode.parentNode;
            const idBlock = modalDel.querySelector('[id="id-block"]');
            idBlock.innerText = str.children[0].innerText; 
        }       
        return;
    }
    const modalEdit = document.getElementById('modal-edit');
    modalEdit.style.display = show ? '' : 'none';
    if (show) {
        const str = dom.parentNode.parentNode;
        editForm[0].value = str.children[0].innerText;
        editForm[1].value = str.children[1].innerText;
        editForm[2].value = str.children[2].innerText;
        editForm[3].value = str.children[3].innerText;
    }
};

const unRequired = function(dom) {
    if (dom.value !== '') dom.classList.remove('required');
};

const deleteUser = async function() {
    const userId = modalDel.querySelector('[id="id-block"]').innerText;
    const response = await fetch(`http://localhost:9090/del-user/${userId}`, {
        method: 'POST',
    });
    if (response.status === 200) document.location.reload();
};

const changePage = async function(dom) {
    if (dom.classList.contains('active')) return;
    const page = dom.innerText;
    currentPage = page;
    const response = await fetch(`http://localhost:9090/users/${page}`);
    if (response.status === 200) {
        window.top.postMessage({ action: 'changePage', page }, '*');
        document.location.reload();
    }
};