package com.safefoodmitra.safefoodmitra.Modals;

public class DocumentspdfModel {
     String id,doc_title,doc_path,baseurl;

    public DocumentspdfModel(String id, String doc_title, String doc_path, String baseurl) {
        this.id = id;
        this.doc_title = doc_title;
        this.doc_path = doc_path;
        this.baseurl = baseurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoc_title() {
        return doc_title;
    }

    public void setDoc_title(String doc_title) {
        this.doc_title = doc_title;
    }

    public String getDoc_path() {
        return doc_path;
    }

    public void setDoc_path(String doc_path) {
        this.doc_path = doc_path;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
}
