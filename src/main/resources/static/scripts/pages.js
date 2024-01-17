const changePage = async function(dom, template) {
    if (dom.classList.contains('active')) return;
    const page = dom.innerText;
    currentPage = page;
    const response = await fetch(`http://localhost:9090/${template}/${page}`);
    if (response.status === 200) {
        window.top.postMessage({ action: 'changePage', page, template }, '*');
        document.location.reload();
    }
};