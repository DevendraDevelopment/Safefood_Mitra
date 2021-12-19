package com.safefoodmitra.safefoodmitra.Modals;

public class EqupmentModals {

    String id,equip_name;
    private boolean selected;

    public EqupmentModals(String equip_name) {
        this.equip_name = equip_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquip_name() {
        return equip_name;
    }

    public void setEquip_name(String equip_name) {
        this.equip_name = equip_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
