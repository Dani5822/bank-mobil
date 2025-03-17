package com.example.bankApp.data.connect;

import com.example.bankApp.data.model.Card;
import com.example.bankApp.data.model.Expense;
import com.example.bankApp.data.model.Income;
import com.example.bankApp.data.model.LoggedInUser;
import com.example.bankApp.data.model.RepeatableTransaction;
import com.example.bankApp.data.model.currency;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitApiService {
    @GET("user")
    Call<List<LoggedInUser>> getAllUser();

    @GET("/user/userbank/{id}")
    Call<LoggedInUser> getUserById(@Path("id") String userid, @Header("authorization") String token);

    @GET("/accounts/all/{id}")
    Call<Card[]> getallcardbyuserid(@Path("id") String userid, @Header("authorization") String token);

    @GET("/accounts/allex/{id}")
    Call<Expense[]> getallexbycardid(@Path("id") String cardid, @Header("authorization") String token);

    @GET("/accounts/allin/{id}")
    Call<Income[]> getallinbycardid(@Path("id") String cardid, @Header("authorization") String token);

    @FormUrlEncoded
    @POST("user/login")
    Call<LoggedInUser> loginUser(
            @Field("email") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user")
    Call<LoggedInUser> registerUser(
            @Field("firstName") String firstname,
            @Field("lastName") String lastname,
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("currency-api@latest/v1/currencies/{currency}.json")
    Call<currency> getEur(@Path("currency") String currency);

    @GET("currency-api@{date}/v1/currencies/{currencies}.json")
    Call<currency> GetCurrencyByDate(@Path("date") String date, @Path("currencies") String currency);

    @GET("currency-api@latest/v1/currencies/{currency}.json")
    Call<JsonObject> getRatesJson(@Path("currency") String currency);

    @POST("accounts")
    @FormUrlEncoded
    Call<Card> createCard(@Header("authorization") String token, @Field("currency") String currency, @Field("ownerName") String ownerName, @Field("userId") String userId);

    @GET("accounts/user/{id}")
    Call<Card[]> getUsersByCardID(@Path("id") String cardid, @Header("authorization") String token);

    @GET("accounts/alluser/{id}")
    Call<Card> getCardUsers(@Path("id") String cardid, @Header("authorization") String token);

    @PATCH("accounts/disconnect/{id}")
    @FormUrlEncoded
    Call<Card> disconnectUser(@Header("authorization") String token, @Path("id") String cardid, @Field("userId") String userId);

    @PATCH("accounts/{id}")
    @FormUrlEncoded
    Call<Card> updateCard(@Header("authorization") String token, @Path("id") String cardid, @Field("currency") String currency);

    @PATCH("accounts/user/{id}")
    @FormUrlEncoded
    Call<Card> updateCardUser(@Header("authorization") String token, @Path("id") String cardid, @Field("userId") String userId);

    @PATCH("accounts/user/email/{id}")
    @FormUrlEncoded
    Call<Card> updateCardUserList(@Header("authorization") String token, @Path("id") String cardid, @Field("email") String email);

    @DELETE("accounts/{id}")
    Call<Card> deleteCard(@Header("authorization") String token, @Path("id") String cardid);

    @POST("Income")
    @FormUrlEncoded
    Call<Income> createIncome(
            @Header("authorization") String token,
            @Field("total") Double amount,
            @Field("description") String description,
            @Field("bankAccountId") String cardId,
            @Field("category") String category,
            @Field("userId") String userId);


    @POST("expense")
    @FormUrlEncoded
    Call<Expense> createExpense(
            @Header("authorization") String token,
            @Field("total") Double amount,
            @Field("description") String description,
            @Field("bankAccountId") String cardId,
            @Field("category") String category,
            @Field("userId") String userId
    );

    @POST("repeatabletransaction")
    @FormUrlEncoded
    Call<RepeatableTransaction> createRepeatableTransaction(
            @Header("authorization") String token,
            @Field("total") Double amount,
            @Field("category") String category,
            @Field("description") String description,
            @Field("accountId") String cardId,
            @Field("repeatAmount") int repeatAmount,
            @Field("repeatMetric") String repeatMetric,
            @Field("repeatStart") String repeatStart,
            @Field("repeatEnd") String repeatEnd,
            @Field("userId") String userId
    );

    @PATCH("repeatabletransaction/update/{id}")
    @FormUrlEncoded
    Call<RepeatableTransaction> updateRepeatableTransactions(
            @Header("authorization") String token,
            @Path("id") String accountid,
            @Field("userId") String userId
    );

    @GET("accounts/{id}")
    Call<Card> getCardById(@Path("id") String cardid, @Header("authorization") String token);

    @POST("accounts/transfer")
    @FormUrlEncoded
    Call<Card> createTransfer(@Header("authorization") String accessToken,
                              @Field("ammount") Double total,
                              @Field("accountfrom") String from,
                              @Field("accountto") String to,
                              @Field("userId") String userId);

    @GET("expense/{id}")
    Call<Expense> getExpenseById(@Path("id") String expenseid, @Header("authorization") String token);

    @GET("income/{id}")
    Call<Income> getIncomeById(@Path("id") String incomeid, @Header("authorization") String token);


    @GET("repeatabletransaction/{id}")
    Call<RepeatableTransaction> getRepeatableTransactionById(@Path("id") String repeatableTransactionId, @Header("authorization") String token);

    @DELETE("repeatabletransaction/{id}")
    Call<RepeatableTransaction> deleteRepeatableTransaction(@Path("id") String repeatableTransactionId, @Header("authorization") String token);

    @DELETE("expense/{id}")
    Call<Expense> deleteExpenseTransaction(@Path("id") String id,@Header("authorization") String token);

    @DELETE("Income/{id}")
    Call<Income> deleteIncomeTransaction(@Path("id") String id,@Header("authorization") String token);
}
