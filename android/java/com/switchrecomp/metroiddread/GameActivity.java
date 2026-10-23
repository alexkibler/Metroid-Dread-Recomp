package com.switchrecomp.metroiddread;

import android.app.NativeActivity;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.InputDevice;

/** Track controllers even before their first button press, including hotplug. */
public final class GameActivity extends NativeActivity implements InputManager.InputDeviceListener {
    static { System.loadLibrary("dread"); }
    private InputManager inputs;
    private static native void controllerConnected(boolean connected);
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        inputs = (InputManager)getSystemService(INPUT_SERVICE);
        inputs.registerInputDeviceListener(this, null);
        updateController();
    }
    private void updateController() {
        boolean connected = false;
        for (int id : InputDevice.getDeviceIds()) {
            InputDevice device = InputDevice.getDevice(id);
            if (device != null && !device.isVirtual() &&
                    (device.supportsSource(InputDevice.SOURCE_GAMEPAD) || device.supportsSource(InputDevice.SOURCE_JOYSTICK))) {
                connected = true; break;
            }
        }
        controllerConnected(connected);
    }
    @Override public void onInputDeviceAdded(int id) { updateController(); }
    @Override public void onInputDeviceRemoved(int id) { updateController(); }
    @Override public void onInputDeviceChanged(int id) { updateController(); }
    @Override protected void onDestroy() {
        if (inputs != null) inputs.unregisterInputDeviceListener(this);
        super.onDestroy();
    }
}
