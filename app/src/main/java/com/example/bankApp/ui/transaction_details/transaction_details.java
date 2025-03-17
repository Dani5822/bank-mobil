package com.example.bankApp.ui.transaction_details;

import static com.example.bankApp.data.connect.RetrofitClient.getInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.Expense;
import com.example.bankApp.data.model.Income;
import com.example.bankApp.data.model.RepeatableTransaction;
import com.example.bankApp.data.model.Transaction;
import com.example.bankApp.databinding.TransactionDetailsBinding;
import com.example.bankApp.global;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class transaction_details extends Fragment {

    private TransactionDetailsBinding binding;
    private Transaction transaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = TransactionDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }

    public void init() {
        String id = "";
        String token = ((global) getActivity().getApplication()).getAccess_token();
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        if (getArguments().getString("Income") != null) {
            id = getArguments().getString("Income");
            apiService.getIncomeById(id, token).enqueue(new Callback<Income>() {
                @Override
                public void onResponse(Call<Income> call, Response<Income> response) {
                    if (response.isSuccessful()) {
                        transaction = response.body();
                        updateUI();
                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<Income> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
            String finalId = id;
            binding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    apiService.deleteIncomeTransaction(finalId, token).enqueue(new Callback<Income>() {
                        @Override
                        public void onResponse(Call<Income> call, Response<Income> response) {
                            if (response.isSuccessful()) {
                                ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStack();
                            }
                        }

                        @Override
                        public void onFailure(Call<Income> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                }
            });
        } else {
            id = getArguments().getString("Expense");
            apiService.getExpenseById(id, token).enqueue(new Callback<Expense>() {
                @Override
                public void onResponse(Call<Expense> call, Response<Expense> response) {
                    if (response.isSuccessful()) {
                        transaction = response.body();
                        updateUI();
                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<Expense> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
            String finalId = id;
            binding.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    apiService.deleteExpenseTransaction(finalId, token).enqueue(new Callback<Expense>() {
                        @Override
                        public void onResponse(Call<Expense> call, Response<Expense> response) {
                            if (response.isSuccessful()) {
                                ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStack();
                            }
                        }

                        @Override
                        public void onFailure(Call<Expense> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                }
            });
        }

    }

    ;

    public void updateUI() {
        binding.total.setText(String.valueOf(transaction.getTotal()));
        binding.category.setText(transaction.getCategory());
        binding.description.setText(transaction.getDescription());
        binding.cardid.setText(transaction.getAccountId());
        binding.createdAt.setText(new SimpleDateFormat("yyyy-MM-dd").format(transaction.getCreatedAt()));
        if (transaction.getRepeatableTransactionId() != null) {
            binding.repetablelayout.setVisibility(binding.repetablelayout.VISIBLE);
            RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
            apiService.getRepeatableTransactionById(transaction.getRepeatableTransactionId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<RepeatableTransaction>() {
                @Override
                public void onResponse(Call<RepeatableTransaction> call, Response<RepeatableTransaction> response) {
                    if (response.isSuccessful()) {
                        RepeatableTransaction repeatableTransaction = response.body();
                        binding.startdate.setText(new SimpleDateFormat("yyyy-MM-dd").format(repeatableTransaction.getRepeatStart()));
                        binding.enddate.setText(new SimpleDateFormat("yyyy-MM-dd").format(repeatableTransaction.getRepeatEnd()));
                        binding.lastchange.setText(new SimpleDateFormat("yyyy-MM-dd").format(repeatableTransaction.getLastChange()));
                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<RepeatableTransaction> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });

        } else {
            binding.repetablelayout.setVisibility(binding.repetablelayout.GONE);
        }

    }

}
