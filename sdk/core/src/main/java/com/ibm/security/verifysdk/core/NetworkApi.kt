package com.ibm.security.verifysdk.core

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


/**
 * Interface to define network operation calls for {Retrofit}.
 *
 * @since 3.0.0
 */
interface NetworkApi {

    @GET
    suspend fun discover(@Url url: String): Response<ResponseBody>


    @POST
    @FormUrlEncoded
    @Headers("Accept: application/json")
    suspend fun authorizeWithROPC(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String,
        @Field("scope") scope: String,
        @FieldMap additionalParameters: Map<String, String>
    ): Response<ResponseBody>

    @POST
    @FormUrlEncoded
    @Headers("Accept: application/json")
    suspend fun authorizeWithAuthCode(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("grant_type") grantType: String,
        @Field("scope") scope: String,
        @Field("redirect_uri") redirectUri: String,
        @FieldMap additionalParameters: Map<String, String>
    ): Response<ResponseBody>

    @POST
    @FormUrlEncoded
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun refresh(    // OnPrem
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String,
        @Field("scope") scope: Array<String>,
        @FieldMap additionalParameters: Map<String, String>
    ): Response<ResponseBody>

    @POST
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun refresh(    // Cloud
        @Url url: String,
        @Query("metadataInResponse") metadataInResponse: Boolean,
        @Body body: RequestBody
    ): Response<ResponseBody>

    @GET
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun transaction(    // Cloud
        @Url url: String,
        @Header("Authorization") token: String
    ): Response<ResponseBody>

    @GET
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun completeTransaction(    // Cloud
        @Url url: String,
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Response<ResponseBody>

    @POST
    @Headers("Accept: application/json", "Content-Type: application/json")
    fun login(
        @Url url: String,
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Response<ResponseBody>


}