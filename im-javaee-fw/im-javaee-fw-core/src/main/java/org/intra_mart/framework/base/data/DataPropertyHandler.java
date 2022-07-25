/*
 * DataPropertyHandler.java
 *
 * Created on 2001/10/29, 14:36
 */

package org.intra_mart.framework.base.data;

import org.intra_mart.framework.system.property.PropertyHandler;

/**
 * DAOの設定情報に接続するクラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface DataPropertyHandler extends PropertyHandler {

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     *
     * @return true：プロパティの動的読み込みが可能、false：プロパティの動的読み込み不可
     * @throws DataPropertyException チェック時に例外が発生
     * @since 3.2
     */
    public boolean isDynamic() throws DataPropertyException;

    /**
     * DAOのクラス名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたDAOのクラス名を取得します。
     *
     * @param application アプリケーションID
     * @param key DAOのキー
     * @param connect 接続情報
     * @return DAOのクラス名
     * @throws DataPropertyException DAOのクラス名の取得に失敗
     */
    public String getDAOName(String application, String key, String connect) throws DataPropertyException;

    /**
     * DAOに対するデータコネクタ名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたDAOのデータコネクタ名を取得します。
     * 対応するデータコネクタ名が指定されていない場合、nullが返ります。
     *
     * @param application アプリケーションID
     * @param key DAOのキー
     * @param connect 接続情報
     * @return データコネクタの名前
     * @throws DataPropertyException データコネクタ名の取得時に例外が発生
     */
    public String getConnectorName(String application, String key, String connect) throws DataPropertyException;

    /**
     * データコネクタのクラス名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたデータコネクタのクラス名を取得します。
     *
     * @param connectorName データコネクタ名
     * @return データコネクタのクラス名
     * @throws DataPropertyException データコネクタのクラス名の取得に失敗
     */
    public String getConnectorClassName(String connectorName) throws DataPropertyException;

    /**
     * データコネクタのリソース名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたデータコネクタのリソース名を取得します。
     * 対応するリソース名がない場合、nullを返します。
     *
     * @param connectorName データコネクタ名
     * @return データコネクタのリソース名
     * @throws DataPropertyException データコネクタのリソース名の取得時に例外が発生
     */
    public String getConnectorResource(String connectorName) throws DataPropertyException;

    /**
     * リソースのパラメータを取得します。
     * <CODE>name</CODE>で指定されたリソースのパラメータを取得します。
     *
     * @param name リソース名
     * @return リソースのパラメータ
     * @throws DataPropertyException リソースのパラメータの取得時に例外が発生
     */
    public ResourceParam[] getResourceParams(String name) throws DataPropertyException;
}
