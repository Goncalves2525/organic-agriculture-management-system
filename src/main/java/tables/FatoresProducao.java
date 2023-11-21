package tables;

public class FatoresProducao {

    // FATORES_PRODUCAO(idFatorProducao, fabricante, FORMULACAOID, FICHATECNICAID)
    private String nomeComercial;
    private String fabricante;
    private int formulacaoId;
    private int fichaTecnicaId;

    // CONSTRUCTORS
    public FatoresProducao(String nomeComercial, String fabricante, int formulacaoId, int fichaTecnicaId) {
        this.nomeComercial = nomeComercial;
        this.fabricante = fabricante;
        this.formulacaoId = formulacaoId;
        this.fichaTecnicaId = fichaTecnicaId;
    }

    // GETTERS
    public String getNomeComercial() {
        return nomeComercial;
    }

    public String getFabricante() {
        return fabricante;
    }

    public int getFormulacaoId() {
        return formulacaoId;
    }

    public int getFichaTecnicaId() {
        return fichaTecnicaId;
    }

    // FUNCTIONS
    public void print(){
        // INSERT INTO FATORES_PRODUCAO(idFatorProducao, fabricante, FORMULACAOID, FICHATECNICAID) VALUES (?, ?, ?, ?);
        System.out.println("INSERT INTO FATORES_PRODUCAO(idFatorProducao, fabricante, FORMULACAOID, FICHATECNICAID) VALUES ('" +
                this.nomeComercial + "', '" + this.fabricante + "', " + this.formulacaoId + ", " + this.fichaTecnicaId + ");");
    }

    public String append() {
        String text = "INSERT INTO FATORES_PRODUCAO(idFatorProducao, fabricante, FORMULACAOID, FICHATECNICAID) VALUES ('" +
                this.nomeComercial + "', '" + this.fabricante + "', " + this.formulacaoId + ", " + this.fichaTecnicaId + ");";
        return text;
    }
}
