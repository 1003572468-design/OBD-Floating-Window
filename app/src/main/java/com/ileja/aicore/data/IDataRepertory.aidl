package com.ileja.aicore.data;

import android.os.Bundle;
import com.ileja.aicore.data.IDataCallback;

interface IDataRepertory {
    void register(String filter, IDataCallback callback);
    void unregister(String filter, IDataCallback callback);
    Bundle get(String filter);
}
