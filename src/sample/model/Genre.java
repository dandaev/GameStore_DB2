package sample.model;

public class Genre {
    private Integer GeNr;
    private String GeName;

    @Override
    public String toString() {
        return "("+GeNr+") " + GeName;
    }

    public Genre() {
    }

    public Genre(Integer geNr, String geName) {
        GeNr = geNr;
        GeName = geName;
    }

    public Integer getGeNr() {
        return GeNr;
    }

    public void setGeNr(Integer geNr) {
        GeNr = geNr;
    }

    public String getGeName() {
        return GeName;
    }

    public void setGeName(String geName) {
        GeName = geName;
    }
}
