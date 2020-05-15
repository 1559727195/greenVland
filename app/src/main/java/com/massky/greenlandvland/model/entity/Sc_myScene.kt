package com.massky.greenlandvland.model.entity

/**
 * Created by Administrator on 2017/11/6 0006.
 */

class  Sc_myScene(var mySceneParams: MySceneParams?,var  mySceneResult: MySceneResult?) {

    override fun toString(): String {
        return "Sc_myScene{" +
                "mySceneParams=" + mySceneParams +
                ", mySceneResult=" + mySceneResult +
                '}'.toString()
    }

    //class MySceneParams(var token: String?, var projectCode: String?, var boxNumber: String?) {
    class MySceneParams(var token:String?,var projectCode:String?,var boxNumber:String?){

        override fun toString(): String {
            return "MySceneParams{" +
                    "token='" + token + '\''.toString() +
                    ", projectCode='" + projectCode + '\''.toString() +
                    ", boxNumber='" + boxNumber + '\''.toString() +
                    '}'.toString()
        }
    }

  //  class MySceneResult(var result: Int, var sceneList: List<Scene>?) {
    class MySceneResult(var  result:Int? ,var  sceneList: List<Scene> ?) {

        override fun toString(): String {
            return "MySceneResult{" +
                    "result=" + result +
                    ", sceneList=" + sceneList +
                    '}'.toString()
        }

        class Scene(var id: Int, var sceneName: String?) {

            override fun toString(): String {
                return "Scene{" +
                        "id=" + id +
                        ", sceneName='" + sceneName + '\''.toString() +
                        '}'.toString()
            }
        }
    }
}
