package com.example.preupmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class PaymentActivity extends AppCompatActivity {

    Button bank_account, payment, service, investment, chat;
    ImageButton settings, profile;
    NestedScrollView nsv;
    ConstraintLayout csl;
    View search;
    Intent intent;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        bank_account = findViewById(R.id.bank_account);
        payment = findViewById(R.id.payment);
        service = findViewById(R.id.service);
        investment = findViewById(R.id.investment);
        chat = findViewById(R.id.chat);
        settings = findViewById(R.id.settings);
        profile = findViewById(R.id.user);
        search = findViewById(R.id.search_bar);
        nsv = findViewById(R.id.nsv);
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();
        getContacts();
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
    @SuppressLint({"Range", "ResourceAsColor", "SetTextI18n"})
    public void getContacts() {

        //Связываемся с контактными данными и берем с них значения id контакта, имени контакта и его номера:
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        //Запускаем цикл обработчик для каждого контакта:
        if (cursor.getCount() > 0) {

            //Если значение имени и номера контакта больше 0 (то есть они существуют) выбираем
            //их значения в приложение привязываем с соответствующие поля "Имя" и "Номер":
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                @SuppressLint("Range") int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                String phoneNumber = null;

                //Получаем имя:
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                            Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    //и соответствующий ему номер:
                    while (phoneCursor.moveToNext())
                    {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        Button btn = new Button(getApplicationContext());
                        btn.setText("Имя: " + name);
                        btn.setTextColor(R.color.main_light_font_and_elem);
                        String finalPhoneNumber = phoneNumber;
                        btn.setOnClickListener(view -> {
                            intent = new Intent(this, ContactFullActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("phone", finalPhoneNumber);
                            startActivity(intent);
                        });
                        nsv.addView(btn);
                    }
                }
            }
        }
    }
}