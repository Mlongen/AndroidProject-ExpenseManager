package com.example.marcelolongen.expensemanager;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddExpense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);


        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();

                Intent intent = new Intent(getApplicationContext(), Overview.class);
                startActivity(intent);

            }
        });
    }

    private void addExpense() {
        TextView descriptionText = findViewById(R.id.description);
        TextView categoryText = findViewById(R.id.category);
        TextView amountText = findViewById(R.id.amount);
        DatePicker datePicker = findViewById(R.id.date);
        String description = descriptionText.getText().toString();
        String date = String.valueOf(datePicker.getMonth() + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear());
        String amount = amountText.getText().toString();
        String category = categoryText.getText().toString();
        String formatted = description + ";" + amount + ";" + date + ";" + category + ";";

        String MYFILE = Environment.getExternalStorageDirectory().toString();
        String fileName = "entries.txt";
        File f = new File(MYFILE,fileName);

        try (FileOutputStream fop = new FileOutputStream(f, true)) {

            // if file doesn't exists, then create it
            if (!f.exists()) {
                f.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = formatted.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        toast(description + " added.");
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
