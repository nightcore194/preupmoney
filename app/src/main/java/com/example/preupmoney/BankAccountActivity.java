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
    View search, add_btn;
    ConstraintLayout csl;
    DatabaseHelper mDBHelper;
    SQLiteDatabase mDb;
    Intent intent;
    Cursor cursor;
    Float aFloat = 0f;
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
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
        csl = findViewById(R.id.csl);
        add_btn = findViewById(R.id.add_btn);
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getWritableDatabase();
        SharedPreferences preference = this.getSharedPreferences("preupmoney", MODE_PRIVATE);
        cursor = mDb.rawQuery("SELECT * FROM bank_account where id_client = ?", new String[]{preference.getString("id","")});
        if (cursor.moveToFirst()) {
            do {
                Button btn = new Button(getApplicationContext());
                btn.setText(cursor.getString(5));
                btn.setTextColor(R.color.main_light_font_and_elem);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT , ConstraintLayout.LayoutParams.WRAP_CONTENT);
                aFloat += (aFloat>=1f) ? 0.05f : 0f;
                params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                params.verticalBias = aFloat;
                btn.setLayoutParams(params);
                String date = cursor.getString(3);
                String tariff = cursor.getString(5);
                btn.setOnClickListener(view -> {
                    intent = new Intent(this, BankAccountInfoActivity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("tariff", tariff);
                    startActivity(intent);
                });
                csl.addView(btn);
            } while (cursor.moveToNext());
        }
        add_btn.setOnClickListener(view -> {
            intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        });
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
        search.setOnClickListener(view -> startActivity(new Intent(this, SearchActivity.class)));
    }
}