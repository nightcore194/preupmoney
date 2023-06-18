package com.example.preupmoney;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper
{
    // для дальнейшего улучшения - https://guides.codepath.com/android/local-databases-with-sqliteopenhelper
    private static final String DB_NAME = "preupmoney";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;
        copyDataBase();
        this.getReadableDatabase();
        /*try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.preupmoney), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {sb.append(line);}
            mDataBase.execSQL(sb.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();
            copyDataBase();
            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getResources().openRawResource(R.raw.preupmoney);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String line="create table company(\n" +
                "id_company int not null,\n" +
                "name_company varchar(20) not null,\n" +
                "address varchar(30) not null,\n" +
                "license varchar(50) not null,\n" +
                "phone int not null,\n" +
                "constraint pk_id_company primary key (id_company)\n" +
                ");";
        String line2 = "create table workers(\n" +
                "id_company int not null,\n" +
                "id_worker int not null,\n" +
                "FIO varchar(50) not null,\n" +
                "post varchar(50) not null,\n" +
                "phone int not null,\n" +
                "status varchar(50) not null,\n" +
                "constraint fk_id_company foreign key (id_company) references company(id_company),\n" +
                "constraint pk_id_worker primary key (id_worker)\n" +
                ");";
        String line3 = "create table clients(\n" +
                "id_client int not null,\n" +
                "FIO varchar(50) not null,\n" +
                "gender varchar(10) not null,\n" +
                "passport_data varchar(50) not null,\n" +
                "address varchar(50) not null,\n" +
                "constraint pk_id_client primary key (id_client)\n" +
                ");\n";
        String line4 = "create table consult(\n" +
                "id_client int not null,\n" +
                "id_worker int not null,\n" +
                "id_consult int not null,\n" +
                "date_consult date not null,\n" +
                "type_consult varchar(50) not null,\n" +
                "status varchar(50) not null,\n" +
                "constraint fk_id_worker_consult foreign key (id_worker) references workers(id_worker),\n" +
                "constraint fk_id_client_consult foreign key (id_client) references clients(id_client),\n" +
                "constraint pk_id_consult primary key (id_consult)\n" +
                ");\n";
        String line5 =
                "create table bank_account(\n" +
                "id_client int not null,\n" +
                "id_company int not null,\n" +
                "id_bank_account int not null,\n" +
                "date_of_open date not null,\n" +
                "status_of_account varchar(20) not null,\n" +
                "tariff varchar(20) not null,\n" +
                "constraint fk_id_client_bank_account foreign key (id_client) references clients(id_client),\n" +
                "constraint fk_id_company_bank_account foreign key (id_company) references company(id_company),\n" +
                "constraint pk_id_bank_account primary key (id_bank_account)\n" +
                ");\n";
        String line6 =
                "create table bank_requs(\n" +
                "id_bank_account int not null,\n" +
                "bik int not null,\n" +
                "inn int not null,\n" +
                "kpp int not null,\n" +
                "corp_account int not null,\n" +
                "constraint fk_id_bank_account_requs foreign key (id_bank_account) references bank_account(id_bank_account)\n" +
                ");";
        db.execSQL(line);
        db.execSQL(line2);
        db.execSQL(line3);
        db.execSQL(line4);
        db.execSQL(line5);
        db.execSQL(line6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    } }