/*
 * DBConnector.java
 *
 * Created on 2001/11/12, 18:02
 */

package org.intra_mart.framework.base.data;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * データベースに対するコネクタです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class DBConnector extends DataConnector {

    /**
     * リソース一覧
     */
    protected Map resources;

    /**
     * DBConnectorを新規に生成します。
     */
    public DBConnector() {
        super();
        this.resources = new HashMap();
    }

    /**
     * 接続先のリソースを追加します。
     *
     * @return コネクション
     * @param resource リソース
     * @param params リソースのパラメータ
     * @throws DataConnectException 接続に失敗
     */
    protected abstract Connection putResource(
        String resource,
        ResourceParam[] params)
        throws DataConnectException;

    /**
     * リソースを取得します。
     *
     * @param key キー
     * @param connect 接続情報
     * @param resource リソース名
     * @return リソース
     * @throws DataPropertyException リソースの取得に失敗
     * @throws DataConnectException 接続に失敗
     */
    protected Object getResource(String key, String connect, String resource)
        throws DataPropertyException, DataConnectException {
        Object obj = this.resources.get(resource);
        if (obj == null) {
            ResourceParam[] resourceInfo =
                getDataPropertyHandler().getResourceParams(resource);
            obj = putResource(resource, resourceInfo);
        }

        return obj;
    }

    /**
     * データベースへのコネクションを取得します。
     *
     * @param resource リソース名
     * @return データベースへのコネクション
     * @throws DataPropertyException データベースへのコネクションの取得に失敗
     * @throws DataConnectException データベースへの接続に失敗
     */
    public Connection getConnection(String resource)
        throws DataPropertyException, DataConnectException {
        return (Connection)getResource("", "", resource);
    }

    /**
     * コミットします。
     * 実際にはこのクラスではこのメソッドは何もしません。
     *
     * @throws DataConnectException コミットに失敗
     */
    public void commit() throws DataConnectException {
    }

    /**
     * ロールバックします。
     * 実際にはこのクラスではこのメソッドは何もしません。
     *
     * @throws DataConnectException ロールバックに失敗
     */
    public void rollback() throws DataConnectException {
    }

    /**
     * データストアの資源を解放します。
     *
     * @throws DataConnectException 資源開放に失敗
     * 実際にはこのクラスではこのメソッドは何もしません。
     */
    public void release() throws DataConnectException {
    }
}
