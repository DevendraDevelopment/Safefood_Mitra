package com.safefoodmitra.safefoodmitra.Apis;

public interface Apis {
    String Base_Url = "http://safefoodmitra.com/admin/api/";
    String Base_UrlVerification = "http://safefoodmitra.com/admin/api/cmdata/verify";
    //post method
    String Login = Base_Url + "requestotp";
    String Otp = Base_Url + "validateotp";
    String Fbtoken = Base_Url + "fbtoken";

    String documentlist = Base_Url + "getfsmscats/";
    String documentpdflist = Base_Url + "getfsmsdocs/";
    String foodsafetylist = Base_Url + "getfsscats/";
    String foodsafetypdflist = Base_Url + "getfssdocs/";
    String addinspection = Base_Url + "addinspection";
    String closeinspection = Base_Url + "closeinspection";
    String joballocation = Base_Url + "jobtypeupdate";

    String imageBase_Url = "http://safefoodmitra.com/admin/public/inspections/";
    String imageBaseicon_Url = "http://safefoodmitra.com/admin/public/icon_path/";
    String imageNewsBase_Url="http://safefoodmitra.com/admin/public/news/";
    String imagecmfdetils = "http://safefoodmitra.com/admin/public/cmimage/";
    String imageslides = "http://safefoodmitra.com/admin/public/slides/";
    String advancefilter = Base_Url + "searchfss";
    String advancefiltersearchdata = Base_Url + "searchfss";
    String subcategorylist = Base_Url + "regcats/";
    String inspections = Base_Url + "inspections/";
    String Register = Base_Url + "userregister";
    //get method
    String get_Base_Url = Base_Url + "getlist/";


    // post store urls
    String post_Base_Url = Base_Url + "store/";

    String Addzones = post_Base_Url + "zones";
    String Addlocations = post_Base_Url + "locations";
    String Adddeparments = post_Base_Url + "departments";
    String Addequipments = post_Base_Url + "equipments";
    String Addrecords = post_Base_Url + "records";
    String Addfobunits = post_Base_Url + "fobunits";
    String Addusers = post_Base_Url + "users";
    String Addinspetrespo = post_Base_Url + "responsibilities";

    // post edit urls
    String post_edit_Base_Url = Base_Url + "edit/";
    String Editzones = post_edit_Base_Url + "zones/";
    String Editlocation = post_edit_Base_Url + "locations/";
    String Editdepartment = post_edit_Base_Url + "departments/";
    String Editequpment = post_edit_Base_Url + "equipments/";
    String Editrecord = post_edit_Base_Url + "records/";
    String Editunit = post_edit_Base_Url + "fobunits/";
    String EditInspectRespo = post_edit_Base_Url + "responsibilities/";
    String EditUser = post_edit_Base_Url + "users/";

    // post delete urls

    String post_delete_Base_Url = Base_Url + "deldata/";
    String Deletezones = post_delete_Base_Url + "zones/";
    String Deletelocation = post_delete_Base_Url + "locations/";
    String Deletedepartment = post_delete_Base_Url + "departments/";
    String Deletequpment = post_delete_Base_Url + "equipments/";
    String Deleterecord = post_delete_Base_Url + "records/";
    String Deleteunit = post_delete_Base_Url + "fobunits/";
    String Deleteinspectrespo = post_delete_Base_Url + "responsibilities/";
    String DeleteUser = post_delete_Base_Url + "users/";
    String DeleteInspections = post_delete_Base_Url + "inspections/";

    String closetask = Base_Url + "cmaction";

}
