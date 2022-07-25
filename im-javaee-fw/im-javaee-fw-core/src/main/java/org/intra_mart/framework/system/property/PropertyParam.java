/*
 * PropertyParam.java
 *
 * Created on 2002/02/18, 16:50
 */

package org.intra_mart.framework.system.property;

/**
 * プロパティのパラメータを表現する汎用的なクラスです。
 * このクラスのインスタンス１つがパラメータ名とパラメータの値という１つのペアに対応します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class PropertyParam {

    /**
     * パラメータ名
     */
    private String name;

    /**
     * パラメータの値
     */
    private String value;

    /**
     * PropertyParamを新規に生成します。
     */
    public PropertyParam() {
        setName(null);
        setValue(null);
    }

    /**
     * パラメータ名を設定します。
     *
     * @param name パラメータ名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * パラメータ名を取得します。
     *
     * @return パラメータ名
     */
    public String getName() {
        return this.name;
    }

    /**
     * パラメータの値を設定します。
     *
     * @param value パラメータの値
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * パラメータの値を取得します。
     *
     * @return パラメータの値
     */
    public String getValue() {
        return this.value;
    }
}
