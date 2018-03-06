package com.bus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bus.form.R;
import com.bus.model.locBean;
import com.bus.service.EditLocationName;

import java.util.ArrayList;
import java.util.List;

public class LocCommonAdapter extends RecyclerView.Adapter {
    private MyItemClickListener mItemClickListener;
    private Context context;
    private View rootview;
    private Activity activity;
    private List<locBean> data;
    private PopupWindow popwindow_edit;
    private Button edit_list;
    private Button delete_list;
    private Button cancel_list;

    public LocCommonAdapter(Context context, ArrayList<locBean> data, View rootview, Activity activity){
        this.data=data;
        this.context=context;
        this.rootview=rootview;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item_addlist,parent,false);
        //将全局的监听传递给holder
        MyHolder holder = new MyHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //给空间内的元素控件赋值
        if(holder instanceof MyHolder) {
            ((MyHolder)holder).loc_add_star.setImageResource(data.get(position).getIcon_star());
            ((MyHolder)holder).loc_add_tag.setText(data.get(position).getTag());
            ((MyHolder)holder).loc_add_more.setImageResource(data.get(position).getIcon_more());
        }
    }
    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView loc_add_star;
        private TextView loc_add_tag ;
        private  ImageView loc_add_more;
        private MyItemClickListener mListener;
        MyHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            loc_add_star= (ImageView) itemView.findViewById(R.id.loc_addlist_img);
            loc_add_tag= (TextView) itemView.findViewById(R.id.loc_addlist_tag);
            loc_add_more= (ImageView) itemView.findViewById(R.id.loc_addlist_more);
            loc_add_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showpopWindowMore();
                }
            });
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        private void showpopWindowMore() {
            // // 获取PopupWindow布局View
            final View popview = LayoutInflater.from(context).inflate(R.layout.pop_chooseedit, null);
            popwindow_edit = new PopupWindow(popview,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            edit_list=(Button)popview.findViewById(R.id.popup_btn_edit) ;
            delete_list=(Button)popview.findViewById(R.id.popup_btn_delete);
            cancel_list=(Button)popview.findViewById(R.id.popup_btn_cancel);
            edit_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, EditLocationName.class));
                }
            });
            delete_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    Log.e("test",String.valueOf(getAdapterPosition()));
                    popwindow_edit.dismiss();
                }
            });
            cancel_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popwindow_edit.dismiss();
                }
            });
            popwindow_edit.setBackgroundDrawable(new ColorDrawable(0x00000000));
            popwindow_edit.setOutsideTouchable(false);
            popwindow_edit.setFocusable(true);

            // 在底部显示
            popwindow_edit.showAtLocation(rootview.findViewById(R.id.body_locfrag),
                    Gravity.BOTTOM, 0, 0);

            // 窗体半透明
            bgAlpha(0.5f);

            popwindow_edit.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //popupwindow消失的时候恢复成原来的透明度

                   bgAlpha(1f);
                }
            });
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

    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
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

    public Object getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }



}
