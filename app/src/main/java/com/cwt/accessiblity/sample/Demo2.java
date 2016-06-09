package com.cwt.accessiblity.sample;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by CwT on 16/6/9.
 */
public class Demo2 extends AccessibilityService {

    private static final String LOG_TAG = "cc";
    private static final String TEXT_TAG = "text";
    private static final String SEPARATOR = ", ";
    private static final String DASHLINE = "--------------";

    private boolean mStart = true;
    private boolean mStop = false;

    @Override
    public void onServiceConnected() {
        Log.d(LOG_TAG, "hi!!");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d(LOG_TAG, event.toString());

        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }

        if (!event.getPackageName().toString().equals("com.cyanogenmod.filemanager"))
            return;

        if (source.getClassName().toString().equals("android.widget.ListView")) {

            for (int i = 0; i < source.getChildCount(); i++) {
                final AccessibilityNodeInfo child = source.getChild(i);
                if (child != null) {
                    Log.d(LOG_TAG, "child " + i + ": " + child.toString());
                    if (child.getClassName().toString().equals("android.widget.LinearLayout")) {

                        for (int j = 0; j < child.getChildCount(); j++) {
                            final AccessibilityNodeInfo each = child.getChild(j);
                            if (each != null && each.getText() != null) {
                                Log.d(TEXT_TAG, each.getText().toString());
                                if (each.getText().toString().equals("wandoujia")) {
                                    Log.d(LOG_TAG, "Yeah, find it");
                                    mStop = true;
                                    if (child.isClickable())
                                        child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                }
                            }
                        }
                    }
                }
            }

            if (!mStop) {
                if (mStart || event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
                    Log.d(LOG_TAG, "Not find it, try scroll forward");
                    source.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    mStart = false;
                }
            }
        }

        Log.d(LOG_TAG, source.toString());
        Log.d(LOG_TAG, DASHLINE);
//        event.recycle();
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
