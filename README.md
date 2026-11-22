# FingerLock Detector

A small Android diagnostic app for testing whether a device exposes Android's fingerprint **swipe gesture** API to an Accessibility Service.

## Important limitation

Android's public `FingerprintGestureController` API reports swipe gestures (up, down, left, right). It does **not** provide a general single-tap fingerprint event. A device must also implement and expose this feature. This project does not promise that the Infinix Hot 11S exposes it.

## Build

The project uses Android Gradle Plugin 8.7.3, Gradle 8.9, JDK 17, compile SDK 35, and minimum SDK 26.

A GitHub Actions workflow at `.github/workflows/build.yml` builds a debug APK whenever code is pushed to `main`, or when manually triggered.

## Test on the phone

1. Install the debug APK.
2. Open FingerLock Detector.
3. Tap **Open Accessibility Settings**.
4. Enable **FingerLock fingerprint diagnostic**.
5. Return to the app and tap **Refresh status**.
6. Try swiping (not tapping) on the fingerprint sensor.
7. Check the app status again. The latest reported result is shown there.

If the service reports that gesture detection is unavailable, the phone firmware is not exposing the supported gesture API to this app.

## Privacy

The app does not request Internet, camera, microphone, location, or biometric permissions. It does not read fingerprint images or biometric templates. The Accessibility Service is used only to register the platform fingerprint gesture callback; it does not inspect window content.

## Current scope

This is a diagnostic build only. It does not lock the screen. A simple fingerprint tap cannot be implemented through this public API unless the device exposes an additional vendor-specific event.
