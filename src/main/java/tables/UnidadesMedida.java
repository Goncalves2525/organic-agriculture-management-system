package tables;

public class UnidadesMedida {

    // UNIDADES_MEDIDA(idUnidadeMedida, nomeCompleto)

    private String idUnidadeMedida;
    private String nomeCompleto;

    // CONSTRUCTOR

    public UnidadesMedida(String idUnidadeMedida, String nomeCompleto) {
        this.idUnidadeMedida = idUnidadeMedida;
        this.nomeCompleto = nomeCompleto;
    }

    // Getters

    public String getIdUnidadeMedida() {
        return idUnidadeMedida;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    // Methods

    public void print() {
        // INSERT INTO UNIDADES_MEDIDA(idUnidadeMedida, nome_completo) VALUES (?, ?);
        System.out.println("INSERT INTO UNIDADES_MEDIDA(idUnidadeMedida, nomeCompleto) VALUES ('" +
                this.idUnidadeMedida + "', '" + this.nomeCompleto + "');");
    }

    public String append() {
        String text = "INSERT INTO UNIDADES_MEDIDA(idUnidadeMedida, nomeCompleto) VALUES ('" +
                this.idUnidadeMedida + "', '" + this.nomeCompleto + "');";
        return text;
    }
}

