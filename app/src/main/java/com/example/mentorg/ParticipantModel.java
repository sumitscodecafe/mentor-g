package com.example.mentorg;

public class ParticipantModel {
    String fullname;

    public ParticipantModel() { }
    public ParticipantModel(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
