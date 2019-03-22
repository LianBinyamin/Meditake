package com.example.lian.meditake;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class RefillMedsReminderDialogFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "numberOfMeds";
    private int numberOfMeds;
    private EditText editText;

    private RefillMedsReminderListener mListener;

    public RefillMedsReminderDialogFragment() {
        // Required empty public constructor
    }

    public static RefillMedsReminderDialogFragment newInstance(int param1) {
        RefillMedsReminderDialogFragment fragment = new RefillMedsReminderDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            numberOfMeds = getArguments().getInt(ARG_PARAM1);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_refill_meds_reminder_dialog, null);

        builder.setView(view).setTitle(R.string.refill_reminder_dialog_title);

        editText = (EditText)view.findViewById(R.id.edit_amount_meds);

        editText.setText(getResources().getString(R.string.amount_meds_edit_text_dialog, numberOfMeds));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editText.getText().toString().equals("")) {
                    numberOfMeds = Integer.parseInt(s.toString());
                }
            }
        });

        builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {
                    mListener.RefillMedsReminderPositiveClick(RefillMedsReminderDialogFragment.this, numberOfMeds);
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.RefillMedsReminderNegativeClick(RefillMedsReminderDialogFragment.this);
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RefillMedsReminderListener) {
            mListener = (RefillMedsReminderListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RefillMedsReminderListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface RefillMedsReminderListener {
        void RefillMedsReminderPositiveClick(DialogFragment dialog, int numberOfMeds);
        void RefillMedsReminderNegativeClick(DialogFragment dialog);
    }
}
