package com.example.preupmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ContactFullActivity extends AppCompatActivity {

    Button bank_account, payment, service, investment, chat;
    ImageButton settings, profile;
    TextView textView, left_balance_text;
    EditText sum_input;
    Spinner choosen_bank;
    DatabaseHelper mDBHelper;
    SQLiteDatabase mDb;
    Cursor cursor;
    Intent intent;
    ImageButton back;
    int id;
    ConstraintLayout csl, transaction_btn;
    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_full);
        back = findViewById(R.id.go_back);
        bank_account = findViewById(R.id.bank_account);
        sum_input = findViewById(R.id.sum_input);
        payment = findViewById(R.id.payment);
        service = findViewById(R.id.service);
        investment = findViewById(R.id.investment);
        chat = findViewById(R.id.chat);
        settings = findViewById(R.id.settings);
        profile = findViewById(R.id.user);
        csl = findViewById(R.id.contact_full_block);
        textView = findViewById(R.id.contact_full_block_tv);
        left_balance_text = findViewById(R.id.left_balance_text);
        transaction_btn = findViewById(R.id.transaction_btn);
        choosen_bank = findViewById(R.id.choosen_bank);
        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("name").toString();
        String phone = arguments.get("phone").toString();
        textView.setText("Имя: "+ name + "\nНомер телефона - "+ phone);
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getWritableDatabase();
        SharedPreferences preference = this.getSharedPreferences("preupmoney", MODE_PRIVATE);
        cursor = mDb.rawQuery("SELECT * FROM bank_account where id_client = ?", new String[]{preference.getString("id","")});
        ArrayList<String[]> courseModalArrayList = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String[] ss = {cursor.getString(0), cursor.getString(1), cursor.getString(5), cursor.getString(6)};
                courseModalArrayList.add(ss);
                strings.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        String[] slist = new String[strings.size()];
        slist = strings.toArray(slist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, slist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choosen_bank.setAdapter(adapter);
        choosen_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                left_balance_text.setText("Остаток счета:\t\t" + courseModalArrayList.get(i)[3]);
                id = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        transaction_btn.setOnClickListener(view ->
        {
            if(Integer.parseInt(courseModalArrayList.get(id)[3])>=Integer.parseInt(sum_input.getText().toString()))
            {
                int new_sum = Integer.parseInt(courseModalArrayList.get(id)[3]) - Integer.parseInt(sum_input.getText().toString());
                Toast.makeText(this, "Перевод успешен!", Toast.LENGTH_SHORT).show();
                ContentValues cv = new ContentValues();
                cv.put("balance", Integer.toString(new_sum));
                mDb.update("bank_account", cv, "id_bank_account = ?", new String[]{courseModalArrayList.get(id)[0]});
                intent = new Intent(this, BankAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(this, "Сумма перевода не должна превышать сумму остатка.", Toast.LENGTH_SHORT).show();

        });
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