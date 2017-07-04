package com.cashlog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cashlog.DbHelper.BudgetsDbHelper;
import com.cashlog.Entities.Budgets;
import com.cashlog.Entities.Expenses;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * Created by Nahid on 21.11.2016.
 */

public class AddTransaction extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1000;
    private GoogleApiClient mClient;
    ImageButton buttonBack;
    ImageButton buttonSave;
    SearchableSpinner ss;
    BudgetsDbHelper dbHelper;
    EditText amount,note,time,date,placeL;
    RatingBar ratingBar;
    int id ;
    DecimalFormat df = new DecimalFormat("@#############");
    String [] array_expense = {"Food","Leisure","Transport","Clothes","Travel","Health","Bills","Hobbies",
            "Gifts","Groceries","Kids","Education","Beauty","Sports","Other"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        mClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        placeL = (EditText) findViewById(R.id.expense_location);
        amount = (EditText) findViewById(R.id.edit_amount);
        note = (EditText) findViewById(R.id.edit_note);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.timeinhour);
        ss  = (SearchableSpinner) findViewById(R.id.spinner);
        ss.setTitle("Expense Categories");
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_expense);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ss.setAdapter(adapter);
        DatePicker();
        TimePicker();
        PlacePicker();
        dbHelper = new BudgetsDbHelper(getApplicationContext());
       Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        if(id==0){
        }else{
            Expenses e =dbHelper.getExpenseById((int) id);
            placeL.setText(e.getPayee().toString());
            amount.setText(df.format( e.getAmount()));
            note.setText(e.getNote());
            date.setText(e.getDate());
            time.setText(e.getTime());
            ratingBar.setProgress((int) e.getRating());
            int pos=0;
            for(int i=0;i<array_expense.length;i++){
                if(array_expense[i].equals(e.getCategory())){
                    pos=i;
                }
            }
            ss.setSelection(pos);
        }
        buttonSave = (ImageButton) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
                Intent intent = new Intent(AddTransaction.this,MainActivity.class);
                startActivity(intent);
            }
        });
        buttonBack = (ImageButton) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTransaction.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void addTransaction(){
        if(id==0){
            try{
            Budgets budgets=dbHelper.getBudgetByStatus();
            Expenses e = new Expenses(placeL.getText().toString(),Double.parseDouble(amount.getText().toString()),ss.getSelectedItem().toString(),date.getText().toString(),note.getText().toString(),time.getText().toString(),2*ratingBar.getRating(),budgets.getBudget_id());
            dbHelper.addExpense(e);
            dbHelper.close();
                Toast.makeText(AddTransaction.this, "Transaction successfully saved!!!",
                        Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this,"You can't add Expense,\n Choose budget to add",Toast.LENGTH_SHORT).show();
            }
        }else{
            Expenses e = dbHelper.getExpenseById(id);
            e.setPayee(placeL.getText().toString());
            e.setNote(note.getText().toString());
            e.setTime(time.getText().toString());
            e.setDate(date.getText().toString());
            e.setCategory(ss.getSelectedItem().toString());
            e.setAmount(Double.parseDouble(amount.getText().toString()));
            e.setRating(2*ratingBar.getRating());
            dbHelper.updateExpense(e);
            dbHelper.close();
        }
    }

    public void DatePicker(){
        Calendar mcurrentDate=Calendar.getInstance();
        final int  mYear=mcurrentDate.get(Calendar.YEAR);
        final int mMonth=mcurrentDate.get(Calendar.MONTH);
        final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
        date.setText(mDay+"/"+mMonth+"/"+mYear);
       // date.setText(mDay+"/"+mMonth+"/"+mYear);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog mDatePicker=new DatePickerDialog(AddTransaction.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        date.setText(selectedday+"/"+selectedmonth+"/"+selectedyear, TextView.BufferType.EDITABLE);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }
    public void TimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        time.setText( hour + ":" + minute);
       // time.setText(hour+":"+minute);
    time.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddTransaction.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    time.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }
    });

    }
    protected void onStart() {
        super.onStart();
        mClient.connect();
    }
    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }
    public void PlacePicker(){
       placeL.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
               try {
                   startActivityForResult(builder.build(AddTransaction.this), PLACE_PICKER_REQUEST);
               } catch (GooglePlayServicesRepairableException e) {
                   e.printStackTrace();
               } catch (GooglePlayServicesNotAvailableException e) {
                   e.printStackTrace();
               }
           }
       });
    }
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                stBuilder.append(" ");
                stBuilder.append(placename);
                placeL.setText(stBuilder.toString());
            }
        }
    }
}
