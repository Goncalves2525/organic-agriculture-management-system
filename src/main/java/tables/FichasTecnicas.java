package tables;

public class FichasTecnicas {

    // FICHAS_TECNICAS(idFichaTecnica)
    private int idFichaTecnica;

    // CONSTRUCTORS
    public FichasTecnicas(int idFichaTecnica) {
        this.idFichaTecnica = idFichaTecnica;
    }

    // GETTERS
    public int getIdFichaTecnica() {
        return idFichaTecnica;
    }

    // FUNCTIONS

    public void print(){
        // INSERT INTO FICHAS_TECNICAS(idFichaTecnica) VALUES (?);
        System.out.println("INSERT INTO FICHAS_TECNICAS(idFichaTecnica) VALUES (" +
                this.idFichaTecnica+");");
    }

    public String append(){
        String text = "INSERT INTO FICHAS_TECNICAS(idFichaTecnica) VALUES (" +
                this.idFichaTecnica+");";
        return text;
    }
}
