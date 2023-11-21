package tables;

public enum ProdutoDaEspecie {

    ABOBORA_MANTEIGA("ABOBORA MANTEIGA", "ABOBORA MANTEIGA"),
    AMEIXOEIRA("AMEIXOEIRA",	"AMEIXA"),
    DAMASQUEIRO("DAMASQUEIRO", "DAMASCO"),
    MACIEIRA("MACIEIRA",	"MAÇA"),
    PERA_NASHI("PERA NASHI",	"PERA NASHI"),
    CENOURA("CENOURA", "CENOURA"),
    TREMOCO("TREMOÇO", "TREMOÇO"),
    MILHO("MILHO", "MILHO"),
    NABO_GRELEIRO("NABO GRELEIRO", "NABO GRELEIRO"),
    OLIVEIRA("OLIVEIRA", "AZEITONA"),
    NABO("NABO", "NABO"),
    VIDEIRA("VIDEIRA", "UVA");

    private final String especie;
    private final String produto;

    ProdutoDaEspecie(String especie, String produto){
        this.especie = especie;
        this.produto = produto;
    }

    public String getEspecie() {
        return especie;
    }

    public String getProduto() {
        return produto;
    }
}
