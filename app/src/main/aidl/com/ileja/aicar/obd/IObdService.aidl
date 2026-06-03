// IObdService.aidl
package com.ileja.aicar.obd;

interface IObdService {
    void open();
    void close();
    void sendCommand(String cmd);
    void checkObdUpgrade(String str);
}
