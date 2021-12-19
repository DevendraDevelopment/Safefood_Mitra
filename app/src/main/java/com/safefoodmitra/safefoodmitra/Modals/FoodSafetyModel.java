package com.safefoodmitra.safefoodmitra.Modals;

public class FoodSafetyModel {
    String id,cat_name,parent_id,baseurl,icon_path;

    public FoodSafetyModel(String id, String cat_name, String parent_id, String baseurl, String icon_path) {
        this.id = id;
        this.cat_name = cat_name;
        this.parent_id = parent_id;
        this.baseurl = baseurl;
        this.icon_path = icon_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }
}
