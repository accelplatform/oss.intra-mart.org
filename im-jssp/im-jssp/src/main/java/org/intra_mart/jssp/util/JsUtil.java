package org.intra_mart.jssp.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * JavaScriptの型からJavaの型に変換するユーティリティクラス。<BR>
 * <BR>
 * JavaScriptの型からJavaの型に変換するユーティリティを提供します。<BR>
 */
public class JsUtil {

    /**
     * コンストラクタ。<BR>
     */
    private JsUtil() {
        super();
    }
    
    public static Scriptable toObject(Object object,Scriptable def){
        return (object instanceof Scriptable) ? (Scriptable)object : def;
    }

    public static Scriptable toObject(Object object){
        return JsUtil.toObject(object,null);
    }

    public static String toString(Object object,String def){
        return (object instanceof String) ? (String)object : def;
    }

    public static String toString(Object object){
        return JsUtil.toString(object,null);
    }
    

    public static int toInt(Object object,int def){
        return (object instanceof Number) ? ((Number)object).intValue() : def;
    }

    public static int toInt(Object object){
        return JsUtil.toInt(object,0);
    }
    
    public static double toDouble(Object object,double def){
        return (object instanceof Number) ? ((Number)object).doubleValue() : def;
    }

    public static double toDouble(Object object){
        return JsUtil.toDouble(object,0);
    }

    public static boolean toBoolean(Object object,boolean def){
        return (object instanceof Boolean) ? ((Boolean)object).booleanValue() : def;
    }

    public static boolean toBoolean(Object object){
        return JsUtil.toBoolean(object,false);
    }

    public static Date toDate(Object object, Date def){
    	if(!isDate(object)){
    		return def;
    	}
    	
    	try {
			return (Date) Context.jsToJava(object, Date.class);
		}
		catch (Exception e) {
			return def;
		}
    }

    public static Date toDate(Object object){
        return JsUtil.toDate(object,null);
    }
    
    public static boolean isDate(Object object){    	
    	return NativeJavaObject.canConvert(object, Date.class);
    }

    public static Object getBeanToObject(Object bean){
        try {
            if(bean instanceof String) {
                return bean;
            }
            else if (bean instanceof Number) {
                return new Double(((Number)bean).doubleValue());
            }
            else if (bean instanceof Boolean) {
                return bean;
            }
            else if (bean instanceof Date) {
                return RuntimeObject.newDate(((Date)bean).getTime()); 
            }
            else {
	            ValueObject result = new ValueObject();
	
	            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
	
	            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
	            
	            for (int i = 0; i < descriptors.length; i++) {
	                Method method = descriptors[i].getReadMethod();
	                
	                Object ret = method.invoke(bean,new Object[0]);
	                
	                if(ret instanceof String) {
	                    result.defineProperty(descriptors[i].getName(),ret,ScriptableObject.EMPTY);  
	                }
	                else if(ret instanceof Number) {
	                    result.defineProperty(descriptors[i].getName(),new Double(((Number)ret).doubleValue()),ScriptableObject.EMPTY);  
	                }
	                else if(ret instanceof Boolean) {
	                    result.defineProperty(descriptors[i].getName(),ret,ScriptableObject.EMPTY);  
	                }
	                else if(ret instanceof Date) {
	                    result.defineProperty(descriptors[i].getName(),RuntimeObject.newDate(((Date)ret).getTime()),ScriptableObject.EMPTY);  
	                }
	                
	                descriptors[i].getName();
	            }
	            return result;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
