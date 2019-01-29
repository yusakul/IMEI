package com.example.xu.myimei;

/**
 * 权限相关的回调.
 */
public interface PermissionCallback {

  /**
   * 权限是否被允许.
   * @param granted        是否被允许.
   */
  void onPermissionResult(boolean granted);
}
