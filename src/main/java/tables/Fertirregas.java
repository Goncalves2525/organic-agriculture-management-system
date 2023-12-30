package tables;

public class Fertirregas {

    // FERTIRREGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, RECEITAID, horaInicio)
    private int operacaoId;
    private double quantidade;
    private String unidadeMedida;
    private int setor;
    private int receitaId;
    private String horaInicio;

    // CONSTRUCTORS
    public Fertirregas(int operacaoId, double quantidade, String unidadeMedida, int setor, int receitaId) {
        this.operacaoId = operacaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.setor = setor;
        this.receitaId = receitaId;
    }

    public Fertirregas(int operacaoId, double quantidade, String unidadeMedida, int setor, int receitaId, String horaInicio) {
        this.operacaoId = operacaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.setor = setor;
        this.receitaId = receitaId;
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

    public int getReceitaId() {
        return receitaId;
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
        // INSERT INTO FERTIRREGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, RECEITAID) VALUES (?, ?, ?, ?, ?);
        if (this.getHoraInicio() == null) {
            System.out.println("INSERT INTO FERTIRREGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, RECEITAID) VALUES (" +
                    this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ", " + this.receitaId + ");");
        } else {
            // INSERT INTO FERTIRREGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, RECEITAID, horaInicio) VALUES (?, ?, ?, ?, ?, ?);
            System.out.println("INSERT INTO FERTIRREGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, RECEITAID, horaInicio) VALUES (" +
                    this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ", " + this.receitaId + ", '" + this.horaInicio + "');");
        }
    }

    public String append() {
        if (this.getHoraInicio() == null) {
            String text = "INSERT INTO FERTIRREGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, RECEITAID) VALUES (" +
                    this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ", " + this.receitaId + ");";
            return text;
        } else {
            String text = "INSERT INTO FERTIRREGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, RECEITAID, horaInicio) VALUES (" +
                    this.operacaoId + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + this.setor + ", " + this.receitaId + ", '" + this.horaInicio + "');";
            return text;
        }
    }
}
