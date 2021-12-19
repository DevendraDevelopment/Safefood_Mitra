package com.safefoodmitra.safefoodmitra.Modals;

public class DepartItem extends ListItem {

    private String dept_name;

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    @Override
    public int getType() {
        return TYPE_DEPART;
    }
}
