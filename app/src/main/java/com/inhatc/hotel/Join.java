package com.inhatc.hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Join extends AppCompatActivity implements View.OnClickListener{

    SQLiteDatabase myDB;
    ArrayList<String> DBList;
    Cursor allRCD;
    ContentValues insertVlaue;
    EditText joinID, joinPW, joinPWC, joinName, joinEmail;
    Button btnIDC,btnJoin;
    String DBL;
    String Stordid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //입력 텍스트
        joinID = (EditText)findViewById(R.id.joinID);
        joinPW = (EditText)findViewById(R.id.joinPW);
        joinPWC = (EditText)findViewById(R.id.joinPWC);
        joinName = (EditText)findViewById(R.id.joinName);
        joinEmail = (EditText)findViewById(R.id.joinEmail);

        // 버튼
        btnIDC = (Button)findViewById(R.id.btnIDC);
        btnIDC.setOnClickListener(this);

        btnJoin = (Button)findViewById(R.id.btnjoin);
        btnJoin.setOnClickListener(this);
    }


    // DB 정보를 가져오는 메소드
    public void getDBData(String strWhere, int Column){
        allRCD = myDB.query("member", null, strWhere, null, null,
                null, null, null);

        if(allRCD != null){
            if (allRCD.moveToFirst()) {
                do{
                    DBL = allRCD.getString(Column);
//                    DBList.add(allRCD.getString(1));
                }while(allRCD.moveToNext());
            }
        }
    }


    // 회원가입 버튼
    @Override
    public void onClick(View v) {
        try {
            myDB = this.openOrCreateDatabase("member", MODE_PRIVATE, null);

        //가입 버튼 클릭시
        if(v == btnJoin){
            if(joinID.getText().toString().equals("") ||
               joinPW.getText().toString().equals("") ||
               joinName.getText().toString().equals("") ||
               joinEmail.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"입력하지 않은 정보가 있습니다!", Toast.LENGTH_LONG).show();
            }
            else if(Stordid.equals("") || !Stordid.equals(joinID.getText().toString())){
                Toast.makeText(getApplicationContext(),"아이디 중복을 확인해 주세요", Toast.LENGTH_LONG).show();
            }
            else if(!joinPW.getText().toString().equals(joinPWC.getText().toString())){
                Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
            }
            else{
              insertVlaue = new ContentValues();
              insertVlaue.put("UserId", joinID.getText().toString());
              insertVlaue.put("UserPw", joinPW.getText().toString());
              insertVlaue.put("UserName", joinName.getText().toString());
              insertVlaue.put("UserEmail", joinEmail.getText().toString());
              myDB.insert("member", null, insertVlaue);

              Toast.makeText(getApplicationContext(),"회원가입 성공", Toast.LENGTH_LONG).show();

              // 회원가입 성공시 로그인페이지로 이동
              Intent login = new Intent(Join.this, Login.class);
              startActivity(login);
            }

        }

        // 아이디 중복검사
        if(v == btnIDC){
            Stordid = joinID.getText().toString();
            String str = "UserId = '" + joinID.getText().toString() + "'";
            getDBData(str,0);
            try {
                if(joinID.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요", Toast.LENGTH_LONG).show();
                }
                else if (joinID.getText().toString().equals(DBL)) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디 입니다.", Toast.LENGTH_LONG).show();
                    Stordid = "";
                } else {
                    Toast.makeText(getApplicationContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "기타에러 : e", Toast.LENGTH_LONG).show();
            }
        }

        } catch (SQLiteException e) {
            e.printStackTrace() ;
        }

    }
}
