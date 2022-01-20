package pl.edu.wszib.songbookapp.models;

public class Team {
    private String teamName;
    private String teamPassword;
    private String songName;

    public Team() {
    }

    public Team(String teamName, String teamPassword, String songName) {
        this.teamName = teamName;
        this.teamPassword = teamPassword;
        this.songName = songName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamPassword() {
        return teamPassword;
    }

    public void setTeamPassword(String teamPassword) {
        this.teamPassword = teamPassword;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
