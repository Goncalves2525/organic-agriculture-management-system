package dto;

import java.util.ArrayList;

public class ReceitasDTO {

    int idReceita;
    ArrayList<FatProdDTO> receita;

    public ReceitasDTO(int idReceita) {
        this.idReceita = idReceita;
        this.receita = new ArrayList<>();
    }

    public int getIdReceita() {
        return idReceita;
    }

    public ArrayList<FatProdDTO> getReceita() {
        return receita;
    }
}
