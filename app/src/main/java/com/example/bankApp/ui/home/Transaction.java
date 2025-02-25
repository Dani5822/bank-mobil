package com.example.bankApp.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.connect.RetrofitClient;
import com.example.bankApp.databinding.ActivityTransactionBinding;
import com.example.bankApp.global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

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

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int) ( width*.8), (int) (height*.8));

         binding= ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.back.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED, new Intent());finish();
        });

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updateSpinner();
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


    }

    public  void updateSpinner(){
        if(binding.radioGroup.getCheckedRadioButtonId()==R.id.expense){
        binding.category.setAdapter(ArrayAdapter.createFromResource(this, R.array.expense, android.R.layout.simple_spinner_dropdown_item));}
        else{
            binding.category.setAdapter(ArrayAdapter.createFromResource(this, R.array.income, android.R.layout.simple_spinner_dropdown_item));
        }
    }

    public void init(){
        binding.radioGroup.check(R.id.expense);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getBaseContext(),R.array.expense,
                        android.R.layout.simple_spinner_item);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.category.setAdapter(staticAdapter);

        binding.category.setSelection(staticAdapter.getPosition("Other"));
        binding.save.setOnClickListener(v -> {
            if(binding.radioGroup.getCheckedRadioButtonId()==-1){
                binding.error.setText("Please select a type");
            }
            else if(binding.total.getText().toString().isEmpty()){
                binding.error.setText("Please enter an amount");
            }
            else{
                binding.error.setText("");
            }
            RetrofitApiService apiService = RetrofitClient.getInstance().create(RetrofitApiService.class);
            String accessToken = ((global) getApplication()).getAccess_token();
            Intent intent = getIntent();
            String cardId = intent.getStringExtra("cardid");
            String userId = intent.getStringExtra("userId");
            if(binding.radioGroup.getCheckedRadioButtonId()==R.id.income){
                apiService.createIncome(accessToken,Double.valueOf(binding.total.getText().toString()),binding.description.getText().toString(),cardId,binding.category.getSelectedItem().toString(),binding.vendor.getText().toString(),userId).enqueue(new retrofit2.Callback<com.example.bankApp.data.model.Income>() {
                    @Override
                    public void onResponse(retrofit2.Call<com.example.bankApp.data.model.Income> call, retrofit2.Response<com.example.bankApp.data.model.Income> response) {
                        if(response.isSuccessful()){
                            setResult(Activity.RESULT_OK, new Intent());finish();
                        }else{
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<com.example.bankApp.data.model.Income> call, Throwable t) {
                        System.out.println(t.getMessage());
                        binding.error.setText("Error");
                    }
                });
            }else{
                apiService.createExpense(accessToken,Double.valueOf(binding.total.getText().toString()),binding.description.getText().toString(),cardId,binding.category.getSelectedItem().toString(),binding.vendor.getText().toString(),userId).enqueue(new retrofit2.Callback<com.example.bankApp.data.model.Expense>() {
                    @Override
                    public void onResponse(retrofit2.Call<com.example.bankApp.data.model.Expense> call, retrofit2.Response<com.example.bankApp.data.model.Expense> response) {
                        if(response.isSuccessful()){
                            setResult(Activity.RESULT_OK, new Intent());finish();
                        }else{
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<com.example.bankApp.data.model.Expense> call, Throwable t) {
                        System.out.println(t.getMessage());
                        binding.error.setText("Error");
                    }
                });
            }
        });

    }


}