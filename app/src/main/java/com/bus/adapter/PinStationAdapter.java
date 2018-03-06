package com.bus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bus.form.R;
import com.bus.model.stationBean;

import java.util.ArrayList;


/**
 * Created by John Yan on 4/22/2017.
 */

public class PinStationAdapter extends RecyclerView.Adapter {

    private MyItemClickListener mItemClickListener;
    private Context context;
    private ArrayList<stationBean> data;

    public PinStationAdapter(Context context, ArrayList<stationBean> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.station_view_list,parent,false);
        //将全局的监听传递给holder

        MyHolder holder = new PinStationAdapter.MyHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //给空间内的元素控件赋值
        if(holder instanceof PinStationAdapter.MyHolder) {
            ((MyHolder)holder).icon_des.setImageResource(data.get(position).getIcon_loc());
            ((MyHolder)holder).station_des.setText(data.get(position).getKeyname());
            ((MyHolder)holder).des_address.setText(data.get(position).getAddress());


        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView station_des ;
        private TextView des_address;
        private ImageView icon_des;

        private MyItemClickListener mListener;

        public MyHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            station_des= (TextView) itemView.findViewById(R.id.line_loc_place);
            des_address= (TextView) itemView.findViewById(R.id.line_loc_distribute);
            icon_des= (ImageView) itemView.findViewById(R.id.line_logo);
            this.mListener=mItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(PinStationAdapter.MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
