package com.delelong.diandian.fragment;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.delelong.diandian.BaseActivity.MyHeadTask;
import com.delelong.diandian.MainActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.adapter.MyMenuLvAdapter;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.MenuListItem;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.http.HttpUtils;
import com.delelong.diandian.menuActivity.FeedBackActivity;
import com.delelong.diandian.menuActivity.MallActivity;
import com.delelong.diandian.menuActivity.MenuInfoActivity;
import com.delelong.diandian.menuActivity.SettingActivity;
import com.delelong.diandian.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MenuFrag extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    private static final String TAG = "BAIDUMAPFORTEST";

    View view;
    LayoutTransition transition;
    MenuFrag menuFrag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_menu, container, false);

        initView();
        initClient();
        setListener();
        return view;
    }

    Client client;
    HttpUtils httpUtils;
    private void initClient() {
        if (activity == null){
            activity = (MainActivity) getActivity();
        }
        httpUtils = new HttpUtils(activity);
//        client = activity.getClientByGET(URL_MEMBER);
        client = httpUtils.getClientByGET(Str.URL_MEMBER);
        String phone = client.getPhone();
        String nick_name = client.getNick_name();
        String head_portrait = client.getHead_portrait();

        MyHeadTask myHeadTask = new MyHeadTask(img_head);
        myHeadTask.execute(Str.URL_HEAD_PORTRAIT, head_portrait);
        if (!nick_name.isEmpty()) {
            this.nick_name.setText(nick_name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head:
                //编辑个人信息页面
                startActivityWithBundle(MenuInfoActivity.class);
                break;
            case R.id.ly_back:
                hideMenu();//隐藏本菜单界面
                break;
            case R.id.btn_loginOut:
                List<String> loginOutResult = httpUtils.getLoginOutResultByGET(Str.URL_LOGINOUT);
                if (loginOutResult.get(0).equalsIgnoreCase("OK")){
                    hideMenu();//隐藏本菜单界面
                    activity.setLogining(false);
                }
                break;
        }
    }

    /**
     *
     * @param tClass 目标Class
     * @param <T>
     */
    private <T> void startActivityWithBundle(Class<T> tClass) {
        Bundle bundle = new Bundle();
        if (activity.myAMapLocation != null) {
            bundle.putSerializable("myAMapLocation", activity.myAMapLocation);//传递我的位置
        }
        if (client != null) {
            bundle.putSerializable("client", client);
        }
        activity.intentActivityWithBundle(activity, tClass, bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_menu:
                //ListView
                switch (position) {
                    //item position
                    case 0://我的行程

                        break;
                    case 1://我的钱包

                        break;
                    case 2://问题反馈
                        startActivityWithBundle(FeedBackActivity.class);
                        break;
                    case 3://推荐有奖

                        break;
                    case 4://司机招募

                        break;
                    case 5://合作商城
                        activity.startActivity(new Intent(activity, MallActivity.class));
                        break;
                    case 6://兑 换 码

                        break;
                    case 7://设    置
                        startActivityWithBundle(SettingActivity.class);
                        break;
                }
                break;
        }
    }

    private boolean showGrid;


    LinearLayout menu_frag;
    LinearLayout ly_display;
    RoundImageView img_head;
    TextView nick_name;
    ListView lv_menu;
    Button btn_loginOut;

    LinearLayout ly_back;//隐藏菜单

    private void initView() {
        mGestureDetector = new GestureDetector(new MySimpleGestureListener());
        menu_frag = (LinearLayout) view.findViewById(R.id.menu_frag);


        ly_display = (LinearLayout) view.findViewById(R.id.ly_display);
        //更多更能滑动显示效果（取消显示按钮，不用此功能了）
//        transition = new LayoutTransition();
//        ly_display.setLayoutTransition(transition);

        img_head = (RoundImageView) view.findViewById(R.id.img_head);
        nick_name = (TextView) view.findViewById(R.id.nick_name);

        lv_menu = (ListView) view.findViewById(R.id.lv_menu);

        btn_loginOut = (Button) view.findViewById(R.id.btn_loginOut);

        ly_back = (LinearLayout) view.findViewById(R.id.ly_back);
        //加载ListView
        initListView();
    }
    private void setListener() {
        img_head.setOnClickListener(this);
        ly_back.setOnClickListener(this);
        btn_loginOut.setOnClickListener(this);
        lv_menu.setOnItemClickListener(this);
        lv_menu.setOnTouchListener(this);
        menu_frag.setOnTouchListener(this);
    }

    private List<MenuListItem> itemList;
    private MyMenuLvAdapter myLvAdapter;

    private void initListView() {
        MenuListItem item0 = new MenuListItem(R.drawable.icon_menu_xingcheng, "我的行程");
        MenuListItem item1 = new MenuListItem(R.drawable.icon_menu_qianbao, "我的钱包");
        MenuListItem item2 = new MenuListItem(R.drawable.icon_menu_fankui, "问题反馈");
        MenuListItem item3 = new MenuListItem(R.drawable.icon_menu_tuijian, "推荐有奖");
        MenuListItem item4 = new MenuListItem(R.drawable.icon_menu_zhaomu, "司机招募");
        MenuListItem item5 = new MenuListItem(R.drawable.icon_menu_hezuo, "合作商城");
        MenuListItem item6 = new MenuListItem(R.drawable.icon_menu_duihuan, "兑  换  码");
        MenuListItem item7 = new MenuListItem(R.drawable.icon_menu_shezhi, "设        置");
        itemList = new ArrayList<>();
        itemList.add(item0);
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);
        itemList.add(item6);
        itemList.add(item7);

        myLvAdapter = new MyMenuLvAdapter(activity, itemList);
        lv_menu.setAdapter(myLvAdapter);
    }

    MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!activity.getSupportActionBar().isShowing())
            activity.getSupportActionBar().show();
    }

    /**
     * 隐藏本菜单界面
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void hideMenu() {
        if (menuFrag == null) {
            menuFrag = (MenuFrag) activity.getFragmentManager().findFragmentByTag("menuFrag");
        }
        activity.getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).hide(menuFrag).commit();
        activity.getSupportActionBar().show();
        activity.enableClick();//地图层按钮可用
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector mGestureDetector;

    private class MySimpleGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        /*****
         * OnGestureListener的函数
         *****/
        final int FLING_MIN_DISTANCE = 200, FLING_MIN_VELOCITY = 200;
        // 触发条件 ：
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒

        // 参数解释：
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度，像素/秒
        // velocityY：Y轴上的移动速度，像素/秒
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling left
                Log.i(TAG, "Fling left");
                hideMenu();//隐藏菜单栏
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling right
                Log.i(TAG, "Fling right");
            }
            return true;
        }

    }
}
