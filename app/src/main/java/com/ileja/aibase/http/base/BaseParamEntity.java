package com.ileja.aibase.http.base;

import com.ileja.aibase.http.http.ParamEntity;

/* JADX INFO: loaded from: classes.dex */
public class BaseParamEntity implements ParamEntity {
    @Override // com.ileja.aibase.http.http.ParamEntity
    public String getCommonParam() {
        return ComNetParam.getCommonParam();
    }
}