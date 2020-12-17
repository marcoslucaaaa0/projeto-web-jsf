var accordion = document.getElementsByClassName("ui-accordion-header");




function alterarImagem(){

    if (accordion.classList.contains('ui-state-active')) {
        document.getElementsByClassName("distanciar-imagem").src = "../imgs/scroll.png";
    }
}