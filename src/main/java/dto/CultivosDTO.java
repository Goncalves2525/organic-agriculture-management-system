package dto;

import utils.AnsiColor;

public class CultivosDTO {

    private int parcelaid;
    private String parcela;
    private int culturaid;
    private String cultura;
    private String produto;
    private int quantidade;
    private String UNIDADE = "un";
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

    public int getQuantidade() {
        return quantidade;
    }

    public String getUNIDADE() {
        return UNIDADE;
    }

    public String getDataColheita() {
        return dataColheita;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setDataColheita(String dataColheita) {
        this.dataColheita = dataColheita;
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
