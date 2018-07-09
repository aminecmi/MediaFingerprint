package bou.amine.apps.mediafingerprint

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.FingerprintGestureController
import android.accessibilityservice.FingerprintGestureController.*
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class SensorService : AccessibilityService() {
    val tag = SensorService::javaClass.name
    private lateinit var gestureController: FingerprintGestureController
    private lateinit var callback: FingerprintGestureCallback

    override fun onInterrupt() {
        Log.d(tag, "onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d(tag, "onAccessibilityEvent")
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        Log.d(tag, "onKeyEvent " + event?.keyCode)
        return super.onKeyEvent(event)
    }

    override fun onGesture(gestureId: Int): Boolean {
        Log.d(tag, "onGesture $gestureId")
        return super.onGesture(gestureId)
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")
        super.onDestroy()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(tag, "onServiceConnected")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            gestureController = fingerprintGestureController

            Toast.makeText(applicationContext, "Is available: " + gestureController.isGestureDetectionAvailable, Toast.LENGTH_LONG).show()
            Log.e(tag, "Is available: " + gestureController.isGestureDetectionAvailable)

             callback  = object: FingerprintGestureCallback() {
                override fun onGestureDetectionAvailabilityChanged(available: Boolean) {
                    super.onGestureDetectionAvailabilityChanged(available)
                    Toast.makeText(applicationContext,
                                   "Gesture available change to: $available", Toast.LENGTH_SHORT).show()
                    Log.d(tag, "onGestureDetectionAvailabilityChanged $available")
                }

                override fun onGestureDetected(gesture: Int) {
                    super.onGestureDetected(gesture)
                    val eventtime = SystemClock.uptimeMillis()
                    Toast.makeText(applicationContext, "Gesture: $gesture", Toast.LENGTH_SHORT).show()
                    if (gesture == FINGERPRINT_GESTURE_SWIPE_RIGHT) {
                        val downIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
                        val downEvent = KeyEvent(
                            eventtime, eventtime,
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT, 0
                        )
                        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent)
                        sendOrderedBroadcast(downIntent, null)

                        val upIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
                        val upEvent = KeyEvent(
                            eventtime, eventtime,
                            KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0
                        )
                        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent)
                        sendOrderedBroadcast(upIntent, null)
                    }


                    if (gesture == FINGERPRINT_GESTURE_SWIPE_LEFT) {
                        val downIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
                        val downEvent = KeyEvent(
                            eventtime, eventtime,
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0
                        )
                        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent)
                        sendOrderedBroadcast(downIntent, null)

                        val upIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
                        val upEvent = KeyEvent(
                            eventtime, eventtime,
                            KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0
                        )
                        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent)
                        sendOrderedBroadcast(upIntent, null)
                    }


                    if (gesture == FINGERPRINT_GESTURE_SWIPE_UP) {
                        val downIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
                        val downEvent = KeyEvent(
                            eventtime, eventtime,
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE, 0
                        )
                        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent)
                        sendOrderedBroadcast(downIntent, null)

                        val upIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
                        val upEvent = KeyEvent(
                            eventtime, eventtime,
                            KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE, 0
                        )
                        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent)
                        sendOrderedBroadcast(upIntent, null)
                    }


                    if (gesture == FINGERPRINT_GESTURE_SWIPE_DOWN) {
                        val downIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
                        val downEvent = KeyEvent(
                            eventtime, eventtime,
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY, 0
                        )
                        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent)
                        sendOrderedBroadcast(downIntent, null)

                        val upIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null)
                        val upEvent = KeyEvent(
                            eventtime, eventtime,
                            KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY, 0
                        )
                        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent)
                        sendOrderedBroadcast(upIntent, null)
                    }

                    Log.d(tag, "onGestureDetected $gesture");                }
            }

            gestureController.registerFingerprintGestureCallback(callback, Handler())
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(tag, "onUnbind " )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            gestureController.unregisterFingerprintGestureCallback(callback)
        }
        return super.onUnbind(intent)
    }


}
