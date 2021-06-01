package sample.model;

public class Kommentar {
    private Integer KommNr;
    private String KommText;
    private Integer KommSpiel;
    private Integer KommNutzer;

    @Override
    public String toString() {
        return "Kommentar{" +
                "KommNr=" + KommNr +
                ", KommText='" + KommText + '\'' +
                ", KommSpiel=" + KommSpiel +
                ", KommNutzer=" + KommNutzer +
                '}';
    }

    public Kommentar() {
    }

    public Kommentar(Integer kommNr, String kommText, Integer kommSpiel, Integer kommNutzer) {
        KommNr = kommNr;
        KommText = kommText;
        KommSpiel = kommSpiel;
        KommNutzer = kommNutzer;
    }

    public Integer getKommNr() {
        return KommNr;
    }

    public void setKommNr(Integer kommNr) {
        KommNr = kommNr;
    }

    public String getKommText() {
        return KommText;
    }

    public void setKommText(String kommText) {
        KommText = kommText;
    }

    public Integer getKommSpiel() {
        return KommSpiel;
    }

    public void setKommSpiel(Integer kommSpiel) {
        KommSpiel = kommSpiel;
    }

    public Integer getKommNutzer() {
        return KommNutzer;
    }

    public void setKommNutzer(Integer kommNutzer) {
        KommNutzer = kommNutzer;
    }
}
