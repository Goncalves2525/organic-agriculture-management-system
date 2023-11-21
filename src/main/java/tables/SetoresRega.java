package tables;

public class SetoresRega {

    // SETORES_REGA(idSetor, QUINTAID, EDIFICIOID, dataInicio, dataFim, caudalMaximo, UNIDADEMEDIDA)
    private int idSetor;
    private int quintaId;
    private int edificioId;
    private String dataInicio;
    private String dataFim;
    private double caudalMaximo;
    private String unidadeMedida;

    // CONSTRUCTORS
    public SetoresRega(int idSetor, int quintaId, int edificioId, String dataInicio, String dataFim, double caudalMaximo, String unidadeMedida) {
        this.idSetor = idSetor;
        this.quintaId = quintaId;
        this.edificioId = edificioId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.caudalMaximo = caudalMaximo;
        this.unidadeMedida = unidadeMedida;
    }

    // GETTERS
    public int getIdSetor() {
        return idSetor;
    }

    public int getQuintaId() {
        return quintaId;
    }

    public int getEdificioId() {
        return edificioId;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public double getCaudalMaximo() {
        return caudalMaximo;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    // FUNCTIONS
    public void print() {
        String dataFimPrint = null;

        if (dataFim != null) {
            dataFimPrint = "TO_DATE('" + this.dataFim.split("T")[0] + "', 'YYYY-MM-DD')";
        }

        // INSERT INTO SETORES_REGA(idSetor, QUINTAID, EDIFICIOID, dataInicio, dataFim, caudalMaximo, UNIDADEMEDIDA) VALUES (?, ?, ?, ?, ?, ?, ?);
        System.out.println("INSERT INTO SETORES_REGA(idSetor, QUINTAID, EDIFICIOID, dataInicio, dataFim, caudalMaximo, UNIDADEMEDIDA) VALUES (" +
                this.idSetor + ", " + this.quintaId + ", " + this.edificioId +
                ", TO_DATE('" + this.dataInicio.split("T")[0] + "', 'YYYY-MM-DD'), " + dataFimPrint +
                ", " + this.caudalMaximo + ", '" + this.unidadeMedida + "');");
    }

    public String append() {
        String dataFimPrint = null;

        if (dataFim != null) {
            dataFimPrint = "TO_DATE('" + this.dataFim.split("T")[0] + "', 'YYYY-MM-DD')";
        }

        String text = "INSERT INTO SETORES_REGA(idSetor, QUINTAID, EDIFICIOID, dataInicio, dataFim, caudalMaximo, UNIDADEMEDIDA) VALUES (" +
                this.idSetor + ", " + this.quintaId + ", " + this.edificioId +
                ", TO_DATE('" + this.dataInicio.split("T")[0] + "', 'YYYY-MM-DD'), " + dataFimPrint +
                ", " + this.caudalMaximo + ", '" + this.unidadeMedida + "');";
        return text;
    }
}
