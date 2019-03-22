package com.example.lian.meditake;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class TodayRemindersAdapter extends RecyclerView.Adapter<TodayRemindersAdapter.TodayRemindersHolder> {
    private List<MedicationsWithReminders> allMedsToday;

    @NonNull
    @Override
    public TodayRemindersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row_recycler, viewGroup, false);

        return new TodayRemindersHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayRemindersHolder todayRemindersHolder, int i) {
        MedicationsWithReminders currentMed = allMedsToday.get(i);
        todayRemindersHolder.nameTextView.setText(currentMed.medication.getMedName());
        todayRemindersHolder.dosesTextView.setText(todayRemindersHolder.dosesTextView.getContext()
                .getResources().getString(R.string.doses_text_recycler, currentMed.reminder.getDoses()));
        String str = AddMedicationActivity.setTimeDetails(currentMed.reminder.getHour(), currentMed.reminder.getMinutes());
        todayRemindersHolder.timeTextView.setText(str);
    }

    @Override
    public int getItemCount() {
        return allMedsToday.size();
    }

    class TodayRemindersHolder extends RecyclerView.ViewHolder {
        // each data item is just a string
        private TextView nameTextView;
        private TextView timeTextView;
        private TextView dosesTextView;

        public TodayRemindersHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_recycler);
            timeTextView = (TextView) itemView.findViewById(R.id.time_recycler);
            dosesTextView = (TextView) itemView.findViewById(R.id.doses_recycler);
        }
    }

    public void setAllMeds(List<MedicationsWithReminders> meds) {
        this.allMedsToday = meds;
        notifyDataSetChanged();
    }

}
