package cn.richinfo.utils;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by yanjuegong on 2015/12/29.
 * 反射工具类
 */
public class ReflectionUtil {

    /**
     *
     * @param obj 类
     * @param MethodName 方法名
     * @param params 参数
     * @return
     */
    public static Object load(Object obj, String MethodName, Object[] params) {
        Object retObject = null;

        try {
            Class cls=obj.getClass();
            // 获取指定方法
            Method meth=null;
            if (params.length==0){
                meth=cls.getMethod(MethodName);
            }else {
                // 根据方法名获取指定方法的参数类型列表
                Class paramTypes[] = getParamTypes(cls, MethodName);
                meth = cls.getMethod(MethodName, paramTypes);
            }
            if (meth==null){
                Log.e("ReflectionUtil","无法获该函数");
            }
            meth.setAccessible(true);
            // 调用指定的方法并获取返回值为Object类型
            retObject = meth.invoke(obj, params);


        } catch (Exception e) {
            System.err.println(e);
        }

        return retObject;
    }



    /**
     * 获取参数类型，返回值保存在Class[]中
     */
    public static Class[] getParamTypes(Class cls, String mName) {
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
        }
        return cs;
    }


}
