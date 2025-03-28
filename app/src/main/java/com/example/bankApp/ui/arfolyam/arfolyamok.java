package com.example.bankApp.ui.arfolyam;

import static com.example.bankApp.data.connect.RetrofitClient.getEuroInstance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.currency;
import com.example.bankApp.databinding.FragmentArfolyamokBinding;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class arfolyamok extends Fragment {

    private FragmentArfolyamokBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArfolyamokBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    private void init() {
        List<String> currencyList = Arrays.asList(getResources().getStringArray(R.array.currency));
        binding.loading.setVisibility(View.VISIBLE);
        new FetchCurrencyTask().execute(currencyList.toArray(new String[0]));
    }

    private class FetchCurrencyTask extends AsyncTask<String, Void, List<List<currency>>> {

        @Override
        protected List<List<currency>> doInBackground(String... currencies) {
            List<List<currency>> currencys = new ArrayList<>();
            for (String currency : currencies) {
                currencys.add(fetchCurrency(currency));
            }

            return currencys;
        }

        @Override
        protected void onPostExecute(List<List<currency>> result) {
            super.onPostExecute(result);
            var oszlop1=result.subList(0, result.size()/2);
            var oszlop2=result.subList(result.size()/2, result.size());
            arfolyam adapter = new arfolyam(getContext(), oszlop1,oszlop2);
            binding.currency.setAdapter(adapter);
            binding.loading.setVisibility(View.GONE);
        }
    }

    private List<currency> fetchCurrency(String currency) {
        List<currency> currencys = Arrays.asList(null, null);
        CountDownLatch latch = new CountDownLatch(2);
        LocalDate x = LocalDate.now();
        RetrofitApiService currencyService = getEuroInstance(getContext()).create(RetrofitApiService.class);
        currencyService.GetCurrencyByDate(x.toString(), currency).enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currencys.set(0, response.body());
                } else {
                    System.out.println("Error fetching current currency: " + response.message());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<currency> call, Throwable t) {
                System.out.println("Error fetching current currency: " + t.getMessage());
                latch.countDown();
            }
        });

        currencyService.GetCurrencyByDate(x.minusDays(1).toString(), currency).enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currencys.set(1, response.body());
                } else {
                    System.out.println("Error fetching past currency: " + response.message());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<currency> call, Throwable t) {
                System.out.println("Error fetching past currency: " + t.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await(); // Wait for both API calls to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return currencys;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}