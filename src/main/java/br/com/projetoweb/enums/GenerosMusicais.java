package br.com.projetoweb.enums;

public enum GenerosMusicais {
    ROCK("R"),
    FORRO("F"),
    MPB("M"),
    REGGAE("RE"),
    SERTANEJO("S"),
    ELETRONICA("E"),
    GOSPEL("G"),
    AXE("A"),
    FUNK("F");

    private String sigla;

    GenerosMusicais(String sigla){
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }

    public static String retonarGeneroMusical(String sigla) {
        for (GenerosMusicais generos : GenerosMusicais.values()) {
            if (generos.getSigla().equals(sigla))
                return generos.name();

        }

        return "não existe sigla";
    }
}

