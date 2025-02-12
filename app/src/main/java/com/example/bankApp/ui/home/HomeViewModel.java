package com.example.bankApp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.currency;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<currency> currencyLiveData = new MutableLiveData<>();
    private currency cachedCurrency = null;
    private final RetrofitApiService apiService;

    public HomeViewModel(RetrofitApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<currency> getCurrency() {
        return currencyLiveData;
    }

    public void loadCurrency(String currencyCode) {
        if (cachedCurrency != null) {
            currencyLiveData.setValue(cachedCurrency);
            return;
        }

        apiService.getEur(currencyCode).enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cachedCurrency = response.body();
                    currencyLiveData.setValue(cachedCurrency);
                }
            }

            @Override
            public void onFailure(Call<currency> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
