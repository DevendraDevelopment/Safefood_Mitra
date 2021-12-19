package com.safefoodmitra.safefoodmitra.Apis;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetApis {

    @GET("zones/{input}")
    Call<ResponseBody> Zones(@Path("input") String input, @Header("Authorization") String Authorization);

    @GET("locations/{input}")
    Call<ResponseBody> Locations(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("departments/{input}")
    Call<ResponseBody> Deparments(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("equipments/{input}")
    Call<ResponseBody> Equpments(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("records/{input}")
    Call<ResponseBody> Records(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("responsibilities/{input}")
    Call<ResponseBody> InspectRespo(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("appuser/{input}")
    Call<ResponseBody> Users(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("fobunits/1")
    Call<ResponseBody> Units(@Header("Authorization") String Authorization);

    @GET("fobtypes")
    Call<ResponseBody> FobType(@Header("Authorization") String Authorization);

    @GET("locations/{input}/0")
    Call<ResponseBody> Locationstype(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("departments/{input}/0")
    Call<ResponseBody> Departmentype(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("getfsmscats/0/{input}")
    Call<ResponseBody> FsmsCategories(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("getfsmscats/{catinput}/{fobinput}")
    Call<ResponseBody> FsmsDocs(@Path("catinput") String catinput,@Path("fobinput") String fobinput,@Header("Authorization") String Authorization);

    @GET("getfsmsdocs/{catinput}/{fobinput}")
    Call<ResponseBody> FsmsSubDocs(@Path("catinput") String catinput,@Path("fobinput") String fobinput,@Header("Authorization") String Authorization);


    @GET("getfsscats/0")
    Call<ResponseBody> FssCategories(@Header("Authorization") String Authorization);

    @GET("deptList/{input}")
    Call<ResponseBody> Depatlist(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("locationlist/{input}")
    Call<ResponseBody> LocationList(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("areaList")
    Call<ResponseBody> ConcernList(@Header("Authorization") String Authorization);


    @GET("inspections/{input}/1")
    Call<ResponseBody> Inspectionssend(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("inspections/{input}/2")
    Call<ResponseBody> Inspectionsrecive(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("regcats/0")
    Call<ResponseBody> Categories(@Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST("jobtypes")
    Call<ResponseBody> JobType(@Header("Authorization") String Authorization,@Field("users_id") String users_id);

    @FormUrlEncoded
    @POST("inspections/{input}/1")
    Call<ResponseBody> Inspectionsearchdatasen(@Path("input") String input,@Header("Authorization") String Authorization,@Field("firstdate") String firstdate,@Field("seconddate") String seconddate,@Field("locatinid") String locatinid,@Field("respoid") String respoid,@Field("conceid") String conceid,@Field("status") String status);

    @FormUrlEncoded
    @POST("inspections/{input}/2")
    Call<ResponseBody> Inspectionsearchdatarecive(@Path("input") String input,@Header("Authorization") String Authorization,@Field("firstdate") String firstdate,@Field("seconddate") String seconddate,@Field("locatinid") String locatinid,@Field("respoid") String respoid,@Field("conceid") String conceid,@Field("status") String status);

    @GET("cmlist/{input}")
    Call<ResponseBody> CFMLisetget(@Path("input") String input,@Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST("cmlist/{input}")
    Call<ResponseBody> CFMLisetpost(@Path("input") String input,@Header("Authorization") String Authorization,@Field("dispdata") String dispdata);

    @FormUrlEncoded
    @POST("cmsave/{input}")
    Call<ResponseBody> CFMSaveLiset(@Path("input") String input,@Header("Authorization") String Authorization,@FieldMap HashMap<String, String> map);

    @GET("locations/{input}/{subloc}")
    Call<ResponseBody> SubLocations(@Path("input") String input,@Path("subloc") String subloc,@Header("Authorization") String Authorization);

    @GET("cmdata/{input}")
    Call<ResponseBody> CFMListDetails(@Path("input") String input,@Header("Authorization") String Authorization);

    @GET("cmdeactive/{input}")
    Call<ResponseBody> RemoveTask(@Path("input") String input,@Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST("enquiry")
    Call<ResponseBody> NewUserData(@Header("Authorization") String Authorization,@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("cmlist/{input}")
    Call<ResponseBody> CMFFillterList(@Path("input") String input,@Header("Authorization") String Authorization,@Field("firstdate") String firstdate,@Field("seconddate") String seconddate,@Field("status") String status,@Field("cmftype") String cmftype,@Field("cmfzoneid") String cmfzoneid,@Field("cmflocid") String cmflocid,@Field("cmfsublocid") String cmfsublocid,@Field("cmfrespoid") String cmfrespoid,@Field("cmfequid") String cmfequid,@Field("searchdata") String searchdata);

    @GET("slidedata")
    Call<ResponseBody> BannarLiset(@Header("Authorization") String Authorization);
}
