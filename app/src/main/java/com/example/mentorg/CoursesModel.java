package com.example.mentorg;

public class CoursesModel {
    String name;
    String description;
    String mentor;
    public CoursesModel(){}

    public CoursesModel(String name, String description, String mentor) {
        this.name = name;
        this.description = description;
        this.mentor = mentor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }
}
