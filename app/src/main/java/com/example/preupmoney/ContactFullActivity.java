package com.example.preupmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class ContactFullActivity extends AppCompatActivity {

    Button bank_account, payment, service, investment, chat;
    ImageButton settings, profile;
    TextView textView;
    Intent intent;
    ImageButton back;
    ConstraintLayout csl;
    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_full);
        back = findViewById(R.id.go_back);
        bank_account = findViewById(R.id.bank_account);
        payment = findViewById(R.id.payment);
        service = findViewById(R.id.service);
        investment = findViewById(R.id.investment);
        chat = findViewById(R.id.chat);
        settings = findViewById(R.id.settings);
        profile = findViewById(R.id.user);
        csl = findViewById(R.id.contact_full_block);
        textView = findViewById(R.id.contact_full_block_tv);
        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("name").toString();
        String phone = arguments.get("phone").toString();
        textView.setText("Имя: "+ name + "\nНомер телефона - "+ phone);
        back.setOnClickListener(view -> finish());
        bank_account.setOnClickListener(view -> {
            intent = new Intent(this, BankAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        payment.setOnClickListener(view -> {
            intent = new Intent(this, PaymentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        service.setOnClickListener(view -> {
            intent = new Intent(this, ServiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        investment.setOnClickListener(view -> {
            intent = new Intent(this, InvestmentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        chat.setOnClickListener(view -> {
            intent =new Intent(this, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        profile.setOnClickListener(view -> startActivity(new Intent(this, ProfileActivity.class)));
        settings.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}