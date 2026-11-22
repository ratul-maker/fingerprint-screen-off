package com.ratulmaker.fingerlockdetector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 48, 32, 32);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("FingerLock Detector");
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        status = new TextView(this);
        status.setTextSize(17);
        status.setPadding(0, 32, 0, 24);
        layout.addView(status);

        Button settingsButton = new Button(this);
        settingsButton.setText("Open Accessibility Settings");
        settingsButton.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        layout.addView(settingsButton);

        Button refreshButton = new Button(this);
        refreshButton.setText("Refresh status");
        refreshButton.setOnClickListener(v -> updateStatus());
        layout.addView(refreshButton);

        TextView note = new TextView(this);
        note.setText("\nAfter enabling “FingerLock fingerprint diagnostic”, return here. "
                + "Then try supported swipe gestures on the fingerprint sensor. "
                + "This Android API does not report a simple tap.");
        note.setTextSize(15);
        layout.addView(note);

        setContentView(layout);
        updateStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void updateStatus() {
        boolean enabled = FingerprintProbeService.isEnabled(this);
        String last = getSharedPreferences("probe", MODE_PRIVATE)
                .getString("last_result", "No diagnostic result yet.");
        status.setText("Accessibility service: " + (enabled ? "ENABLED" : "NOT ENABLED")
                + "\n\n" + last
                + "\n\nCheck the service log after testing.");
    }
}
