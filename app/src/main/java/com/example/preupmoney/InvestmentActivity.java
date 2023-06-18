package com.example.preupmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvestmentActivity extends AppCompatActivity {

    Button bank_account, payment, service, investment, chat;
    ImageButton settings, profile;
    View search;
    NestedScrollView stocks;
    String apiKey = "dNsL06T1Xe6gZmPc6UwLqdeGzDfoZa6A";
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investment);
        bank_account = findViewById(R.id.bank_account);
        payment = findViewById(R.id.payment);
        service = findViewById(R.id.service);
        investment = findViewById(R.id.investment);
        chat = findViewById(R.id.chat);
        settings = findViewById(R.id.settings);
        profile = findViewById(R.id.user);
        search = findViewById(R.id.search_bar);
        stocks = findViewById(R.id.stocks);
        try
        {
            // получение техущей даты
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            // уменьшение текущей даты на 1, т.к. нельзя следить через эту api в realtime
            String data = dtf.format(now).substring(0, dtf.format(now).length()-1)+ (Integer.parseInt(dtf.format(now).substring(dtf.format(now).length() - 1)) - 1);
            // настраивание подключение к api по url
            URL tickers = new URL("https://api.polygon.io/v3/reference/tickers?active=true&apiKey="+apiKey);
            URLConnection yc = tickers.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            // занесение ответа в буфферную переменную
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            // преобразование буфернной переменной в json объект
            JSONObject myobj = new JSONObject(String.valueOf(response));
            JSONArray responses = myobj.getJSONArray("results");
            // получение тикеров по точно такой же схеме
            for (int i = 0; i<responses.length();i++)
            {
                try {
                    // настраивание подключение к api по url
                    JSONObject tempobj = new JSONObject(String.valueOf(responses.get(i)));
                    URL ticker = new URL("https://api.polygon.io/v1/open-close/" + tempobj.getString("ticker") + "/" + data + "?active=true&apiKey=" + apiKey);
                    URLConnection ti = ticker.openConnection();
                    in = new BufferedReader(new InputStreamReader(ti.getInputStream()));
                    // занесение ответа в буфферную переменную
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null)
                        response.append(inputLine);
                    in.close();
                    // преобразование буфернной переменной в json объект
                    JSONObject tick = new JSONObject(String.valueOf(response));
                    String name_of_stock = tempobj.getString("name");
                    String data_of_stock = "открытие - "+tick.getInt("open") + "\nзакрытие -" + tick.getInt("close");
                    // добавление кнопки с данными о тикере
                    Button btn = new Button(getApplicationContext());
                    btn.setText(name_of_stock+"\n"+data_of_stock);
                    btn.setTextColor(R.color.main_light_font_and_elem);
                    btn.setOnClickListener(view -> {
                        try {
                            SharedPreferences preferences = getSharedPreferences("isExistAny", MODE_PRIVATE);
                            SharedPreferences tick_pref = getSharedPreferences("tickers", MODE_PRIVATE);
                            SharedPreferences.Editor edit_pref = preferences.edit();
                            SharedPreferences.Editor edit_tick = tick_pref.edit();
                            edit_pref.putString("isExistAny", "true");
                            edit_tick.putString("tickers", tick_pref.getString("tickers","")+tempobj.getString("ticker")+"/"+name_of_stock+";");
                            edit_tick.apply();
                            edit_pref.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    stocks.addView(btn);
                } catch (Exception ignored) {}
            }
        } catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
        bank_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(InvestmentActivity.this, BankAccountActivity.class));
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(InvestmentActivity.this, PaymentActivity.class));
            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(InvestmentActivity.this, ServiceActivity.class));
            }
        });
        investment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(InvestmentActivity.this, InvestmentActivity.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(InvestmentActivity.this, ChatActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InvestmentActivity.this, ProfileActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InvestmentActivity.this, SettingsActivity.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InvestmentActivity.this, SearchActivity.class));
            }
        });

    }
}