package com.ileja.aibase.phone;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.ileja.aibase.AiBase;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class SensorHelper implements SensorEventListener {
    private static SensorHelper instance;
    private boolean active;
    private Application mContext;
    private Set<SensorHelperListener> mSensorListeners;
    private SensorManager mSensorManager;

    /* JADX INFO: renamed from: r */
    private float[] f5918r = new float[9];
    private float[] values = new float[3];
    private float[] gravity = null;
    private float[] geomagnetic = null;

    public interface SensorHelperListener {
        void sensorChanged(float f);
    }

    private SensorHelper() {
        Application context = AiBase.getInst().getContext();
        this.mContext = context;
        try {
            this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mSensorListeners = new HashSet();
    }

    private void calcOrientation() {
        float[] fArr;
        float[] fArr2 = this.gravity;
        if (fArr2 == null || (fArr = this.geomagnetic) == null || !SensorManager.getRotationMatrix(this.f5918r, null, fArr2, fArr)) {
            return;
        }
        SensorManager.getOrientation(this.f5918r, this.values);
        notifySensorListerners((radToDeg(this.values[0]) + 360.0f) % 360.0f);
    }

    public static SensorHelper getInstance() {
        if (instance == null) {
            instance = new SensorHelper();
        }
        return instance;
    }

    private float radToDeg(float f) {
        return (float) Math.toDegrees(f);
    }

    public void notifySensorListerners(float f) {
        Iterator<SensorHelperListener> it = this.mSensorListeners.iterator();
        while (it.hasNext()) {
            it.next().sensorChanged(f);
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.mSensorManager != null && this.active) {
            int type = sensorEvent.sensor.getType();
            if (type == 1) {
                this.gravity = sensorEvent.values;
                calcOrientation();
            } else {
                if (type != 2) {
                    return;
                }
                this.geomagnetic = sensorEvent.values;
                calcOrientation();
            }
        }
    }

    public void registerSensorListener(SensorHelperListener sensorHelperListener) {
        if (sensorHelperListener != null) {
            this.mSensorListeners.add(sensorHelperListener);
        }
    }

    public void startSensor() {
        SensorManager sensorManager;
        if (this.active || (sensorManager = this.mSensorManager) == null) {
            return;
        }
        Sensor defaultSensor = sensorManager.getDefaultSensor(1);
        Sensor defaultSensor2 = this.mSensorManager.getDefaultSensor(2);
        this.mSensorManager.registerListener(this, defaultSensor, 2);
        this.mSensorManager.registerListener(this, defaultSensor2, 2);
        this.active = true;
    }

    public void stopSensor() {
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null) {
            return;
        }
        Sensor defaultSensor = sensorManager.getDefaultSensor(1);
        Sensor defaultSensor2 = this.mSensorManager.getDefaultSensor(2);
        this.mSensorManager.unregisterListener(this, defaultSensor);
        this.mSensorManager.unregisterListener(this, defaultSensor2);
        this.active = false;
    }

    public void unregisterSensorListener(SensorHelperListener sensorHelperListener) {
        if (sensorHelperListener != null) {
            this.mSensorListeners.remove(sensorHelperListener);
        }
    }
}