package tables;

public class Regas {

    // REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR)
    private int operacaoId;
    private double quantidade;
    private String unidadeMedida;
    private int setor;
    private String horaInicio;

    // CONSTRUCTORS
    public Regas(int operacaoId, double quantidade, String unidadeMedida, int setor) {
        this.operacaoId = operacaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.setor = setor;
    }

    public Regas(int operacaoId, double quantidade, String unidadeMedida, int setor, String horaInicio) {
        this.operacaoId = operacaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.setor = setor;
        this.horaInicio = horaInicio;
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

    public String getHoraInicio() {
        return horaInicio;
    }

    // SETTERS
    public void setSetor(int setor) {
        this.setor = setor;
    }

    // FUNCTIONS
    public void print() {
        // INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR) VALUES (?, ?, ?, ?);
        if (this.getHoraInicio() == null) {
            System.out.println("INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR) VALUES (" +
                    this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ");");
        } else {
            // INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, horaInicio) VALUES (?, ?, ?, ?, ?);
            System.out.println("INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, horaInicio) VALUES (" +
                    this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ", '" + this.horaInicio + "');");
        }
    }

    public String append() {
        if (this.getHoraInicio() == null) {
            String text = "INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR) VALUES (" +
                    this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ");";
            return text;
        } else {
            String text = "INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, horaInicio) VALUES (" +
                    this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ", '" + this.horaInicio + "');";
            return text;
        }
    }
}
