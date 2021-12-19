package com.safefoodmitra.safefoodmitra.Modals;

public class MySliderList {
    private String id,imagename,imageurl,isactive,baseurl;

    public MySliderList(String id, String imagename, String imageurl, String isactive, String baseurl) {
        this.id = id;
        this.imagename = imagename;
        this.imageurl = imageurl;
        this.isactive = isactive;
        this.baseurl = baseurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
}
