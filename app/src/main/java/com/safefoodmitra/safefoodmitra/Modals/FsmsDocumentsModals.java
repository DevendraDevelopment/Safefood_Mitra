package com.safefoodmitra.safefoodmitra.Modals;

public class FsmsDocumentsModals {
    String id,cat_name,parent_id;

    public FsmsDocumentsModals(String id, String cat_name, String parent_id) {
        this.id = id;
        this.cat_name = cat_name;
        this.parent_id = parent_id;
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
}
