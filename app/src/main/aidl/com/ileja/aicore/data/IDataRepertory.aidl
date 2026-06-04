// IDataRepertory.aidl
package com.ileja.aicore.data;

import com.ileja.aicore.data.IDataCallback;
import android.os.Bundle;

interface IDataRepertory {
    void register(String filter, IDataCallback callback);
    void unregister(String filter, IDataCallback callback);
    Bundle get(String filter);
}
