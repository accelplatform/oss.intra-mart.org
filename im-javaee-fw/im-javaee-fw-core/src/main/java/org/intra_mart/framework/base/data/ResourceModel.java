/*
 * ResorceModel.java
 * 
 * Created on 2004/09/11 ,13:38:18
 */
package org.intra_mart.framework.base.data;

/**
 * Resource情報を管理するクラスです。
 * @author INTRAMART
 * @version 1.0
 */
public class ResourceModel {
	public static final String ID = "resource";
	public static final String P_ID_RESOURCE_NAME = "resource-name";
	public static final String P_ID_RESOURCE_PARAM = "init-param";
	public static final String P_ID_PARAM_NAME = "param-name";
	public static final String P_ID_PARAM_VALUE = "param-value";
	
	
	/**
	 * リソース名
	 */
	private String connectorResource;
	
	private ResourceParam[] params;

	/**
	 * リソース名を取得します。
	 * @return リソース名
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

	/**
	 * パラメータ一覧を設定します。
	 * @param params
	 */
    void setParams(ResourceParam[] params) {
        this.params = params;
    }

	/**
	 * パラメータ一覧を取得します。
	 * @return パラメータ一覧
	 */
    ResourceParam[] getParams() {
        return params;
    }
}
