/*
 * 
 * 
 */
package org.intra_mart.framework.system.property;

import java.util.Map;

/**
 * データモデルクラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class PropertyConfigModel {

    /**
     * パラメータを設定します。
     * @param map
     */
    protected void setProperties(Map map) {
        this.map = map;
    }

    /**
     * 指定されたキーのプロパティハンドラ名を取得します。
     * @return String 
     */
    protected String getPropertyHandlerName(String key) {
        if ( key == null ) {
            return null;
        }
        PropertyModel prop = (PropertyModel) map.get(key);
        if ( prop == null ) {
            return null;
        }
        String name = prop.getPropertyHandlerName();
        return name;
    }

    /**
     * 指定されたキーのプロパティのパラメータを取得します。
     * @return PropertyParam[] 
     */
    protected PropertyHandlerParam[] getPropertyParams(String key) {
        if ( key == null ) {
            return null;
        }
        PropertyModel prop = (PropertyModel) map.get(key);
        if ( prop == null ) {
            return null;
        }
        PropertyHandlerParam[] params = prop.getPropertyHandlerParams();
        return params;
    }

    static final String ID = "property-config";
    static final String SERVICE_ID = "service";
    static final String EVENT_ID = "event";
    static final String DATA_ID = "data";
//    static final String SESSION_ID = "session";
//    static final String MESSAGE_ID = "message";
    static final String I18NMESSAGE_ID = "i18n_message";
    static final String LOG_ID = "log";
    static final String HANDLER_ID = "handler-class";
    
    private Map map;
}
