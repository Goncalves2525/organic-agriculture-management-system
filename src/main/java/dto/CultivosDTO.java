package dto;

public class CultivosDTO {

    private int parcelaid;
    private String parcela;
    private int culturaid;
    private String cultura;
    private String produto;
    private double quantidade;
    private String unidadeColheita = "un";

    private String unidadeMonda = "ha";
    private String dataColheita;

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

    public String getUnidadeColheita() {
        return unidadeColheita;
    }

    public String getUnidadeMonda() {
        return unidadeMonda;
    }

    public String getDataColheita() {
        return dataColheita;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public void setUnidadeColheita(String unidadeColheita) {
        this.unidadeColheita = unidadeColheita;
    }

    public void setDataColheita(String dataColheita) {
        this.dataColheita = dataColheita;
    }

    public void setUnidadeMonda(String unidadeMonda) {
        this.unidadeMonda = unidadeMonda;
    }

    public CultivosDTO(int parcelaid, String parcela, int culturaid, String cultura, String produto, String unidadeColheita) {
        this.parcelaid = parcelaid;
        this.parcela = parcela;
        this.culturaid = culturaid;
        this.cultura = cultura;
        this.produto = produto;
        this.quantidade = 0;
        this.unidadeColheita = unidadeColheita;
        this.dataColheita = null;
    }
    public CultivosDTO(int parcelaid, String parcela, int culturaid, String cultura, String produto) {
        this.parcelaid = parcelaid;
        this.parcela = parcela;
        this.culturaid = culturaid;
        this.cultura = cultura;
        this.produto = produto;
        this.quantidade = 0;
        this.dataColheita = null;
    }

    @Override
    public String toString() {
        return "CULTIVO DE " + produto + " | de (" +
                parcelaid + ") " + parcela + ", (" +
                culturaid + ") " + cultura + ".";
    }
}
