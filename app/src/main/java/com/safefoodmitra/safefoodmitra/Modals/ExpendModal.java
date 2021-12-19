package com.safefoodmitra.safefoodmitra.Modals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpendModal {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> cricket = new ArrayList<String>();
        cricket.add("India");
        expandableListDetail.put("CRICKET TEAMS", cricket);
        return expandableListDetail;
    }
}
