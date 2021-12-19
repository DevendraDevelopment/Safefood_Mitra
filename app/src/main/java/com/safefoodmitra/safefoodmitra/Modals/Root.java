package com.safefoodmitra.safefoodmitra.Modals;

import java.util.List;

public class Root {

    private String parcat_name;


    private List<ValuesBean> values;

    public Root(String parcat_name, List<ValuesBean> values) {
        this.parcat_name = parcat_name;
        this.values = values;
    }

    public String getParcat_name() {
        return parcat_name;
    }

    public void setParcat_name(String parcat_name) {
        this.parcat_name = parcat_name;
    }

    public List<ValuesBean> getValues() {
        return values;
    }

    public void setValues(List<ValuesBean> values) {
        this.values = values;
    }

    public static class ValuesBean {
        private int id;
        private String parameter;
        private String perm_limit;
        private String prod_name;
        private String paramcat;
        private String parcat_name;
        private String perm_desc;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        public String getPerm_limit() {
            return perm_limit;
        }

        public void setPerm_limit(String perm_limit) {
            this.perm_limit = perm_limit;
        }

        public String getProd_name() {
            return prod_name;
        }

        public void setProd_name(String prod_name) {
            this.prod_name = prod_name;
        }

        public String getParamcat() {
            return paramcat;
        }

        public void setParamcat(String paramcat) {
            this.paramcat = paramcat;
        }

        public String getParcat_name() {
            return parcat_name;
        }

        public void setParcat_name(String parcat_name) {
            this.parcat_name = parcat_name;
        }

        public String getPerm_desc() {
            return perm_desc;
        }

        public void setPerm_desc(String perm_desc) {
            this.perm_desc = perm_desc;
        }
    }
}


