package org.intra_mart.framework.base.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * レスポンスヘッダを保持するためのクラスです。
 *
 * @author INTRAMART
 * @since 4.2
 */
class ServletResponseHeaderInfo implements Serializable {

    public static String SET_CONTENT_TYPE = "SET_CONTENT_TYPE";
    public static String SET_CONTENT_LENGTH = "SET_CONTENT_LENGTH";
    public static String SET_LOCALE = "SET_LOCALE";
    public static String ADD_DATE_HEADER = "ADD_DATE_HEADER";
    public static String ADD_HEADER = "ADD_HEADER";
    public static String ADD_INT_HEADER = "ADD_INT_HEADER";
    public static String SET_DATE_HEADER = "SET_DATE_HEADER";
    public static String SET_HEADER = "SET_HEADER";
    public static String SET_INT_HEADER = "SET_INT_HEADER";
    public static String SET_STATUS = "SET_STATUS";
    public static String SET_STATUS_2 = "SET_STATUS_2";
	public static String RESET = "RESET";
	public static String RESET_BUFFER = "RESET_BUFFER";


    private static HashMap methods = null;
    private static HashMap parameterTypes = null;

    private String methodId = null;
    private ArrayList parameters = new ArrayList();

    static {
        methods = new HashMap();
        methods.put(SET_CONTENT_TYPE, "setContentType");
        methods.put(SET_CONTENT_LENGTH, "setContentLength");
        methods.put(SET_LOCALE, "setLocale");
        methods.put(ADD_DATE_HEADER, "addDateHeader");
        methods.put(ADD_HEADER, "addHeader");
        methods.put(ADD_INT_HEADER, "addIntHeader");
        methods.put(SET_DATE_HEADER, "setDateHeader");
        methods.put(SET_HEADER, "setHeader");
        methods.put(SET_INT_HEADER, "setIntHeader");
        methods.put(SET_STATUS, "setStatus");
        methods.put(SET_STATUS_2, "setStatus");
		methods.put(RESET, "reset");
		methods.put(RESET_BUFFER, "resetBuffer");

        Class strCls = String.class;
        Class intCls = Integer.TYPE;
        Class longCls = Long.TYPE;
        Class localeCls = Locale.class;

        parameterTypes = new HashMap();
        parameterTypes.put(SET_CONTENT_TYPE, new Class[] { strCls });
        parameterTypes.put(SET_CONTENT_LENGTH, new Class[] { intCls });
        parameterTypes.put(SET_LOCALE, new Class[] { localeCls });
        parameterTypes.put(ADD_DATE_HEADER, new Class[] { longCls });
        parameterTypes.put(ADD_HEADER, new Class[] { strCls, strCls });
        parameterTypes.put(ADD_INT_HEADER, new Class[] { strCls, intCls });
        parameterTypes.put(SET_DATE_HEADER, new Class[] { longCls });
        parameterTypes.put(SET_HEADER, new Class[] { longCls });
        parameterTypes.put(SET_INT_HEADER, new Class[] { intCls });
        parameterTypes.put(SET_STATUS, new Class[] { intCls });
        parameterTypes.put(SET_STATUS_2, new Class[] { intCls, strCls });
		parameterTypes.put(RESET, new Class[] {});
		parameterTypes.put(RESET_BUFFER, new Class[] {});
    }

    /**
     * ServletResponseHeaderInfoを新規に生成します。
     *
     * @param methodId メソッドを表す識別子
     */
    public ServletResponseHeaderInfo(String methodId) {
        this.methodId = methodId;
    }

    /**
     * パラメータに与えられる値を追加します。
     *
     * @param value 値
     */
    public void addParameter(String value) {
        parameters.add(value);
    }

    /**
     * パラメータに与えられる値を追加します。
     *
     * @param value 値
     */
    public void addParameter(int value) {
        parameters.add(new Integer(value));
    }

    /**
     * パラメータに与えられる値を追加します。
     *
     * @param value 値
     */
    public void addParameter(long value) {
        parameters.add(new Long(value));
    }

    /**
     * パラメータに与えられる値を追加します。
     *
     * @param value 値
     */
    public void addParameter(Locale value) {
        parameters.add(value);
    }

    /**
     * パラメータの値を取得します。
     *
     * パラメータの値
     */
    public Object[] getParameters() {
        return parameters.toArray();
    }

    /**
     * パラメータの型を取得します。
     *
     * @return パラメータの型
     */
    public Class[] getParameterTypes() {
        return (Class[])parameterTypes.get(this.methodId);
    }

    /**
     * メソッド名を取得します。
     *
     * @return メソッド名
     */
    public String getMethodName() {
        return (String)methods.get(this.methodId);
    }

    /**
     * メソッドを表す識別子を取得します。
     *
     * @return メソッドを表す識別子
     */
    public String getMethdId() {
        return this.methodId;
    }

    /**
     * このオブジェクトの文字列表現を返します。
     *
     * @return このオブジェクトの文字列表現
     */
    public String toString() {
        String str = getMethodName() + " {";
        Object[] obj = getParameters();

        for (int cnt = 0; cnt < obj.length; cnt++) {
            str += ",\"" + obj[cnt].toString() + "\"";
        }

        str += "}";
        return str;
    }
}
