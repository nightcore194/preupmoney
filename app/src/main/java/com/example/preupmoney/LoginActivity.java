package com.example.preupmoney;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.preupmoney.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import io.getstream.chat.android.client.models.User;

public class LoginActivity extends AppCompatActivity
{
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Button one, two, three, four, five, six, seven, eight, nine, zero, forgot, singIn;
    private ImageButton delete, face_id;
    private String pass = "", pass_complete;
    private Boolean auth = false;
    private EditText phone, password;
    private ConstraintLayout pin, authorize;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    protected static User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        delete = findViewById(R.id.delete);
        face_id = findViewById(R.id.unlock_face_id);
        forgot = findViewById(R.id.forgot);
        pin = findViewById(R.id.pin);
        authorize = findViewById(R.id.auth);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        singIn = findViewById(R.id.sing_in);
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getWritableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM auth_data", null);
        ArrayList<String[]> courseModalArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String[] ss = {cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)};
                courseModalArrayList.add(ss);
            } while (cursor.moveToNext());
        }
        cursor.close();
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences preference_id = getSharedPreferences("id", MODE_PRIVATE);
        String authed = preferences.getString("auth","");
        if(authed.equals("true"))
        {
            authorize.setVisibility(View.INVISIBLE);
            pin.setVisibility(View.VISIBLE);
        }
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();
        BiometricManager biometricManager = BiometricManager.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    auth = true;
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    auth = false;
                    break;
            }
        }
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Ошибка авторизации: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Успешно!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Авторизация провалилась...",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        if(!auth)
            face_id.setVisibility(View.GONE);
        singIn.setOnClickListener(view -> {
            for (String[] s: courseModalArrayList) {
                if(phone.getText().equals(s[1])&&password.getText().equals(s[2]))
                {
                    SharedPreferences preferences1 = getSharedPreferences("auth", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putString("auth", "ture");
                    editor.putString("id", cursor.getString(0));
                    editor.apply();
                    User user = new User();
                    user.setId(s[1]);
                    user.setName(mDb.rawQuery("SELECT FIO FROM clients WHERE id_auth ="+s[0]+"", null).getString(0));
                    pass_complete = s[3 ];
                    authorize.setVisibility(View.INVISIBLE);
                    pin.setVisibility(View.VISIBLE);
                } else
                {
                    SharedPreferences preferences1 = getSharedPreferences("auth", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putString("auth", "false");
                    editor.putString("id", "");
                    editor.apply();
                }
            }

        });
        face_id.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));
        delete.setOnClickListener(view -> {
            if(pass.length()>0)
                pass = pass.substring(0,pass.length()-2);
        });

        one.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + one.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        two.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + two.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        three.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + three.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        four.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + four.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        five.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + five.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        six.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + six.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        seven.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + seven.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        eight.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + eight.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        nine.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + nine.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
        zero.setOnClickListener(view -> {
            if(pass.length()<4)
                pass = pass + zero.getText();
            if(pass.equals(pass_complete))
                startActivity(new Intent(LoginActivity.this, BankAccountActivity.class));
        });
    }
}