package tables;

public class EspeciesVegetais {

    // ESPECIES_VEGETAIS(idEspecieVegetal, especie, nomeComum)

    private int idEspecieVegetal;
    private String especie;
    private String nomeComum;

    // CONSTRUCTORS

    public EspeciesVegetais(int idEspecieVegetal, String especie, String nomeComum) {
        this.idEspecieVegetal = idEspecieVegetal;
        this.especie = especie;
        this.nomeComum = nomeComum;
    }

    // GETTERS

    public int getIdEspecieVegetal() {
        return idEspecieVegetal;
    }

    public String getEspecie() {
        return especie;
    }

    public String getNomeComum() {
        return nomeComum;
    }

    // FUNCTIONS

    public void print(){
        // INSERT INTO ESPECIES_VEGETAIS(idEspecieVegetal, especie, nomeComum) VALUES (?, ?, ?);
        System.out.println("INSERT INTO ESPECIES_VEGETAIS(idEspecieVegetal, especie, nomeComum) VALUES (" +
                this.idEspecieVegetal+", '"+this.especie+"', '"+this.nomeComum+"');");
    }

    public String append(){
        String text = "INSERT INTO ESPECIES_VEGETAIS(idEspecieVegetal, especie, nomeComum) VALUES (" +
                this.idEspecieVegetal+", '"+this.especie+"', '"+this.nomeComum+"');";
        return text;
    }
}
