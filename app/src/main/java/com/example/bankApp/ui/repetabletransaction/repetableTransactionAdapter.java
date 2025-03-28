package com.example.bankApp.ui.repetabletransaction;

import static com.example.bankApp.data.connect.RetrofitClient.getInstance;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.example.bankApp.R;
import com.example.bankApp.data.connect.RetrofitApiService;
import com.example.bankApp.data.model.RepeatableTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class repetableTransactionAdapter extends BaseAdapter {
    private List<RepeatableTransaction> repeatableTransactions;
    private Context context;
    private String token;

    public repetableTransactionAdapter(List<RepeatableTransaction> repeatableTransactions, Context context,String token) {
        this.repeatableTransactions = new ArrayList<>(repeatableTransactions);
        this.context = context;
        this.token = token;
    }

    @Override
    public int getCount() {
        return repeatableTransactions.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.repetabletransactionitem, viewGroup, false);

        TextView transactionName = view.findViewById(R.id.name);
        TextView transactionStartDate = view.findViewById(R.id.startdate);
        TextView transactionEndDate = view.findViewById(R.id.enddate);
        TextView total = view.findViewById(R.id.total);
        transactionName.setText(repeatableTransactions.get(i).getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        transactionStartDate.setText(simpleDateFormat.format(repeatableTransactions.get(i).getRepeatStart()));
        transactionEndDate.setText(simpleDateFormat.format(repeatableTransactions.get(i).getRepeatEnd()));
        total.setText(String.valueOf(Math.round(repeatableTransactions.get(i).getTotal())) + " HUF");


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("repeatableTransactionId", repeatableTransactions.get(i).getId());
                Navigation.findNavController(view).navigate(R.id.repetable_detail, bundle);
            }
        });


        return view;
    }
}
