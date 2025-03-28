package com.example.bankApp.ui.repetabletransaction;

import static com.example.bankApp.data.connect.RetrofitClient.getInstance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.RepeatableTransaction;
import com.example.bankApp.databinding.FragmentRepetableDetailBinding;
import com.example.bankApp.databinding.TransactionDetailsBinding;
import com.example.bankApp.global;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class repetable_detail extends Fragment {

    private FragmentRepetableDetailBinding binding;
    private String token;
    private String repeatableTransactionId;
    private RepeatableTransaction repeatableTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRepetableDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }

    private void init() {
        token= ((global) getActivity().getApplication()).getAccess_token();
        repeatableTransactionId = getArguments().getString("repeatableTransactionId");
        getRepetable();;


    }

    private void updateUI() {
        binding.name.setText(repeatableTransaction.getName());

        binding.total.setText(String.valueOf(repeatableTransaction.getTotal()));

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.expense,
                        android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.category.setAdapter(staticAdapter);
        binding.category.setSelection(staticAdapter.getPosition(repeatableTransaction.getCategory()));

        binding.description.setText(repeatableTransaction.getDescription());
        binding.expensecount.setText(String.valueOf(repeatableTransaction.getExpenses().length));

        ArrayAdapter<CharSequence> staticAdapter1 = ArrayAdapter
                .createFromResource(getContext(), R.array.metrik,
                        android.R.layout.simple_spinner_item);
        staticAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.metrik.setAdapter(staticAdapter1);
        binding.metrik.setSelection(staticAdapter1.getPosition(repeatableTransaction.getRepeatMetric()));

        binding.ammount.setText(String.valueOf(repeatableTransaction.getRepeatAmount()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binding.datestart.setText(dateFormat.format(repeatableTransaction.getRepeatStart()));

        binding.dateend.setText(dateFormat.format(repeatableTransaction.getRepeatEnd()));

        binding.lastchange.setText(dateFormat.format(repeatableTransaction.getLastChange()));


        binding.datestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new Date().before(repeatableTransaction.getRepeatStart())){
                    showDatePickerDialog(binding.datestart, repeatableTransaction.getRepeatStart());
                }else{
                    Toast.makeText(getContext(), "Nem tudod megváltoztatni a kezdő dátumot többé", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.dateend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(binding.dateend, repeatableTransaction.getRepeatEnd());
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitApiService apiService= getInstance().create(RetrofitApiService.class);
                apiService.updateRepeatableTransaction(
                        token,
                        repeatableTransactionId,
                        Double.parseDouble(binding.total.getText().toString()),
                        binding.category.getSelectedItem().toString(),
                        binding.description.getText().toString(),
                        Integer.parseInt(binding.ammount.getText().toString()),
                        binding.metrik.getSelectedItem().toString(),
                        binding.datestart.getText().toString(),
                        binding.dateend.getText().toString(),
                        binding.name.getText().toString()
                ).enqueue(new retrofit2.Callback<RepeatableTransaction>() {
                    @Override
                    public void onResponse(retrofit2.Call<RepeatableTransaction> call, retrofit2.Response<RepeatableTransaction> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getContext(), "Sikeres módosítás", Toast.LENGTH_SHORT).show();
                            getRepetable();
                        }else{
                            System.out.println(response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<RepeatableTransaction> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Törlés");
                builder.setMessage("Biztosan törölni szeretnéd? (Vele törlöd az összes költséget is)");
                builder.setPositiveButton("Igen", (dialog, which) -> {
                    RetrofitApiService apiService= getInstance().create(RetrofitApiService.class);
                    apiService.deleteRepeatableTransaction(repeatableTransactionId, token).enqueue(new retrofit2.Callback<RepeatableTransaction>() {
                        @Override
                        public void onResponse(retrofit2.Call<RepeatableTransaction> call, retrofit2.Response<RepeatableTransaction> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getContext(), "Sikeres törlés", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }else{
                                System.out.println(response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<RepeatableTransaction> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                });
                builder.setNegativeButton("Nem", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();

            }
        });

        binding.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Törlés");
                builder.setMessage("Biztosan törölni szeretnéd? (A költségek megmaradnak)");
                builder.setPositiveButton("Igen", (dialog, which) -> {
                    RetrofitApiService apiService= getInstance().create(RetrofitApiService.class);
                    apiService.stopRepeatableTransaction(repeatableTransactionId, token).enqueue(new retrofit2.Callback<RepeatableTransaction>() {
                        @Override
                        public void onResponse(retrofit2.Call<RepeatableTransaction> call, retrofit2.Response<RepeatableTransaction> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getContext(), "Sikeres törlés", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }else{
                                System.out.println(response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<RepeatableTransaction> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                });
                builder.setNegativeButton("Nem", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();

            }
        });
    }

    private void showDatePickerDialog(TextView date, Date date1) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String formattedDate = "";
                    if (month1 < 9) {
                        formattedDate = year1 + "-0" + (month1 + 1) + "-" + dayOfMonth;
                    } else {
                        formattedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    }
                    date.setText(formattedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void getRepetable() {
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        apiService.getRepeatableTransactionByIdWithExpenses(repeatableTransactionId, token).enqueue(new retrofit2.Callback<RepeatableTransaction>() {
            @Override
            public void onResponse(retrofit2.Call<RepeatableTransaction> call, retrofit2.Response<RepeatableTransaction> response) {
                if (response.isSuccessful()) {
                    repeatableTransaction = response.body();
                    updateUI();
                }else{
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RepeatableTransaction> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}