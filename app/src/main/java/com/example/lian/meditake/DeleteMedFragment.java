package com.example.lian.meditake;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DeleteMedFragment extends DialogFragment {

    private OnDeleteFragmentListener mListener;
    private static final String ARG_PARAM1 = "medication";
    private int id;

    public DeleteMedFragment() {
        // Required empty public constructor
    }

    public static DeleteMedFragment newInstance(int id) {
        DeleteMedFragment fragment = new DeleteMedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            this.id = getArguments().getInt(ARG_PARAM1);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.delete_title).setMessage(R.string.delete_dialog_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onPositiveDeleteClicked(id);
                            
                        }
                    }
                }).setNegativeButton(R.string.no_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onNegativeDeleteClicked(DeleteMedFragment.this);
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDeleteFragmentListener) {
            mListener = (OnDeleteFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDeleteFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDeleteFragmentListener {
        void onPositiveDeleteClicked(int id);
        void onNegativeDeleteClicked(DialogFragment dialogFragment);
    }
}
