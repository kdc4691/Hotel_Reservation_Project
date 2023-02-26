package com.inhatc.hotel;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;
    File folder, file ;
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_db);

        folder = new File("/data/data/com.inhatc.hotel/databases/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        file = new File("/data/data/com.inhatc.hotel/databases/member");

        if (!file.exists()) {
            startActivity(new Intent(FirstAuthActivity.this,CreateDB.class));
        } else {
            if(SaveSharedPreference.getUserName(FirstAuthActivity.this).length() == 0) {
                // call Login Activity
                intent = new Intent(FirstAuthActivity.this, Login.class);
                startActivity(intent);
                this.finish();
            } else {
                // Call Next Activity
                intent = new Intent(FirstAuthActivity.this, RoomList.class);
                intent.putExtra("STD_NUM", SaveSharedPreference.getUserName(this).toString());
                startActivity(intent);
                this.finish();
            }
        }
    }
}