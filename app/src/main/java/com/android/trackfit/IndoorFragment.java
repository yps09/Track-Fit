package com.android.trackfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndoorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndoorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IndoorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndoorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndoorFragment newInstance(String param1, String param2) {
        IndoorFragment fragment = new IndoorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_indoor,container, false);

        Button squat = view.findViewById(R.id.startsquat);
        Button jumping = view.findViewById(R.id.strtjumping);
        Button pushUp = view.findViewById(R.id.startpushup);
        Button pullUp = view.findViewById(R.id.startpullups);
        Button diamondPushup = view.findViewById(R.id.start_pushuptxt);

        squat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SquatActivity.class);
                startActivity(intent);
            }
        });
        jumping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),JumpingJackActivity.class);
                startActivity(intent);
            }
        });

        pushUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PushUpActivity.class);
                startActivity(intent);
            }
        });
        pullUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PullupActivity.class);
                startActivity(intent);
            }
        });
        diamondPushup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DiamondActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
}