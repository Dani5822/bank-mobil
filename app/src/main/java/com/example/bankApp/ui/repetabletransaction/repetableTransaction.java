package com.example.bankApp.ui.repetabletransaction;

import static com.example.bankApp.data.connect.RetrofitClient.getInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.RepeatableTransaction;
import com.example.bankApp.databinding.FragmentRepetableTransactionBinding;
import com.example.bankApp.global;
import com.example.bankApp.ui.transaction_details.transaction_details;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class repetableTransaction extends Fragment {
    private FragmentRepetableTransactionBinding binding;
    private RetrofitApiService apiService;
    private String token;
    private String cardId;
    private RepeatableTransaction[] repeatableTransactions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRepetableTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        return root;
    }

    private void init() {
        apiService = getInstance().create(RetrofitApiService.class);
        token = ((global) getActivity().getApplication()).getAccess_token();
        cardId = getArguments().getString("activeCardId");

        apiService.getAllRepeatableTransactions(cardId, token).enqueue(new Callback<RepeatableTransaction[]>() {
            @Override
            public void onResponse(Call<RepeatableTransaction[]> call, Response<RepeatableTransaction[]> response) {
                repeatableTransactions = response.body();
                updateUI();
            }

            @Override
            public void onFailure(Call<RepeatableTransaction[]> call, Throwable t) {

            }
        });


    }

    private void updateUI() {
        if (repeatableTransactions != null) {
            var adapter = new repetableTransactionAdapter(Arrays.asList(repeatableTransactions), getContext(), token);
            binding.repetableList.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}