package dto;

public class FatProdDTO {

    String fatProd;
    double quantidadePorHectar;
    String unidadeMedida;

    // acessório
    String fabricante;
    String formato;
    String tipo;
    String aplicacao;

    public FatProdDTO(String fatProd, double quantidadePorHectar, String unidadeMedida) {
        this.fatProd = fatProd;
        this.quantidadePorHectar = quantidadePorHectar;
        this.unidadeMedida = unidadeMedida;
    }

    public FatProdDTO(String fatProd, String fabricante, String formato, String tipo, String aplicacao) {
        this.fatProd = fatProd;
        this.fabricante = fabricante;
        this.formato = formato;
        this.tipo = tipo;
        this.aplicacao = aplicacao;
    }

    public String getFatProd() {
        return fatProd;
    }

    public double getQuantidadePorHectar() {
        return quantidadePorHectar;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public String getFabricante() {
        return fabricante;
    }

    public String getFormato() {
        return formato;
    }

    public String getTipo() {
        return tipo;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    @Override
    public String toString() {
        return fatProd + ", d@ " + fabricante + "\n" +
                "\tFormato: " + formato +
                "; Tipo: " + tipo +
                "; Aplicação: " + aplicacao;
    }
}
