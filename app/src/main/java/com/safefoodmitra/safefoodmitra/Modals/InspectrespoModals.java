package com.safefoodmitra.safefoodmitra.Modals;

import java.util.List;

public class InspectrespoModals {

    String id,record_name,dept_name;
    private List<InspectrespoModals2> inspectrespoModals2s;

    public InspectrespoModals(String dept_name, List<InspectrespoModals2> inspectrespoModals2s) {
        this.dept_name = dept_name;
        this.inspectrespoModals2s = inspectrespoModals2s;
    }

    public List<InspectrespoModals2> getInspectrespoModals2s() {
        return inspectrespoModals2s;
    }

    public void setInspectrespoModals2s(List<InspectrespoModals2> inspectrespoModals2s) {
        this.inspectrespoModals2s = inspectrespoModals2s;
    }

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
