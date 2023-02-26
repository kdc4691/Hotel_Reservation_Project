package com.inhatc.hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

public class CreateDB extends AppCompatActivity {

    SQLiteDatabase myDB;
    Cursor allRCD;
    String DBL;
    ContentValues insertVlaue;

    ArrayList<String> listRoom = new ArrayList<String>();
    ArrayList<String> listRoomPrice = new ArrayList<String>();
    ArrayList<String> listRoomEa = new ArrayList<String>();
    ArrayList<String> listRoomExplane = new ArrayList<String>();
    ArrayList<Integer> listRoomImage = new ArrayList<Integer>();
    ArrayList<String> listRoomResCheck = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_db);

        //DB
        myDB = this.openOrCreateDatabase("member",MODE_PRIVATE,null);

        //create table 'member'
        myDB.execSQL("Drop table if exists member");
        myDB.execSQL("Create table if not exists member(" +
                "UserId text primary key," +
                "UserPw text not null," +
                "UserName text not null," +
                "UserEmail text not null);");

        //create table 'Room'
        myDB.execSQL("Drop table if exists Room");
        myDB.execSQL("Create table if not exists Room(" +
                "RoomNum text primary key," +
                "RoomPrice text not null," +
                "RoomEa text ," +
                "RoomExplane text ," +
                "RoomImage int not null ," +
                "RoomResCheck text not null);");

        //create table 'Reservation'
        myDB.execSQL("Drop table if exists Reservation");
        myDB.execSQL("Create table if not exists Reservation(" +
                "ResNum integer primary key ," +
                "UserId text not null," +
                "RoomNum text not null," +
                "ResCheckOut text not null);");


        // DB에 insert할 함수를 Arraylist에 저장
        listRoom.add("101호");
        listRoom.add("102호");
        listRoom.add("103호");
        listRoom.add("201호");
        listRoom.add("202호");
        listRoom.add("203호");

        listRoomPrice.add("25000");
        listRoomPrice.add("30000");
        listRoomPrice.add("40000");
        listRoomPrice.add("25000");
        listRoomPrice.add("30000");
        listRoomPrice.add("40000");

        listRoomImage.add(R.drawable.room1);
        listRoomImage.add(R.drawable.room2);
        listRoomImage.add(R.drawable.room3);
        listRoomImage.add(R.drawable.room4);
        listRoomImage.add(R.drawable.room5);
        listRoomImage.add(R.drawable.room6);

        listRoomEa.add("2개");
        listRoomEa.add("3개");
        listRoomEa.add("4개");
        listRoomEa.add("2개");
        listRoomEa.add("3개");
        listRoomEa.add("4개");

        listRoomExplane.add("TV 영화 다시보기는 기본, 컴퓨터, " +
                "최신형 안마의자까지 마음껏 즐기세요");
        listRoomExplane.add("TV 영화 다시보기는 기본, 컴퓨터, " +
                "최신형 안마의자까지 마음껏 즐기세요");
        listRoomExplane.add("TV 영화 다시보기는 기본, 컴퓨터, " +
                "최신형 안마의자까지 마음껏 즐기세요");
        listRoomExplane.add("TV 영화 다시보기는 기본, 컴퓨터, " +
                "최신형 안마의자까지 마음껏 즐기세요");
        listRoomExplane.add("TV 영화 다시보기는 기본, 컴퓨터, " +
                "최신형 안마의자까지 마음껏 즐기세요");
        listRoomExplane.add("TV 영화 다시보기는 기본, 컴퓨터, " +
                "최신형 안마의자까지 마음껏 즐기세요");

        listRoomResCheck.add("예약가능");
        listRoomResCheck.add("예약가능");
        listRoomResCheck.add("예약가능");
        listRoomResCheck.add("예약가능");
        listRoomResCheck.add("예약가능");
        listRoomResCheck.add("예약가능");


        // DB에 기본 정보를 insert 하는 함수 실행
        insert(listRoom, listRoomPrice, listRoomImage,listRoomEa,listRoomExplane,listRoomResCheck);

        //Arraylist 비우기
        listRoom.clear();
        listRoomPrice.clear();
        listRoomImage.clear();
        listRoomEa.clear();
        listRoomExplane.clear();

        //getDBData(null)

        //close DB
        if(myDB != null) myDB.close();

        //db생성및 초기값 입력 후 Login activity 실행
        Intent roomList = new Intent(CreateDB.this, Login.class);
        startActivity(roomList);
    }

    //insert data to table 'Room'
    private void insert(ArrayList<String> insertRoom,
                        ArrayList<String> insertRoomPrice,
                        ArrayList<Integer> insertRoomImage,
                        ArrayList<String> insertRoomEa,
                        ArrayList<String> insertRoomExplane,
                        ArrayList<String> insertResCheck) {
        insertVlaue = new ContentValues();
        for(int i=0; i<insertRoom.size(); i++) {
            insertVlaue.put("RoomNum", insertRoom.get(i));
            insertVlaue.put("RoomPrice", insertRoomPrice.get(i));
            insertVlaue.put("RoomEa", insertRoomEa.get(i));
            insertVlaue.put("RoomExplane", insertRoomExplane.get(i));
            insertVlaue.put("RoomImage", insertRoomImage.get(i));
            insertVlaue.put("RoomResCheck", insertResCheck.get(i));
            myDB.insert("Room", null, insertVlaue);
        }

        //close DB
        if (myDB != null) myDB.close();
    }


}
