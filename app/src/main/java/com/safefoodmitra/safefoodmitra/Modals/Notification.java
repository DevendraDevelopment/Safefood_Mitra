package com.safefoodmitra.safefoodmitra.Modals;

public class Notification {
    private String title;
    private String message;
    private String type;
    private String date;
    private String pdflink;
    private String value;
    Notification(){

    }

    public Notification(String title, String message, String type, String date, String pdflink, String value) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.date = date;
        this.pdflink = pdflink;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPdflink() {
        return pdflink;
    }

    public void setPdflink(String pdflink) {
        this.pdflink = pdflink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
