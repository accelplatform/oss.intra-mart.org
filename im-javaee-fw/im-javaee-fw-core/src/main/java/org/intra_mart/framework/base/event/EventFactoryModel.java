/*
 * EventFactoryModel.java
 * 
 */
package org.intra_mart.framework.base.event;

/**
 * イベントリスナーファクトリー情報を管理するクラスです。
 * @author INTRAMART
 * @version 1.0
 */
public class EventFactoryModel {

    public static final String ID = "event-factory";
    public static final String P_ID_FACTORY_CLASS = "factory-class";
    public static final String P_ID_FACTORY_PARAM = "init-param";
    public static final String P_ID_PARAM_NAME = "param-name";
    public static final String P_ID_PARAM_VALUE = "param-value";

    /**
     * イベントリスナーファクトリー
     */
    private String factoryName;

    /**
     * イベントリスナーファクトリーの初期パラメータ
     */
    private EventListenerFactoryParam[] factoryParams;

    /**
     * ファクトリーの初期パラメータを設定します。
     * @param factoryParams
     */
    void setFactoryParams(EventListenerFactoryParam[] factoryParams) {
        this.factoryParams = factoryParams;
    }

    /**
     * ファクトリーの初期パラメータを取得します。
     * @return EventListenerFactoryParam[] ファクトリーの初期パラメータ
     */
    EventListenerFactoryParam[] getFactoryParams() {
        return factoryParams;
    }

	/**
	 * ファクトリ名を設定します。
	 * @param factoryName
	 */ 
    void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

	/**
	 * ファクトリ名を取得します。
	 * @return ファクトリ名
	 */
    String getFactoryName() {
        return factoryName;
    }
}
