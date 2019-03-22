package com.example.lian.meditake;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface OnTimePickerFragmentListener {
        void onTimePickerTextViewTouched(int hourOfDay, int minute, int textViewToPass);
    }

    private static final String ARG_PARAM1 = "textViewNumber";
    private int textViewTimeToPass;

    private OnTimePickerFragmentListener mListener;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(int textViewNumber) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, textViewNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        textViewTimeToPass = getArguments().getInt(ARG_PARAM1);

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            OnTimePickerFragmentListener listener = (OnTimePickerFragmentListener) context;

            if (listener != null)
                this.registerListener(listener);

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onTimePickerTextViewTouched");
        }
    }

    public void registerListener(OnTimePickerFragmentListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (TimePickerFragment.this.mListener != null) {
            TimePickerFragment.this.mListener.onTimePickerTextViewTouched(hourOfDay, minute, textViewTimeToPass);
        }
    }

}
