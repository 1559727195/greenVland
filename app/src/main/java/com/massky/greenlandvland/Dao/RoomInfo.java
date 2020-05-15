package com.massky.greenlandvland.Dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 2017/8/9.
 */

public class RoomInfo implements Serializable {
    private static final long serialVersionUID = 3L;
    public List<Room> roomList = new ArrayList<>();//门禁权限
}
