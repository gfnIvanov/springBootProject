window.onmessage = function(event){
    const pages = document.querySelectorAll('[class$="-page"]');
    if (event.data.action == 'changePage') {
        let usersPage = Array.from(pages).find(page => {
            return page.classList.contains('users-page');
        });
        usersPage.setAttribute('src', `users/${event.data.page}`);
    }
};

const showHideSidebar = function() {
    const workSpace = document.querySelector('.work-space');
    const sbFull = document.querySelector('.sb-full');
    const sbMini = document.querySelector('.sb-mini');
    const pages = document.querySelectorAll('[class$="-page"]');
    if (sbMini.classList.contains('invisible')) {
        workSpace.classList.remove('indent-292');
        workSpace.classList.add('indent-92');
        sbFull.classList.add('invisible');
        sbMini.classList.remove('invisible');
        pages.forEach(page => page.style.width = 'calc(100% - 92px)');
        return;
    }
    workSpace.classList.remove('indent-92');
    workSpace.classList.add('indent-292');
    sbMini.classList.add('invisible');
    sbFull.classList.remove('invisible');
    pages.forEach(page => page.style.width = 'calc(100% - 292px)');
};

const showPage = function(pageName) {
    const pages = document.querySelectorAll('[class$="-page"]');
    pages.forEach(page => {
        if (page.classList.contains(`${pageName}-page`)) {
            page.style.display = '';
        } else {
            page.style.display = 'none';
        }
    });
};
