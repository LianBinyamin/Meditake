package com.example.lian.meditake;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Delete;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MedicationFragment extends Fragment {

    private OnMedicationFragmentInteractionListener mListener;
    List<MedicationsTable> allMedications = new ArrayList<>();
    TextView textView;

    public AllMedsAdapter getAdapter() {
        return adapter;
    }

    AllMedsAdapter adapter;

    public MedicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medication, container, false);

        textView = (TextView)view.findViewById(R.id.no_meds);
        textView.setVisibility(View.GONE);

        FloatingActionButton buttonAddMedication = (FloatingActionButton)view.findViewById(R.id.btn_add_med);
        buttonAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onButtonAddMedicationPressed();
                }
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MedicationsDataBase db = MedicationsDataBase.getInstance(getActivity().getApplication());
                allMedications = db.medicationsDao().getAllMedications();
            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (allMedications.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new AllMedsAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setAllMeds(allMedications);

//        MedicationsDataBase db = MedicationsDataBase.getInstance(getContext().getApplicationContext());
//
//        db.medicationsDao().getAllMedications().observe(this, new Observer<List<MedicationsTable>>() {
//            @Override
//            public void onChanged(@Nullable List<MedicationsTable> allMeds) {
//                adapter.setAllMeds(allMeds);
//            }
//        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMedicationFragmentInteractionListener) {
            mListener = (OnMedicationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMedicationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMedicationFragmentInteractionListener {
        void onButtonAddMedicationPressed();
    }
}
