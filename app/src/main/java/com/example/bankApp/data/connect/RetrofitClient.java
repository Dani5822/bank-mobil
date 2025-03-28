package com.example.bankApp.data.connect;

import android.content.Context;
import android.content.res.Resources;

import com.example.bankApp.R;
import com.example.bankApp.data.model.currency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String localhost = "http://10.0.2.2:3000/";
    private static final String api = "https://cdn.jsdelivr.net/npm/@fawazahmed0/";
    private static Retrofit retrofit;
    private static Retrofit euroRetrofit;

    public static Retrofit getInstance() {

        OkHttpClient client = new OkHttpClient().newBuilder().callTimeout(60, TimeUnit.SECONDS)       // Csatlakozási timeout
                .readTimeout(60, TimeUnit.SECONDS)    // Olvasási timeout (pl. adat érkezése a szerverről)
                .writeTimeout(60, TimeUnit.SECONDS)   // Írási timeout (pl. adat küldése a szerverre)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(localhost).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        }
        return retrofit;
    }

    public static Retrofit getEuroInstance(Context context) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(currency.class, new PersonDeserializer(context));
        Gson gson = gsonBuilder.create();
        if (euroRetrofit == null) {
            euroRetrofit = new Retrofit.Builder().baseUrl(api).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return euroRetrofit;
    }

    private static class PersonDeserializer implements JsonDeserializer<currency> {
        private Context context;

        public PersonDeserializer(Context context) {
            this.context = context;
        }

        @Override
        public currency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Resources res= this.context.getResources();
            List<String> currencyList = Arrays.asList(res.getStringArray(R.array.currency));
            JsonObject jsonObject = json.getAsJsonObject();
            currency currency = new currency();
            for (String name : currencyList) {
                if (jsonObject.has(name)) {
                    JsonObject currencyObject = jsonObject.getAsJsonObject(name);
                    try {
                        currency.setDate((new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.get("date").getAsString())));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    currency.setEur(new Gson().fromJson(currencyObject, Map.class));
                    currency.setName(name);
                    break;
                }
            }

            return currency;
        }
    }
}
