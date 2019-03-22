package com.example.lian.meditake;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SetDosageDialogFragment extends DialogFragment {

    private SetDosageDialogListener mListener;
    private double dosageAmountMg;
    private Button minus;
    private Button plus;
    private EditText amountEditText;
    private static final String ARG_PARAM1 = "dosageAmount";

    public SetDosageDialogFragment() {
        // Required empty public constructor
    }

    public static SetDosageDialogFragment newInstance(double param1) {
        SetDosageDialogFragment fragment = new SetDosageDialogFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            dosageAmountMg = getArguments().getDouble(ARG_PARAM1);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_set_dosage_dialog, null);

        builder.setView(view).setTitle(R.string.dosage_dialog_title);

        minus = (Button) view.findViewById(R.id.minus_btn);
        plus = (Button) view.findViewById(R.id.plus_btn);
        amountEditText = (EditText) view.findViewById(R.id.edit_amount_dosage);

        amountEditText.setText(getResources().getString(R.string.dosage_amount_edit_text, dosageAmountMg));

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    dosageAmountMg = Double.parseDouble(s.toString());
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Double.parseDouble(amountEditText.getText().toString())) > 1.0) {
                    dosageAmountMg -= 0.25;
                    amountEditText.setText(getResources().getString(R.string.dosage_amount_edit_text, dosageAmountMg));
                }
                else {
                    Toast.makeText(getContext(),"Min Dosage 1 mg", Toast.LENGTH_SHORT).show();
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Double.parseDouble(amountEditText.getText().toString())) < 1000.0) {
                    dosageAmountMg+=0.25;
                    amountEditText.setText(getResources().getString(R.string.dosage_amount_edit_text, dosageAmountMg));
                }
            }
        });

        builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {
                    mListener.onSetDosageDialogPositiveClick(SetDosageDialogFragment.this, dosageAmountMg);
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onSetDosageDialogNegativeClick(SetDosageDialogFragment.this);
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SetDosageDialogListener) {
            mListener = (SetDosageDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SetDosageDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SetDosageDialogListener {
        void onSetDosageDialogPositiveClick(DialogFragment dialog, double amount);

        void onSetDosageDialogNegativeClick(DialogFragment dialog);
    }
}
