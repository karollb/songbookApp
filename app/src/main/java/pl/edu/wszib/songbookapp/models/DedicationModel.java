package pl.edu.wszib.songbookapp.models;

public class DedicationModel {
    private String id;
    private String dedicationContent;
    private String songName;
    private String songPath;



    public DedicationModel() {
    }

    public DedicationModel(String id, String dedicationContent, String songName, String songPath) {
        this.id = id;
        this.dedicationContent = dedicationContent;
        this.songName = songName;
        this.songPath = songPath;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDedicationContent() {
        return dedicationContent;
    }

    public void setDedicationContent(String dedicationContent) {
        this.dedicationContent = dedicationContent;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

}
