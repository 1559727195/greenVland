package com.massky.greenlandvland.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;


import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.XListView;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.event.MyDialogEvent;
import com.massky.greenlandvland.event.MyEvent;
import com.massky.greenlandvland.ui.base.BaseFragment1;
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer;
import com.massky.greenlandvland.ui.sraum.User;
import com.massky.greenlandvland.ui.sraum.Util.DialogUtil;
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp;
import com.massky.greenlandvland.ui.sraum.Util.Mycallback;
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil;
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper;
import com.massky.greenlandvland.ui.sraum.adapter.AutoSceneAdapter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zhu on 2017/11/30.
 */

public class AutoSceneFragment extends BaseFragment1 implements XListView.IXListViewListener {
    @BindView(R.id.xListView_scan)
    XListView xListView_scan;
    private Handler mHandler = new Handler();
    private String[] autoElements = {"人体传感联动", "厨房联动", "PM2.5联动", "地下室", "防盗门打开"
            , "漏水"};
    private List<Map> list_hand_scene = new ArrayList<>();
    private AutoSceneAdapter autoSceneAdapter;
    private DialogUtil dialogUtil;
    private List<User.deviceLinkList> list = new ArrayList<>();
    private List<Map> list_atuo_scene = new ArrayList<>();
    private int first_add;
    private String loginPhone;
    private boolean vibflag;
    private boolean musicflag;

    @Override
    protected void onData() {

    }

    @Override
    protected void onEvent() {

    }

    @Override
    public void onEvent(MyDialogEvent eventData) {

    }

    @Override
    protected int viewId() {
        return R.layout.auto_scene_lay;
    }

    @Override
    protected void onView(View view) {
        dialogUtil = new DialogUtil(getActivity());
        EventBus.getDefault().register(this);
        autoSceneAdapter = new AutoSceneAdapter(getActivity(), list_atuo_scene, dialogUtil, vibflag, musicflag, new AutoSceneAdapter.RefreshListener() {
            @Override
            public void refresh() {
//                get_myDeviceLink();
//                common_second();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sraum_getAutoScenes();
                    }
                }).start();
                common_second();
                MyEvent event = new MyEvent();
                event.setContent("刷新");
                EventBus.getDefault().post(event);
            }
        });
        xListView_scan.setAdapter(autoSceneAdapter);
        xListView_scan.setPullLoadEnable(false);
        xListView_scan.setXListViewListener(this);
        xListView_scan.setFootViewHide();
        first_add = 1;
    }

    @Override
    public void onClick(View v) {

    }

    public static AutoSceneFragment newInstance() {
        AutoSceneFragment newFragment = new AutoSceneFragment();
        Bundle bundle = new Bundle();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    private void onLoad() {
        xListView_scan.stopRefresh();
        xListView_scan.stopLoadMore();
        xListView_scan.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        onLoad();
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        init_music_flag();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sraum_getAutoScenes();
            }
        }).start();

        common_second();
        if (first_add == 1) {
            first_add = 2;
        } else {
            MyEvent event = new MyEvent();
            event.setContent("刷新");
//...设置event
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 获取自动场景
     */
    private void sraum_getAutoScenes() {
        Map map = new HashMap();
        String areaNumber = (String) SharedPreferencesUtil.getData(getActivity(), "areaNumber", "");
        String order = (String) SharedPreferencesUtil.getData(getActivity(), "order", "1");
        map.put("areaNumber", areaNumber);
        map.put("token", SharedPreferencesUtils.getToken(getActivity()));
        map.put("order", order);
//        if (dialogUtil != null) {
//            dialogUtil.loadDialog();
//        }

//        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(SelectSensorActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_getAutoScenes

                , map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {//刷新togglen数据
                        sraum_getAutoScenes();
                    }
                }, getActivity(), dialogUtil) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }

                    @Override
                    public void pullDataError() {
                        super.pullDataError();
                    }

                    @Override
                    public void emptyResult() {
                        super.emptyResult();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                        //重新去获取togglen,这里是因为没有拉到数据所以需要重新获取togglen
                    }

                    @Override
                    public void wrongBoxnumber() {
                        super.wrongBoxnumber();
                    }

                    @Override
                    public void onSuccess(final User user) {

                        list_atuo_scene = new ArrayList<>();
                        for (int i = 0; i < user.deviceLinkList.size(); i++) {
                            Map map = new HashMap();
                            map.put("id", user.deviceLinkList.get(i).id);
                            map.put("name", user.deviceLinkList.get(i).name);
                            map.put("isUse", user.deviceLinkList.get(i).isUse);
                            map.put("type", user.deviceLinkList.get(i).type);
                            list_atuo_scene.add(map);
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    private void init_music_flag() {
        loginPhone = (String) SharedPreferencesUtil.getData(getActivity(), "loginPhone", "");
        SharedPreferences preferences = getActivity().getSharedPreferences("sraum" + loginPhone,
                Context.MODE_PRIVATE);
        vibflag = preferences.getBoolean("vibflag", false);
//        musicflag = preferences.getBoolean("musicflag", false);
        musicflag = (boolean) SharedPreferencesUtil.getData(getActivity(), "musicflag", false);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (list_atuo_scene.size() == 0) {
                        xListView_scan.setVisibility(View.GONE);
                    } else {
                        xListView_scan.setVisibility(View.VISIBLE);
                    }

                    autoSceneAdapter.addAll(list_atuo_scene);//不要new adapter
                    autoSceneAdapter.addFlag(vibflag, musicflag);
                    autoSceneAdapter.notifyDataSetChanged();
                    break;
                case 1:

                    break;
            }
        }
    };

    /**
     * 清除联动信息
     */
    private void common_second() {
        SharedPreferencesUtil.saveData(getActivity(), "linkId", "");
        SharedPreferencesUtil.saveInfo_List(getActivity(), "list_result", new ArrayList<Map>());
        SharedPreferencesUtil.saveInfo_List(getActivity(), "list_condition", new ArrayList<Map>());
        SharedPreferencesUtil.saveData(getActivity(), "editlink", false);
        SharedPreferencesUtil.saveInfo_List(getActivity(), "link_information_list", new ArrayList<Map>());
        SharedPreferencesUtil.saveData(getActivity(), "add_condition", false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(MyEvent event) {
        String status = event.getContent();
        switch (status) {
            case "scene_second":
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sraum_getAutoScenes();
                    }
                }).start();
                break;
        }
    }
}
