package com.example.xu.myimei;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;


public class ReadPhonePermissionChecker implements PermissionChecker {

    private static final int READPHONE_REQUEST_CODE = 100;

    private final Context context;

    public ReadPhonePermissionChecker(Context context) {
        this.context = context;
    }

    private boolean hasCameraPermission() {
        boolean isAllowed;
        isAllowed = AndPermission.hasPermission(context, Manifest.permission.READ_PHONE_STATE);
        return isAllowed;
    }

    @Override
    public void check(final PermissionCallback callback) {

        boolean isAllowed = hasCameraPermission();
        if (isAllowed) {
            callback.onPermissionResult(isAllowed);
            return;
        }

        AndPermission.with(context)
                .permission(Manifest.permission.READ_PHONE_STATE)
                .requestCode(READPHONE_REQUEST_CODE)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                    }
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(final int requestCode, @NonNull List<String> grantPermissions) {
                        handlePermissionResult(READPHONE_REQUEST_CODE, callback);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        handlePermissionResult(READPHONE_REQUEST_CODE, callback);
                    }
                })
                .start();
    }

    private void handlePermissionResult(
            final int requestCode, final PermissionCallback permissionCallback) {
        HandlerUtils.postDelay(new Runnable() {
            @Override
            public void run() {
                if (requestCode == READPHONE_REQUEST_CODE) {
                    if (hasCameraPermission()) {
                        if (permissionCallback != null) {
                            permissionCallback.onPermissionResult(true);
                        }
                    } else {
                        if (permissionCallback != null) {
                            permissionCallback.onPermissionResult(false);
                        }
                    }
                }
            }
        }, 100);
    }
}
