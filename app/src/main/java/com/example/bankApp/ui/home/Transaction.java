package com.example.bankApp.ui.home;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.connect.RetrofitClient;
import com.example.bankApp.data.model.Card;
import com.example.bankApp.data.model.RepeatableTransaction;
import com.example.bankApp.databinding.ActivityTransactionBinding;
import com.example.bankApp.global;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transaction extends AppCompatActivity {

    ActivityTransactionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hide the title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.back.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        });

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updateSpinner();
            if (checkedId == R.id.expense) {
                binding.content.setVisibility(binding.content.VISIBLE);
                binding.repeatlayout.setVisibility(binding.repeat.VISIBLE);
                binding.transferLayout.setVisibility(binding.transferLayout.GONE);
                if (binding.repeat.isChecked()) {
                    binding.repeatexpense.setVisibility(binding.repeatexpense.VISIBLE);
                } else {
                    binding.repeatexpense.setVisibility(binding.repeatexpense.GONE);
                }
            } else if(checkedId == R.id.income){
                binding.content.setVisibility(binding.content.VISIBLE);
                binding.repeatlayout.setVisibility(binding.repeat.GONE);
                binding.repeatexpense.setVisibility(binding.repeatexpense.GONE);
                binding.transferLayout.setVisibility(binding.transferLayout.GONE);
            }else{
                binding.content.setVisibility(binding.content.GONE);
                binding.transferLayout.setVisibility(binding.transferLayout.VISIBLE);

            }
        });

        binding.category.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    // Az alkalmazás sötét módban van
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                } else {
                    // Az alkalmazás világos módban van
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                }

            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });
        binding.datestart.setOnClickListener(v -> showDatePickerDialog(binding.datestart));

        binding.datened.setOnClickListener(v -> showDatePickerDialog(binding.datened));

        binding.repeat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.repeatexpense.setVisibility(binding.repeatexpense.VISIBLE);
            } else {
                binding.repeatexpense.setVisibility(binding.repeatexpense.GONE);
            }
        });

    }

    public void updateSpinner() {
        if (binding.radioGroup.getCheckedRadioButtonId() == R.id.expense) {
            binding.category.setAdapter(ArrayAdapter.createFromResource(this, R.array.expense, android.R.layout.simple_spinner_dropdown_item));
        } else {
            binding.category.setAdapter(ArrayAdapter.createFromResource(this, R.array.income, android.R.layout.simple_spinner_dropdown_item));
        }
    }

    public void init() {
        binding.radioGroup.check(R.id.expense);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getBaseContext(), R.array.expense,
                        android.R.layout.simple_spinner_item);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.category.setAdapter(staticAdapter);

        binding.category.setSelection(staticAdapter.getPosition("Other"));
        binding.save.setOnClickListener(v -> {
            binding.loading.setVisibility(View.VISIBLE);
            System.out.println(binding.loading.getVisibility());
            if (binding.radioGroup.getCheckedRadioButtonId() == -1) {
                binding.error.setText("Please select a type");
                binding.loading.setVisibility(View.GONE);
            } else if (binding.total.getText().toString().isEmpty()) {
                binding.error.setText("Please enter an amount");
                binding.loading.setVisibility(View.GONE);
            } else {
                binding.error.setText("");
            }
            RetrofitApiService apiService = RetrofitClient.getInstance().create(RetrofitApiService.class);
            String accessToken = ((global) getApplication()).getAccess_token();
            Intent intent = getIntent();
            String cardId = intent.getStringExtra("cardid");
            String userId = intent.getStringExtra("userId");
            if (binding.radioGroup.getCheckedRadioButtonId() == R.id.income) {
                apiService.createIncome(accessToken, Double.valueOf(binding.total.getText().toString()), binding.description.getText().toString(), cardId, binding.category.getSelectedItem().toString(), userId).enqueue(new retrofit2.Callback<com.example.bankApp.data.model.Income>() {
                    @Override
                    public void onResponse(retrofit2.Call<com.example.bankApp.data.model.Income> call, retrofit2.Response<com.example.bankApp.data.model.Income> response) {
                        if (response.isSuccessful()) {
                            setResult(Activity.RESULT_OK, new Intent());
                            finish();
                        } else {
                            binding.error.setText("Error");
                            System.out.println(response.message());
                            binding.loading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<com.example.bankApp.data.model.Income> call, Throwable t) {
                        System.out.println(t.getMessage());
                        binding.error.setText("Error");
                        binding.loading.setVisibility(View.GONE);
                    }
                });
            } else if(binding.radioGroup.getCheckedRadioButtonId() == R.id.expense){
                if (binding.repeat.isChecked()) {
                    if (Integer.valueOf(binding.ammount.getText().toString()) <= 0) {
                        System.out.println(Integer.valueOf(binding.ammount.getText().toString()));
                        binding.error.setText("Please enter a valid amount");
                        binding.loading.setVisibility(View.GONE);
                    } else if (Date.valueOf(binding.datestart.getText().toString()).compareTo(Date.valueOf(binding.datened.getText().toString())) > 0) {
                        binding.error.setText("Start date must be before end date");
                        binding.loading.setVisibility(View.GONE);
                    } else {

                        apiService.createRepeatableTransaction(accessToken, Double.valueOf(binding.total.getText().toString()),binding.category.getSelectedItem().toString(),binding.description.getText().toString(),cardId,Integer.valueOf(binding.ammount.getText().toString()),binding.metrik.getSelectedItem().toString(),binding.datestart.getText().toString(),binding.datened.getText().toString(),userId).enqueue(new Callback<RepeatableTransaction>() {
                            @Override
                            public void onResponse(Call<RepeatableTransaction> call, Response<RepeatableTransaction> response) {
                                if(response.isSuccessful()){
                                    setResult(Activity.RESULT_OK, new Intent());
                                    finish();
                                }
                                else{
                                    System.out.println(response.message());
                                    binding.error.setText("Error");
                                    binding.loading.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<RepeatableTransaction> call, Throwable t) {
                                binding.error.setText("Error");
                                System.out.println(t.getMessage());
                                binding.loading.setVisibility(View.GONE);
                            }
                        });
                    }
                } else {
                    apiService.createExpense(accessToken, Double.valueOf(binding.total.getText().toString()), binding.description.getText().toString(), cardId, binding.category.getSelectedItem().toString(), userId).enqueue(new retrofit2.Callback<com.example.bankApp.data.model.Expense>() {
                        @Override
                        public void onResponse(retrofit2.Call<com.example.bankApp.data.model.Expense> call, retrofit2.Response<com.example.bankApp.data.model.Expense> response) {
                            if (response.isSuccessful()) {
                                setResult(Activity.RESULT_OK, new Intent());
                                finish();
                            } else {
                                binding.error.setText("Error");
                                System.out.println(response.message());
                                binding.loading.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<com.example.bankApp.data.model.Expense> call, Throwable t) {
                            System.out.println(t.getMessage());
                            binding.error.setText("Error");
                            binding.loading.setVisibility(View.GONE);
                        }
                    });

                }
            }else{
                apiService.createTransfer(accessToken, Double.valueOf(binding.total.getText().toString()),binding.szamlaszam.getText().toString(),cardId,userId).enqueue(new Callback<Card>() {
                    @Override
                    public void onResponse(Call<Card> call, Response<Card> response) {
                        if(response.isSuccessful()){
                            setResult(Activity.RESULT_OK, new Intent());
                            finish();
                        }
                        else{
                            System.out.println(response.message());
                            binding.error.setText("Error");
                            binding.loading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Card> call, Throwable t) {
                        binding.error.setText("Error");
                        System.out.println(t.getMessage());
                        binding.loading.setVisibility(View.GONE);
                    }
                });
            }

        });
        LocalDate currentDate = LocalDate.now();
        binding.datestart.setText(currentDate.toString());
        binding.datened.setText(currentDate.plusDays(1).toString());

        binding.metrik.setAdapter(ArrayAdapter.createFromResource(this, R.array.metrik, android.R.layout.simple_spinner_dropdown_item));
        binding.metrik.setSelection(0);

        binding.ammount.setText("1");


    }

    private void showDatePickerDialog(TextView date) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
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


}