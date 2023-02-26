package com.inhatc.hotel;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.hotel.model.roomDTO;

import java.util.ArrayList;


public class RoomList extends AppCompatActivity implements View.OnClickListener {

    SQLiteDatabase myDB;
    Cursor allRCD;
    String DBL;

    ArrayList<String> listRoom = new ArrayList<String>();
    ArrayList<String> listRoomPrice = new ArrayList<String>();
    ArrayList<String> listRoomEa = new ArrayList<String>();
    ArrayList<String> listRoomExplane = new ArrayList<String>();
    ArrayList<Integer> listRoomImage = new ArrayList<Integer>();
    ArrayList<String> listRoomResCheck = new ArrayList<String>();

    EditText edtRoom_Search;

    private RecyclerAdapter adapter;

    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview_roomlist);

        init();
        getData();
    }


    //초기 실행
    private void init() {
        //Recucleview 사용
        RecyclerView recyclerView = findViewById(R.id.roomlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        // Toolbar 사용
        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar) ;
        setSupportActionBar(tb) ;

        //Toolbar를 Appbar로 설정
        ActionBar ab = getSupportActionBar() ;

        edtRoom_Search = (EditText) findViewById(R.id.edtRoom_Search);

        // recycleview 클릭시 상세페이지로 이동
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick( View v, int position) {
                String CurrentUser = SaveSharedPreference.getUserName(RoomList.this);
                if(CurrentUser.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RoomList.this);
                        builder.setTitle("로그인 후 이용가능합니다.");
                        builder.setMessage("로그인 하시겠 습니까?");
                        builder.setPositiveButton("예",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent gotoLogin = new Intent(RoomList.this, FirstAuthActivity.class);
                                        startActivity(gotoLogin);
                                    }
                                });
                        builder.setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        builder.show();
                }else {
                    // 클릭했을때 원하는데로 처리해주는 부분
                    Intent roomList = new Intent(RoomList.this, RoomDetail.class);
                    //다른 클래스로 데이터 송신
                    roomList.putExtra("position", position);

                    startActivity(roomList);
                }
            }
        });

//        list.setAdapter(adapter);

    }

    // Toolbar에 item 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        MenuItem Log = menu.findItem(R.id.action_clearData);
        MenuItem Acount = menu.findItem(R.id.action_account);

        // 현재 유저 로그인 정보를 판별
        String CurrentUser = SaveSharedPreference.getUserName(RoomList.this);
        if(!CurrentUser.equals("")) {
            Acount.setTitle(CurrentUser);
        }else{
            Acount.setIcon(R.drawable.ic_account);
        }

        return true ;
    }

    // Toolbar에 item click 이벤트 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search :
                myDB = this.openOrCreateDatabase("member", MODE_PRIVATE, null);
                String str = "RoomNum like '%" + edtRoom_Search.getText().toString() + "%'";
                getRoomDBData(str);

                if (myDB != null) myDB.close();

                //찾기 버튼 클릭시 이전 recycleview 를 clear하고 찾는 값만 출력
                    adapter.clearData();

                    for (int i = 0; i < listRoom.size(); i++) {
                        roomDTO data = new roomDTO();
                        // 각 List의 값들을 data 객체에 set 해준다.
                        data.setRoom(listRoom.get(i));
                        data.setRoomPrice(listRoomPrice.get(i));
                        data.setRoomImage(listRoomImage.get(i));
                        data.setRoomResCheck(listRoomResCheck.get(i));

                        // 각 값이 들어간 data를 adapter에 추가한다.
                        adapter.addItem(data);
                    }

                    //Arraylist 비우기
                    listRoom.clear();
                    listRoomPrice.clear();
                    listRoomImage.clear();
                    listRoomResCheck.clear();

                    adapter.notifyDataSetChanged();
                return true ;

            case R.id.action_account :
                // 유저 로그인, 로그아웃 action
                        // 현재 유저 로그인 정보 확인
                        final String CurrentUser = SaveSharedPreference.getUserName(RoomList.this);
                        // 로그인 되어있을경우 유저정보를 초기화 ( 알림창 사용 )
                        AlertDialog.Builder builder = new AlertDialog.Builder(RoomList.this);
                        if(!CurrentUser.equals("")) {
                            builder.setTitle("Alert");
                            builder.setMessage("로그아웃 하시겠습니까?");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SaveSharedPreference.clearUserName(RoomList.this);
                                            Intent gotoLogin = new Intent(RoomList.this, FirstAuthActivity.class);
                                            startActivity(gotoLogin);
                                        }
                                    });
                            builder.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            builder.show();
                        }else{
                            Intent gotoLogin = new Intent(RoomList.this, FirstAuthActivity.class);
                            startActivity(gotoLogin);
                        }
                return true ;
            case R.id.action_mypage :
                Intent my = new Intent(RoomList.this, userpage.class);
                startActivity(my);
                return true ;
            case R.id.action_clearData :
                Intent gotoCreateDB = new Intent(RoomList.this, CreateDB.class);
                startActivity(gotoCreateDB);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
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
                    listRoomEa.add(allRCD.getString(2));
                    listRoomExplane.add(allRCD.getString(3));
                    listRoomImage.add(allRCD.getInt(4));
                    listRoomResCheck.add(allRCD.getString(5));
                }while(allRCD.moveToNext());
            }
        }
    }

    // 객실 정보 데이터를 가져오는 메소드
    private void getData() {
        myDB = this.openOrCreateDatabase("member",MODE_PRIVATE,null);
        getRoomDBData(null);

        if (myDB != null) myDB.close();

        for (int i = 0; i < listRoom.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.

            roomDTO data = new roomDTO();
            data.setRoom(listRoom.get(i));
            data.setRoomPrice(listRoomPrice.get(i));
            data.setRoomImage(listRoomImage.get(i));

            data.setRoomResCheck(listRoomResCheck.get(i));

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
        }
}
