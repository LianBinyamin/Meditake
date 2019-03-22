package com.example.lian.meditake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class AllMedsAdapter extends RecyclerView.Adapter<AllMedsAdapter.AllMedsHolder> {

    private List<MedicationsTable> allMeds = new ArrayList<>();
    private FragmentManager fm;


    public AllMedsAdapter(Fragment fragment) {
        this.fm = fragment.getFragmentManager();
    }

    @NonNull
    @Override
    public AllMedsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row_medications_recycler, viewGroup, false);

        return new AllMedsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllMedsHolder allMedsHolder, int i) {
        final MedicationsTable currentMed = allMeds.get(i);
        allMedsHolder.nameTextView.setText(currentMed.getMedName());
        allMedsHolder.mgTextView.setText(allMedsHolder.mgTextView.getResources()
                .getString(R.string.mg_text_recycler, currentMed.getMg()));
        allMedsHolder.remindersTextView.setText(allMedsHolder.remindersTextView.getResources()
                .getString(R.string.reminders_text_recycler, currentMed.getAmountRemindersPerDay()));
        allMedsHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMedFragment dialog = DeleteMedFragment.newInstance(currentMed.getId());
                dialog.show(fm, "DeleteDialog");
                allMeds.remove(currentMed);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allMeds.size();
    }

    class AllMedsHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView mgTextView;
        private TextView remindersTextView;
        private Button btnRemove;

        public AllMedsHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_recycler_med);
            mgTextView = (TextView) itemView.findViewById(R.id.mg_recycler);
            remindersTextView = (TextView) itemView.findViewById(R.id.reminders_recycler);
            btnRemove = (Button) itemView.findViewById(R.id.btn_remove_med_recycler);
        }
    }

    public void setAllMeds(List<MedicationsTable> meds) {
        this.allMeds = meds;
        notifyDataSetChanged();
    }
}
