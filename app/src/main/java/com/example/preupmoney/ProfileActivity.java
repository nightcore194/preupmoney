package com.example.preupmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class ProfileActivity extends AppCompatActivity {

    Button bank_account, payment, service, investment, chat;
    ImageButton settings, profile, go_back, logout;
    EditText profile_info_name, profile_info_gender, profile_info_pasport, profile_info_address;
    ConstraintLayout save_profile;
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
        logout = findViewById(R.id.logout);
        profile_info_name = findViewById(R.id.profile_info_name);
        profile_info_gender = findViewById(R.id.profile_info_gender);
        profile_info_pasport = findViewById(R.id.profile_info_pasport);
        profile_info_address = findViewById(R.id.profile_info_address);
        save_profile = findViewById(R.id.save_profile);
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
            profile_info_name.setText(cursor.get().getString(1));
            profile_info_gender.setText(cursor.get().getString(2));
            profile_info_pasport.setText(cursor.get().getString(3));
            profile_info_address.setText(cursor.get().getString(4));
        }
        go_back.setOnClickListener(view -> finish());
        logout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = preference.edit();
            editor.putString("auth", "false");
            editor.putString("id","");
            editor.commit();
            intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        save_profile.setOnClickListener(view ->
        {
            ContentValues cv = new ContentValues();
            cv.put("FIO", profile_info_name.getText().toString()); //These Fields should be your String values of actual column names
            cv.put("gender", profile_info_gender.getText().toString());
            cv.put("passport_data", profile_info_pasport.getText().toString());
            cv.put("address", profile_info_address.getText().toString());
            mDb.update("clients", cv, "id_client = ?", new String[]{preference.getString("id","")});
            Toast.makeText(this, "Успешно сохранено!", Toast.LENGTH_SHORT).show();
            finish();
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