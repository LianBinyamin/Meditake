package com.example.lian.meditake;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PrescriptionsRefillFragment extends Fragment {

    private List<MedicationsWithPrescriptions> allPrescriptions = new ArrayList<>();
    private TextView textView;
    //private OnFragmentInteractionListener mListener;

    public PrescriptionsRefillFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prescriptions_refill, container, false);

        textView = (TextView)view.findViewById(R.id.no_prescriptions);
        textView.setVisibility(View.GONE);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MedicationsDataBase db = MedicationsDataBase.getInstance(getActivity().getApplication());
                allPrescriptions = db.medicationsDao().getAllPrescriptionReminders();

            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (allPrescriptions.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_prescriptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        PrescriptionsRefillAdapter adapter = new PrescriptionsRefillAdapter();
        adapter.setAllMeds(allPrescriptions);
        recyclerView.setAdapter(adapter);


        return view;
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
