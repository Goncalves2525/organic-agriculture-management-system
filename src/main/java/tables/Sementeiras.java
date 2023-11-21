package tables;

public class Sementeiras {

    // SEMENTEIRAS(OPERACAOID, quantidade, UNIDADEMEDIDA)
    private int operacaoId;
    private double quantidade;
    private String unidadeMedida;

    // CONSTRUCTORS
    public Sementeiras(int operacaoId, double quantidade, String unidadeMedida) {
        this.operacaoId = operacaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
    }

    // GETTERS
    public int getOperacaoId() {
        return operacaoId;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }


    // FUNCTIONS
    public void print() {
        // INSERT INTO SEMENTEIRAS(OPERACAOID, quantidade, UNIDADEMEDIDA) VALUES (?, ?, ?);
        System.out.println("INSERT INTO SEMENTEIRAS(OPERACAOID, quantidade, UNIDADEMEDIDA) VALUES (" +
                this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "');");
    }

    public String append() {
        String text = "INSERT INTO SEMENTEIRAS(OPERACAOID, quantidade, UNIDADEMEDIDA) VALUES (" +
                this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "');";
        return text;
    }
}
