package com.example.preupmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    EditText phone, pass, pass_confirm;
    Button confirm_register, go_back;
    DatabaseHelper mDBHelper;
    SQLiteDatabase mDb;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        pass_confirm = findViewById(R.id.password_confim);
        go_back = findViewById(R.id.go_back);
        confirm_register = findViewById(R.id.confirm_register);
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getWritableDatabase();
        SharedPreferences preference = this.getSharedPreferences("preupmoney", MODE_PRIVATE);
        go_back.setOnClickListener(view -> finish());
        confirm_register.setOnClickListener(view ->
        {
            if(pass.getText().toString().equals(pass_confirm.getText().toString()))
            {
                mDb.execSQL("INSERT INTO auth_data (phone_number_auth,  password_auth, pin_code) values(?,?,?)", new String[]{phone.getText().toString(), pass.getText().toString(), "0000"});
                SharedPreferences.Editor editor = preference.edit();
                editor.putString("auth", "true");
                Cursor cursor = mDb.rawQuery("SELECT id_auth from auth_data where phone_number_auth = ?", new String[]{phone.getText().toString()});
                if(cursor.moveToFirst())
                    editor.putString("id", cursor.getString(0));
                mDb.execSQL("insert into clients (id_auth, FIO, gender, passport_data, address) values(?, ?, ?, ?, ?)", new String[]{cursor.getString(0), "", "", "", ""});
                editor.commit();
                intent = new Intent(this, BankAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Пароли не совпадают!")
                        .setMessage("Проверьте правильность введенных паролей.")
                        .setIcon(R.drawable.lock)
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create();
            }
        });
    }
}