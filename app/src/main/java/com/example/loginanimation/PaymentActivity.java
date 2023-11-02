package com.example.loginanimation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class PaymentActivity extends AppCompatActivity {
    EditText cardNumberEditText, expiryDateEditText, cvvEditText;
    Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        payButton = findViewById(R.id.payButton);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add payment processing logic here
                String cardNumber = cardNumberEditText.getText().toString();
                String expiryDate = expiryDateEditText.getText().toString();
                int cvv = Integer.parseInt(cvvEditText.getText().toString());

                // Initiate the payment using a payment gateway or service
                initiatePayment(cardNumber, expiryDate, cvv);
            }
        });
    }

    // Add your payment initiation method here
    private void initiatePayment(String cardNumber, String expiryDate, int cvv) {
    }
}