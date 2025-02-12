package com.example.bankApp.ui.cardsinfo;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.connect.RetrofitClient;
import com.example.bankApp.data.model.Card;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardInfoViewModel extends AndroidViewModel {
    private final MutableLiveData<Card> cardLiveData = new MutableLiveData<>();
    private final RetrofitApiService retrofitApiService;

    public CardInfoViewModel(Application application) {
        super(application);
        retrofitApiService = RetrofitClient.getInstance().create(RetrofitApiService.class);
    }

    public LiveData<Card> getCardLiveData() {
        return cardLiveData;
    }


    public void fetchCardInfo(String cardId, String accessToken) {
        retrofitApiService.getUsersByCardID(cardId, accessToken).enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful()) {
                    cardLiveData.setValue(response.body());
                    System.out.println(cardLiveData.hasActiveObservers());
                } else {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                cardLiveData.setValue(null);
            }
        });
    }




}
