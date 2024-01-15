let addUserForm;
let editUserForm;

document.addEventListener("DOMContentLoaded", () => {
    addUserForm = document.getElementById('add-user-form');
    editUserForm = document.getElementById('edit-user-form');
    addUserForm.addEventListener('submit', handleFormSubmit);
    editUserForm.addEventListener('submit', handleFormSubmit);
});

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
        editUserForm[0].value = str.children[0].innerText;
        editUserForm[1].value = str.children[1].innerText;
        editUserForm[2].value = str.children[2].innerText;
        editUserForm[3].value = str.children[3].innerText;
    }
};

const deleteUser = async function() {
    const modalDel = document.getElementById('modal-del');
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