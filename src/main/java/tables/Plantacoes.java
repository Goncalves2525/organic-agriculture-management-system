package tables;

public class Plantacoes {

    // PLANTACOES(OPERACAOID, quantidade, UNIDADEMEDIDA)
    private int operacaoId;
    private double quantidade;
    private String unidadeMedida;

    // CONSTRUCTORS
    public Plantacoes(int operacaoId, double quantidade, String unidadeMedida) {
        this.operacaoId = operacaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
    }

    public Plantacoes(int operacaoId, double quantidade, double compasso, double distancia) {
        this.operacaoId = operacaoId;
        this.quantidade = (compasso * distancia * 0.0001) * quantidade;
        this.unidadeMedida = "ha";
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
        // INSERT INTO PLANTACOES(OPERACAOID, quantidade, UNIDADEMEDIDA) VALUES (?, ?, ?);
        System.out.println("INSERT INTO PLANTACOES(OPERACAOID, quantidade, UNIDADEMEDIDA) VALUES (" +
                this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "');");
    }

    public String append() {
        String text = "INSERT INTO PLANTACOES(OPERACAOID, quantidade, UNIDADEMEDIDA) VALUES (" +
                this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "');";
        return text;
    }
}
