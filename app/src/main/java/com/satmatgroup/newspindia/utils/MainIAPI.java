package com.satmatgroup.newspindia.utils;


import com.satmatgroup.newspindia.activities_aeps.twofactorAuthentication.BaseAUthRegistrationResponse;
import com.satmatgroup.newspindia.payout.BaseUserPayoutBankModel;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface MainIAPI {

    String BASE_URL = "http://spindiapan.com";


    @Multipart
    @POST("/api/Aepstransection")
    Call<BaseAUthRegistrationResponse> checkTwoStepReg(@Part("nationalbankidentification") RequestBody nationalbankidentification,
                                                       @Part("mobileNumber") RequestBody mobileNumber,
                                                       @Part("longitude") RequestBody longitude,
                                                       @Part("latitude") RequestBody latitude,
                                                       @Part("adhaarnumber") RequestBody adhaarnumber,
                                                       @Part("rtid") RequestBody rtid,
                                                       @Part("fingerprintdata") RequestBody fingerprintdata,
                                                       @Part("callfunctn") RequestBody callfunctn,
                                                       @Part("transactionAmount") RequestBody transactionAmount);

    @Multipart
    @POST("/api/Payouts")
    Call<BaseUserPayoutBankModel> callUserPayoutAccountList(@Part("rtid") RequestBody rtid,
                                                            @Part("callfunction") RequestBody callfunction);



}
