package com.ileja.aibase.http.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import proguard.annotation.KeepImplementations;

/* JADX INFO: loaded from: classes.dex */
@KeepImplementations
public interface URLBuilder {

    public static class DefaultURLBuilder implements URLBuilder {
        private Map<String, Object> paramsMap;
        private String url;

        @Override // com.ileja.aibase.http.http.URLBuilder
        public Map<String, Object> getParams() {
            return this.paramsMap;
        }

        @Override // com.ileja.aibase.http.http.URLBuilder
        public String getUrl() {
            return this.url;
        }

        @Override // com.ileja.aibase.http.http.URLBuilder
        public String makeUrl() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("?");
            for (Map.Entry<String, Object> entry : this.paramsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key != null && value != null) {
                    stringBuffer.append(key);
                    stringBuffer.append("=");
                    stringBuffer.append(value);
                    stringBuffer.append("&");
                }
            }
            if (stringBuffer.length() > 1) {
                return getUrl() + stringBuffer.substring(0, stringBuffer.length() - 1);
            }
            return getUrl() + stringBuffer.toString();
        }

        @Override // com.ileja.aibase.http.http.URLBuilder
        public void parse(Path path, Map<String, Field> map, ParamEntity paramEntity) throws IllegalAccessException {
            this.url = path.host() + path.url();
            this.paramsMap = new HashMap();
            if (map != null) {
                for (Map.Entry<String, Field> entry : map.entrySet()) {
                    Object obj = entry.getValue().get(paramEntity);
                    if (obj != null) {
                        if (entry.getKey().startsWith("_")) {
                            this.url += ((String) obj);
                        } else {
                            this.paramsMap.put(entry.getKey(), obj);
                        }
                    }
                }
            }
        }
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Path {
        Class<? extends URLBuilder> builder() default DefaultURLBuilder.class;

        String host() default "";

        String url();
    }

    Map<String, Object> getParams();

    String getUrl();

    String makeUrl();

    void parse(Path path, Map<String, Field> map, ParamEntity paramEntity);
}