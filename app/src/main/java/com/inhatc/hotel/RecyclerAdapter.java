package com.inhatc.hotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.hotel.model.roomDTO;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list
    private ArrayList<roomDTO> listData = new ArrayList<>();

    roomDTO data = new roomDTO();

    // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킨다.
    // return 인자는 ViewHolder
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_roomitem, parent, false);
        return new ItemViewHolder(view);
    }

    // Item을 하나, 하나 보여주는(bind 되는) 함수.
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    // RecyclerView의 총 개수
    @Override
    public int getItemCount() {
        return listData.size();
    }

    // 외부에서 item을 추가시킬 함수.
    void addItem(roomDTO data) {
        listData.add(data);
    }

    // 데이터 삭제 함수
    void clearData(){
        listData.clear();
    }

    //itemclick
    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener {
        void onItemClick(  View v, int position) ;
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }



    // RecyclerView의 핵심인 ViewHolder
    // 여기서 subView를 setting 한다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView txtroom;
        private TextView txtroomPrice;
        private ImageView roomImage;
        private TextView txtResCheck;
        private TextView txtResDate;

        ItemViewHolder(View itemView) {
            super(itemView);

            txtroom = itemView.findViewById(R.id.txtroom);
            txtroomPrice = itemView.findViewById(R.id.txtroomPrice);
            roomImage = itemView.findViewById(R.id.roomImage);

            txtResCheck = itemView.findViewById(R.id.txtResCheck);
            txtResDate = itemView.findViewById(R.id.txtResDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : process click event.
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick( v,position);

                        }
                    }
                }
            });
        }

        void onBind(roomDTO data) {
            txtroom.setText(data.getRoom());
            txtroomPrice.setText(data.getRoomPrice());
            roomImage.setImageResource(data.getRoomImage());

            txtResCheck.setText(data.getRoomResCheck());
            txtResDate.setText(data.getResDate());
        }
    }
}