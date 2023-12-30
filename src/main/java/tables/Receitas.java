package tables;

public class Receitas {

    // RECEITAS(RECEITAID, FATORPRODID, quantidadePorHectar, UNIDADEMEDIDA)
    private int receitaId;
    private String fatorProdId;
    private double quantidadePorHectar;
    private String unidade;

    // CONSTRUCTORS
    public Receitas(int receitaId, String fatorProdId, double quantidadePorHectar, String unidade) {
        this.receitaId = receitaId;
        this.fatorProdId = fatorProdId;
        this.quantidadePorHectar = quantidadePorHectar;
        this.unidade = unidade;
    }

    // GETTERS
    public int getReceitaId() {
        return receitaId;
    }

    public String getFatorProdId() {
        return fatorProdId;
    }

    public double getQuantidadePorHectar() {
        return quantidadePorHectar;
    }

    public String getUnidade() {
        return unidade;
    }

    // FUNCTIONS
    public void print() {
        // INSERT INTO RECEITAS(RECEITAID, FATORPRODID, quantidadePorHectar, UNIDADEMEDIDA) VALUES (?, ?, ?, ?);
        System.out.println("INSERT INTO RECEITAS(RECEITAID, FATORPRODID, quantidadePorHectar, UNIDADEMEDIDA) VALUES (" +
                this.receitaId + ", '" + this.fatorProdId + "', " + this.quantidadePorHectar + ", '" + this.unidade + "');");
    }

    public String append() {
        String text = "INSERT INTO RECEITAS(RECEITAID, FATORPRODID, quantidadePorHectar, UNIDADEMEDIDA) VALUES (" +
                this.receitaId + ", '" + this.fatorProdId + "', " + this.quantidadePorHectar + ", '" + this.unidade + "');";
        return text;
    }
}
