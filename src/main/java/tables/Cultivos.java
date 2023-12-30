package tables;

public class Cultivos {

    // CULTIVOS(QUINTAID, PARCELAID, CULTURAID, dataInicio, dataFim, quantidade, UNIDADESMEDIDAID, SETORREGAID)
    private int quintaId;
    private int parcelaId;
    private int culturaId;
    private String dataInicio;
    private String dataFim;
    private double quantidade;
    private String unidadeMedida;
    private int setor;

    // CONSTRUCTORS
    public Cultivos(int quintaId, int parcelaId, int culturaId, String dataInicio, String dataFim, double quantidade, String unidadeMedida, int setor) {
        this.quintaId = quintaId;
        this.parcelaId = parcelaId;
        this.culturaId = culturaId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.setor = setor;
    }

    public Cultivos(int quintaId, int parcelaId, int culturaId, String dataInicio, String dataFim, double quantidade, double compasso, double distancia, int setor) {
        this.quintaId = quintaId;
        this.parcelaId = parcelaId;
        this.culturaId = culturaId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidade = (compasso * distancia * 0.0001) * quantidade;
        this.unidadeMedida = "ha";
        this.setor = setor;
    }

    // GETTERS
    public int getQuintaId() {
        return quintaId;
    }

    public int getParcelaId() {
        return parcelaId;
    }

    public int getCulturaId() {
        return culturaId;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
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
        String dataInicioPrint = "TO_DATE('2015-01-01', 'YYYY-MM-DD')";
        String dataFimPrint = null;
        String setorPrint = null;

        if (dataInicio != null) {
            dataInicioPrint = "TO_DATE('" + this.dataInicio.split("T")[0] + "', 'YYYY-MM-DD')";
        }

        if (dataFim != null) {
            dataFimPrint = "TO_DATE('" + this.dataFim.split("T")[0] + "', 'YYYY-MM-DD')";
        }

        if (setor != 0) {
            setorPrint = String.valueOf(setor);
        }

        // INSERT INTO CULTIVOS(QUINTAID, PARCELAID, CULTURAID, dataInicio, dataFim, quantidade, UNIDADESMEDIDAID, SETORREGAID) VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        System.out.println("INSERT INTO CULTIVOS(QUINTAID, PARCELAID, CULTURAID, dataInicio, dataFim, quantidade, UNIDADEMEDIDA, SETORREGAID) VALUES (" +
                this.quintaId + ", " + this.parcelaId + ", " + this.culturaId + ", " + dataInicioPrint + ", " +
                dataFimPrint + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + setorPrint + ");");
    }

    public String append() {
        String dataInicioPrint = "TO_DATE('2015-01-01', 'YYYY-MM-DD')";
        String dataFimPrint = null;
        String setorPrint = null;

        if (dataInicio != null) {
            dataInicioPrint = "TO_DATE('" + this.dataInicio.split("T")[0] + "', 'YYYY-MM-DD')";
        }

        if (dataFim != null) {
            dataFimPrint = "TO_DATE('" + this.dataFim.split("T")[0] + "', 'YYYY-MM-DD')";
        }

        if (setor != 0) {
            setorPrint = String.valueOf(setor);
        }

        String text = "INSERT INTO CULTIVOS(QUINTAID, PARCELAID, CULTURAID, dataInicio, dataFim, quantidade, UNIDADEMEDIDA, SETORREGAID) VALUES (" +
                this.quintaId + ", " + this.parcelaId + ", " + this.culturaId + ", " + dataInicioPrint + ", " +
                dataFimPrint + ", " + this.quantidade + ", '" + this.unidadeMedida + "', " + setorPrint + ");";
        return text;
    }
}
