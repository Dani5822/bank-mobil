package com.example.bankApp.ui.cardsinfo;


import static com.example.bankApp.data.connect.RetrofitClient.getEuroInstance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.connect.RetrofitClient;
import com.example.bankApp.data.model.Card;
import com.example.bankApp.data.model.LoggedInUser;
import com.example.bankApp.databinding.FragmentCardinfoBinding;
import com.example.bankApp.global;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cardinfo extends Fragment {

    private static String cardId;
    private FragmentCardinfoBinding binding;
    private String accessToken;
    private CardInfoViewModel cardInfoViewModel;
    private final Observer<Card> cardObserver = this::updateUI;
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        cardInfoViewModel.fetchCardInfo(cardId, accessToken);
                    }
                }
            });

    public static String getCardId() {
        return cardId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardinfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cardId = getArguments().getString("cardId");
        cardInfoViewModel = new ViewModelProvider(this).get(CardInfoViewModel.class);

        cardInfoViewModel.getCardLiveData().observe(this.getViewLifecycleOwner(), cardObserver);

        init();
        binding.addUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), popUpAddUser.class);
            intent.putExtra("cardId", cardId);
            someActivityResultLauncher.launch(intent);

        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Törlés");
                builder.setMessage("Biztosan törölni szeretnéd?");
                builder.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RetrofitApiService retrofitApiService = RetrofitClient.getInstance().create(RetrofitApiService.class);
                        if (((global) getActivity().getApplication()).getId().equals(cardInfoViewModel.getCardLiveData().getValue().getOwnerId())) {
                            retrofitApiService.deleteCard(accessToken, cardId).enqueue(new Callback<Card>() {
                                @Override
                                public void onResponse(Call<Card> call, Response<Card> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getContext(), "Sikeres törlés", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(view).navigate(R.id.nav_gallery);
                                    } else {
                                        System.out.println(response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Card> call, Throwable t) {
                                    System.out.println(t.getMessage());
                                }
                            });

                        } else {
                            retrofitApiService.disconnectUser(accessToken, cardId, ((global) getActivity().getApplication()).getId()).enqueue(new Callback<Card>() {
                                @Override
                                public void onResponse(Call<Card> call, Response<Card> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getContext(), "Sikeres törlés", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(view).navigate(R.id.nav_gallery);
                                    } else {
                                        System.out.println(response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Card> call, Throwable t) {
                                    System.out.println(t.getMessage());
                                }
                            });
                        }

                    }
                });
                builder.setNegativeButton("Nem", null);
                AlertDialog dialog = builder.create();
                builder.show();
            }
        });
        return root;
    }

    public void init() {
        accessToken = ((global) getActivity().getApplication()).getAccess_token();
        cardInfoViewModel.fetchCardInfo(cardId, accessToken);
    }

    private void updateUI(Card card) {

        if (card != null) {
            ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                    .createFromResource(getContext(), R.array.currencies,
                            android.R.layout.simple_spinner_item);

            staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            binding.currency.setSelection(staticAdapter.getPosition(card.getCurrency()));
            binding.currency.setAdapter(staticAdapter);
            binding.card.cardname.setText(card.getOwnerName());
            if (card.getCurrency().equals("HUF")) {
                int x = Math.round(card.getTotal());
                binding.card.balance.setText(String.format("%d ", x) + binding.currency.getSelectedItem().toString());
            } else {
                binding.card.balance.setText(String.format("%.2f ", card.getTotal()) + binding.currency.getSelectedItem().toString());
            }

            binding.card.id.setText(card.getId());

            binding.save.setOnClickListener(v -> {
                binding.loading.setVisibility(View.VISIBLE);
                String currency = binding.currency.getSelectedItem().toString();
                cardInfoViewModel.fetchCardInfo(cardId, ((global) getActivity().getApplication()).getAccess_token());
            });

            binding.currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    RetrofitApiService currencyService = getEuroInstance().create(RetrofitApiService.class);
                    currencyService.getRatesJson(binding.currency.getSelectedItem().toString().toLowerCase()).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                JsonObject eur = response.body().getAsJsonObject(binding.currency.getSelectedItem().toString().toLowerCase());
                                double rate = eur.get("huf").getAsDouble();
                                if (binding.currency.getSelectedItem().equals("HUF")) {
                                    int x = (int) Math.round(card.getTotal() / rate);
                                    binding.card.balance.setText(String.format("%d ", x) + binding.currency.getSelectedItem().toString());
                                } else {
                                    binding.card.balance.setText(String.format("%.2f ", card.getTotal() / rate) + binding.currency.getSelectedItem().toString());
                                }

                            } else {
                                System.out.println(response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            updateCardUserList(card);

        }
    }

    private void updateCardUserList(Card card) {
        binding.table.removeAllViews();
        for (LoggedInUser user : card.getUsers()) {
            TableRow row = new TableRow(getContext());
            row.setWeightSum(4);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            TextView name = new TextView(getContext());
            TextView email = new TextView(getContext());
            Button delete = new Button(getContext());

            name.setText(user.getFirstname() + " " + user.getLastname());
            name.setLayoutParams(lp);
            name.setTextSize(15);
            name.setTextAppearance(R.style.edittext);

            email.setText(user.getEmail());
            email.setLayoutParams(lp);
            email.setTextSize(15);
            email.setTextAppearance(R.style.edittext);

            lp.weight = 2;


            delete.setText("Delete");
            delete.setLayoutParams(lp);
            delete.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            delete.setBackgroundColor(getResources().getColor(R.color.red, null));
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(v -> {
                RetrofitApiService retrofitApiService = RetrofitClient.getInstance().create(RetrofitApiService.class);
                retrofitApiService.disconnectUser(((global) getActivity().getApplication()).getAccess_token(), cardId, user.getId()).enqueue(new Callback<Card>() {
                    @Override
                    public void onResponse(Call<Card> call, Response<Card> response) {
                        if (response.isSuccessful()) {
                            binding.table.removeView(row);
                        } else {
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Card> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            });
            row.addView(name);
            row.addView(email);
            row.addView(delete);
            if (!user.getId().equals(card.getOwnerId()) && ((global) getActivity().getApplication()).getId().equals(card.getOwnerId())) {
                delete.setVisibility(View.VISIBLE);
                delete.setEnabled(true);
            } else {
                delete.setVisibility(View.INVISIBLE);
                delete.setEnabled(false);
            }
            binding.table.addView(row);
        }
    }
}