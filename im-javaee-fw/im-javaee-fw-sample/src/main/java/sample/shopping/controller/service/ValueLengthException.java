/*
 * ValueLengthException.java
 *
 * Created on 2002/03/14, 14:10
 */

package sample.shopping.controller.service;

import org.intra_mart.framework.base.service.RequestException;

/**
 * 値の長さが不正な時の例外です。
 *
 * @author  NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class ValueLengthException extends RequestException {

    /**
     * パラメータ名
     */
    private String prameterName;

    /**
     * 詳細メッセージを指定しないで <code>ValueLengthException</code> を構築します。
     */
    public ValueLengthException() {
        super();
        setParameterName(null);
    }

    /**
     * 指定された詳細メッセージを持つ <code>ValueLengthException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public ValueLengthException(String msg) {
        super(msg);
        setParameterName(null);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>ValueLengthException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public ValueLengthException(String msg, Throwable throwable) {
        super(msg, throwable);
        setParameterName(null);
    }

    /**
     * 長さが不正なパラメータ名を格納します。
     *
     * @param prameterName パラメータ名
     */
    public void setParameterName(String prameterName) {
        this.prameterName = prameterName;
    }

    /**
     * 長さが不正なパラメータ名を返却します。
     *
     * @return パラメータ名
     */
    public String getPrameterName() {
        return this.prameterName;
    }
}
