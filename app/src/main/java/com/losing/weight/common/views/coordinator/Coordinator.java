package com.losing.weight.common.views.coordinator;

import android.graphics.Rect;
import android.view.View;

public class Coordinator {

  public static int[] getTopXY(View v){
    int[] array = new int[2];
    int xOffset = 0;
    int yOffset = 0;
    Rect gvr = new Rect();

    View parent = (View) v.getParent();
    int parentHeight = parent.getHeight();

    if (v.getGlobalVisibleRect(gvr)) {
      View root = v.getRootView();

      int halfWidth = root.getRight() / 2;
      int halfHeight = root.getBottom() / 2;

      int parentCenterX = ((gvr.right - gvr.left) / 2) + gvr.left;

      int parentCenterY = ((gvr.bottom - gvr.top) / 2) + gvr.top;

      if (parentCenterY <= halfHeight) {
        yOffset = -(halfHeight - parentCenterY) - parentHeight;
      } else {
        yOffset = (parentCenterY - halfHeight) - parentHeight;
      }

      if (parentCenterX < halfWidth) {
        xOffset = -(halfWidth - parentCenterX);
      }

      if (parentCenterX >= halfWidth) {
        xOffset = parentCenterX - halfWidth;
      }
    }
    array[0] = xOffset;
    array[1] = yOffset;
    return array;
  }


  public static int[] getBottomInfo(View toast, View clicker){
    int[] array = new int[2];
    int xOffset = 0;
    int yOffset = 0;
    int toastHeight = toast.getHeight();
    int toastWidth = toast.getWidth();

    int clickerX = (int) clicker.getX();
    int clickerY = (int) clicker.getY();

    xOffset = clickerX - toastWidth;
    yOffset = clickerY + toastHeight;
    array[0] = xOffset;
    array[1] = yOffset;
    return array;
  }
}
