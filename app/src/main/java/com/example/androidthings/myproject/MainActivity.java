/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Skeleton of the main Android Things activity. Implement your device's logic
 * in this class.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * led = service.openGpio("BCM6");
 * led.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * led.setValue(true);
 * }</pre>
 *
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 */
public class MainActivity extends Activity {

    private static final String TAG = "Things";
    private static final int ONE_SECOND = 1000;

    private Gpio led;
    private boolean isLedOn = false;
    private int round = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        initLed();
        setTimer();
    }

    private void initLed() {
        try {
            PeripheralManagerService service =
                    new PeripheralManagerService();
            led = service.openGpio("BCM6");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTimer() {
        (new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                round++;
                toggleLed();
            }
        }, ONE_SECOND, ONE_SECOND);
    }

    private void toggleLed() {
        try {
            led.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            if (isLedOn) {
                // if LED is on, turn it off.
                led.setValue(false);
                isLedOn = false;
                Log.d(TAG, round + " toggleLed: OFF");
            } else {
                // if LED is off, turn it on.
                led.setValue(true);
                isLedOn = true;
                Log.d(TAG, round + " toggleLed: ON");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
