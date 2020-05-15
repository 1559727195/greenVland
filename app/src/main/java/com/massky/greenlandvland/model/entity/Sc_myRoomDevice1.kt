package com.massky.greenlandvland.model.entity


/**
 * Created by masskywcy on 2017-11-06.
 */

class Sc_myRoomDevice1(var myRoomDeviceParams: MyRoomDeviceParams?, var myRoomDeviceResult: MyRoomDeviceResult?) {

    override fun toString(): String {
        return "Sc_myRoomDevice{" +
                "myRoomDeviceParams=" + myRoomDeviceParams +
                ", myRoomDeviceResult=" + myRoomDeviceResult +
                '}'.toString()
    }

    class MyRoomDeviceParams(var token: String?, var projectCode: String?, var deviceId: String?) {

        override fun toString(): String {
            return "MyRoomDeviceParams{" +
                    "token='" + token + '\''.toString() +
                    ", projectCode='" + projectCode + '\''.toString() +
                    ", deviceId='" + deviceId + '\''.toString() +
                    '}'.toString()
        }
    }

    class MyRoomDeviceResult {


        /**
         * result : 100
         * deviceInfo : {"status":0,"speed":"","name":"4039KEY8","number":24386,"dimmer":"","name1":"","type":1,"name2":"","temperature":"","mode":""}
         */

        var result: String? = null
        var deviceInfo: DeviceInfoBean? = null

        class DeviceInfoBean {
            /**
             * status : 0
             * speed :
             * name : 4039KEY8
             * number : 24386
             * dimmer :
             * name1 :
             * type : 1
             * name2 :
             * temperature :
             * mode :
             */

            var status: Int = 0
            var speed: String? = null
            var name: String? = null
            var number: Int = 0
            var dimmer: String? = null
            var name1: String? = null
            var type: Int = 0
            var name2: String? = null
            var temperature: String? = null
            var mode: String? = null
        }
    }
}
