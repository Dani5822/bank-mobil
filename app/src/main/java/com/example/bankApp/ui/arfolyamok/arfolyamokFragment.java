package com.example.bankApp.ui.arfolyamok;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.example.bankApp.arfolyam;
import com.example.bankApp.databinding.FragmentArfolyamokBinding;

public class arfolyamokFragment extends Fragment {

    private FragmentArfolyamokBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArfolyamokBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }

    public void init(){


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}