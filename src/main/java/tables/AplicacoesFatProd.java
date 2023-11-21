package tables;

public class AplicacoesFatProd {

    // APLICACOES_FATPROD(OPERACAOID, FATORPRODID, quantidade, UNIDADEMEDIDA)
    private int operacaoId;
    private String fatorProducaoId;
    private double quantidade;
    private String unidadeMedida;

    // CONSTRUCTORS
    public AplicacoesFatProd(int operacaoId, String fatorProducaoId, double quantidade, String unidadeMedida) {
        this.operacaoId = operacaoId;
        this.fatorProducaoId = fatorProducaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
    }

    // GETTERS
    public int getOperacaoId() {
        return operacaoId;
    }

    public String getFatorProducaoId() {
        return fatorProducaoId;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }


    // FUNCTIONS
    public void print() {
        // INSERT INTO APLICACOES_FATPROD(OPERACAOID, FATORPRODID, quantidade, UNIDADEMEDIDA) VALUES (?, ?, ?, ?);
        System.out.println("INSERT INTO APLICACOES_FATPROD(OPERACAOID, FATORPRODID, quantidade, UNIDADEMEDIDA) VALUES (" +
                this.operacaoId + ", '" + this.fatorProducaoId + "', " + this.quantidade + ", '" + this.unidadeMedida + "');");
    }

    public String append() {
        String text = "INSERT INTO APLICACOES_FATPROD(OPERACAOID, FATORPRODID, quantidade, UNIDADEMEDIDA) VALUES (" +
                this.operacaoId + ", '" + this.fatorProducaoId + "', " + this.quantidade + ", '" + this.unidadeMedida + "');";
        return text;
    }

    @Override
    public String toString() {
        return "AplicacoesFatProd{" +
                "operacaoId=" + operacaoId +
                ", fatorProducaoId='" + fatorProducaoId + '\'' +
                ", quantidade=" + quantidade +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                '}';
    }
}
