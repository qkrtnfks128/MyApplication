package com.example.myapplication.utils;
import com.example.myapplication.MyApplication;

public class WCV {
    private static final WCV instance = new WCV();

    public static WCV getInstance() {
        return instance;
    }

    private com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV wonderfulCV;

    public com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV getWonderfulCV() {
        return wonderfulCV;
    }

    public boolean isInit = false;

    public void setWonderfulCV(com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV wonderfulCV) {
        this.wonderfulCV = wonderfulCV;
    }

    private WCV() {
    }

    public void init(String kindergartenUuid, String password) {
        wonderfulCV = new com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV();

        if (!kindergartenUuid.equals("shine4014@naver.com"))
            kindergartenUuid = kindergartenUuid + "@1thefull.com";

        wonderfulCV.initiateServerConnectionWithoutToken(
                MyApplication.instance,
//                "59.29.245.161", 5000,
                "1thefullcv.com",5000,
                kindergartenUuid,
                password);
    }

}
