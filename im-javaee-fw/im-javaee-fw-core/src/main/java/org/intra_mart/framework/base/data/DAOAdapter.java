/*
 * DAOAdapter.java
 *
 * Created on 2002/08/23, 19:30
 */

package org.intra_mart.framework.base.data;

/**
 * DAOのすべてのメソッドを何もしないように実装したクラスです。
 * データリソースへの接続を必要としない場合や、
 * 開発時のスタブ用のDAOとして一時的に使うDAOを作るときなどは
 * このクラスを継承すると便利です。
 *
 * @author INTRAMART
 * @since 3.2
 */
public class DAOAdapter implements DAO {

    /**
     * DAOAdapterを新規に生成します。
     */
    public DAOAdapter() {
    }

    /**
     * データリソースに接続する時の情報を設定します。
     * このクラスではkのメソッドは何もしません。
     *
     * @param connector データコネクタ
     * @param resource リソース
     * @param key キー
     * @param connect 接続情報
     * @throws DataConnectException 接続に失敗
     */
    public void setConnectInfo(DataConnector connector, String resource, String key, String connect) throws DataConnectException {
    }
}
