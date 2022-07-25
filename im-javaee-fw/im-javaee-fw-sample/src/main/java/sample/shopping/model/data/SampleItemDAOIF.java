/*
 * SampleItemDAOIF.java
 *
 * Created on 2002/02/21, 15:32
 */

package sample.shopping.model.data;

import java.util.Collection;

import org.intra_mart.framework.base.data.DataAccessException;
import org.intra_mart.framework.base.data.DataConnectException;
import org.intra_mart.framework.base.data.DataPropertyException;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * b_fw_sample_itemテーブル操作用DAOのインターフェースです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public interface SampleItemDAOIF {

    /**
     * insert処理を行います。
     *
     * @param item 商品情報
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     */
    public void insert(SampleItemModelObject item)
        throws DataConnectException, DataAccessException, DataPropertyException;

    /**
     * update処理を行います。
     *
     * @param item 商品情報
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     */
    public void update(SampleItemModelObject item)
        throws DataConnectException, DataAccessException, DataPropertyException;

    /**
     * delete処理を行います。
     *
     * @param code 削除を行うレコードのコード
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     */
    public void delete(String code)
        throws DataConnectException, DataAccessException, DataPropertyException;

    /**
     * select処理(1件取得)を行います。
     *
     * @param code 取得するレコードのコード
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報
     */
    public SampleItemModelObject select(String code)
        throws DataConnectException, DataAccessException, DataPropertyException;

    /**
     * select処理(全件取得)を行います。
     *
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
    public Collection select()
        throws DataConnectException, DataAccessException, DataPropertyException;

    /**
     * select処理を行います。
     *
     * @param sort ソートするカラム名
     * @param startRow 取得するデータの開始位置
     * @param listNum 取得するデータ数
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
    public Collection select(String sort, int startRow, int listNum)
        throws DataConnectException, DataAccessException, DataPropertyException;

    /**
     * select処理(条件検索)を行います。
     *
     * @param code レコードのコード（検索条件）
     * @param name レコードの名前（検索条件）
     * @param sort ソートするカラム名
     * @param startRow データを取得する開始位置
     * @param listNum データを取得する件数
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
    public Collection select(String code, String name, String sort,int startRow,int listNum)
        throws DataConnectException, DataAccessException, DataPropertyException;

    /**
     * テーブルの件数を取得します。
     *
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
    public int getCount()
        throws DataConnectException, DataAccessException, DataPropertyException;

    /**
     * 条件に一致するレコードの件数を取得します。
     * 
     * @param code 商品コード（検索条件）
     * @param name 商品名（検索条件）
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
    public int getCount(String code, String name)
        throws DataConnectException, DataAccessException, DataPropertyException;
}
