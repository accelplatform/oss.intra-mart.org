/*
 * DataConnector.java
 *
 * Created on 2001/10/29, 14:53
 */

package org.intra_mart.framework.base.data;

/**
 * データストアに接続するコネクタです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class DataConnector {

    /** データプロパティハンドラ */
    private DataPropertyHandler handler;

    /**
     * 新規のDataConnectorを生成します。
     */
    public DataConnector() {
        this.handler = null;
    }

    /**
     * データプロパティハンドラを設定します。
     *
     * @param handler データプロパティハンドラ
     */
    public void setDataPropertyHandler(DataPropertyHandler handler) {
        this.handler = handler;
    }

    /**
     * データプロパティハンドラを取得します。
     *
     * @return データプロパティハンドラ
     */
    public DataPropertyHandler getDataPropertyHandler() {
        return this.handler;
    }

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
    protected abstract Object getResource(String key, String connect, String resource) throws DataPropertyException, DataConnectException;

    /**
     * コミットします。
     *
     * @throws DataConnectException コミットに失敗
     */
    public abstract void commit() throws DataConnectException;

    /**
     * ロールバックします。
     *
     * @throws DataConnectException ロールバックに失敗
     */
    public abstract void rollback() throws DataConnectException;

    /**
     * データストアの資源を解放します。
     *
     * @throws DataConnectException 資源開放に失敗
     */
    public abstract void release() throws DataConnectException;
}
