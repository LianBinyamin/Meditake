package com.example.lian.meditake;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Pattern;


public class AddMedifriendDialogFragment extends DialogFragment {

    private OnAddMedifriendDialogFragmentListener mListener;
    private EditText nameEditText;
    private Spinner spinnerPhone;
    private EditText phoneEditText;
    private String prefix;
    private String phone;
    private final int PHONE_LENGTH = 7;

    public AddMedifriendDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_medifriend_dialog, null);

        builder.setView(view);
        builder.setTitle(R.string.add_medifriend_dialog_title)
                .setMessage(R.string.add_medifriend_dialog_msg)
                .setPositiveButton(R.string.insert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing here
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener!= null) {
                    mListener.onNegativeMedifriendClicked(AddMedifriendDialogFragment.this);
                }
            }
        });

        nameEditText = (EditText)view.findViewById(R.id.name_edit_text);
        spinnerPhone = (Spinner)view.findViewById(R.id.spinner_medifriend);
        phoneEditText = (EditText)view.findViewById(R.id.phone_edit_text);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.phone_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(spinnerAdapter);
        spinnerPhone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getSelectedItemPosition()) {
                    case 0:
                        default:
                        prefix = "050";
                        break;
                    case 1:
                        prefix = "052";
                        break;
                    case 2:
                        prefix = "054";
                        break;
                    case 3:
                        prefix = "058";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                prefix = "050";
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isValidPhone(phoneEditText.getText().toString())) {
                    Toast.makeText(getContext(), R.string.toast_error_phone, Toast.LENGTH_SHORT).show();
                }
                else if (nameEditText.getText().toString().isEmpty() || prefix.isEmpty() ||
                        phoneEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.toast_error_medifriend, Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    phone = prefix+phoneEditText.getText().toString();
                    if (mListener != null) {
                        mListener.onAddMedifriendPositiveClicked(nameEditText.getText().toString(), phone);
                    }
                }
            }
        });


        return dialog;
    }

    public boolean isValidPhone(String phone)  {
        boolean check;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() != PHONE_LENGTH) {
                check = false;
            }
            else {
                check = true;
            }
        }
        else {
            check = false;
        }
        return check;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddMedifriendDialogFragmentListener) {
            mListener = (OnAddMedifriendDialogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddMedifriendDialogFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddMedifriendDialogFragmentListener {
        void onAddMedifriendPositiveClicked(String name, String phone);
        void onNegativeMedifriendClicked(DialogFragment dialogFragment);
    }
}
