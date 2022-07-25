/*
 * 
 * 
 */
package org.intra_mart.framework.system.property;

/**
 * プロパティモデルクラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class PropertyModel {

    private String propertyHandlerName = null;
    private PropertyHandlerParam[] params = null;

    static final String INIT_ID = "init-param";
    static final String PARAM_NAME_ID = "param-name";
    static final String PARAM_VALUE_ID = "param-value";

    /**
     * 初期パラメータを設定します。
     * @param factoryParams
     */
    void setPropertyHandlerParams(PropertyHandlerParam[] params) {
        this.params = params;
    }

    /**
     * 初期パラメータを取得します。
     * @return PropertyHandlerParam[] 
     */
    PropertyHandlerParam[] getPropertyHandlerParams() {
        return params;
    }

	/**
	 * ハンドラ名を設定します。
	 * @param factoryName
	 */ 
    void setPropertyHandlerName(String propertyHandlerName) {
        this.propertyHandlerName = propertyHandlerName;
    }

	/**
	 * ハンドラ名を取得します。
	 * @return ファクトリ名
	 */
    String getPropertyHandlerName() {
        return propertyHandlerName;
    }

}
