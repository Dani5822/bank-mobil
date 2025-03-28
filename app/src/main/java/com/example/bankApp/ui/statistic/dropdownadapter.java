package com.example.bankApp.ui.statistic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.bankApp.R;
import com.example.bankApp.data.model.Transaction;
import com.example.bankApp.data.model.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

public class dropdownadapter extends ArrayAdapter<Transaction> {

    public dropdownadapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.statisticlistitemview, parent, false);
        }

        ArrayList<Transaction> x=new ArrayList<>();
        x.add(getItem(position));
        ListView transactionDetail = convertView.findViewById(R.id.category);
        transactionDetail.setAdapter(new TransactionAdapter(x, getContext()));
        return convertView;
    }

}