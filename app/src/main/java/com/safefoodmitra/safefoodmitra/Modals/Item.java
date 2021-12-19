package com.safefoodmitra.safefoodmitra.Modals;

public class Item extends ListItem {

    private RecordModals pojoOfJsonArray;

    public RecordModals getPojoOfJsonArray() {
        return pojoOfJsonArray;
    }

    public void setPojoOfJsonArray(RecordModals pojoOfJsonArray) {
        this.pojoOfJsonArray = pojoOfJsonArray;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
