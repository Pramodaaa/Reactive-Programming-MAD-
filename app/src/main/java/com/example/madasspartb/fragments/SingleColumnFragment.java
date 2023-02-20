package com.example.madasspartb.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madasspartb.R;
import com.example.madasspartb.adaptors.ImageAdapter;
import com.example.madasspartb.utility.GlobalClass;

import java.util.ArrayList;
import java.util.List;


public class SingleColumnFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Bitmap> imageList;
    private RecyclerView recyclerView;
    GlobalClass sharedData;

    public SingleColumnFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SingleColumnFragment newInstance(String param1, String param2) {
        SingleColumnFragment fragment = new SingleColumnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageList = new ArrayList<>();
        sharedData = GlobalClass.getInstance();
        imageList = sharedData.getImageList();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_column, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.oneColRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        ImageAdapter listAdapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(listAdapter);
    }
}