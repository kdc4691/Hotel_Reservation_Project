package com.inhatc.hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.hotel.model.roomDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RoomDetail extends AppCompatActivity implements View.OnClickListener {

    SQLiteDatabase myDB;
    Cursor allRCD;
    ContentValues insertVlaue;
    ImageView ResDetailImage;
    TextView ResDetail_txtRoomNum, ResDetail_txtRoomPrice, ResDetail_txtRoomEa, ResDetail_txtRoomExplane;
    Button btngoback, btnReservation, btnResCancel;
    int position = 0;
    String getresRoomId;

    roomDTO data = new roomDTO(); ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        RecyclerAdapter adapter = new RecyclerAdapter();

        ResDetailImage = (ImageView)findViewById(R.id.ResDetailImage);

        // 자료입력 텍스트
        ResDetail_txtRoomNum = (TextView)findViewById(R.id.ResDetail_txtRoomNum);
        ResDetail_txtRoomPrice = (TextView)findViewById(R.id.ResDetail_txtRoomPrice);
        ResDetail_txtRoomEa = (TextView)findViewById(R.id.ResDetail_txtRoomEa);
        ResDetail_txtRoomExplane = (TextView)findViewById(R.id.ResDetail_txtRoomExplane);


        btngoback = (Button)findViewById(R.id.btngoback);
        btngoback.setOnClickListener(this);

        btnReservation = (Button)findViewById(R.id.btnReservation);
        btnReservation.setOnClickListener(this);

        btnResCancel = (Button)findViewById(R.id.btnResCancel);
        btnResCancel.setOnClickListener(this);

        // RoomList 클래스에서 송신한 데이터를 수신
        Intent getroomlist = getIntent();
        position = getroomlist.getExtras().getInt("position");
        getresRoomId = getroomlist.getExtras().getString("resRoomId");

        init();
    }

    public void getDBData(String strWhere, int Column) {
        myDB = this.openOrCreateDatabase("member", MODE_PRIVATE, null);
        allRCD = myDB.query("Room", null, strWhere, null, null,
                null, null, null);

        if (allRCD != null) {
            if(allRCD.moveToPosition(position)){
                data.setRoom(allRCD.getString(0));
                data.setRoomPrice(allRCD.getString(1));
                data.setRoomEa(allRCD.getString(2));
                data.setRoomExplane(allRCD.getString(3));
                data.setRoomImage(allRCD.getInt(4));
            }
        }

        if (myDB != null) myDB.close();
    }

    //예약데이터 insert함수
    public void insert(){
        myDB = this.openOrCreateDatabase("member",MODE_PRIVATE,null);
        allRCD = myDB.query("Reservation", null, null, null, null,
                null, null, null);

        //Date 함수 사용
        Date date = new Date();
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);

        insertVlaue = new ContentValues();
            insertVlaue.put("UserId", SaveSharedPreference.getUserName(RoomDetail.this));
            insertVlaue.put("RoomNum", ResDetail_txtRoomNum.getText().toString());
            insertVlaue.put("RescheckOut", formatDate);
            myDB.insert("Reservation", null, insertVlaue);

        //close DB
        if (myDB != null) myDB.close();
    }

    //예약된 객실을 변별해주는 메소드
    public void ResCheck(){
        myDB = this.openOrCreateDatabase("member",MODE_PRIVATE,null);

        String str = "Update Room Set RoomResCheck ='예약마감' " +
                "Where RoomNum = '" + ResDetail_txtRoomNum.getText().toString() + "';";
        myDB.execSQL(str);

        //close DB
        if (myDB != null) myDB.close();
    }

    //초기 실행
    private void init() {
        String CurrentUser = SaveSharedPreference.getUserName(RoomDetail.this);
        if(getresRoomId == null) {
            getDBData(null, 0);
        }else{
            String str = "RoomNum ='" + getresRoomId + "'";
            getDBData(str, 0);
            getresRoomId = "";
        }

        ResDetailImage.setImageResource(data.getRoomImage());
        ResDetail_txtRoomNum.setText(data.getRoom());
        ResDetail_txtRoomPrice.setText(data.getRoomPrice());
        ResDetail_txtRoomEa.setText(data.getRoomEa());
        ResDetail_txtRoomExplane.setText(data.getRoomExplane());

        // 예약자 확인을 위한 db 가져오기
        String str = "RoomNum = '" + ResDetail_txtRoomNum.getText() + "'";
        myDB = this.openOrCreateDatabase("member", MODE_PRIVATE, null);
        allRCD = myDB.query("Reservation", null, str, null, null,
                null, null, null);
        if(allRCD != null){
            if (allRCD.moveToFirst()) {
                do {
                    str = allRCD.getString(1);
                } while (allRCD.moveToNext());
            }
        }

        //예약된 방과 예약되지 않은 방 구분
        //예약자만 예약취소 버튼 생성 (나머지는 예약하기 및 아무것도 뒤로가기 밖에 없음)
        System.out.println(str);
        if((CurrentUser).equals(str)) {
            btnReservation.setVisibility(View.INVISIBLE);
            btnResCancel.setVisibility(View.VISIBLE);
        }else if (str.equals("RoomNum = '" + ResDetail_txtRoomNum.getText() + "'")){
            btnReservation.setVisibility(View.VISIBLE);
            btnResCancel.setVisibility(View.INVISIBLE);
        }else{
            btnReservation.setVisibility(View.INVISIBLE);
            btnResCancel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        // 예약하기
        if(v == btnReservation){
            try {
                insert();
                ResCheck();
                Toast.makeText(getApplicationContext(), "예약되었습니다.", Toast.LENGTH_LONG).show();

                Intent roomList = new Intent(RoomDetail.this, RoomList.class);
                startActivity(roomList);
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "에러가 있습니다 : e", Toast.LENGTH_LONG).show();
                throw e;
            }
        }

        // 예약취소
        if(v == btnResCancel){
            myDB = this.openOrCreateDatabase("member",MODE_PRIVATE,null);
            String str = "Delete from reservation where RoomNum ='" + ResDetail_txtRoomNum.getText() + "'";
            myDB.execSQL(str);

            str = "Update Room Set RoomResCheck ='예약가능' " +
                    "Where RoomNum = '" + ResDetail_txtRoomNum.getText().toString() + "';";
            myDB.execSQL(str);

            myDB.close();

            Toast.makeText(getApplicationContext(), "예약이 취소되었습니다.", Toast.LENGTH_LONG).show();
            Intent roomList = new Intent(RoomDetail.this, RoomList.class);
            startActivity(roomList);

        }

        //뒤로가기
        if(v == btngoback){
            Intent roomList = new Intent(RoomDetail.this, RoomList.class);
            startActivity(roomList);
        }
    }
}
