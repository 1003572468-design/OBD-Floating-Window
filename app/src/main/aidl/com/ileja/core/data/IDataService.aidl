// IDataService.aidl
package com.ileja.core.data;

import com.ileja.core.data.DataCallback;
import android.os.Bundle;

interface IDataService {
    void register(String filter, DataCallback callback);
    void unregister(String filter, DataCallback callback);
    Bundle getValue(String filter);
}
