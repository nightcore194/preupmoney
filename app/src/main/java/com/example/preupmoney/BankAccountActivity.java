package com.example.preupmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

public class BankAccountActivity extends AppCompatActivity
{
    Button bank_account, payment, service, investment, chat;
    ImageButton settings, profile;
    View search;
    NestedScrollView nsv;
    ConstraintLayout csl;
    DatabaseHelper mDBHelper;
    SQLiteDatabase mDb;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_account);
        bank_account = findViewById(R.id.bank_account);
        payment = findViewById(R.id.payment);
        service = findViewById(R.id.service);
        investment = findViewById(R.id.investment);
        chat = findViewById(R.id.chat);
        settings = findViewById(R.id.settings);
        profile = findViewById(R.id.user);
        search = findViewById(R.id.search_bar);
        nsv = findViewById(R.id.nsv);
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getWritableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM bank_account", null);
        if (cursor.moveToFirst()) {
            do {
                Button btn = new Button(getApplicationContext());
                btn.setText(cursor.getString(5));
                btn.setTextColor(R.color.main_light_font_and_elem);
                btn.setOnClickListener(view -> {
                    setContentView(R.layout.bank_account_info);
                    TextView textView = new TextView(getApplicationContext());
                    csl = findViewById(R.id.bank_account_full_info);
                    textView.setText("Дата открытия - "+ cursor.getString(3)+"\nТариф - "+ cursor.getString(5));
                    csl.addView(textView);
                });
                nsv.addView(btn);
            } while (cursor.moveToNext());
        }
        bank_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(BankAccountActivity.this, BankAccountActivity.class));
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(BankAccountActivity.this, PaymentActivity.class));
            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(BankAccountActivity.this, ServiceActivity.class));
            }
        });
        investment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(BankAccountActivity.this, InvestmentActivity.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(BankAccountActivity.this, ChatActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BankAccountActivity.this, ProfileActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BankAccountActivity.this, SettingsActivity.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BankAccountActivity.this, SearchActivity.class));
            }
        });
    }
}