package com.example.bankApp.ui.cardsinfo;


import static com.example.bankApp.data.connect.RetrofitClient.getInstance;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.Card;
import com.example.bankApp.global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class popUpAddUser extends AppCompatActivity {
    Button back, send;
    EditText email;
    String cardid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pop_up_add_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hide the title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        init();

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int) ( width*.8), (int) (height*.25));

        back.setOnClickListener(view -> {setResult(Activity.RESULT_CANCELED, new Intent());finish();});

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
                String accessToken = ((global) getApplication()).getAccess_token();
                apiService.updateCardUserList(accessToken, cardid, email.getText().toString()).enqueue(new Callback<Card>() {
                    @Override
                    public void onResponse(Call<Card> call, Response<Card> response) {
                            Intent resultIntent=new Intent();
                        if (response.isSuccessful()) {
                            Toast.makeText(popUpAddUser.this, "User hozzáadva", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK, resultIntent);
                          finish();

                        } else {
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Card> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });

            }
        });
    }
        public void init () {
            back = findViewById(R.id.back);
            send = findViewById(R.id.send);
            email = findViewById(R.id.email);
            cardid=getIntent().getStringExtra("cardId");
        }
}