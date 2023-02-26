package com.inhatc.hotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inhatc.hotel.model.roomDTO;

import java.util.ArrayList;

public class userpage extends AppCompatActivity implements View.OnClickListener {

    private RecyclerAdapter adapter;

    SQLiteDatabase myDB;
    Cursor allRCD, resAllRCD, resAllRCD2;
    String DBL;
    Button btn_user_back;

    ArrayList<String> listRoom = new ArrayList<String>();
    ArrayList<String> listRoomPrice = new ArrayList<String>();
    ArrayList<Integer> listRoomImage = new ArrayList<Integer>();
    // 이 class 에서는 예약날짜를 가져오는 용도로 사용
    ArrayList<String> listRoomResCheck = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        RecyclerView recyclerView = findViewById(R.id.user_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_user_back = (Button)findViewById(R.id.btn_user_back);
        btn_user_back.setOnClickListener(this);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        getData();

        final String CurrentUser = SaveSharedPreference.getUserName(userpage.this);
        String str = "UserID ='" + CurrentUser + "'";
        getUserData(str);

        // recycleview 클릭시 상세페이지로 이동
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick( View v, int position) {
                if(false){

                }else {
                    // 클릭했을때 원하는데로 처리해주는 부분
                    Intent roomList = new Intent(userpage.this, RoomDetail.class);
                    //다른 클래스로 데이터 송신
                    myDB = openOrCreateDatabase("member", MODE_PRIVATE, null);
                    DBL = "UserId ='" + CurrentUser + "'";
                    roomList.putExtra("resRoomId", getReservationDBData2(DBL, position));
                    myDB.close();
                    DBL = "";

                    startActivity(roomList);
                }
            }
        });
    }

    // 'member' table 의 DB 정보를 가져오는 메소드
    public void getUserData(String strWhere){
        myDB = this.openOrCreateDatabase("member",MODE_PRIVATE,null);
        allRCD = myDB.query("member", null, strWhere, null, null,
                null, null, null);

        if(allRCD != null){
            if (allRCD.moveToFirst()) {
                do{
                    ((TextView)findViewById(R.id.user_id)).setText(allRCD.getString(0));
                    ((TextView)findViewById(R.id.user_name)).setText(allRCD.getString(2));
                }while(allRCD.moveToNext());
            }
        }

        if (myDB != null) myDB.close();
    }

    // 'Room' table 의 DB 정보를 가져오는 메소드
    public void getRoomDBData(String strWhere){
        allRCD = myDB.query("Room", null, strWhere, null, null,
                null, null, null);

        if(allRCD != null){
            if (allRCD.moveToFirst()) {
                do{
                    listRoom.add(allRCD.getString(0));
                    listRoomPrice.add(allRCD.getString(1));
                    listRoomImage.add(allRCD.getInt(4));
                }while(allRCD.moveToNext());
            }
        }
    }

    // 'Reservation' table 의 DB 정보를 가져오는 메소드
    public void getReservationDBData(String strWhere){
        resAllRCD = myDB.query("Reservation", null, strWhere, null, null,
                null, null, null);

        if(resAllRCD != null){
            if (resAllRCD.moveToFirst()) {
                do{
                    String DBL = "RoomNum ='" + resAllRCD.getString(2) + "'";
                    getRoomDBData(DBL);
                    listRoomResCheck.add(resAllRCD.getString(3));
                }while(resAllRCD.moveToNext());
            }
            if(resAllRCD.moveToLast()){
                ((TextView)findViewById(R.id.user_reservation)).setText(resAllRCD.getString(3));
            }
        }
    }

    // 'Reservation' table 의 DB 정보를 가져오는 메소드 2
    public String getReservationDBData2(String strWhere, int position) {
        resAllRCD2 = myDB.query("Reservation", null, strWhere, null, null,
                null, null, null);
        if(resAllRCD2 != null){
            if(resAllRCD2.moveToPosition(position)){
                DBL = resAllRCD2.getString(2);
            }
        }
        return DBL;
    }

    // 객실 정보 데이터를 가져오는 메소드
    private void getData() {
        myDB = this.openOrCreateDatabase("member",MODE_PRIVATE,null);

        String CurrentUser = SaveSharedPreference.getUserName(userpage.this);
        String str = "UserID ='" + CurrentUser + "'";
        getReservationDBData(str);

        if (myDB != null) myDB.close();

        for (int i = 0; i < listRoom.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.

            roomDTO data = new roomDTO();
            data.setRoom(listRoom.get(i));
            data.setRoomPrice(listRoomPrice.get(i));
            data.setRoomImage(listRoomImage.get(i));
            data.setResDate(listRoomResCheck.get(i));
            // 각 값이 들어간 data를 adapter에 추가한다.
            adapter.addItem(data);
        }

        //Arraylist 비우기
        listRoom.clear();
        listRoomPrice.clear();
        listRoomImage.clear();
        listRoomResCheck.clear();

        // adapter의 값이 변경되었다는 것을 알려준다.( 실제 ui가 보여지게 되는 역할)
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(v == btn_user_back){
            Intent gotoRoomlist = new Intent(userpage.this, RoomList.class);
            startActivity(gotoRoomlist);
        }
    }
}
