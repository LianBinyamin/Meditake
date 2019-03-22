package com.example.lian.meditake;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    public interface OnHomeFragmentInteractionListener {
        void onAddButtonClicked();
    }

    List<MedicationsWithReminders> todayMeds = new ArrayList<>();
    private FloatingActionButton btnAddMedication;
    private OnHomeFragmentInteractionListener listener;
    private TextView textView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MedicationsDataBase db = MedicationsDataBase.getInstance(getActivity().getApplication());
                todayMeds = db.medicationsDao().getMedicationsWithRemindersByDay(getDay());
            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        textView = (TextView)view.findViewById(R.id.no_reminders);
        textView.setVisibility(View.GONE);

        if (todayMeds.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_today_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        TodayRemindersAdapter adapter = new TodayRemindersAdapter();
        adapter.setAllMeds(todayMeds);
        recyclerView.setAdapter(adapter);

        btnAddMedication = (FloatingActionButton)view.findViewById(R.id.btn_add_med_home);

        btnAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onAddButtonClicked();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteractionListener) {
            listener = (OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMedicationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static String getDay() {
        String day = "";
        Calendar calendar = Calendar.getInstance();
        int theDay = calendar.get(Calendar.DAY_OF_WEEK);

        switch (theDay) {
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
        }

        Log.d("CALANDER", day);

        return day;
    }

}
