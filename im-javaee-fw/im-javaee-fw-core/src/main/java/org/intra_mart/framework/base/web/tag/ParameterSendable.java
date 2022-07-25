/*
 * ParameterSendable.java
 *
 * Created on 2002/01/29, 13:27
 */

package org.intra_mart.framework.base.web.tag;

/**
 * パラメータを渡すことができるタグです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface ParameterSendable {

    /**
     * パラメータを追加します。
     *
     * @param name パラメータ名
     * @param value パラメータの値
     */
    public void addParameter(String name, String value);
}
