package com.example.bankApp.data.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bankApp.R;

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
        transactionAmount.setText(tarnsactionList.get(i).getTotal() + "");
        transactioncategory.setText(tarnsactionList.get(i).getDescription());
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

        return view;
    }
}
