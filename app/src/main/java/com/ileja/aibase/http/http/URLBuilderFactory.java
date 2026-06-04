package com.ileja.aibase.http.http;

import com.ileja.aibase.http.http.URLBuilder;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class URLBuilderFactory {
    private static final Map<Class, EntityInfo> entityInfoCache = new HashMap();

    private static class EntityInfo {

        /* JADX INFO: renamed from: a */
        URLBuilder.Path f5914a;

        /* JADX INFO: renamed from: b */
        Map<String, Field> f5915b;

        private EntityInfo() {
        }
    }

    private URLBuilderFactory() {
    }

    public static URLBuilder build(ParamEntity paramEntity) {
        if (paramEntity == null) {
            throw new IllegalArgumentException("entity must not be null.");
        }
        Class<?> cls = paramEntity.getClass();
        EntityInfo entityInfo = entityInfoCache.get(cls);
        if (entityInfo == null) {
            entityInfo = new EntityInfo();
            URLBuilder.Path path = (URLBuilder.Path) cls.getAnnotation(URLBuilder.Path.class);
            if (path == null) {
                throw new IllegalArgumentException("参数entity必需有URLBuilder.Path注解");
            }
            entityInfo.f5914a = path;
            HashMap map = new HashMap();
            entityInfo.f5915b = map;
            resolveAllFields(cls, map);
            entityInfoCache.put(cls, entityInfo);
        }
        Class<? extends URLBuilder> clsBuilder = entityInfo.f5914a.builder();
        if (clsBuilder == null) {
            clsBuilder = entityInfo.f5914a.builder();
        }
        try {
            URLBuilder uRLBuilderNewInstance = clsBuilder.newInstance();
            uRLBuilderNewInstance.parse(entityInfo.f5914a, entityInfo.f5915b, paramEntity);
            return uRLBuilderNewInstance;
        } catch (IllegalAccessException unused) {
            throw new RuntimeException(clsBuilder.getName() + "必须有public空构造方法");
        } catch (InstantiationException unused2) {
            throw new RuntimeException(clsBuilder.getName() + "必须有空构造方法");
        }
    }

    private static void resolveAllFields(Class cls, Map<String, Field> map) {
        if (cls == null || cls.equals(Object.class)) {
            return;
        }
        Field[] declaredFields = cls.getDeclaredFields();
        if (declaredFields != null) {
            for (Field field : declaredFields) {
                String name = field.getName();
                if (!name.startsWith("this$")) {
                    field.setAccessible(true);
                    map.put(name, field);
                }
            }
        }
        resolveAllFields(cls.getSuperclass(), map);
    }
}