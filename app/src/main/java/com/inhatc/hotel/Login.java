package com.inhatc.hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button btnJoin, btnLogin;
    ImageView imageView;
    EditText txtLoginID, txtLoginPW;
    SQLiteDatabase myDB;
    Cursor allRCD;
    String DBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLoginID = (EditText)findViewById(R.id.txtLoginID);
        txtLoginPW = (EditText)findViewById(R.id.txtLoginPW);

        btnJoin = (Button)findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(this);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(this);

    }

    public void getDBData(String strWhere, int Column) {
        myDB = this.openOrCreateDatabase("member", MODE_PRIVATE, null);
        allRCD = myDB.query("member", null, strWhere, null, null,
                null, null, null);

        if (allRCD != null) {
            if (allRCD.moveToFirst()) {
                do {
                    DBL = allRCD.getString(Column);
//                    DBList.add(allRCD.getString(1));
                } while (allRCD.moveToNext());
            }
        }
    }

    public void onClick(View v){
        String getID;
        String getPW;

        String str = "UserId = '" + txtLoginID.getText().toString() + "'";
        getDBData(str,0);
        getID = DBL;

        str = "UserId = '" + txtLoginID.getText().toString() + "'";
        getDBData(str,1);
        getPW = DBL;

        if(v == btnJoin){
            Intent join = new Intent(Login.this, Join.class);
            startActivity(join);
        }

        if(v == btnLogin){
            myDB = this.openOrCreateDatabase("member", MODE_PRIVATE, null);
            String getId = "UserId = '" + txtLoginID.getText().toString() + "'";
            getDBData(getId,0);

            if(txtLoginID.getText().toString().equals("")
             ||txtLoginPW.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"???????????? ??????????????? ?????? ????????? ?????????", Toast.LENGTH_LONG).show();
            }
            else if(!txtLoginID.getText().toString().equals(getID)){
                Toast.makeText(getApplicationContext(),"???????????? ?????? ????????? ?????????.", Toast.LENGTH_LONG).show();
            }
            else if(!txtLoginPW.getText().toString().equals(getPW)){
                Toast.makeText(getApplicationContext(),"??????????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();
            }
            else {
                ProgressDialog.show(Login.this, "Please wait", "Loading...");
                // SharedPreference API??? ????????? ??????ID ?????? -> ????????? ?????? ??????
                SaveSharedPreference.setUserName(Login.this, txtLoginID.getText().toString());
                Toast.makeText(getApplicationContext(), "???????????????.", Toast.LENGTH_LONG).show();
                Intent roomList = new Intent(Login.this, RoomList.class);
                startActivity(roomList);
            }
        }

        if(v == imageView){
            Intent roomList = new Intent(Login.this, RoomList.class);
            startActivity(roomList);
        }
    }
}
