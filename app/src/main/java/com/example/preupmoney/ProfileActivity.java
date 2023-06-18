package com.example.preupmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    Button bank_account, payment, service, investment, chat;
    ImageButton settings, profile, go_back;
    NestedScrollView nsv;
    ConstraintLayout csl;
    DatabaseHelper mDBHelper;
    SQLiteDatabase mDb;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        bank_account = findViewById(R.id.bank_account);
        payment = findViewById(R.id.payment);
        service = findViewById(R.id.service);
        investment = findViewById(R.id.investment);
        chat = findViewById(R.id.chat);
        settings = findViewById(R.id.settings);
        profile = findViewById(R.id.user);
        go_back = findViewById(R.id.go_back);
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getWritableDatabase();
        SharedPreferences preference_id = getSharedPreferences("id", MODE_PRIVATE);
        Cursor cursor = mDb.rawQuery("SELECT * FROM clients where id_client = "+preference_id.getString("id",""), null);
        setContentView(R.layout.bank_account_info);
        TextView textView = new TextView(getApplicationContext());
        csl = findViewById(R.id.block_profile);
        textView.setText("ФИО - "+ cursor.getString(1)+
                "\nПол - "+ cursor.getString(2) +
                "\nАдрес - "+ cursor.getString(4));
        csl.addView(textView);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bank_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, BankAccountActivity.class));
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, PaymentActivity.class));
            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, ServiceActivity.class));
            }
        });
        investment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, InvestmentActivity.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });
    }
}