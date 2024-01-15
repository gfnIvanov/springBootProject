const showAddForm = function(show) {
    const addFormContainer = document.querySelector('.add-form');
    const addButton = document.querySelector('.add-btn');
    addFormContainer.style.display = show ? '' : 'none';
    addButton.style.display = show ? 'none' : '';
    const addForm = addFormContainer.firstElementChild;
    if (!show) {
        Array.from(addForm.elements).forEach(elem => {
            elem.value = '';
            elem.nodeName === 'SELECT' && setInputColor(elem);
            elem.classList.remove('required');
        });
    }
};

const checkVarExist = function(varObj) {
    return typeof(varObj !== 'undefined') ? varObj : undefined;
}

const handleFormSubmit = function(event) {
    const form = event.target;
    event.preventDefault();
    let validForm = true;
    Array.from(form.elements).forEach(elem => {
        if (elem.nodeName !== 'INPUT' && elem.nodeName !== 'SELECT') return;
        if (elem.value === '') {
            elem.classList.add('required');
            validForm = false;
        }
    });
    if (validForm) form.submit();
};

const intOnly = function(event) {
    isNaN(event.key) && event.preventDefault();
}