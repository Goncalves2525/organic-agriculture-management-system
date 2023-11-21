package tables;

public class Operacoes {

    // OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio, dataFim)
    private int idOperacao;
    private int quintaId;
    private int parcelaId;
    private int culturaId;
    private int operadorId;
    private String dataInicio;
    private String dataFim;


    // CONSTRUCTORS
    public Operacoes(int idOperacao, int quintaId, int parcelaId, int culturaId, int operadorId, String dataInicio, String dataFim) {
        this.idOperacao = idOperacao;
        this.quintaId = quintaId;
        this.parcelaId = parcelaId;
        this.culturaId = culturaId;
        this.operadorId = operadorId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    // GETTERS
    public int getIdOperacao() {
        return idOperacao;
    }

    public int getQuintaId() {
        return quintaId;
    }

    public int getParcelaId() {
        return parcelaId;
    }

    public int getCulturaId() {
        return culturaId;
    }

    public int getOperadorId() {
        return operadorId;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }


    // FUNCTIONS
    public void print() {
        String dataFimPrint = null;
        String setorPrint = null;

        if (dataFim != null) {
            dataFimPrint = "TO_DATE('" + this.dataFim.split("T")[0] + "', 'YYYY-MM-DD')";
        }

        // INSERT INTO OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio, dataFim) VALUES (?, ?, ?, ?, ?, ?, ?);
        System.out.println("INSERT INTO OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio, dataFim) VALUES (" +
                this.idOperacao + ", " + this.quintaId + ", " + this.parcelaId + ", " + this.culturaId + ", " + this.operadorId +
                ", TO_DATE('" + this.dataInicio.split("T")[0] + "', 'YYYY-MM-DD'), " + dataFimPrint + ");");
    }

    public String append(){
        String dataFimPrint = null;
        String setorPrint = null;

        if (dataFim != null) {
            dataFimPrint = "TO_DATE('" + this.dataFim.split("T")[0] + "', 'YYYY-MM-DD')";
        }

        String text = "INSERT INTO OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio, dataFim) VALUES (" +
                this.idOperacao + ", " + this.quintaId + ", " + this.parcelaId + ", " + this.culturaId + ", " + this.operadorId +
                ", TO_DATE('" + this.dataInicio.split("T")[0] + "', 'YYYY-MM-DD'), " + dataFimPrint + ");";
        return text;
    }
}
