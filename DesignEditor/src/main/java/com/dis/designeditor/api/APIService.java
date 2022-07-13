package com.dis.designeditor.api;

import com.dis.designeditor.model.DesignStudioBottomModel;
import com.dis.designeditor.model.FavouriteModel;
import com.dis.designeditor.model.FooterResponse;
import com.dis.designeditor.model.HeaderModel;
import com.dis.designeditor.model.ReminderModel;
import com.dis.designeditor.model.SaveImageModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
//test





    @Multipart
    @POST("uploads/file_upload.php")
    Call<SaveImageModel> saveReminderImage(
            @Part("appname") RequestBody appName,
            @Part("ansType") RequestBody type,
            @Part("action") RequestBody action,
            @Part("User_Id") RequestBody userId,
            @Part MultipartBody.Part image
    );



    @FormUrlEncoded
    @POST("footer_img_api")
    Call<FooterResponse> getFooter(
            @Field("user_id") String appName
    );
    @FormUrlEncoded
    @POST("header_img_api")
    Call<HeaderModel> getHeader(
            @Field("user_id") String appName
    );
    @FormUrlEncoded
    @POST("ClientMaterialListTypeWise.php")
    Call<DesignStudioBottomModel> designStudioApi(
            @Field("appname") String appName,
            @Field("MaterialType") String type,
            @Field("Client_Cd") int clientid,
            @Field("page") int page

    );

    @FormUrlEncoded
    @POST("AddToFavourite.php")
    Call<FavouriteModel> favouriteFont(
            @Field("appname") String appName,
            @Field("type") String type,
            @Field("Favourite") int favourite,
            @Field("Font_ID") int fontid,
            @Field("Client_Cd") int clientid,
            @Field("UserName") String username
    );

    @FormUrlEncoded
    @POST("AddToFavourite.php")
    Call<FavouriteModel> favouriteQuotes(
            @Field("appname") String appName,
            @Field("type") String type,
            @Field("Favourite") int favourite,
            @Field("Quotes_ID") int quotesid,
            @Field("Client_Cd") int clientid,
            @Field("UserName") String username
    );

    @FormUrlEncoded
    @POST("AddToFavourite.php")
    Call<FavouriteModel> favourite(
            @Field("appname") String appName,
            @Field("type") String type,
            @Field("Favourite") int favourite,
            @Field("ClientMaterial_ID") int id,
            @Field("Client_Cd") int clientid,
            @Field("UserName") String username
    );

    @FormUrlEncoded
    @POST("SaveFinalImage.php")
    Call<FavouriteModel> saveDesignStudio(
            @Field("appname") String appName,
            @Field("CreatedBy") String by, //exe name
            @Field("FinalImageURL") String img,
            @Field("Template_ID") int templateid,
            @Field("Client_Cd") int clientid,
            @Field("UserName") String username
    );




    @FormUrlEncoded
    @POST("UpdateTaskStatus.php")
    Call<FavouriteModel> updateCalendarTask (
            @Field("appname") String appname,
            @Field("Executive_Cd") int Executive_Cd,
            @Field("operation") String operation,
            @Field("statusremark") String statusremark,
            @Field("Designation") String designation,
            @Field("taskid") int taskid,
            @Field("statuscd") int statuscd
      );


    @FormUrlEncoded
    @POST("ReminderMenuList.php")
    Call<ReminderModel> getReminderList(
            @Field("appname") String appName,
            @Field("UserName") String username,
            @Field("User_Id") int userId,
            @Field("Designation") String designation

    );





}
