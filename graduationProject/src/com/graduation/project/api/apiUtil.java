package com.graduation.project.api;

import com.graduation.project.fernflower.SundayTest;
import com.graduation.project.loophole.loophole;
import com.graduation.project.loophole.loopholeType;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/15/015 14:04
 */
public class apiUtil {


    public static void apiAnalysisByJava(String dir) {
        System.out.println("\t【基于Java文件危险API检测】");
        String[] keyWord = new String[]{"sendDataMessage", "sendMultipartTextMessage", "sendTextMessage", "getOriginatingAddress", "getMessageBody", "getDisplayOriginatingAddress", "getDisplayMessageBody", "getContentResolver().query", ".query(", ".delete(", ".insert(", "onCallStateChanged", "openConnection", ".connect(", "addRequestProperty", "getInputStream", "getOutputStream", "exec", "sendDataMessage", "MediaRecorder", "killBackgroundProcess", "getLastKnownLocation", "getLatitude", "getLongitude"};
        for (String key : keyWord) {
            api api = apiType.getTypeByJava(key);
            SundayTest.findApi(dir, key, api);
        }
    }

    public static void apiAnalysisBySmali(String dir) {

        String[] keyWord = new String[]{"Landroid/telephony/smsManager;->sendDataMessage",
                "Landroid/telephony/SmsManager;->sendMultipartTextMessage",
                "Landroid/telephony/SmsManager;->sendTextMessage",
                "Landroid/telephony/smsManager;->getOriginatingAddress",
                "Landroid/telephony/smsManager;->getMessageBody",
                "Landroid/telephony/smsManager;->getDisplayOriginatingAddress",
                "Landroid/telephony/smsManager;->getDisplayMessageBody",
                "Landroid/provider/ContactsContract",
                "Landroid/contentContentResolver;->query",
                "Landroid/content/ContentResolver;->delete",
                "Landroid/content/ContentResolver;->insert",
                "Landroic/telephony/PhoneStateListener;->oncallStateChanged",
                "Ljava/net/HttpURLConnection;->openConnection",
                "Ljava/net/HttpURLConnection;->connect",
                "Ljava/netHttpURLConnection;->addRequestProperty",
                "Ljava/net/URLConnection;->getInputStream",
                "Ljava/net/URLConnection;->getOutputStream",
                "Ljava/lang/Runtime;->exec",
                "Lorg/xmlpull/vl/XmlPullParser",
                "Landroid/media/MediaRecorder;->MediaRecorder",
                "Landroid/app/ActivityManager;->killBackgroundProcess",
                "Lcom/googleads",
                "Landroid/location/LocationManager;->getLastKnownLocation",
                "Landroid/location/LocationManager;->getLatitude",
                "Landroid/location/LocationManager;->getLongitude"};
        System.out.println("\t【基于Smali文件危险API检测】");
        for (String key : keyWord) {
            api api = apiType.getTypeBySmali(key);
            SundayTest.findApi(dir, key, api);
        }
    }

}
