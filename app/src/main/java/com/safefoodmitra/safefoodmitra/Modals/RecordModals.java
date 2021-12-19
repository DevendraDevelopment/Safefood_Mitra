package com.safefoodmitra.safefoodmitra.Modals;

public class RecordModals {

    public RecordModals(String record_name, String dept_name) {
        this.record_name = record_name;
        this.dept_name = dept_name;
    }

    String id,record_name,dept_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecord_name() {
        return record_name;
    }

    public void setRecord_name(String record_name) {
        this.record_name = record_name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }
}
