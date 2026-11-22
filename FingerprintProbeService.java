package com.ratulmaker.fingerlockdetector;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.FingerprintGestureController;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class FingerprintProbeService extends AccessibilityService {
    private static final String TAG = "FingerLockProbe";
    private FingerprintGestureController controller;
    private FingerprintGestureController.FingerprintGestureCallback callback;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        controller = getFingerprintGestureController();
        boolean available = controller != null && controller.isGestureDetectionAvailable();
        saveResult("Service connected. Fingerprint swipe gesture detection available: " + available);
        Log.i(TAG, "Fingerprint gesture detection available: " + available);

        if (!available) return;

        callback = new FingerprintGestureController.FingerprintGestureCallback() {
            @Override
            public void onGestureDetected(int gesture) {
                String name = gestureName(gesture);
                saveResult("Detected fingerprint gesture: " + name);
                Log.i(TAG, "Detected fingerprint gesture: " + name);
            }
        };

        controller.registerFingerprintGestureCallback(callback, null);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // This diagnostic does not inspect screen content or user input.
    }

    @Override
    public void onInterrupt() {
        saveResult("Accessibility service interrupted.");
    }

    @Override
    public void onDestroy() {
        if (controller != null && callback != null) {
            controller.unregisterFingerprintGestureCallback(callback);
        }
        super.onDestroy();
    }

    private void saveResult(String result) {
        getSharedPreferences("probe", MODE_PRIVATE)
                .edit().putString("last_result", result).apply();
    }

    private String gestureName(int gesture) {
        switch (gesture) {
            case FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_UP:
                return "SWIPE UP";
            case FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_DOWN:
                return "SWIPE DOWN";
            case FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_LEFT:
                return "SWIPE LEFT";
            case FingerprintGestureController.FINGERPRINT_GESTURE_SWIPE_RIGHT:
                return "SWIPE RIGHT";
            default:
                return "UNKNOWN (" + gesture + ")";
        }
    }

    public static boolean isEnabled(Context context) {
        String enabled = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabled == null) return false;

        ComponentName expected = new ComponentName(context, FingerprintProbeService.class);
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        splitter.setString(enabled);
        while (splitter.hasNext()) {
            ComponentName component = ComponentName.unflattenFromString(splitter.next());
            if (expected.equals(component)) return true;
        }
        return false;
    }
}
