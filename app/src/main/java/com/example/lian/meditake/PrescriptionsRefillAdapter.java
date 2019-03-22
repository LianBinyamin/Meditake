package com.example.lian.meditake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class PrescriptionsRefillAdapter extends RecyclerView.Adapter<PrescriptionsRefillAdapter.PrescriptionsRefillHolder> {
    private List<MedicationsWithPrescriptions> allPrescriptions;

    @NonNull
    @Override
    public PrescriptionsRefillHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row_recycler, viewGroup, false);

        return new PrescriptionsRefillHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionsRefillHolder prescriptionsRefillHolder, int i) {
        MedicationsWithPrescriptions currentPrescription = allPrescriptions.get(i);
        prescriptionsRefillHolder.nameTextView.setText(currentPrescription.medication.getMedName());
        prescriptionsRefillHolder.dosesTextView.setText(prescriptionsRefillHolder.dosesTextView
                .getContext().getResources().getString(R.string.mg_text_recycler, currentPrescription.medication.getMg()));
        String str = AddMedicationActivity.setTimeDetails(currentPrescription.prescriptionReminder.getHour(),
                currentPrescription.prescriptionReminder.getMinutes());
        prescriptionsRefillHolder.timeTextView.setText(str);
    }

    @Override
    public int getItemCount() {
        return allPrescriptions.size();
    }

    class PrescriptionsRefillHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView timeTextView;
        private TextView dosesTextView;

        public PrescriptionsRefillHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_recycler);
            timeTextView = (TextView) itemView.findViewById(R.id.time_recycler);
            dosesTextView = (TextView) itemView.findViewById(R.id.doses_recycler);
        }
    }

    public void setAllMeds(List<MedicationsWithPrescriptions> prescriptions) {
        this.allPrescriptions = prescriptions;
        notifyDataSetChanged();
    }

//    public String setTimeDetails(PrescriptionReminder prescription) {
//        String str="";
//        if (prescription.getHour() <= 9 && prescription.getHour() >=1) {
//            if (prescription.getMinutes() <= 9 && prescription.getMinutes() >=1) {
//                str = "0" + prescription.getHour() + ":0" + prescription.getMinutes();
//            }
//            else {
//                str = "0" + prescription.getHour() + ":" +prescription.getMinutes();
//            }
//        }
//        else if (prescription.getMinutes() <= 9 && prescription.getMinutes() >=1) {
//            str = prescription.getHour() + ":0" + prescription.getMinutes();
//        }
//        else {
//            str = prescription.getHour() + ":" + prescription.getMinutes();
//        }
//
//        return str;
//    }
}
