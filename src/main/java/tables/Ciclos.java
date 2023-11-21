package tables;

public class Ciclos {

    // CICLOS(nomeCiclo, ESPECIEVEGETALID, inicio, fim, UNIDADEMEDIDAID)

    private String nomeCiclo;
    private int especieVegetalId;
    private int[] inicio;
    private int[] fim;
    private String unidades;

    // CONSTRUCTORS

    public Ciclos(String nomeCiclo, int especieVegetalId, String intervaloTemporal) {
        this.nomeCiclo = nomeCiclo;
        this.especieVegetalId = especieVegetalId;
        setInicioFimUnidades(intervaloTemporal);
    }

    // GETTERS

    public String getNomeCiclo() {
        return nomeCiclo;
    }

    public int getEspecieVegetalId() {
        return especieVegetalId;
    }

    public int[] getInicio() {
        return inicio;
    }

    public int[] getFim() {
        return fim;
    }

    public String getUnidades() {
        return unidades;
    }

    // SETTERS

    private void setInicio(int[] inicio) {
        this.inicio = inicio;
    }

    private void setFim(int[] fim) {
        this.fim = fim;
    }

    private void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    // FUNCTIONS

    public void print() {
        // INSERT INTO CICLOS(nomeCiclo, ESPECIEVEGETALID, inicio, fim, UNIDADEMEDIDAID) VALUES (?, ?, ?, ?, ?);
        for (int i = 0; i < this.inicio.length; i++) {
            System.out.println("INSERT INTO CICLOS(nomeCiclo, ESPECIEVEGETALID, inicio, fim, UNIDADEMEDIDAID) VALUES ('" +
                    this.nomeCiclo + "', " + this.especieVegetalId + ", " + this.inicio[i] + ", " + this.fim[i] + ", '" + this.unidades + "');");
        }
    }

    public String append(){
        String returnText = "";
        for (int i = 0; i < this.inicio.length; i++) {
            String text = "INSERT INTO CICLOS(nomeCiclo, ESPECIEVEGETALID, inicio, fim, UNIDADEMEDIDAID) VALUES ('" +
                    this.nomeCiclo + "', " + this.especieVegetalId + ", " + this.inicio[i] + ", " + this.fim[i] + ", '" + this.unidades + "');\n";
            returnText = returnText.concat(text);
        }
        return returnText;
    }

    /**
     * Goes through a String containing the interveal of time for a cycle of a given type of operation.
     * Sets start, end, and units.
     *
     * @param intervaloTemporal
     */
    private void setInicioFimUnidades(String intervaloTemporal) {
        if (Character.isDigit(intervaloTemporal.charAt(0))) {
            String[] splitted = intervaloTemporal.split(" ");
            int[] inicio = {0};
            setInicio(inicio);
            int[] fim = {Integer.parseInt(splitted[0])};
            setFim(fim);
            setUnidades("dia");
            return;
        }
        String[] splitted = intervaloTemporal.split(", ");
        int[] inicio = new int[splitted.length];
        int[] fim = new int[splitted.length];
        int i = 0;
        for (String s : splitted) {
            String[] startToEnd = s.split(" a ");
            inicio[i] = convertMonthStrToMonthInt(startToEnd[0].toUpperCase());
            if (startToEnd.length > 1) {
                fim[i] = convertMonthStrToMonthInt(startToEnd[1].toUpperCase());
            } else {
                fim[i] = inicio[i];
            }
            i++;
        }
        setInicio(inicio);
        setFim(fim);
        setUnidades("mes");
    }

    private int convertMonthStrToMonthInt(String month) {
        switch (month) {
            case ("JANEIRO"):
                return 1;
            case ("FEVEREIRO"):
                return 2;
            case ("MARÇO"):
                return 3;
            case ("ABRIL"):
                return 4;
            case ("MAIO"):
                return 5;
            case ("JUNHO"):
                return 6;
            case ("JULHO"):
                return 7;
            case ("AGOSTO"):
                return 8;
            case ("SETEMBRO"):
                return 9;
            case ("OUTUBRO"):
                return 10;
            case ("NOVEMBRO"):
                return 11;
            case ("DEZEMBRO"):
                return 12;
            default:
                return 0;
        }
    }
}
