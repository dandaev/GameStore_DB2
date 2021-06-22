package sample.model;

public class Publisher {
    private Integer PubNr;
    private String PublisherName;

    @Override
    public String toString() {
        return  "("+PubNr+") " + PublisherName;
    }

    public Publisher() {
    }

    public Publisher(Integer pubNr, String publisherName) {
        PubNr = pubNr;
        PublisherName = publisherName;
    }

    public Integer getPubNr() {
        return PubNr;
    }

    public void setPubNr(Integer pubNr) {
        PubNr = pubNr;
    }

    public String getPublisherName() {
        return PublisherName;
    }

    public void setPublisherName(String publisherName) {
        PublisherName = publisherName;
    }
}
