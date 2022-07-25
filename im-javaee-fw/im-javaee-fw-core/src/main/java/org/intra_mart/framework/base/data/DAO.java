/*
 * DAO.java
 *
 * Created on 2001/10/29, 14:46
 */

package org.intra_mart.framework.base.data;

/**
 * データリソースにアクセスするインタフェースです。
 * すべてのDAOはこのインタフェースを実装する必要があります。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface DAO {

    /**
     * データリソースに接続する時の情報を設定します。
     *
     * @param connector データコネクタ
     * @param resource リソース
     * @param key キー
     * @param connect 接続情報
     * @throws DataConnectException 接続に失敗
     */
    public void setConnectInfo(DataConnector connector, String resource, String key, String connect) throws DataConnectException;
}
