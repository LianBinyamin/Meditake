package com.example.lian.meditake;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MedifriendFragment extends Fragment {
    private OnMedifriendFragmentInteractionListener mListener;
    private CardView cardView;
    private FloatingActionButton buttonAddMedifriend;
    private TextView nameMedifriend, phoneMedifriend, medifriendDescription;
    private Button btnDeleteMedifriend;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static final String SMS_PREFS = "SMS_PREFS";
    public static final String SMS_PERMISSION = "sms_permission";


    public MedifriendFragment() {
        // Required empty public constructor
    }

//    public static MedifriendFragment newInstance(String param1, String param2) {
//        MedifriendFragment fragment = new MedifriendFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medi_friend, container, false);

        cardView = (CardView) view.findViewById(R.id.card_view);
        buttonAddMedifriend = (FloatingActionButton) view.findViewById(R.id.btn_add_medifriend);
        nameMedifriend = (TextView) view.findViewById(R.id.name_card);
        phoneMedifriend = (TextView) view.findViewById(R.id.phone_card);
        btnDeleteMedifriend = (Button) view.findViewById(R.id.delete_medifriend);
        medifriendDescription = (TextView) view.findViewById(R.id.medifriend_description);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. check if the user has denied permission
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            // Permission has already been granted
            medifriendDescription.setText(R.string.medifriend_description);
            SharedPreferences sp = getActivity().getSharedPreferences(SMS_PREFS, MODE_PRIVATE);
            sp.edit().putBoolean(SMS_PERMISSION, true).apply();
        }

        SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.PREFS_MEDIFRIEND, MODE_PRIVATE);
        String name = prefs.getString(MainActivity.NAME_MEDIFRIEND_PREFS, null);
        String phone = prefs.getString(MainActivity.PHONE_MEDIFRIEND_PREFS, null);
        if (name == null && phone == null) {
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);
            nameMedifriend.setText(name);
            phoneMedifriend.setText(phone);
        }

        buttonAddMedifriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences(SMS_PREFS, MODE_PRIVATE);
                boolean smsEnabled = sp.getBoolean(SMS_PERMISSION, false);
                if (smsEnabled) {
                    if (cardView.getVisibility() == View.VISIBLE) {
                        Toast.makeText(getContext(), R.string.toast_remove_medifriend, Toast.LENGTH_LONG).show();
                    } else {
                        if (mListener != null) {
                            mListener.addMedifriendButtonClicked();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), R.string.toast_medifriend_permission, Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDeleteMedifriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                deleteMedifriend();
            }
        });

        return view;
    }

    public void deleteMedifriend() {
        getContext().getSharedPreferences(MainActivity.PREFS_MEDIFRIEND, MODE_PRIVATE).edit()
                .putString(MainActivity.NAME_MEDIFRIEND_PREFS, null).apply();
        getContext().getSharedPreferences(MainActivity.PREFS_MEDIFRIEND, MODE_PRIVATE).edit()
                .putString(MainActivity.PHONE_MEDIFRIEND_PREFS, null).apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    SharedPreferences sp = getActivity().getSharedPreferences(SMS_PREFS, MODE_PRIVATE);
                    sp.edit().putBoolean(SMS_PERMISSION, true).apply();
                    medifriendDescription.setText(R.string.medifriend_description);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    medifriendDescription.setText(R.string.medifriend_permission_msg);
                }
                return;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMedifriendFragmentInteractionListener) {
            mListener = (OnMedifriendFragmentInteractionListener) context;
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

    public interface OnMedifriendFragmentInteractionListener {
        void addMedifriendButtonClicked();
    }
}
