package com.example.bankApp.ui.home;

import static com.example.bankApp.data.connect.RetrofitClient.getEuroInstance;
import static com.example.bankApp.data.connect.RetrofitClient.getInstance;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.Card;
import com.example.bankApp.data.model.Expense;
import com.example.bankApp.data.model.Income;
import com.example.bankApp.data.model.LoggedInUser;
import com.example.bankApp.data.model.OnSwipeTouchListener;
import com.example.bankApp.data.model.RepeatableTransaction;
import com.example.bankApp.data.model.Transaction;
import com.example.bankApp.data.model.TransactionAdapter;
import com.example.bankApp.data.model.currency;
import com.example.bankApp.databinding.FragmentHomeBinding;
import com.example.bankApp.global;

import java.math.BigInteger;
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
    private final ArrayList<Income> incomes = new ArrayList<>();
    private final ArrayList<Expense> expenses = new ArrayList<>();
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
                        for (Card cards : user.getCards()) {
                            updateRepeatTransactions(cards.getId());
                        }
                    }
                    updateUI();
                } else {
                    System.out.println("Error fetching user: " + response.message());
                }
            }


            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                System.out.println("Error fetching user: " + t.getMessage());
            }
        });


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

        binding.felvetel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.bankApp.ui.home.Transaction.class);
                intent.putExtra("cardid", activeCard.getId());
                intent.putExtra("userId", user.getId());
                someActivityResultLauncher.launch(intent);
            }
        });

        binding.statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("activeCardId",activeCard.getId());
                Navigation.findNavController(view).navigate(R.id.statisticFragment,bundle);
            }
        });

        binding.arfolyam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.arfolyamok);
            }
        });

        binding.repetableinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("activeCardId",activeCard.getId());
                Navigation.findNavController(view).navigate(R.id.repetableTransaction,bundle);
            }
        });
    }

    private void updateRepeatTransactions(String cardId) {
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);

                apiService.updateRepeatableTransactions( ((global) getActivity().getApplication()).getAccess_token(), cardId, user.getId()).enqueue(new Callback<RepeatableTransaction>() {
                    @Override
                    public void onResponse(Call<RepeatableTransaction> call, Response<RepeatableTransaction> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateUI();
                        } else {
                            System.out.println("Error fetching repeatable transactions: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<RepeatableTransaction> call, Throwable t) {
                        System.out.println("Error fetching repeatable transactions: " + t.getMessage());
                    }
                });
            }



    private void updateUI() {
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        Card card = activeCard;
        if (binding!=null&&card != null && binding.cardlayout!=null) {
            updateTotal();
            binding.cardlayout.cardname.setText(card.getOwnerName());

            apiService.getallexbycardid(card.getId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Expense[]>() {
                @Override
                public void onResponse(Call<Expense[]> call, Response<Expense[]> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Expense[] expenseslocal = response.body();
                        expenses.clear();
                        expenses.addAll(Arrays.asList(expenseslocal));

                    } else {
                        expenses.clear();
                        System.out.println("Error fetching expenses: " + response.message());
                    }
                    updatetransaction();
                }

                @Override
                public void onFailure(Call<Expense[]> call, Throwable t) {
                    expenses.clear();
                    System.out.println("Error fetching expenses: " + t.getMessage());
                }
            });
            apiService.getallinbycardid(card.getId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Income[]>() {
                @Override
                public void onResponse(Call<Income[]> call, Response<Income[]> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Income[] incomeslocal = response.body();
                        incomes.clear();
                        incomes.addAll(Arrays.stream(incomeslocal).collect(Collectors.toList()));

                    } else {
                        incomes.clear();
                        System.out.println("Error fetching incomes: " + response.message());
                    }
                    updatetransaction();
                }

                @Override
                public void onFailure(Call<Income[]> call, Throwable t) {
                    incomes.clear();
                    System.out.println("Error fetching incomes: " + t.getMessage());
                }
            });
        }
    }

    public void updatetransaction() {
        transactions.clear();
        transactions.addAll(incomes);
        transactions.addAll(expenses);
        notifyDataSetChanged();
        if (binding!=null&&transactions.size() > 0 && transactions != null) {
            transactions.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
            binding.transactionsListView.setAdapter(new TransactionAdapter(transactions, HomeFragment.this.getContext()));
        }
        updateTotal();
    }

    public void updateTotal() {
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        apiService.getCardById(activeCard.getId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful() && response.body() != null &&binding!=null&& binding.cardlayout != null) {
                    activeCard = response.body();
                    if (activeCard.getCurrency().equals("HUF")) {
                        BigInteger x = BigInteger.valueOf(activeCard.getTotal());
                        binding.cardlayout.balance.setText(x + " HUF");
                    } else {
                        binding.cardlayout.balance.setText(String.format("%.2f", activeCard.getTotal()) + " " + activeCard.getCurrency());
                    }
                } else {
                    System.out.println("Error fetching card: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                System.out.println("Error fetching card: " + t.getMessage());
            }
        });

    }

    private void notifyDataSetChanged() {
        if (binding!=null&&binding.transactionsListView!=null&&binding.transactionsListView.getAdapter() != null) {
            ((TransactionAdapter) binding.transactionsListView.getAdapter()).notifyDataSetChanged();
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
                slideCardvissza(card, 0, -toX);
                slideCardvissza(card, 100, 0);
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

    private void slideCardvissza(View card, int time, float toX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(card, "translationX", toX);
        animator.setAutoCancel(true);
        animator.setDuration(time);
        animator.start();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        init();
                        updatetransaction();
                    }
                }
            }
    );
}