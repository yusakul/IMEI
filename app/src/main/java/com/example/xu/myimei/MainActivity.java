package com.example.xu.myimei;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    TextView tv_info, textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_info = findViewById(R.id.tv_info);
        textView = findViewById(R.id.textView);
        ReadPhonePermissionChecker cameraPermissionCheckerV21 = new ReadPhonePermissionChecker(this);
        cameraPermissionCheckerV21.check(new PermissionCallback() {
            @Override
            public void onPermissionResult(boolean granted) {
                if (granted) {
                    getImeis();
                    textView.setText("imei2:" + getIMEI_2(MainActivity.this) + "\n硬件码" + getSplicingDeviceCode() + "\n 加密码" + getUniquePsuedoID());
                }
            }
        });
    }

    private void testTime() {
        int day = 24 * 60 * 60 * 1000;
        long firstInTime = System.currentTimeMillis() - 2 * day;
        String time = TimeUtils.getTime(System.currentTimeMillis() - firstInTime);
        Log.e("flag", "--------------------time:" + time + "day" + TimeUtils.getNoZeroDay());
    }

    /**
     * 获取imei获取第二个不一定准确
     *
     * @return
     */
    public String[] getImeis() {
        String[] imeiArray = {"", ""};
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return imeiArray;
//        }
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        for (int slot = 0; slot < telephonyManager.getPhoneCount(); slot++) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return imeiArray;
            }
            try {
                String imei = telephonyManager.getDeviceId(slot);
                Log.e("flag", "----------------------Imei" + slot + "---:" + imei);
                imeiArray[slot] = imei;
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < imeiArray.length; i++) {
            if (0 == i) {
                stringBuilder.append("imei1:" + imeiArray[i]);
            } else {
                stringBuilder.append("imei2:" + imeiArray[i]);
            }
        }
        tv_info.setText("获取到的IMEI" + stringBuilder.toString());
        Toast.makeText(this, "获取到的IMEI" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
        return imeiArray;
    }

    /**
     * 反射获取imei
     * 如果2个imei  这种方式比较靠谱
     *
     * @param context
     * @return
     */
    public static String getIMEI_2(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = tm.getClass();
        try {
            Method getImei = clazz.getDeclaredMethod("getImei", int.class);
            return getImei.invoke(tm, 1).toString();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取拼接设备信息 cpu 等硬件信息生成15位设备码
     * 重复的可能性比较小
     *
     * @return
     */
    public static String getSplicingDeviceCode() {
        return "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

    }

    /**
     * 获得独一无二的Psuedo ID
     * 适配99.5%
     * SERIAL code  与拼接设备id信息 加密
     */
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
