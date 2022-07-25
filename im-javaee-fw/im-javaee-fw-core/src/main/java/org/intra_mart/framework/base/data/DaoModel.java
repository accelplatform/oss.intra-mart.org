/*
 * DAOModel.java
 * 
 */
package org.intra_mart.framework.base.data;

/**
 * DaoModelを管理するクラスです。
 * @author INTRAMART
 * @version 1.0
 */
public class DaoModel {
	
    public static final String ID = "dao";
	 public static final String P_ID_CONNECT_NAME = "connect-name";
    public static final String P_ID_DAO_CLASS = "dao-class";
    public static final String P_ID_DAO_CONNECTOR_NAME = "connector-name";

    /**
     * コネクト名
     */
    private String connectName;

    /**
     * DAOクラス名
     */
    private String daoClass;

    /**
     * DAOに対するデータコネクタ名を取得する
     */
    private String connectorName;

    /**
     * コネクト名を設定します。
     * @param connectName
     */
    void setConnectName(String connectName) {
        this.connectName = connectName;
    }

    /**
     * コネクト名を取得します。
     * @return String コネクト名 
     */
    String getConnectName() {
        return connectName;
    }

    /**
     * daoクラスを設定します。
     * @param daoClass
     */
    void setDaoClass(String daoClass) {
        this.daoClass = daoClass;
    }

    /**
     * daoクラスを取得します。
     * @return String daoクラス
     */
    String getDaoClass() {
        return daoClass;
    }

    /**
     * コネクタ名を設定します。
     * @param connectorName
     */
    void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
    }

    /**
     * コネクタ名を取得します。
     * @return
     */
    String getConnectorName() {
        return connectorName;
    }

}
