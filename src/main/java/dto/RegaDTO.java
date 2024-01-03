package dto;

public class RegaDTO {
    private int parcelaid;
    private String parcela;
    private int culturaid;
    private String cultura;
    private String produto;
    private double quantidade;
    private String unidadeRega = "min";
    private int setorId;
    private String dataColheita;
    private String horaInicio;

    // Constructors
    public RegaDTO() {
        // Default constructor
    }

    public RegaDTO(int parcelaid, String parcela, int culturaid, String cultura, String produto,
                   double quantidade, String unidadeRega, int setorId, String dataColheita, String horaInicio) {
        this.parcelaid = parcelaid;
        this.parcela = parcela;
        this.culturaid = culturaid;
        this.cultura = cultura;
        this.produto = produto;
        this.quantidade = quantidade;
        this.unidadeRega = unidadeRega;
        this.setorId = setorId;
        this.dataColheita = dataColheita;
        this.horaInicio = horaInicio;
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

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public void setUnidadeRega(String unidadeRega) {
        this.unidadeRega = unidadeRega;
    }

    public void setSetorId(int setorId) {
        this.setorId = setorId;
    }

    public void setDataColheita(String dataColheita) {
        this.dataColheita = dataColheita;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    // Getters
    public int getParcelaid() {
        return parcelaid;
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

    public double getQuantidade() {
        return quantidade;
    }

    public String getUnidadeRega() {
        return unidadeRega;
    }

    public int getSetorId() {
        return setorId;
    }

    public String getDataColheita() {
        return dataColheita;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    // toString method
    @Override
    public String toString() {
        return "RegaDTO{" +
                "parcelaid=" + parcelaid +
                ", parcela='" + parcela + '\'' +
                ", culturaid=" + culturaid +
                ", cultura='" + cultura + '\'' +
                ", produto='" + produto + '\'' +
                ", quantidade=" + quantidade +
                ", unidadeRega='" + unidadeRega + '\'' +
                ", setorId=" + setorId +
                ", dataColheita='" + dataColheita + '\'' +
                ", horaInicio='" + horaInicio + '\'' +
                '}';
    }
}
