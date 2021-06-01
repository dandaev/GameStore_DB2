package sample.model;

public class Kategorie {
    private Integer KatNr;
    private String KatName;

    @Override
    public String toString() {
        return "Kategorie{" +
                "KatNr=" + KatNr +
                ", KatName='" + KatName + '\'' +
                '}';
    }

    public Kategorie() {
    }

    public Kategorie(Integer katNr, String katName) {
        KatNr = katNr;
        KatName = katName;
    }

    public Integer getKatNr() {
        return KatNr;
    }

    public void setKatNr(Integer katNr) {
        KatNr = katNr;
    }

    public String getKatName() {
        return KatName;
    }

    public void setKatName(String katName) {
        KatName = katName;
    }
}
