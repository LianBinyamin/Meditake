package com.example.lian.meditake;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class TimeAndDosesAlertDialogFragment extends DialogFragment {

    public interface TimeAndDosesDialogListener {
        void onTimeAndDosesDialogPositiveClick(DialogFragment dialog, double numberOfDoses, int tvNumber, int hour, int minutes);
        void onTimeAndDosesDialogNegativeClick(DialogFragment dialog);
    }

    private Button minus;
    private Button plus;
    private EditText dosesEditText;
    private double numberOfDoses=1.00;
    private static final String ARG_PARAM1 = "textViewNumber";
    private int textViewDoseToPass;
    private TimePicker timePicker;
    private int hourPicked = -1;
    private int minutesPicked = -1;

    private TimeAndDosesDialogListener mListener;

    public TimeAndDosesAlertDialogFragment() {
    }

    public static TimeAndDosesAlertDialogFragment newInstance(int textViewNumber) {
        TimeAndDosesAlertDialogFragment fragment = new TimeAndDosesAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, textViewNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments() != null) {
            textViewDoseToPass = getArguments().getInt(ARG_PARAM1);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_time_and_doses_alert_dialog, null);

        builder.setView(view);

        builder.setTitle(R.string.set_time_dose_title_dialog);

        // Add action buttons
        builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {
                    mListener.onTimeAndDosesDialogPositiveClick(TimeAndDosesAlertDialogFragment.this,
                            numberOfDoses, textViewDoseToPass, hourPicked, minutesPicked);
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onTimeAndDosesDialogNegativeClick(TimeAndDosesAlertDialogFragment.this);
                        }
                    }
                });

        //timePicker = (TimePicker)view.findViewById(R.id.time_picker);
        minus = (Button)view.findViewById(R.id.minus_btn_doses);
        plus = (Button)view.findViewById(R.id.plus_btn_doses);
        dosesEditText = (EditText)view.findViewById(R.id.edit_amount_dose);
        timePicker = (TimePicker)view.findViewById(R.id.time_picker);

        dosesEditText.setText(getResources().getString(R.string.number_doses_edit_text, numberOfDoses));

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(dosesEditText.getText().toString()) > 0.25) {
                    numberOfDoses -= 0.25;
                    dosesEditText.setText(getResources().getString(R.string.number_doses_edit_text, numberOfDoses));
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(dosesEditText.getText().toString()) < 3.00) {
                    numberOfDoses += 0.25;
                    dosesEditText.setText(getResources().getString(R.string.number_doses_edit_text, numberOfDoses));
                }
            }
        });

        final Calendar c = Calendar.getInstance();
        hourPicked = c.get(Calendar.HOUR_OF_DAY);
        minutesPicked = c.get(Calendar.MINUTE);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hourPicked = hourOfDay;
                minutesPicked = minute;
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TimeAndDosesDialogListener) {
            mListener = (TimeAndDosesDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TimeAndDosesDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
