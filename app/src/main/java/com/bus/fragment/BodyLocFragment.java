package com.bus.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bus.adapter.LocCommonAdapter;
import com.bus.form.R;
import com.bus.home.HomeNavLine;
import com.bus.home.HomePinFirst;
import com.bus.home.HomePinSecond;
import com.bus.home.HomePinStar;
import com.bus.listenner.RecyclerItemClickListener;
import com.bus.model.locBean;

import java.util.ArrayList;


public class BodyLocFragment extends Fragment implements View.OnClickListener {
    ArrayList<locBean> data;
    LocCommonAdapter adapter;
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;

    private RelativeLayout exchange;
    private RelativeLayout addLocation;
    private Boolean TAG_firstLayoutTop=true;
   private RelativeLayout firstLayout;
  private RelativeLayout secondLayout;
    private TextView meloc;
    private TextView medes;
    private TextView tip_home;
    private TextView tip_work;
    private TextView firstText;
    private TextView secondText;
    private TextView search_line;
    private int locPosition;


    private ImageView more_edit;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.activity_body_loc_fragment,null);
        setupView(rootview);
        listenChange();
        setListener();

        return rootview;
    }

    private void setListener() {
        firstLayout.setOnClickListener(this);
        firstLayout.setTag(1);
        secondLayout.setOnClickListener(this);
        secondLayout.setTag(2);
        tip_home.setOnClickListener(this);
        tip_home.setTag(3);
        tip_work.setOnClickListener(this);
        tip_work.setTag(3);
        meloc.setOnClickListener(this);
        meloc.setTag(4);
        medes.setOnClickListener(this);
        medes.setTag(4);
        search_line.setOnClickListener(this);
        search_line.setTag(4);
    }

    @Override
    public void onClick(View v) {
        int tag= (int) v.getTag();
        Log.e("tag",String.valueOf(tag));
        switch (tag){
            case 1:
                startActivity(new Intent(getActivity(), HomePinFirst.class).putExtra("startPointcode","1"));

                break;
            case 2:
                startActivity(new Intent(getActivity(), HomePinSecond.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(), HomePinStar.class));
                break;
            case 4:
                startActivity(new Intent(getActivity(), HomeNavLine.class));
                break;

        }
    }

    private void listenChange() {


        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "exchange!", Toast.LENGTH_SHORT).show();

                if(TAG_firstLayoutTop){
                    //move upward and downward 300
                    ObjectAnimator.ofFloat(firstLayout, "TranslationY", 170).setDuration(500).start();
                    ObjectAnimator.ofFloat(secondLayout, "TranslationY", -170).setDuration(500).start();



                    TAG_firstLayoutTop = false;
                }else{
                    //back to normal position
                    ObjectAnimator.ofFloat(firstLayout, "TranslationY", 0).setDuration(500).start();
                    ObjectAnimator.ofFloat(secondLayout, "TranslationY", 0).setDuration(500).start();
                    secondText.setText("你要去哪儿？");
                    TAG_firstLayoutTop = true;
                }
            }
        });
    }




    private void setupView(final View rootview) {
        data=new ArrayList<>();
        exchange= (RelativeLayout) rootview.findViewById(R.id.exchange_layout);
        addLocation=(RelativeLayout)rootview.findViewById(R.id.add_locuse);

        TAG_firstLayoutTop = true;

        firstLayout=(RelativeLayout)rootview.findViewById(R.id.my_location);
        secondLayout=(RelativeLayout)rootview.findViewById(R.id.towhere_location);
        firstText= (TextView) rootview.findViewById(R.id.first_text);
        secondText=(TextView)rootview.findViewById(R.id.second_text);
        tip_home=(TextView)rootview.findViewById(R.id.tip_home);
        tip_work=(TextView)rootview.findViewById(R.id.tip_work);
        meloc= (TextView) rootview.findViewById(R.id.text_meloc);
        medes= (TextView) rootview.findViewById(R.id.text_medes);
        search_line= (TextView) rootview.findViewById(R.id.search_line);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.add(new locBean(R.mipmap.icon_use, "常去地点", R.mipmap.more_edit));

                addLoc(rootview);
            }
        });





    }
    public void addLoc(View rootview){
        adapter=new LocCommonAdapter(rootview.getContext(),data,rootview,getActivity());


        // 布局管理器
//         线性布局
        layoutManager = new LinearLayoutManager(rootview.getContext()) ;
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        // 网格布局
//        manager = new GridLayoutManager(this , 3) ;
        // 瀑布布局
//        manager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL) ;
//        // 控件
        rv = (RecyclerView) rootview.findViewById(R.id.common_routeview);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("touch", "onLongClick position : " + position);
                    }

                    @Override
                    public void onLongClick(View view, int posotion) {

                        startActivity(new Intent(getActivity(),HomeNavLine.class));
                    }
                }));
        // 控件调用两个set方法
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);

    }



}
