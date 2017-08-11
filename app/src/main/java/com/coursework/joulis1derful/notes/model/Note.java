package com.coursework.joulis1derful.notes.model;

public class Note {

    private String firebaseKey;
    private String title;
    private String body;

    public Note() {
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Note(String firebaseKey, String title, String body) {

        this.firebaseKey = firebaseKey;
        this.title = title;
        this.body = body;
    }
}
