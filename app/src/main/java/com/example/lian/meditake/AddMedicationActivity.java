package com.example.lian.meditake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.lian.meditake.R.array.reminder_times_array;

public class AddMedicationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TimePickerFragment.OnTimePickerFragmentListener, TimeAndDosesAlertDialogFragment.TimeAndDosesDialogListener,
        PickDaysDialogFragment.PickDaysDialogListener,
        SetDosageDialogFragment.SetDosageDialogListener, RefillMedsReminderDialogFragment.RefillMedsReminderListener,
        PickTimeFragment.PickTimeFragmentListener, DoneDialogFragment.DoneDialogFragmentListener,
        ExitAlertDialogFragment.OnExitFragmentInteractionListener {

    private Spinner spinner;
    private Button submitBtn;
    private AutoCompleteTextView medicationNameAutoTextView;
    private TextView pickTimeTextView;
    private TextView numberOfDoses;
    private RelativeLayout relativeReminderTimes;
    private TextView pickTimeTextView2;
    private TextView numberOfDoses2;
    private TextView pickTimeTextView3;
    private TextView numberOfDoses3;
    private RelativeLayout relativeSchedule;
    private RadioGroup rgDays;
    private RadioButton checkedRadioButtonDays;
    private TextView pickedDays;
    private boolean isPickedDaysTextClicked = false;
    private RelativeLayout dosageRelative;
    private TextView tapToSetDosage;
    private Button btnDone;
    private Button btnMoreOptions;
    private RelativeLayout relativePrescription;
    private Switch switchButtonPrescription;
    private TextView refillMsg;
    private EditText amountMedsEditText;
    private TextView refillReminderTxt;
    private TextView chooseAmountMeds;
    private TextView whatTimeRefillReminder;
    private TextView refillTimeText;
    private String[] daysStringArr = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private boolean[] daysBooleanArr = {false, false, false, false, false, false, false};
    private NotificationHelper notificationHelper;
    private MedicationsDataBase db;

    //FOR MEDICATIONS TABLE
    private String medName;
    private double amountDosageMg = 100.00;
    private boolean isSelectedDosage = false;
    private boolean isSelectedTimeAndDoses = false;
    private int remindersAmountPerDay;

    //FOR REMINDERS TABLE
    private ArrayList<ReminderDetails> allReminderTimes = new ArrayList<>();
    private ArrayList<String> selectedDaysSet = new ArrayList<>();

    //FOR PRESCRIPTIONS REMINDERS TABLE
    private boolean isTimePrescriptionSet = false;
    private int hourPrescriptionReminder = 0;
    private int minutesPrescriptionReminder = 0;
    private int medsCurrentAmount = 0;
    private boolean isMedsRemainingPrescriptionSet = false;
    private int medsRemainingPrescriptionReminder = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        notificationHelper = new NotificationHelper(this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                db = MedicationsDataBase.getInstance(getApplicationContext());
            }
        });

        t.start();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        spinner = (Spinner) findViewById(R.id.spinner_reminder_times);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        medicationNameAutoTextView = (AutoCompleteTextView) findViewById(R.id.med_name_autocomp);
        pickTimeTextView = (TextView) findViewById(R.id.pickTime);
        numberOfDoses = (TextView) findViewById(R.id.number_of_doses_textview);
        relativeReminderTimes = (RelativeLayout) findViewById(R.id.relative_reminder_times);
        pickTimeTextView2 = (TextView) findViewById(R.id.pickTime2);
        numberOfDoses2 = (TextView) findViewById(R.id.number_of_doses_textview2);
        pickTimeTextView3 = (TextView) findViewById(R.id.pickTime3);
        numberOfDoses3 = (TextView) findViewById(R.id.number_of_doses_textview3);
        relativeSchedule = (RelativeLayout) findViewById(R.id.relative_schedule);
        rgDays = (RadioGroup) findViewById(R.id.rg_days);
        checkedRadioButtonDays = (RadioButton) rgDays.findViewById(rgDays.getCheckedRadioButtonId());
        pickedDays = (TextView) findViewById(R.id.picked_days_text);
        dosageRelative = (RelativeLayout) findViewById(R.id.relative_dosage);
        tapToSetDosage = (TextView) findViewById(R.id.set_dosage_text);
        btnMoreOptions = (Button) findViewById(R.id.more_options_btn);
        btnDone = (Button) findViewById(R.id.done_btn);
        relativePrescription = (RelativeLayout) findViewById(R.id.relative_prescription);
        switchButtonPrescription = (Switch) findViewById(R.id.toggle_refill_reminder);
        refillMsg = (TextView) findViewById(R.id.refill_msg);
        amountMedsEditText = (EditText) findViewById(R.id.amount_meds_edit_text);
        refillReminderTxt = (TextView) findViewById(R.id.refill_reminder_text);
        chooseAmountMeds = (TextView) findViewById(R.id.choose_amount_meds_refill_text);
        whatTimeRefillReminder = (TextView) findViewById(R.id.what_time_refill_text);
        refillTimeText = (TextView) findViewById(R.id.refill_time_text);

        chooseAmountMeds.setText(getResources().getString(R.string.amount_meds_remaining, 10));

        numberOfDoses.setText(getResources().getString(R.string.take_dose_int, 1));
        numberOfDoses2.setText(getResources().getString(R.string.take_dose_int, 1));
        numberOfDoses3.setText(getResources().getString(R.string.take_dose_int, 1));
        pickedDays.setVisibility(View.GONE);
        pickTimeTextView2.setVisibility(View.GONE);
        numberOfDoses2.setVisibility(View.GONE);
        pickTimeTextView3.setVisibility(View.GONE);
        numberOfDoses3.setVisibility(View.GONE);

        setToInvisible();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.reminder_times_array, R.layout.my_spinner_dropdown_list_item);

        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.my_spinner_dropdown_list_item);

        // Apply the spinnerAdapter to the spinner
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

//        ArrayAdapter<String> AutoCompleteAdapter = new ArrayAdapter<>
//                (this, android.R.layout.select_dialog_item, map.values().toArray(new String[3]));

        List<String> allMedicationsName = new ArrayList<>();
        MedicationsListAdapter adapter = new MedicationsListAdapter(this,
                R.layout.autocomplete_item, allMedicationsName);

        medicationNameAutoTextView.setThreshold(1); //will start working from first character
        medicationNameAutoTextView.setAdapter(adapter); //change back to AutoCompleteAdapter

        medicationNameAutoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                medName = (String) parent.getItemAtPosition(position);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitButtonClicked();
            }
        });

        pickTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickTimeClicked(1);
                showTimeAndDosesDialog(1);
            }
        });

        pickTimeTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickTimeClicked(2);
                showTimeAndDosesDialog(2);
            }
        });

        pickTimeTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickTimeClicked(3);
                showTimeAndDosesDialog(3);
            }
        });

        numberOfDoses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeAndDosesDialog(1);
            }
        });

        numberOfDoses2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeAndDosesDialog(2);
            }
        });

        numberOfDoses3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeAndDosesDialog(3);
            }
        });

        setSelectedDaysAllWeek();

        rgDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiobtn_specific_days) {
                    isPickedDaysTextClicked = false;
                    selectedDaysSet.clear();
                    showPickDaysDialog();
                } else {  //checkedId == radiobtn_everyday
                    pickedDays.setVisibility(View.GONE);
                    setSelectedDaysAllWeek();
                }
            }
        });

        pickedDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickedDaysTextClicked = true;
                showPickDaysDialog();
            }
        });

        tapToSetDosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetDosageDialog(amountDosageMg);
            }
        });

        btnMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptionsButtonClicked();
            }
        });

        switchButtonPrescription.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setEnabled(true);
                    showPrescriptionDetails();
                } else {
                    buttonView.setEnabled(false);
                    hidePrescriptionDetails();
                }
            }
        });

        chooseAmountMeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRefillReminderDialog(medsRemainingPrescriptionReminder);
            }
        });

        amountMedsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!amountMedsEditText.getText().toString().equals("")) {
                    medsCurrentAmount = Integer.parseInt(amountMedsEditText.getText().toString());
                }
            }
        });

        refillTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PickTimeFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoneDialog();
            }
        });

    }

    private void setSelectedDaysAllWeek() {
        selectedDaysSet.add("Sunday");
        selectedDaysSet.add("Monday");
        selectedDaysSet.add("Tuesday");
        selectedDaysSet.add("Wednesday");
        selectedDaysSet.add("Thursday");
        selectedDaysSet.add("Friday");
        selectedDaysSet.add("Saturday");
    }

    public void showDoneDialog() {
        DialogFragment dialogFragment = new DoneDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "DoneDialogFragment");
    }

    public void showRefillReminderDialog(int amount) {
        DialogFragment dialog = RefillMedsReminderDialogFragment.newInstance(amount);
        dialog.show(getSupportFragmentManager(), "RefillReminderDialogFragment");
    }

    public void hidePrescriptionDetails() {
        chooseAmountMeds.setVisibility(View.GONE);
        whatTimeRefillReminder.setVisibility(View.GONE);
        refillTimeText.setVisibility(View.GONE);
    }

    public void showPrescriptionDetails() {
        chooseAmountMeds.setVisibility(View.VISIBLE);
        whatTimeRefillReminder.setVisibility(View.VISIBLE);
        refillTimeText.setVisibility(View.VISIBLE);
    }

    public void moreOptionsButtonClicked() {
        int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        relativePrescription.setAlpha(0f);
        relativePrescription.setVisibility(View.VISIBLE);
        relativePrescription.animate().alpha(1f).setDuration(mShortAnimationDuration);
        hidePrescriptionDetails();

        btnMoreOptions.setVisibility(View.GONE);
    }

    public void showSetDosageDialog(double amount) {
        DialogFragment dialog = SetDosageDialogFragment.newInstance(amount);
        dialog.show(getSupportFragmentManager(), "SetDosageDialogFragment");
    }

    public void showPickDaysDialog() {
        DialogFragment dialog = PickDaysDialogFragment.newInstance(daysStringArr, daysBooleanArr, selectedDaysSet);
        dialog.show(getSupportFragmentManager(), "PickDaysDialogFragment");
    }

    public void setToInvisible() {
        relativePrescription.setVisibility(View.GONE);
        btnMoreOptions.setVisibility(View.GONE);
        btnDone.setVisibility(View.GONE);
        dosageRelative.setVisibility(View.GONE);
        relativeReminderTimes.setVisibility(View.GONE);
        relativeSchedule.setVisibility(View.GONE);
    }

    public void setToVisible() {
        int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        dosageRelative.setAlpha(0f);
        dosageRelative.setVisibility(View.VISIBLE);
        dosageRelative.animate().alpha(1f).setDuration(mShortAnimationDuration);
        relativeReminderTimes.setAlpha(0f);
        relativeReminderTimes.setVisibility(View.VISIBLE);
        relativeReminderTimes.animate().alpha(1f).setDuration(mShortAnimationDuration);
        relativeSchedule.setAlpha(0f);
        relativeSchedule.setVisibility(View.VISIBLE);
        relativeSchedule.animate().alpha(1f).setDuration(mShortAnimationDuration);
        btnMoreOptions.setAlpha(0f);
        btnMoreOptions.setVisibility(View.VISIBLE);
        btnMoreOptions.animate().alpha(1f).setDuration(mShortAnimationDuration);
        btnDone.setAlpha(0f);
        btnDone.setVisibility(View.VISIBLE);
        btnDone.animate().alpha(1f).setDuration(mShortAnimationDuration);
    }

    public void SubmitButtonClicked() { //changed: deleted && map.contains(medName)
        medName = medicationNameAutoTextView.getText().toString();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (checkNameInDb(medName)) {   //check if the name exists in list
                    if (!checkNameInMedicationTable(medName)) { //check if already been added to medications list
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                medicationNameAutoTextView.setEnabled(false);
                                submitBtn.setEnabled(false);
                                setToVisible();
                            }
                        });
                    } else {  //medication is already in medications list!
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), R.string.medication_in_list_toast,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.medication_err_toast,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean checkNameInMedicationTable(String name) {
        MedicationsTable medication = db.medicationsDao().getMedicationByName(name);
        if (medication == null) {
            return false;
        }
        return true;
    }

    public boolean checkNameInDb(String name) {
        List<String> allNames = db.medicationsDao().getMedicationsTableByPrefix(name);
        if (allNames.contains(name)) {
            return true;
        }
        return false;
    }


    //belongs to how many times a day spinner!
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getSelectedItemPosition()) {
            case 0:
                pickTimeTextView2.setVisibility(View.GONE);
                numberOfDoses2.setVisibility(View.GONE);
                pickTimeTextView3.setVisibility(View.GONE);
                numberOfDoses3.setVisibility(View.GONE);
                remindersAmountPerDay = 1;
                break;
            case 1:
                pickTimeTextView2.setVisibility(View.VISIBLE);
                numberOfDoses2.setVisibility(View.VISIBLE);
                pickTimeTextView3.setVisibility(View.GONE);
                numberOfDoses3.setVisibility(View.GONE);
                remindersAmountPerDay = 2;
                break;
            case 2:
                pickTimeTextView2.setVisibility(View.VISIBLE);
                numberOfDoses2.setVisibility(View.VISIBLE);
                pickTimeTextView3.setVisibility(View.VISIBLE);
                numberOfDoses3.setVisibility(View.VISIBLE);
                remindersAmountPerDay = 3;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    @Override
    public void onTimePickerTextViewTouched(int hourOfDay, int minute, int number) {
        TextView textView;

        switch (number) {
            case 1:
            default:
                textView = pickTimeTextView;
                break;
            case 2:
                textView = pickTimeTextView2;
                break;
            case 3:
                textView = pickTimeTextView3;
                break;
        }

        String str = setTimeDetails(hourOfDay, minute);
        textView.setText(str);

        if (allReminderTimes.isEmpty() || allReminderTimes.size() < number) {
            allReminderTimes.add(new ReminderDetails(hourOfDay, minute));
        } else {
            allReminderTimes.get(number - 1).setHour(hourOfDay);
            allReminderTimes.get(number - 1).setMinutes(minute);
        }

    }

    public static String setTimeDetails(int hour, int minutes) {
        if ((0 <= hour && hour <= 9)) {
            if (0 <= minutes && minutes <= 9) {
                return "0" + hour + ":" + "0" + minutes;
            } else {
                return "0" + hour + ":" + minutes;
            }
        } else if (0 <= minutes && minutes <= 9) {
            return hour + ":" + "0" + minutes;
        } else {
            return hour + ":" + minutes;
        }
    }

    public void showTimeAndDosesDialog(int number) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = TimeAndDosesAlertDialogFragment.newInstance(number);
        dialog.show(getSupportFragmentManager(), "TimeAndDosesDialogFragment");
    }

    @Override
    public void onTimeAndDosesDialogPositiveClick(DialogFragment dialog, double numberOfDoses
            , int textViewNumber, int hour, int minutes) {
        isSelectedTimeAndDoses = true;
        int number = (int) numberOfDoses;

        String string = setTimeDetails(hour, minutes);

        switch (textViewNumber) {
            case 1:
            default:
                if (numberOfDoses - number == 0) {  //mispar shalem we need to use take_dos_int
                    this.numberOfDoses.setText(getResources().getString(R.string.take_dose_int, number));
                } else {
                    this.numberOfDoses.setText(getResources().getString(R.string.take_dose, numberOfDoses));
                }
                this.pickTimeTextView.setText(string);
                break;
            case 2:
                if (numberOfDoses - number == 0) {
                    this.numberOfDoses2.setText(getResources().getString(R.string.take_dose_int, number));
                } else {
                    this.numberOfDoses2.setText(getResources().getString(R.string.take_dose, numberOfDoses));
                }
                this.pickTimeTextView2.setText(string);

                break;
            case 3:
                if (numberOfDoses - number == 0) {
                    this.numberOfDoses3.setText(getResources().getString(R.string.take_dose_int, number));
                } else {
                    this.numberOfDoses3.setText(getResources().getString(R.string.take_dose, numberOfDoses));
                }
                this.pickTimeTextView3.setText(string);
                break;
        }

        if (allReminderTimes.size() < textViewNumber) {
            ReminderDetails reminder = new ReminderDetails(hour, minutes);
            reminder.setDoses(numberOfDoses);
            allReminderTimes.add(reminder);
        } else {
            allReminderTimes.get(textViewNumber - 1).setHour(hour);
            allReminderTimes.get(textViewNumber - 1).setMinutes(minutes);
            allReminderTimes.get(textViewNumber - 1).setDoses(numberOfDoses);
        }

        Log.d("REMINDER TIMES SIZE", String.valueOf(allReminderTimes.size()));
    }

    @Override
    public void onTimeAndDosesDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    @Override
    public void onPickDaysDialogPositiveCheck(DialogFragment dialog, boolean[] checkedDays, ArrayList<String> selected) {
        this.daysBooleanArr = checkedDays;
        this.selectedDaysSet = selected;
        String toPrint = selectedDaysArrayToString(selected);
        pickedDays.setText(toPrint);
        pickedDays.setVisibility(View.VISIBLE);
        selectedDaysSet = selected;
    }

    private String selectedDaysArrayToString(ArrayList<String> selected) {
        String res = "";

        for (int i = 0; i < selected.size(); i++) {
            String str = selected.get(i).substring(0, 3);
            if (i == selected.size() - 1) {
                res += str;
            } else {
                res += (str + ", ");
            }
        }

        return res;
    }

    @Override
    public void onPickDaysDialogNegativeCheck(DialogFragment dialog, ArrayList<String> selected) {
        if (!isPickedDaysTextClicked) {
            pickedDays.setVisibility(View.GONE);
            RadioButton rb = (RadioButton) findViewById(R.id.radiobtn_everyday);
            rb.setChecked(true);
        }
        selectedDaysSet.clear();
        setSelectedDaysAllWeek();
        dialog.getDialog().cancel();
    }

    @Override
    public void allDaysSelected(DialogFragment dialog) {
        RadioButton rb = (RadioButton) findViewById(R.id.radiobtn_everyday);
        rb.setChecked(true);
        setSelectedDaysAllWeek();
    }

    @Override
    public void onSetDosageDialogPositiveClick(DialogFragment dialog, double amount) {
        int number = (int) amount;

        if (amount - number == 0) {  //mispar shalem we need to use dosage_amount_picked_int
            this.tapToSetDosage.setText(getResources().getString(R.string.dosage_amount_picked_int, number));
        } else {
            this.tapToSetDosage.setText(getResources().getString(R.string.dosage_amount_picked, amount));
        }
        this.isSelectedDosage = true;
        this.amountDosageMg = amount;
    }

    @Override
    public void onSetDosageDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    @Override
    public void RefillMedsReminderPositiveClick(DialogFragment dialog, int numberOfMeds) {
        if (medsCurrentAmount > numberOfMeds) {
            this.medsRemainingPrescriptionReminder = numberOfMeds;
            chooseAmountMeds.setText(getResources().getString(R.string.amount_meds_remaining, numberOfMeds));
            this.isMedsRemainingPrescriptionSet = true;
        } else {
            Toast.makeText(getApplicationContext(), R.string.invalid_input, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void RefillMedsReminderNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    @Override
    public void onTimePicked(int hourOfDay, int minute) {
        if (minute >= 0 && minute <= 9) {
            refillTimeText.setText(hourOfDay + ":0" + minute);
        } else {
            refillTimeText.setText(hourOfDay + ":" + minute);
        }

        this.hourPrescriptionReminder = hourOfDay;
        this.minutesPrescriptionReminder = minute;
        this.isTimePrescriptionSet = true;

    }

    @Override
    public void DoneDialogPositiveClick(DialogFragment dialog) {
        if (validateDetails()) {  //all details checked!
            printLogs();
            dialog.getDialog().cancel();
            insertInfoToDB();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    createNotifications();
                }
            });
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(getApplicationContext(), AddMedicationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void DoneDialogNegativeClick(DialogFragment dialog) {
        if (validateDetails()) {
            printLogs();
            dialog.getDialog().cancel();
            insertInfoToDB();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    createNotifications();
                }
            });
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean validateDetails() {
        if (!isSelectedDosage || !isSelectedTimeAndDoses) {
            Toast.makeText(getApplicationContext(), R.string.fill_all_details_toast, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void printLogs() {
        Log.d("NAME MEDICATION", medName);

        for (int i = 0; i < selectedDaysSet.size(); i++) {
            Log.d("SELECTED DAYS", selectedDaysSet.get(i));
        }
        for (int i = 0; i < allReminderTimes.size(); i++) {
            Log.d("REMINDER NUMBER", String.valueOf(i + 1));
            Log.d("REMINDERS HOUR", String.valueOf(allReminderTimes.get(i).getHour()));
            Log.d("REMINDERS MINUTES", String.valueOf(allReminderTimes.get(i).getMinutes()));
            Log.d("REMINDERS DOSES", String.valueOf(allReminderTimes.get(i).getDoses()));
        }
    }

    public void createNotifications() {

        List<MedicationsWithReminders> medWreminders = db.medicationsDao()
                .getAllMedicationsWithRemindersByName(medName);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        for (int i = 0; i < medWreminders.size(); i++) {
            Log.d("PRINTING DATA", "########## PRINTING LIST! ##########");
            Log.d("REMINDERS ID", String.valueOf(medWreminders.get(i).reminder.getId()));
            Log.d("REMINDERS HOUR", String.valueOf(medWreminders.get(i).reminder.getHour()));
            Log.d("REMINDERS MINUTES", String.valueOf(medWreminders.get(i).reminder.getMinutes()));
            Log.d("REMINDERS DOSES", String.valueOf(medWreminders.get(i).reminder.getDoses()));
            Log.d("REMINDERS DAY", medWreminders.get(i).reminder.getDay());

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Calendar setcalendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, medWreminders.get(i).reminder.getHour());
            calendar.set(Calendar.MINUTE, medWreminders.get(i).reminder.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeekInt(medWreminders.get(i).reminder.getDay()));

            // cancel already scheduled reminders
            notificationHelper.cancelNotification(this, medWreminders.get(i).reminder.getId());

            if (calendar.before(setcalendar)) {
                Log.d("IF CALENDAR BEFORE", "inside if");
                int dayInt = getDayOfWeekInt(medWreminders.get(i).reminder.getDay());
                int days = dayInt + (7 - calendar.get(Calendar.DAY_OF_WEEK)); // how many days until the given day
                calendar.add(Calendar.DATE, days);
                //calendar.add(Calendar.DATE, 1);
            }

            enableReceiver();

            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra(AlertReceiver.NAME_KEY, medName);
            intent.putExtra(AlertReceiver.DOSES_KEY, medWreminders.get(i).reminder.getDoses());
            intent.putExtra(AlertReceiver.DAY_KEY, medWreminders.get(i).reminder.getDay());
            intent.putExtra(AlertReceiver.REMINDER_ID_KEY, medWreminders.get(i).reminder.getId());
            intent.putExtra(AlertReceiver.MEDICATION_ID, medWreminders.get(i).medication.getId());
            intent.putExtra("mg", medWreminders.get(i).medication.getMg());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                    medWreminders.get(i).reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        }
    }

    public void enableReceiver() {
        ComponentName receiver = new ComponentName(this, AlertReceiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public int getDayOfWeekInt(String dayOfWeek) {
        int day = 0;
        switch (dayOfWeek) {
            case "Sunday":
                day = 1;
                break;
            case "Monday":
                day = 2;
                break;
            case "Tuesday":
                day = 3;
                break;
            case "Wednesday":
                day = 4;
                break;
            case "Thursday":
                day = 5;
                break;
            case "Friday":
                day = 6;
                break;
            case "Saturday":
                day = 7;
                break;
        }
        return day;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_med_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
                showExitAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showExitAlertDialog() {
        ExitAlertDialogFragment dialog = new ExitAlertDialogFragment();
        dialog.show(getSupportFragmentManager(), "ExitDialogFragment");
    }

    public void insertInfoToDB() {

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                int medId = db.medicationsDao().getMedicationIdByName(medName);

                //insert data to medications table
                MedicationsTable medication = new MedicationsTable(medId, medName, amountDosageMg, remindersAmountPerDay);
                db.medicationsDao().insert(medication);

                //insert data to reminders table
                RemindersTable reminder;
                for (int i = 0; i < selectedDaysSet.size(); i++) {
                    for (int j = 0; j < allReminderTimes.size(); j++) {
                        reminder = new RemindersTable(allReminderTimes.get(j).getHour(),
                                allReminderTimes.get(j).getMinutes(), allReminderTimes.get(j).getDoses()
                                , selectedDaysSet.get(i), medication.getId());

                        db.medicationsDao().insertReminder(reminder);
                    }
                }

                //insert data to prescription reminder table
                if (switchButtonPrescription.isEnabled()) {
                    if (isMedsRemainingPrescriptionSet && isTimePrescriptionSet) {
                        double dif = (double) (medsCurrentAmount - medsRemainingPrescriptionReminder);
                        PrescriptionReminder prescriptionReminder = new PrescriptionReminder(medication.getId(),
                                hourPrescriptionReminder, minutesPrescriptionReminder, medsCurrentAmount,
                                medsRemainingPrescriptionReminder, dif);
                        db.medicationsDao().insertPrescriptionReminder(prescriptionReminder);
                        db.medicationsDao().updateIsPrescriptionByMedicationId(true, medication.getId());
                        Log.d("PRESCRIPTION HOUR", String.valueOf(prescriptionReminder.getHour()));
                        Log.d("PRESCRIPTION MINUTES", String.valueOf(prescriptionReminder.getMinutes()));
                        Log.d("PRESCRIPTION START", String.valueOf(prescriptionReminder.getStartAmount()));
                        Log.d("PRESCRIPTION REMINDER", String.valueOf(prescriptionReminder.getReminderAmount()));
                    }
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void ExitDialogPositiveClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void ExitDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }
}