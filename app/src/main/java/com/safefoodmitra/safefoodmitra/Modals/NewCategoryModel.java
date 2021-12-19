package com.safefoodmitra.safefoodmitra.Modals;

public class NewCategoryModel {
    String cat_name,cattoal,catdte;

    public NewCategoryModel(String cat_name, String cattoal, String catdte) {
        this.cat_name = cat_name;
        this.cattoal = cattoal;
        this.catdte = catdte;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCattoal() {
        return cattoal;
    }

    public void setCattoal(String cattoal) {
        this.cattoal = cattoal;
    }

    public String getCatdte() {
        return catdte;
    }

    public void setCatdte(String catdte) {
        this.catdte = catdte;
    }
}
