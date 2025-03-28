package com.example.bankApp.ui.arfolyam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bankApp.R;
import com.example.bankApp.data.model.currency;

import java.util.List;


public class arfolyam extends BaseAdapter {

    private final List<List<currency>> oszlop1;
    private final List<List<currency>> oszlop2;
    private final Context context;

    public arfolyam(Context context, List<List<currency>> oszlop1,List<List<currency>> oszlop2) {
        this.context = context;
        this.oszlop1 = oszlop1;
        this.oszlop2 = oszlop2;
    }

    @Override
    public int getCount() {
        return oszlop1.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.activity_arfolyam, viewGroup, false);

        TextView currency = view.findViewById(R.id.oszlop1currency);
        TextView currentrate = view.findViewById(R.id.oszlop1currentrate);
        TextView change = view.findViewById(R.id.oszlop1change);
        ImageView icon = view.findViewById(R.id.oszlop1iconbal);
        double currentCurrency = oszlop1.get(i).get(0).getEur().getOrDefault("huf", 0.0);
        double pastcurrency = oszlop1.get(i).get(1).getEur().getOrDefault("huf", 0.0);
        currency.setText(oszlop1.get(i).get(0).getName());
        double szazalek = pastcurrency != 0 ? ((currentCurrency - pastcurrency) / pastcurrency) * 100 : 0;
        currentrate.setText(String.format("%.2f HUF", currentCurrency));
        change.setText(String.format("%.2f", pastcurrency) + " (" + String.format("%.2f", szazalek) + "%)");
        icon.setImageResource(szazalek >= 0 ? R.drawable.up : R.drawable.down);

        TextView currency2 = view.findViewById(R.id.oszlop2currency);
        TextView currentrate2 = view.findViewById(R.id.oszlop2currentrate);
        TextView change2 = view.findViewById(R.id.oszlop2change);
        ImageView icon2 = view.findViewById(R.id.oszlop2iconbal);
        double currentCurrency2 = oszlop2.get(i).get(0).getEur().getOrDefault("huf", 0.0);
        double pastcurrency2 = oszlop2.get(i).get(1).getEur().getOrDefault("huf", 0.0);
        currency2.setText(oszlop2.get(i).get(0).getName());
        double szazalek2 = pastcurrency2 != 0 ? ((currentCurrency2 - pastcurrency2) / pastcurrency2) * 100 : 0;
        currentrate2.setText(String.format("%.2f HUF", currentCurrency2));
        change2.setText(String.format("%.2f", pastcurrency2) + " (" + String.format("%.2f", szazalek2) + "%)");
        icon2.setImageResource(szazalek2 >= 0 ? R.drawable.up : R.drawable.down);

        return view;
    }
}