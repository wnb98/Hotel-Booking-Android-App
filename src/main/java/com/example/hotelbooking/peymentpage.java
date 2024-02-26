package com.example.hotelbooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import androidx.core.util.Pair;
import java.util.concurrent.TimeUnit;
import java.util.Locale;



public class peymentpage extends AppCompatActivity {

    private EditText etCardNumber, etExpirationMonth, etExpirationYear, etCvv;
    private CheckBox chkAgree;
    private Button btnPay;
    private TextView dateRangeText, guestsTextView, totalTXT;
    private NumberPicker adultsPicker;
    private NumberPicker childrenPicker;
    private long numberOfDays = 0;
    private double hotelPrice = 0;
    private String hotelName;

    private boolean isCardNumberValid = false;
    private boolean isMonthSelected = false;
    private boolean isYearSelected = false;
    private boolean isCvvValid = false;
    private boolean isAgreed = false;
    private boolean isDateSelected = false;
    private boolean isVisitorCountValid = false;

    private DatabaseReference bookingsRef;
    private String selectedDate;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peymentpage);

        etCardNumber = findViewById(R.id.etCardNumber);
        etExpirationMonth = findViewById(R.id.etExpirationMonth);
        etExpirationYear = findViewById(R.id.etExpirationYear);
        etCvv = findViewById(R.id.etCvv);
        chkAgree = findViewById(R.id.chkAgree);
        btnPay = findViewById(R.id.btnPay);
        dateRangeText = findViewById(R.id.dateRangeText);
        guestsTextView = findViewById(R.id.guestsTextView);
        totalTXT = findViewById(R.id.totalTXT);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
       // bookingsRef = database.getReference("bookings");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid(); // Get the UserID
        }

        Intent intent = getIntent();
        if (intent != null) {
            hotelPrice = intent.getDoubleExtra("HotelPrice", 0);
            hotelName = intent.getStringExtra("HotelName");
    }

        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 16) {
                    etCardNumber.setError("Card number should be 16 digits");
                } else {
                    etCardNumber.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 3) {
                    etCvv.setError("CVV should be 3 digits");
                } else {
                    etCvv.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        List<String> monthsList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthsList.add(String.valueOf(i));
        }

        final String[] monthsArray = monthsList.toArray(new String[0]);
        etExpirationMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etExpirationMonth.getText().toString().isEmpty()) {
                    etExpirationMonth.setError("Please enter a month");
                } else {
                    int selectedMonth = Integer.parseInt(etExpirationMonth.getText().toString());
                    if (selectedMonth >= 1 && selectedMonth <= 12) {
                        etExpirationMonth.setError(null); // Clear error if a valid month is entered
                    } else {
                        etExpirationMonth.setError("Please enter a valid month (1-12)");
                    }
                }
            }
        });



        etExpirationYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etExpirationYear.getText().toString().isEmpty()) {
                    etExpirationYear.setError("Please enter a year");
                } else {
                    int selectedYear = Integer.parseInt(etExpirationYear.getText().toString());
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
                    if (selectedYear >= 23 && selectedYear <= 50) {
                        etExpirationYear.setError(null); // Clear error if a valid year is entered
                    } else {
                        etExpirationYear.setError("Please enter a valid year");
                    }
                }
            }
        });





        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCardNumberValid = etCardNumber.getText().toString().length() == 16;
                isMonthSelected = !etExpirationMonth.getText().toString().isEmpty();
                isYearSelected = !etExpirationYear.getText().toString().isEmpty();
                isCvvValid = etCvv.getText().toString().length() == 3;
                isAgreed = chkAgree.isChecked();
                isDateSelected = !dateRangeText.getText().toString().equals("Select date");
                isVisitorCountValid = !guestsTextView.getText().toString().equals("Select number of guests");
                selectedDate = dateRangeText.getText().toString();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userid = user.getUid();

                    ////DatabaseReference userBookingsRef = bookingsRef.child(userid);//

                    DatabaseReference userBookingsRef = FirebaseDatabase.getInstance().getReference().child("bookings").child(userid).push();

                    userBookingsRef.child("nights").setValue(numberOfDays);
                    userBookingsRef.child("hotelName").setValue(hotelName);
                    userBookingsRef.child("totalAmount").setValue(hotelPrice * numberOfDays);
                    userBookingsRef.child("bookingDate").setValue(selectedDate);
                    userBookingsRef.child("UserID").setValue(userid);


                } else {
                    Toast.makeText(peymentpage.this, "Please login first", Toast.LENGTH_SHORT).show();
                }



                if (isCardNumberValid && isMonthSelected && isYearSelected && isCvvValid && isAgreed && isDateSelected && isVisitorCountValid) {
                    showPaymentSuccessDialog();
                } else {
                    Toast.makeText(peymentpage.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    if (!isCardNumberValid) {
                        etCardNumber.setError("Card number should be 16 digits");
                    }
                    if (!isMonthSelected) {
                        etExpirationMonth.setError("Please select a month");
                    }
                    if (!isYearSelected) {
                        etExpirationYear.setError("Please select a year");
                    }
                    if (!isCvvValid) {
                        etCvv.setError("CVV should be 3 digits");
                    }
                    if (!isAgreed) {
                        Toast.makeText(peymentpage.this, "Please agree to the terms.", Toast.LENGTH_SHORT).show();
                    }
                    if (!isDateSelected) {
                        dateRangeText.setError("Please select a date");
                    }
                    if (!isVisitorCountValid) {
                        guestsTextView.setError("Please enter the number of visitors");
                    }
                }
            }
        });



        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                .build();


        dateRangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "Tag_picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        long startDate = selection.first;
                        long endDate = selection.second;
                        numberOfDays = calculateNumberOfNights(startDate, endDate);
                        dateRangeText.setText(materialDatePicker.getHeaderText());
                        hotelPrice = getIntent().getDoubleExtra("HotelPrice", 0);

                        updateTotalAmount();

                        // Calculate the number of days
                        long numberOfDays = TimeUnit.MILLISECONDS.toDays(endDate - startDate);

                        dateRangeText.setError(null);

                    }
                });
            }
        });



        guestsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuestsDialog();
            }
        });
    }
    private long calculateNumberOfNights(long startDate, long endDate) {

        long millisecondsPerDay = 24 * 60 * 60 * 1000;
        return (endDate - startDate) / millisecondsPerDay;
    }

    private void updateTotalAmount() {
        String nightsText = String.format(Locale.getDefault(), "You will pay for %d night/s", numberOfDays);
        String totalPriceText = String.format(Locale.getDefault(), "Total amount: %.2f$", hotelPrice * numberOfDays);

        totalTXT.setText(nightsText);
        totalTXT.append("\n in " + hotelName );
        totalTXT.append("\n" + totalPriceText);
    }

    private void showPaymentSuccessDialog() {

        ProgressDialog progressDialog = ProgressDialog.show(peymentpage.this, "", "Processing Payment...", true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialog.dismiss();

                // عرض AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(peymentpage.this);
                builder.setTitle("Payment Success")
                        .setMessage("Payment has been successfully processed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(peymentpage.this, homePage.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.show();

                // تاريخ الحجز
                String selectedDate = dateRangeText.getText().toString();
               // DatabaseReference userBookingsRef = bookingsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

/*
                DatabaseReference newBookingRef = bookingsRef.push();
                newBookingRef.child("nights").setValue(numberOfDays);
                newBookingRef.child("hotelName").setValue(hotelName);
                newBookingRef.child("totalAmount").setValue(hotelPrice * numberOfDays);
                newBookingRef.child("bookingDate").setValue(selectedDate);
                userBookingsRef.child("UserID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
*/

            }
        }, 2000); // المدة بالميلي ثانية (4 ثوانٍ)

    }



    private void showGuestsDialog() {
        adultsPicker = new NumberPicker(this);
        childrenPicker = new NumberPicker(this);

        adultsPicker.setMinValue(0);
        adultsPicker.setMaxValue(10);

        childrenPicker.setMinValue(0);
        childrenPicker.setMaxValue(10);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("          Adult                          Children");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        layout.addView(adultsPicker, params);
        layout.addView(childrenPicker, params);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int adults = adultsPicker.getValue();
                int children = childrenPicker.getValue();

                String guestsText = "Adults: " + adults + ", Children: " + children;
                SpannableString spannableString = new SpannableString(guestsText);

                spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 7, guestsText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                guestsTextView.setText(spannableString);
                guestsTextView.setError(null); // إزالة العلامة الحمراء
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancel button click
            }
        });
        final AlertDialog dialog = builder.create();

        guestsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }
}
