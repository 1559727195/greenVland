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

public class Fragment_contentpayment extends Fragment {
    private View view;
    private CardView cv_contentmaintain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_contentpayment,container,false);
        cv_contentmaintain= (CardView) view.findViewById(R.id.cv_contentpayment);
        cv_contentmaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PaymentActivity.class));
            }
        });
        return view;
    }
}
