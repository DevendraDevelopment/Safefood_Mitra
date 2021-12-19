package com.safefoodmitra.safefoodmitra.Modals;

public class UserItem extends ListItem {

    private InspectrespoModals pojoOfJsonArray;

    public InspectrespoModals getPojoOfJsonArray() {
        return pojoOfJsonArray;
    }

    public void setPojoOfJsonArray(InspectrespoModals pojoOfJsonArray) {
        this.pojoOfJsonArray = pojoOfJsonArray;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
