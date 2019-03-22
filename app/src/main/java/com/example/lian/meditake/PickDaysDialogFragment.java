package com.example.lian.meditake;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PickDaysDialogFragment extends DialogFragment {

    private PickDaysDialogListener mListener;
    private static final String ARG_PARAM1 = "daysStringArray";
    private static final String ARG_PARAM2 = "daysBooleanArray";
    private static final String ARG_PARAM3 = "selectedDaysSet";
    private String[] listDaysString;
    private boolean[] listDaysBoolean;
    private ArrayList<String> selectedDaysSet;

    public PickDaysDialogFragment() {
        // Required empty public constructor
    }

    public static PickDaysDialogFragment newInstance(String[] days, boolean[] checked, ArrayList<String> selectedDays) {
        PickDaysDialogFragment fragment = new PickDaysDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, days);
        args.putSerializable(ARG_PARAM2, checked);
        args.putSerializable(ARG_PARAM3, selectedDays);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        listDaysString = getArguments().getStringArray(ARG_PARAM1);
        listDaysBoolean = getArguments().getBooleanArray(ARG_PARAM2);
        selectedDaysSet = getArguments().getStringArrayList(ARG_PARAM3);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.pick_days_dialog_title);  //setView erased

        builder.setMultiChoiceItems(listDaysString, listDaysBoolean, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    if(!selectedDaysSet.contains(listDaysString[which])) {
                        selectedDaysSet.add(listDaysString[which]);
                        listDaysBoolean[which] = true;
                    }
                }
                else {
                    selectedDaysSet.remove(listDaysString[which]);
                    listDaysBoolean[which] = false;
                }
            }
        });

        builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {

                    if (selectedDaysSet.size() == listDaysString.length) {
                        //all days were checked! need to check everyday rbt
                        mListener.allDaysSelected(PickDaysDialogFragment.this);
                        //mListener.onPickDaysDialogNegativeCheck(PickDaysDialogFragment.this, selectedDaysSet);
                    }
                    else {
                        mListener.onPickDaysDialogPositiveCheck(PickDaysDialogFragment.this, listDaysBoolean, selectedDaysSet);
                    }
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onPickDaysDialogNegativeCheck(PickDaysDialogFragment.this, selectedDaysSet);
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PickDaysDialogListener) {
            mListener = (PickDaysDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PickDaysDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface PickDaysDialogListener {
        void onPickDaysDialogPositiveCheck(DialogFragment dialog, boolean[] checkedDays, ArrayList<String> selected);
        void onPickDaysDialogNegativeCheck(DialogFragment dialog, ArrayList<String> selected);
        void allDaysSelected(DialogFragment dialog);
    }
}
