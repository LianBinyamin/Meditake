package com.example.lian.meditake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MedicationFragment.OnMedicationFragmentInteractionListener,
        HomeFragment.OnHomeFragmentInteractionListener, DeleteMedFragment.OnDeleteFragmentListener,
        MedifriendFragment.OnMedifriendFragmentInteractionListener, AddMedifriendDialogFragment.OnAddMedifriendDialogFragmentListener
        , SosFragment.OnSosFragmentInteractionListener {

    public static final int ADD_ACTIVITY_REQUEST = 1;
    public static final String NAME_MEDIFRIEND_PREFS = "medifriend_name";
    public static final String PHONE_MEDIFRIEND_PREFS = "medifriend_phone";
    private FragmentManager fm;
    private FragmentTransaction ft;
    final static String PREFS_MEDIFRIEND = "MyPrefsMedifriendFile";
    private Fragment fragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init first view
        fragment = new HomeFragment();
        setFragment(fragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        setFragment(fragment);
                        return true;
                    case R.id.navigation_medications:
                        fragment = new MedicationFragment();
                        setFragment(fragment);
                        return true;
                    case R.id.navigation_prescriptions:
                        fragment = new PrescriptionsRefillFragment();
                        setFragment(fragment);
                        return true;
                    case R.id.navigation_medifriend:
                        fragment = new MedifriendFragment();
                        setFragment(fragment);
                        return true;
                    case R.id.navigation_sos:
                        fragment = new SosFragment();
                        setFragment(fragment);
                        return true;
                }
                return false;
            }
        });
    }

    public void setFragment(Fragment fragment) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
        currentFragment = fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (currentFragment instanceof MedifriendFragment) {
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onButtonAddMedicationPressed() {
        Intent intent = new Intent(getApplicationContext(), AddMedicationActivity.class);
        startActivityForResult(intent, ADD_ACTIVITY_REQUEST);
        finish();
    }

    @Override
    public void onAddButtonClicked() {
        onButtonAddMedicationPressed();
    }

    @Override
    public void onPositiveDeleteClicked(final int id) {
        MedicationFragment fragment = (MedicationFragment) currentFragment;
        fragment.getAdapter().notifyDataSetChanged();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MedicationsDataBase db = MedicationsDataBase.getInstance(getApplicationContext());

                //get notifications of this medication and delete from db!
                List<RemindersTable> reminders = db.medicationsDao().getAllRemindersByMedicationId(id);
                for(RemindersTable reminder: reminders) {
                    NotificationHelper.cancelNotification(getApplicationContext(), reminder.getId());
                }

                db.medicationsDao().deleteFromMedicationsById(id);
                db.medicationsDao().deleteFroRemindersById(id);
                db.medicationsDao().deleteFromPrescriptionsById(id);
            }
        });

        thread.start();

    }

    @Override
    public void onNegativeDeleteClicked(DialogFragment dialogFragment) {
        dialogFragment.getDialog().cancel();
    }

    @Override
    public void addMedifriendButtonClicked() {
        AddMedifriendDialogFragment dialog = new AddMedifriendDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddMedifriendDialog");
    }

    @Override
    public void onAddMedifriendPositiveClicked(String name, String phone) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_MEDIFRIEND, MODE_PRIVATE).edit();
        editor.putString(NAME_MEDIFRIEND_PREFS, name);
        editor.putString(PHONE_MEDIFRIEND_PREFS, phone);
        editor.apply();
        fragment = new MedifriendFragment();
        setFragment(fragment);

    }

    @Override
    public void onNegativeMedifriendClicked(DialogFragment dialogFragment) {
        dialogFragment.getDialog().cancel();
    }

    @Override
    public void onSosButtonClicked() {
        SharedPreferences sp = getSharedPreferences(PREFS_MEDIFRIEND, MODE_PRIVATE);
        String phone = sp.getString(PHONE_MEDIFRIEND_PREFS, "");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }
}
