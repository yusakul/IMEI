package com.example.xu.myimei;

import android.os.Handler;
import android.os.Looper;


public class HandlerUtils {

  private static Handler handler;

  public static void post(Runnable runnable) {
    ensureHandler();
    handler.post(runnable);
  }

  public static void postDelay(Runnable runnable, int delay) {
    ensureHandler();
    handler.postDelayed(runnable, delay);
  }

  public static void clear() {
    if (handler != null) {
      handler.removeCallbacksAndMessages(null);
    }
  }

  public static void clearRunnable(Runnable runnable) {
    if (handler != null) {
      handler.removeCallbacks(runnable);
    }
  }

  private static void ensureHandler() {
    if (handler == null) {
      handler = new Handler(Looper.getMainLooper());
    }
  }

}
