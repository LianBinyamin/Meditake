package com.example.lian.meditake;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


public class SosFragment extends Fragment {

    private OnSosFragmentInteractionListener mListener;
    private ImageButton btnSos;

    public SosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sos, container, false);

        btnSos = (ImageButton)view.findViewById(R.id.btn_sos);

        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.PREFS_MEDIFRIEND, MODE_PRIVATE);
                String name = prefs.getString(MainActivity.NAME_MEDIFRIEND_PREFS, null);
                String phone = prefs.getString(MainActivity.PHONE_MEDIFRIEND_PREFS, null);
                if (name == null && phone == null) {
                    Toast.makeText(getContext(), R.string.sos_toast, Toast.LENGTH_SHORT).show();
                }
                else {
                    if (mListener != null) {
                        mListener.onSosButtonClicked();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSosFragmentInteractionListener) {
            mListener = (OnSosFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnSosFragmentInteractionListener {
        void onSosButtonClicked();
    }
}
