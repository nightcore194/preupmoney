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
import java.util.concurrent.atomic.AtomicReference;

public class ProfileActivity extends AppCompatActivity {

    Button bank_account, payment, service, investment, chat;
    ImageButton settings, profile, go_back;
    NestedScrollView nsv;
    ConstraintLayout csl;
    DatabaseHelper mDBHelper;
    SQLiteDatabase mDb;
    Intent intent;
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
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
        SharedPreferences preference = this.getSharedPreferences("preupmoney", MODE_PRIVATE);
        String id = preference.getString("id","");
        AtomicReference<Cursor> cursor = new AtomicReference<>(mDb.rawQuery("SELECT * FROM clients where id_client = ?", new String[]{id}));
        if (cursor.get().moveToFirst())
        {
            TextView textView = new TextView(getApplicationContext());
            csl = findViewById(R.id.block_profile);
            textView.setText("ФИО - " + cursor.get().getString(1) +
                    "\nПол - " + cursor.get().getString(2) +
                    "\nАдрес - " + cursor.get().getString(4));
            csl.addView(textView);
        }
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
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
    }
}