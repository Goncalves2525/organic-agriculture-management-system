package tables;

public class Regas {

    // REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR)
    private int operacaoId;
    private double quantidade;
    private String unidadeMedida;
    private int setor;

    // CONSTRUCTORS
    public Regas(int operacaoId, double quantidade, String unidadeMedida, int setor) {
        this.operacaoId = operacaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.setor = setor;
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

    public int getSetor() {
        return setor;
    }

    // SETTERS
    public void setSetor(int setor) {
        this.setor = setor;
    }

    // FUNCTIONS
    public void print() {
        // INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR) VALUES (?, ?, ?, ?);
        System.out.println("INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR) VALUES (" +
                this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ");");
    }

    public String append() {
        String text = "INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR) VALUES (" +
                this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ");";
        return text;
    }
}
