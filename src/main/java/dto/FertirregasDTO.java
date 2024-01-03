package dto;

public class FertirregasDTO {

    private int parcelaid;
    private String parcela;
    private int culturaid;
    private String cultura;
    private String produto;

    private String dataInicio;

    private int operacaoId;
    private double quantidade;
    private String unidadeMedida;
    private int setor;
    private int receitaId;
    private String horaInicio;

    // Constructors

    public FertirregasDTO(int parcelaid, String parcela, int culturaid, String cultura, String produto, String dataInicio,
                          int operacaoId, double quantidade, String unidadeMedida, int setor, int receitaId, String horaInicio) {
        this.parcelaid = parcelaid;
        this.parcela = parcela;
        this.culturaid = culturaid;
        this.cultura = cultura;
        this.produto = produto;
        this.dataInicio = dataInicio;
        this.operacaoId = operacaoId;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.setor = setor;
        this.receitaId = receitaId;
        this.horaInicio = horaInicio;
    }

    public FertirregasDTO() {
        // Empty constructor
    }

    // Getters

    public int getParcelaid() {
        return parcelaid;
    }

    public String getDataInicio(){
        return dataInicio;
    }

    public String getParcela() {
        return parcela;
    }

    public int getCulturaid() {
        return culturaid;
    }

    public String getCultura() {
        return cultura;
    }

    public String getProduto() {
        return produto;
    }

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

    // Setters

    public void setParcelaid(int parcelaid) {
        this.parcelaid = parcelaid;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public void setCulturaid(int culturaid) {
        this.culturaid = culturaid;
    }

    public void setCultura(String cultura) {
        this.cultura = cultura;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public void setOperacaoId(int operacaoId) {
        this.operacaoId = operacaoId;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public void setSetor(int setor) {
        this.setor = setor;
    }

    public void setReceitaId(int receitaId) {
        this.receitaId = receitaId;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setDataInicio(String dataInicio){
        this.dataInicio = dataInicio;
    }
}