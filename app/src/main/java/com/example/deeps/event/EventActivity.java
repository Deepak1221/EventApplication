package com.example.deeps.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {


    EditText txt_name,txt_number,txt_address,txt_date,txt_time;
    Button btn_save,btn_event,btn_logout ;
   final Calendar myCalendar = Calendar.getInstance();
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(EventActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_number = (EditText) findViewById(R.id.txt_number);
        txt_address = (EditText) findViewById(R.id.txt_address);
        txt_date = (EditText) findViewById(R.id.txt_date);
        txt_time = (EditText) findViewById(R.id.txt_time);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_logout= (Button) findViewById(R.id.btn_logout);
        btn_event = (Button) findViewById(R.id.btn_event);

        btn_logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });


        btn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = txt_name.getText().toString().trim();
                String number = txt_number.getText().toString().trim();
                String address = txt_address.getText().toString().trim();
                String date = txt_date.getText().toString().trim();
                String time = txt_time.getText().toString().trim();



                if(name.length()<1){

                    Toast.makeText(EventActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }

                else if (number.length()<1){
                    Toast.makeText(EventActivity.this, "Enter Number", Toast.LENGTH_SHORT).show();
                }

                else if (address.length()<1){
                    Toast.makeText(EventActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();
                }

                else if (date.length()<1){
                    Toast.makeText(EventActivity.this, "Enter Date", Toast.LENGTH_SHORT).show();
                }

                else if (time.length()<1){
                    Toast.makeText(EventActivity.this, "Enter Time", Toast.LENGTH_SHORT).show();
                }

                else {
                    EventModel item = new EventModel();
                    item.setName(name);
                    item.setMobile_no(number);
                    item.setAddress(address);
                    item.setDate(date);
                    item.setTime(time);

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("events");

                    String userId = mDatabase.push().getKey();

                    mDatabase.child(userId).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EventActivity.this, "done", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });



        btn_event.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EventList.class));
            }
        });

        txt_date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                datePickerDialog.setTitle("Event Date");
                datePickerDialog.show();
//                new DatePickerDialog(EventActivity.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txt_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txt_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };





    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txt_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
