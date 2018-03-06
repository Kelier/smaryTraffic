package com.bus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bus.form.R;
import com.bus.model.stationGroomBean;

import java.util.ArrayList;
import java.util.List;

public class HomeStationAdapter extends RecyclerView.Adapter {
    private MyItemClickListener mItemClickListener;
    private Context context;
    private List<stationGroomBean> data;
    public HomeStationAdapter(Context context, ArrayList<stationGroomBean> data){
        this.data=data;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item_homelist,parent,false);
        //将全局的监听传递给holder
        MyHolder holder = new MyHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //给空间内的元素控件赋值
        if(holder instanceof MyHolder) {
            ((MyHolder)holder).station_degree.setImageResource(data.get(position).getStation_degree());
            ((MyHolder)holder).station_now.setText(data.get(position).getStation_now());
            ((MyHolder)holder).station_distance.setText(data.get(position).getStation_distance());
            ((MyHolder)holder).station_route.setText(data.get(position).getStation_route());
            ((MyHolder)holder).station_next.setText(data.get(position).getStation_next());
            ((MyHolder)holder).station_time.setText(data.get(position).getStation_time());

        }
    }
    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView station_now;
        private TextView station_distance ;
        private TextView station_route;
        private TextView station_next ;
        private TextView station_time;
        private  ImageView station_degree;
        private MyItemClickListener mListener;
        MyHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            station_now= (TextView) itemView.findViewById(R.id.station_now);
            station_distance= (TextView) itemView.findViewById(R.id.station_distance);
            station_route= (TextView) itemView.findViewById(R.id.home_list_route);
            station_next= (TextView) itemView.findViewById(R.id.station_next);
            station_time= (TextView) itemView.findViewById(R.id.time_line);
            station_degree= (ImageView) itemView.findViewById(R.id.crowding_degree);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
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
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }





}
