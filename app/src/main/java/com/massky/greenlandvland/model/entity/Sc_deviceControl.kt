package com.massky.greenlandvland.model.entity

/**
 * Created by masskywcy on 2017-11-21.
 */

class Sc_deviceControl(var deviceControlParams: DeviceControlParams?, var deviceControlResult: DeviceControlResult?) {

    override fun toString(): String {
        return "Sc_deviceControl{" +
                "deviceControlParams=" + deviceControlParams +
                ", deviceControlResult=" + deviceControlResult +
                '}'.toString()
    }

    class DeviceControlParams(var token: String?, var projectCode: String?, var boxNumber: String?, var deviceInfo: DeviceInfo?) {

        override fun toString(): String {
            return "DeviceControlParams{" +
                    "token='" + token + '\''.toString() +
                    ", projectCode='" + projectCode + '\''.toString() +
                    ", boxNumber='" + boxNumber + '\''.toString() +
                    ", deviceInfo=" + deviceInfo +
                    '}'.toString()
        }

        class DeviceInfo(var type: Int, var number: Int, var status: Int, var dimmer: String?, var mode: String?, var temperature: String?, var speed: String?) {

            override fun toString(): String {
                return "DeviceInfo{" +
                        "type=" + type +
                        ", number='" + number + '\''.toString() +
                        ", status=" + status +
                        ", dimmer=" + dimmer +
                        ", mode=" + mode +
                        ", temperature=" + temperature +
                        ", speed=" + speed +
                        '}'.toString()
            }
        }
    }

    class DeviceControlResult(var result: Int) {

        override fun toString(): String {
            return "DeviceControlResult{" +
                    "result=" + result +
                    '}'.toString()
        }
    }
}
