package com.example.bankApp.data.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.bankApp.R;
import com.example.bankApp.ui.transaction_details.transaction_details;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionAdapter extends BaseAdapter {

    private final ArrayList<Transaction> tarnsactionList;
    private final Context context;

    public TransactionAdapter(ArrayList<Transaction> tarnsactionList, Context context) {
        this.tarnsactionList = tarnsactionList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tarnsactionList.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.transactionlistviewitem, viewGroup, false);

        TextView transactionName = view.findViewById(R.id.name);
        TextView transactionAmount = view.findViewById(R.id.amount);
        TextView transactioncategory = view.findViewById(R.id.category);
        ImageView transactionIcon = view.findViewById(R.id.kep);

        transactionName.setText(tarnsactionList.get(i).getCategory());
        transactionAmount.setText(new BigDecimal(tarnsactionList.get(i).getTotal()).toPlainString());
        String ido=new SimpleDateFormat("yyyy MM dd").format(tarnsactionList.get(i).getCreatedAt());
        transactioncategory.setText(ido);

        if (tarnsactionList.get(i).getClass() == Income.class) {
            switch (tarnsactionList.get(i).getCategory()) {
                case "Salary":
                    transactionIcon.setImageResource(R.drawable.salary);
                    break;
                case "Other":
                    transactionIcon.setImageResource(R.drawable.income);
                    break;
                case "Transaction":
                    transactionIcon.setImageResource(R.drawable.income);
                    break;
            }
        } else {
            switch (tarnsactionList.get(i).getCategory()) {
                case "Shopping":
                    transactionIcon.setImageResource(R.drawable.shopping);
                    break;
                case "Transport":
                    transactionIcon.setImageResource(R.drawable.transport);
                    break;
                case "Rent":
                    transactionIcon.setImageResource(R.drawable.rent);
                    break;
                case "Other":
                    transactionIcon.setImageResource(R.drawable.expense);
                    break;
                case "Transaction":
                    transactionIcon.setImageResource(R.drawable.expense);
                    break;
            }

        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                if(tarnsactionList.get(i).getClass() == Income.class){
                    bundle.putString("Income", tarnsactionList.get(i).getId());
                }else{
                    bundle.putString("Expense", tarnsactionList.get(i).getId());
                }

                Navigation.findNavController(view).navigate(R.id.transaction_details,bundle);
            }
        });

        return view;
    }
}
