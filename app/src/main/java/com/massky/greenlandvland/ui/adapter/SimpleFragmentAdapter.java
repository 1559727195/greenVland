package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.Log;
import com.massky.greenlandvland.model.entity.Sc_myRoom;
import com.massky.greenlandvland.ui.Fragment_content;
import com.massky.greenlandvland.ui.Fragment_content1;
import java.util.List;

/**
 * Created by masskywcy on 2017-11-15.
 */

public class SimpleFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Sc_myRoom.MyRoomResult.RoomListBean> sceneList;

    public SimpleFragmentAdapter(Context context, List<Sc_myRoom.MyRoomResult.RoomListBean> sceneList, FragmentManager fm){
        super(fm);
        this.context=context;
        this.sceneList=sceneList;
    }

    @Override
    public Fragment getItem(int position) {
//        List<Fragment> fragmentList=new ArrayList<>();
//        for (int i=0;i<sceneList.size();i++){
//            Fragment_content1 fragment_content=new Fragment_content1();
//            fragmentList.add(fragment_content);
//        }
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("scenes",sceneList.get(position));
//        fragmentList.get(position).setArguments(bundle);
//        return fragmentList.get(position);
        Log.e("TAG","position="+position);
        //return Fragment_content.newInstance(sceneList.get(position));
        return Fragment_content1.Companion.newInstance(sceneList.get(position));
    }

    @Override
    public int getCount() {
        return sceneList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sceneList.get(position).getRoomName();
    }


//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            //fragment可见时加载数据
//            if (cardList != null && cardList.size() != 0) {
//                handler.obtainMessage(0).sendToTarget();
//            } else {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        try {
//                            Thread.sleep(2);
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        handler.obtainMessage(0).sendToTarget();
//                    }
//                }).start();
//            }
//        } else {
//            //fragment不可见时不执行操作
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }
}
