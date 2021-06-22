package sample.model;

public class Spiel {
    private Integer SpNr;
    private String SpName;
    private String Beschreibung;
    private String Publikationsdatum;
    private Integer PublisherNr;
    private Integer KategorieNr;
    private Double SpPreis;
    private String SpImage;
    private String LinkZumDownload;

    @Override
    public String toString() {
        return "Spiel{" +
                "SpNr=" + SpNr +
                ", SpName='" + SpName + '\'' +
                ", Beschreibung='" + Beschreibung + '\'' +
                ", Publikationsdatum='" + Publikationsdatum + '\'' +
                ", PublisherNr=" + PublisherNr +
                ", KategorieNr=" + KategorieNr +
                '}';
    }

    public Spiel() {
    }

    public Spiel(Integer spNr, String spName, String beschreibung, String publikationsdatum, Integer publisherNr, Integer kategorieNr, Double spPreis, String spImage) {
        SpNr = spNr;
        SpName = spName;
        Beschreibung = beschreibung;
        Publikationsdatum = publikationsdatum;
        PublisherNr = publisherNr;
        KategorieNr = kategorieNr;
        SpPreis = spPreis;
        SpImage = spImage;
    }

    public Integer getSpNr() {
        return SpNr;
    }

    public void setSpNr(Integer spNr) {
        SpNr = spNr;
    }

    public String getSpName() {
        return SpName;
    }

    public void setSpName(String spName) {
        SpName = spName;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        Beschreibung = beschreibung;
    }

    public String getPublikationsdatum() {
        return Publikationsdatum;
    }

    public void setPublikationsdatum(String publikationsdatum) {
        Publikationsdatum = publikationsdatum;
    }

    public Integer getPublisherNr() {
        return PublisherNr;
    }

    public void setPublisherNr(Integer publisherNr) {
        PublisherNr = publisherNr;
    }

    public Integer getKategorieNr() {
        return KategorieNr;
    }

    public void setKategorieNr(Integer kategorieNr) {
        KategorieNr = kategorieNr;
    }

    public Double getSpPreis() {
        return SpPreis;
    }

    public void setSpPreis(Double spPreis) {
        SpPreis = spPreis;
    }

    public String getSpImage() {
        return SpImage;
    }

    public void setSpImage(String spImage) {
        SpImage = spImage;
    }

    public String getLinkZumDownload() {
        return LinkZumDownload;
    }

    public void setLinkZumDownload(String linkZumDownload) {
        LinkZumDownload = linkZumDownload;
    }
}
