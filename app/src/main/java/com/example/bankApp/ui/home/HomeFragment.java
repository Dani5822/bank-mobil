package com.example.bankApp.ui.home;

import static com.example.bankApp.data.connect.RetrofitClient.getEuroInstance;
import static com.example.bankApp.data.connect.RetrofitClient.getInstance;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.Card;
import com.example.bankApp.data.model.Expense;
import com.example.bankApp.data.model.Income;
import com.example.bankApp.data.model.LoggedInUser;
import com.example.bankApp.data.model.OnSwipeTouchListener;
import com.example.bankApp.data.model.Transaction;
import com.example.bankApp.data.model.TransactionAdapter;
import com.example.bankApp.data.model.currency;
import com.example.bankApp.databinding.FragmentHomeBinding;
import com.example.bankApp.global;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static LoggedInUser user;
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private currency pastcurrency;
    private currency currentCurrency;
    private int db;
    private Card activeCard;

    public static LoggedInUser getUser() {
        return user;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        String id = ((global) getActivity().getApplication()).getId();
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);

        apiService.getUserById(id, ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {

                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    transactions.clear();
                    if (user.getCards().length > 0) {
                        activeCard = user.getCards()[0];
                    }
                        updateUI();
                }else{
                    System.out.println("Error fetching user: " + response.message());
                }
            }


            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                System.out.println("Error fetching user: " + t.getMessage());
            }
        });


        fetchPastCurrency(1);
        db = 0;
        binding.cardlayout.card.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() throws InterruptedException {
                super.onSwipeLeft();
                db++;
                if (db == user.getCards().length) {
                    db = 0;
                }

                slideCard(binding.cardlayout.frame, -binding.cardlayout.card.getWidth());
                activeCard = user.getCards()[db];
                updateUI();
            }

            @Override
            public void onSwipeRight() throws InterruptedException {
                super.onSwipeRight();
                db--;
                if (db < 0) {
                    db = user.getCards().length - 1;
                }
                slideCard(binding.cardlayout.frame, binding.cardlayout.card.getWidth());
                activeCard = user.getCards()[db];
                updateUI();
            }
        });

    }

    private void fetchPastCurrency(int retryCount) {
        if (retryCount <= 0) {
            return;
        }

        LocalDate x = LocalDate.now();
        RetrofitApiService currencyService = getEuroInstance().create(RetrofitApiService.class);
        currencyService.GetCurrencyByDate(x.toString(), "eur").enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentCurrency = response.body();
                    updatechange();

                } else {
                    fetchPastCurrency(retryCount - 1);
                }
            }

            @Override
            public void onFailure(Call<currency> call, Throwable t) {

            }
        });

        if (LocalTime.now().getHour() >= 16) {
            x = x.minusDays(0);
        } else {
            x = x.minusDays(1);
        }
        currencyService.GetCurrencyByDate(x.toString(), "eur").enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pastcurrency = response.body();
                    updatechange();

                } else {
                    fetchPastCurrency(retryCount - 1);
                }
            }

            @Override
            public void onFailure(Call<currency> call, Throwable t) {
                fetchPastCurrency(retryCount - 1);
            }
        });
    }

    private void updateUI() {
        transactions.clear();
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        Card card = activeCard;
        if (card != null) {
            if (card.getCurrency().equals("HUF")) {
                int x = Math.round(card.getTotal());
                binding.cardlayout.balance.setText(x + " HUF");
            } else {
                binding.cardlayout.balance.setText(String.format("%.2f", card.getTotal()) + " " + card.getCurrency());
            }
            binding.cardlayout.cardname.setText(card.getOwnerName());

            apiService.getallexbycardid(card.getId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Expense[]>() {
                @Override
                public void onResponse(Call<Expense[]> call, Response<Expense[]> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Transaction[] expenses = response.body();
                        transactions.addAll(Arrays.asList(expenses));
                        updatetransaction();
                    } else {
                        System.out.println("Error fetching expenses: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Expense[]> call, Throwable t) {
                    System.out.println("Error fetching expenses: " + t.getMessage());
                }
            });

            apiService.getallinbycardid(card.getId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Income[]>() {
                @Override
                public void onResponse(Call<Income[]> call, Response<Income[]> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Transaction[] incomes = response.body();
                        transactions.addAll(Arrays.stream(incomes).collect(Collectors.toList()));
                        updatetransaction();
                    } else {
                        System.out.println("Error fetching incomes: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Income[]> call, Throwable t) {
                    System.out.println("Error fetching incomes: " + t.getMessage());
                }
            });
        }else {
            binding.cardlayout.card.setImageResource(R.drawable.nocard);
        }
    }

    public void updatetransaction() {
        if (transactions.size() > 0 && transactions != null) {
            transactions.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
            binding.transactionsListView.setAdapter(new TransactionAdapter(transactions, HomeFragment.this.getContext()));
        }
    }

    public void updatechange() {
        if (pastcurrency != null && currentCurrency != null) {
            binding.arfolyam1.currency.setText("EUR");
            double huf = pastcurrency.getEur().getOrDefault("huf", 0.0);
            double currentHuf = currentCurrency.getEur().getOrDefault("huf", 0.0);
            double szazalek = huf != 0 ? ((currentHuf - huf) / huf) * 100 : 0;
            binding.arfolyam1.currentrate.setText(String.format("%.2f HUF", currentHuf));
            binding.arfolyam1.change.setText(String.format("%.2f", huf) + " (" + String.format("%.2f", szazalek) + "%)");
            binding.arfolyam1.iconbal.setImageResource(szazalek >= 0 ? R.drawable.up : R.drawable.down);

            binding.arfolyam2.currency.setText("USD");
            double pastUsd = huf / pastcurrency.getEur().getOrDefault("usd", 0.0);
            double usd = currentHuf / currentCurrency.getEur().getOrDefault("usd", 0.0);
            double szazalek2 = pastUsd != 0 ? ((usd - pastUsd) / pastUsd) * 100 : 0;
            binding.arfolyam2.currentrate.setText(String.format("%.2f HUF", usd));
            binding.arfolyam2.change.setText(String.format("%.2f", pastUsd) + " (" + String.format("%.2f", szazalek2) + "%)");
            binding.arfolyam2.iconbal.setImageResource(szazalek2 >= 0 ? R.drawable.up : R.drawable.down);
        } else {
            binding.arfolyam1.change.setText("N/A (0%)");
            binding.arfolyam2.change.setText("N/A (0%)");
        }
    }

    private void slideCard(View card, float toX) throws InterruptedException {
        ObjectAnimator animator = ObjectAnimator.ofFloat(card, "translationX", toX);
        animator.setAutoCancel(true);
        animator.setDuration(100);
        animator.start();
        animator.addListener(new ObjectAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                binding.cardlayout.card.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateUI();
                slideCardvissza(card,0, -toX);
                slideCardvissza(card,100, 0);
                binding.cardlayout.card.setClickable(true);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }


        });
    }

    private void slideCardvissza(View card,int time, float toX)  {
        ObjectAnimator animator = ObjectAnimator.ofFloat(card, "translationX", toX);
        animator.setAutoCancel(true);
        animator.setDuration(time);
        animator.start();
    }
}