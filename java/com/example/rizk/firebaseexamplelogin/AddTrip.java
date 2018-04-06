package com.example.rizk.firebaseexamplelogin;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class AddTrip extends AppCompatActivity implements View.OnClickListener,

        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    private static final String TAG = "LoginActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    AutoCompleteTextView nAutocompleteTextView;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private PlaceArrayAdapter nPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    TextView editTextDate, editTextTime;
    EditText name, editNotes;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    Button buttonTime, buttonDate, sendData;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int _year, _month, _day, _hour, _minute;
    CheckBox roundChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        editNotes = findViewById(R.id.editTextMulti);
        mAutocompleteTextView = findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        nAutocompleteTextView = findViewById(R.id.autoCompleteTextView2);
        nAutocompleteTextView.setThreshold(3);
        name = findViewById(R.id.tName);
        mAuth = FirebaseAuth.getInstance();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        nAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        nPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);

        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        nAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);

        buttonDate = findViewById(R.id.buttonDatepicker);
        buttonTime = findViewById(R.id.buttonTimePicker);

        buttonDate.setOnClickListener(this);
        buttonTime.setOnClickListener(this);
        roundChk=findViewById(R.id.checkBox2);

        sendData = findViewById(R.id.saveData);
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roundChk.isChecked())
                {
                    String status = "upComming";
                    String from = nAutocompleteTextView.getText().toString().trim();
                    // Toast.makeText(AddTrip.this, from, Toast.LENGTH_SHORT).show();
                    String to = mAutocompleteTextView.getText().toString().trim();
//                Log.v("kjyk",to);
                    String datee = editTextDate.getText().toString();
                    String notes = editNotes.getText().toString().trim();
                    String tname = "Return "+name.getText().toString().trim()+" Trip";
                    SaveData save = new SaveData(tname, from, to, datee, null, notes, status);
                    FirebaseUser user = mAuth.getCurrentUser();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");
                    databaseReference = databaseReference.child(user.getUid());
                    databaseReference.push().setValue(save);



                }

                //  setValues();
                String status = "upComming";
                String from = mAutocompleteTextView.getText().toString().trim();
                // Toast.makeText(AddTrip.this, from, Toast.LENGTH_SHORT).show();
                String to = nAutocompleteTextView.getText().toString().trim();
//                Log.v("kjyk",to);
                String datee = editTextDate.getText().toString();
                //              Toast.makeText(AddTrip.this, datee, Toast.LENGTH_SHORT).show();
                // Log.v("lk",date);
                String time = editTextTime.getText().toString().trim();
                //Log.v("uy",time);
                String notes = editNotes.getText().toString().trim();
                String tname = name.getText().toString().trim();
                SaveData save = new SaveData(tname, from, to, datee, time, notes, status);
                FirebaseUser user = mAuth.getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");
                databaseReference = databaseReference.child(user.getUid());
                databaseReference.push().setValue(save);
                setAlarm();
                startActivity(new Intent(AddTrip.this, ListActivity.class));
                AddTrip.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            // final PlaceArrayAdapter.PlaceAutocomplete item2 = nPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            // final String placeId2 = String.valueOf(item2.placeId);
            Log.i(TAG, "Selected: " + item.description);
            //Log.i(TAG, "Selected: " + item2.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
//            PendingResult<PlaceBuffer> placeResult2 = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId2);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
//            placeResult2.setResultCallback(mUpdatePlaceDetailsCallback);
            //          Log.i(TAG, "Fetching details for ID: " + item2.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();


        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        nPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.buttonDatepicker:
                Calendar calendar = Calendar.getInstance();

                _year = calendar.get(Calendar.YEAR);
                _month = calendar.get(Calendar.MONTH);
                _day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextDate.setText(dayOfMonth + " : " + (month + 1) + " : " + year);
                        _year = year;
                        _month = month;
                        _day = dayOfMonth;
                    }
                }, _year, _month, _day);
                datePickerDialog.show();

                break;
            case R.id.buttonTimePicker:
                Calendar calendar1 = Calendar.getInstance();
                _hour = calendar1.get(Calendar.HOUR);
                _minute = calendar1.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddTrip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        editTextTime.setText(hourOfDay + " :" + minute);
                        _hour = hourOfDay;
                        _minute = minute;

                    }
                }, _hour, _minute, false);

                timePickerDialog.show();

                break;
        }
    }

    private void setAlarm() {

        Calendar cal = Calendar.getInstance();
        cal.set(_year,
                _month,
                _day,
                _hour,
                _minute,
                00);
        Log.v("sss", cal.toString());

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("note", editNotes.getText().toString());
        intent.putExtra("from", mAutocompleteTextView.getText().toString());
        intent.putExtra("to", nAutocompleteTextView.getText().toString());
        final int alarmID = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(),
                alarmID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }
}

