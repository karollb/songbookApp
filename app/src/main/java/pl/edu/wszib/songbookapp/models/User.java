package pl.edu.wszib.songbookapp.models;

public class User {
    private String id;
    private String givenName;
    private String familyName;
    private String email;
    private String teamName;


    public User() {
    }

    public User(String id, String givenName, String familyName, String email, String teamName) {
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.teamName = teamName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
