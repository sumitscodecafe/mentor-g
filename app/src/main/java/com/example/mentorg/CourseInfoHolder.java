package com.example.mentorg;

public class CourseInfoHolder {
    String name, mentor, description;
    public CourseInfoHolder(String name, String mentor, String description) {
        this.name = name;
        this.mentor = mentor;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
