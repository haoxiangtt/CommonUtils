package cn.richinfo.utils;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by yanjuegong on 2015/12/29.
 * 反射工具类
 */
public class ReflectionUtil {

    public static <T> T load(Object obj, String methodName) {
        return load(obj, methodName, null);
    }

    /**
     *
     * @param obj 类
     * @param methodName 方法名
     * @param params 参数
     * @return
     */
    public static <T> T load(Object obj, String methodName, Object[] params) {

        Class cls = obj.getClass();
        // 获取指定方法
        if (params == null || params.length == 0){
            return load(obj, methodName, null, null, false);
        }else {
            // 根据方法名获取指定方法的参数类型列表
            Class paramTypes[] = getParamTypes(cls, methodName, params.length);
            return load(obj, methodName, params, paramTypes, false);
        }

    }

    public static <T> T load(Object obj, String methodName, Object[] params, Class[] paramTypes
            , boolean accessible) {
        if (params != null && paramTypes != null) {
            if (params.length != paramTypes.length) {
                return null;
            }
        } else if (params != null || paramTypes != null){
            return null;
        }

        Object retObject = null;

        try {
            Class cls = obj.getClass();
            // 获取指定方法
            Method meth;
            if (params == null || params.length == 0){
                meth=cls.getMethod(methodName);
            }else {
                // 根据方法名获取指定方法的参数类型列表
                meth = cls.getMethod(methodName, paramTypes);
            }
            if (meth == null){
                Log.e("ReflectionUtil","无法获该函数");
            }
            if (accessible) {
                meth.setAccessible(true);
            }
            // 调用指定的方法并获取返回值为Object类型
            retObject = meth.invoke(obj, params);


        } catch (Exception e) {
            System.err.println(e);
        }

        return (T)retObject;
    }

    public static <T> T loadStatic(Class cls, String methodName) {
        return loadStatic(cls, methodName, null);
    }

    public static <T> T loadStatic(Class cls, String methodName, Object[] params){
        // 获取指定方法
        if (params == null || params.length == 0){
            return loadStatic(cls, methodName, null, null, false);
        }else {
            // 根据方法名获取指定方法的参数类型列表
            Class paramTypes[] = getParamTypes(cls, methodName, params.length);
            return loadStatic(cls, methodName, params, paramTypes, false);
        }

    }

    public static <T> T loadStatic(Class cls, String methodName, Object[] params, Class[] paramTypes
            , boolean accessible) {
        if (params != null && paramTypes != null) {
            if (params.length != paramTypes.length) {
                return null;
            }
        } else if (params != null || paramTypes != null){
            return null;
        }

        Object retObject = null;

        try {
            // 获取指定方法
            Method meth=null;
            if (params == null || params.length == 0){
                meth=cls.getMethod(methodName);
            }else {
                // 根据方法名获取指定方法的参数类型列表
                meth = cls.getMethod(methodName, paramTypes);
            }
            if (meth==null){
                Log.e("ReflectionUtil","无法获该函数");
            }
            if (accessible) {
                meth.setAccessible(true);
            }
            // 调用指定的方法并获取返回值为Object类型
            retObject = meth.invoke(null, params);


        } catch (Exception e) {
            System.err.println(e);
        }

        return (T)retObject;
    }

    private static Class[] getParamTypes(Object[] params) {
        if (params == null || params.length == 0) {
            return null;
        }
        Class[] cs = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            cs[i] = params[i].getClass();
        }
        return cs;
    }



    /**
     * 获取参数类型，返回值保存在Class[]中
     */
    private static Class[] getParamTypes(Class cls, String mName, int len) {
        Class[] cs = null;

        /*
         * Note: 由于我们一般通过反射机制调用的方法，是非public方法
         * 所以在此处使用了getDeclaredMethods()方法
         */
        Method[] mtd = cls.getDeclaredMethods();
        for (int i = 0; i < mtd.length; i++) {
            String str=mtd[i].getName();
            if (!str.equals(mName)) {    // 不是我们需要的参数，则进入下一次循环
                continue;
            }
            cs = mtd[i].getParameterTypes();
            if (cs == null || len != cs.length) {
                continue;
            } else {
                break;
            }
        }
        return cs;
    }


}
