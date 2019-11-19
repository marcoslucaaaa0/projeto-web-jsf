package br.com.projetoweb.enums;

public enum GenerosMusicais {
    ROCK("R"),
    Forró("F"),
    MPB("M"),
    Reggae("RE"),
    Sertanejo("S"),
    Eletrônica("E");

    private String sigla;

    GenerosMusicais(String sigla){
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}

