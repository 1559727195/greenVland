package com.massky.greenlandvland.ui;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.massky.greenlandvland.R;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * Created by masskywcy on 2018-11-09.
 */

public class Fragment_contentcomplain extends Fragment {
    private View view;
    private CardView cv_contentcoplain;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_contentcomplain,container,false);
        cv_contentcoplain= (CardView) view.findViewById(R.id.cv_contentcomplain);
        cv_contentcoplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ComplainActivity.class));
            }
        });
        return view;
    }
}
