package com.example.app.pojo;

public class Page3Data {
    private int image;
    private String title;
    private String singTime;
    private String date;

    private int people;
    private boolean isClose;

    public Page3Data(int image, String title, String singTime, String date, int people, boolean isClose) {
        this.image = image;
        this.title = title;
        this.date = date;
        this.singTime = singTime;
        this.people = people;
        this.isClose = isClose;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSingTime() {
        return singTime;
    }

    public void setSingTime(String singTime) {
        this.singTime = singTime;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}
