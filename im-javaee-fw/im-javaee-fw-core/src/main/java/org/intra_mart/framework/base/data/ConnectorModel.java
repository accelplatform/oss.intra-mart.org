/*
 * ConnectorModel.java
 * 
 */
package org.intra_mart.framework.base.data;

/**
 * コネクタ情報を管理するクラスです。
 * @author INTRAMART
 * @version 1.0
 */
public class ConnectorModel {
    public static final String ID = "connector";
    public static final String P_ID_CONNECTOR_NAME = "connector-name";
    public static final String P_ID_CONNECTOR_CLASS = "connector-class";
    public static final String P_ID_RESOURCE_NAME = "resource-name";
    /**
     * コネクタ名
     */
    private String connectorName;

    /**
     * コネクタクラス
     */
    private String connectorClassName;

    /**
     * リソース名
     */
    private String connectorResource;

    /**
     * コネクタ名を取得します。
     * @return String コネクタ名 
     */
    String getConnectorName() {
        return this.connectorName;
    }

    /**
     * コネクタ名を設定します。
     * @param connectorName
     */
    void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
    }

    /**
     * コネクタクラス名を設定します。
     * @param connectorClassName
     */
    void setConnectorClassName(String connectorClassName) {
        this.connectorClassName = connectorClassName;
    }

    /**
     * コネクタクラス名を取得します。
     * @return String コネクタクラス名 
     */
    String getConnectorClassName() {
        return this.connectorClassName;
    }

    /**
     * リソース名を取得します。
     * @return String リソース名 
     */
    String getConnectorResource() {
        return this.connectorResource;
    }

	/**
     * リソース名を設定します。
	 * @param connectorResource
	 */
    void setConnectorResource(String connectorResource) {
        this.connectorResource = connectorResource;
    }
}
