package org.andulir.utils;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 实用工具类，用于处理基本数据类型的检测
 */
public class TypeUtils {

    /**
     * 判断给定的参数是否为基本数据类型
     *
     * @param parameter 参数对象
     * @return 如果是基本数据类型，返回true；否则返回false
     */
    public static boolean isBasicType(Parameter parameter) {
        Class<?> parameterType = parameter.getType();
        return isBasicType(parameterType);
    }

    /**
     * 判断给定的类型是否为基本数据类型
     *
     * @param type 类型对象
     * @return 如果是基本数据类型，返回true；否则返回false
     */
    public static boolean isBasicType(Type type) {
        Set basicTypes = new HashSet(Arrays.asList(
                int.class, long.class, short.class, byte.class, char.class, double.class, float.class, boolean.class,
                Integer.class, Long.class, Short.class, Byte.class, Character.class, Double.class, Float.class, Boolean.class,
                String.class, Date.class
        ));
        return basicTypes.contains(type);
    }

    /**
     * 根据类型名称判断是否为基本数据类型
     *
     * @param typeName 类型的名称
     * @return 如果是基本数据类型，返回true；否则返回false
     */
    public static boolean isBasicType(String typeName) {
        typeName = switchToPackageClass(typeName);
        Class<?> aClass = null;
        try {
            aClass = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return isBasicType(aClass);
    }

    /**
     * 将基本数据类型名称转换为对应的包装类全名
     *
     * @param typeName 基本数据类型名称
     * @return 对应的包装类的全名
     */
    // Class.forName不是很适用于基本数据类型
    public static String switchToPackageClass(String typeName) {
        typeName = typeName.toLowerCase();
        switch (typeName) {
            case "int":
                typeName = "java.lang.Integer";
                break;
            case "long":
                typeName = "java.lang.Long";
                break;
            case "short":
                typeName = "java.lang.Short";
                break;
            case "byte":
                typeName = "java.lang.Byte";
                break;
            case "char":
                typeName = "java.lang.Character";
                break;
            case "double":
                typeName = "java.lang.Double";
                break;
            case "float":
                typeName = "java.lang.Float";
                break;
            case "boolean":
                typeName = "java.lang.Boolean";
                break;
            default:
                break;
        }
        return typeName;
    }
}
