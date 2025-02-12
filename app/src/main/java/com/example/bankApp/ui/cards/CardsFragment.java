package com.example.bankApp.ui.cards;

import static com.example.bankApp.data.connect.RetrofitClient.getInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.Card;
import com.example.bankApp.data.model.CardsAdapter;
import com.example.bankApp.databinding.FragmentCardsBinding;
import com.example.bankApp.global;
import com.example.bankApp.ui.home.HomeFragment;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardsFragment extends Fragment {

    ListView listView;
    private FragmentCardsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CardsViewModel cardsViewModel =
                new ViewModelProvider(this).get(CardsViewModel.class);

        binding = FragmentCardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitApiService service = getInstance().create(RetrofitApiService.class);
                service.createCard(((global) getActivity().getApplication()).getAccess_token(),"HUF",HomeFragment.getUser().getFirstname()+" "+HomeFragment.getUser().getLastname(),HomeFragment.getUser().getId() ).enqueue(new Callback<Card>() {

                    @Override
                    public void onResponse(Call<Card> call, Response<Card> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(CardsFragment.super.getContext(), "Sikeres kártya hozzáadás", Toast.LENGTH_SHORT).show();
                            init();
                        }else {
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

        return root;
    }

    public void init() {
        listView=binding.cardlistview;
        RetrofitApiService service = getInstance().create(RetrofitApiService.class);
        service.getallcardbyuserid(HomeFragment.getUser().getId(),((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Card[]>() {
            @Override
            public void onResponse(Call<Card[]> call, Response<Card[]> response) {
                if(response.isSuccessful()){
                    List<Card> cards = Arrays.asList(response.body());
                    CardsAdapter adapter = new CardsAdapter(getContext(),cards,getParentFragmentManager());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Card[]> call, Throwable t) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}