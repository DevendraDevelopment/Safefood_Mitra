package com.safefoodmitra.safefoodmitra.Modals;

import java.util.List;

public class CleaningMaintModel {
    String assigneddate,assignedday;
    public List<Datalist> datalist;

    public String getAssigneddate() {
        return assigneddate;
    }

    public void setAssigneddate(String assigneddate) {
        this.assigneddate = assigneddate;
    }

    public String getAssignedday() {
        return assignedday;
    }

    public void setAssignedday(String assignedday) {
        this.assignedday = assignedday;
    }

    public List<Datalist> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<Datalist> datalist) {
        this.datalist = datalist;
    }

    public class Datalist{
        public String taskstatus;
        public String taskid;
        public String cmtype;
        public String zone;
        public String assignedtime;
        public String cmscheduleid;
        public String iseditable;
        public String dept_name;
        public String subloc_name;
        public String equip_name;
        public String loc_name;
        public String taskstatusdisp;
        public String taskcomment;
        public String isverified;
        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        public String getCmtype() {
            return cmtype;
        }

        public void setCmtype(String cmtype) {
            this.cmtype = cmtype;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getAssignedtime() {
            return assignedtime;
        }

        public void setAssignedtime(String assignedtime) {
            this.assignedtime = assignedtime;
        }

        public String getCmscheduleid() {
            return cmscheduleid;
        }

        public void setCmscheduleid(String cmscheduleid) {
            this.cmscheduleid = cmscheduleid;
        }

        public String getIseditable() {
            return iseditable;
        }

        public void setIseditable(String iseditable) {
            this.iseditable = iseditable;
        }

        public String getTaskstatus() {
            return taskstatus;
        }

        public void setTaskstatus(String taskstatus) {
            this.taskstatus = taskstatus;
        }

        public String getDept_name() {
            return dept_name;
        }

        public void setDept_name(String dept_name) {
            this.dept_name = dept_name;
        }

        public String getSubloc_name() {
            return subloc_name;
        }

        public void setSubloc_name(String subloc_name) {
            this.subloc_name = subloc_name;
        }

        public String getEquip_name() {
            return equip_name;
        }

        public void setEquip_name(String equip_name) {
            this.equip_name = equip_name;
        }

        public String getLoc_name() {
            return loc_name;
        }

        public void setLoc_name(String loc_name) {
            this.loc_name = loc_name;
        }

        public String getTaskstatusdisp() {
            return taskstatusdisp;
        }

        public void setTaskstatusdisp(String taskstatusdisp) {
            this.taskstatusdisp = taskstatusdisp;
        }

        public String getTaskcomment() {
            return taskcomment;
        }

        public void setTaskcomment(String taskcomment) {
            this.taskcomment = taskcomment;
        }

        public String getIsverified() {
            return isverified;
        }

        public void setIsverified(String isverified) {
            this.isverified = isverified;
        }
    }


}
