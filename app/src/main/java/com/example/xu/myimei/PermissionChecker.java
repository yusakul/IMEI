package com.example.xu.myimei;



public interface PermissionChecker {
  /**
   * @return 权限是否被容许.
   */
  void check(PermissionCallback callback);
}
