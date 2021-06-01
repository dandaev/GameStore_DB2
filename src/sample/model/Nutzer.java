package sample.model;

public class Nutzer {
    private Integer NutzNr;
    private String NutzName;
    private String NutzLogin;
    private String NutzPasswort;
    private Enum NutzRole;

    @Override
    public String toString() {
        return "Nutzer{" +
                "NutzNr=" + NutzNr +
                ", NutzName='" + NutzName + '\'' +
                ", NutzLogin='" + NutzLogin + '\'' +
                ", NutzPasswort='" + NutzPasswort + '\'' +
                ", NutzRole=" + NutzRole +
                '}';
    }

    public Nutzer() {
    }

    public Nutzer(Integer nutzNr, String nutzName, String nutzLogin, String nutzPasswort, Enum nutzRole) {
        NutzNr = nutzNr;
        NutzName = nutzName;
        NutzLogin = nutzLogin;
        NutzPasswort = nutzPasswort;
        NutzRole = nutzRole;
    }

    public Integer getNutzNr() {
        return NutzNr;
    }

    public void setNutzNr(Integer nutzNr) {
        NutzNr = nutzNr;
    }

    public String getNutzName() {
        return NutzName;
    }

    public void setNutzName(String nutzName) {
        NutzName = nutzName;
    }

    public String getNutzLogin() {
        return NutzLogin;
    }

    public void setNutzLogin(String nutzLogin) {
        NutzLogin = nutzLogin;
    }

    public String getNutzPasswort() {
        return NutzPasswort;
    }

    public void setNutzPasswort(String nutzPasswort) {
        NutzPasswort = nutzPasswort;
    }

    public Enum getNutzRole() {
        return NutzRole;
    }

    public void setNutzRole(Enum nutzRole) {
        NutzRole = nutzRole;
    }
}
