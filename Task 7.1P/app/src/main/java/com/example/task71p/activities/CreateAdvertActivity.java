package com.example.task71p.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.task71p.R;
import com.example.task71p.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    private RadioGroup radioGroupType;
    private RadioButton radioLost;
    private RadioButton radioFound;
    private EditText editName;
    private EditText editPhone;
    private EditText editDescription;
    private EditText editDate;
    private EditText editLocation;
    private Button btnSave;

    private Calendar calendar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize database helper
        dbHelper = DatabaseHelper.getInstance(this);
        
        // Initialize calendar for date picker
        calendar = Calendar.getInstance();
        
        // Initialize UI components
        initializeUI();
        
        // Set up date picker
        setupDatePicker();
        
        // Set up save button click listener
        setupSaveButton();
    }

    private void initializeUI() {
        radioGroupType = findViewById(R.id.radio_group_type);
        radioLost = findViewById(R.id.radio_lost);
        radioFound = findViewById(R.id.radio_found);
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editDescription = findViewById(R.id.edit_description);
        editDate = findViewById(R.id.edit_date);
        editLocation = findViewById(R.id.edit_location);
        btnSave = findViewById(R.id.btn_save);
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateAdvertActivity.this,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
    }

    private void updateDateLabel() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        editDate.setText(sdf.format(calendar.getTime()));
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void saveItem() {
        // Get values from form
        String type = radioLost.isChecked() ? "Lost" : "Found";
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String location = editLocation.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(description) || TextUtils.isEmpty(date) ||
                TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to database
        long result = dbHelper.addItem(type, name, phone, description, date, location);
        
        if (result != -1) {
            Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to previous screen
        } else {
            Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show();
        }
    }
} 