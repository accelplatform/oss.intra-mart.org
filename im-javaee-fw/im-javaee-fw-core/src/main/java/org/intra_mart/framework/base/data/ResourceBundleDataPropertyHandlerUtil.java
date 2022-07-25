/*
 * ResourceBundleDataPropertyHandlerUtil.java
 *
 * Created on 2002/08/13, 10:07
 */

package org.intra_mart.framework.base.data;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import java.util.MissingResourceException;

/**
 * 指定されたリソースバンドルからデータプロパティを取得するユーティリティです。
 *
 * @author INTRAMART
 * @since 3.2
 */
class ResourceBundleDataPropertyHandlerUtil {

    /**
     * DAOのクラス名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたDAOのクラス名を取得します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param key DAOのキー
     * @param connect 接続情報
     * @return DAOのクラス名
     * @throws DataPropertyException DAOのクラス名の取得に失敗
     */
    public static String getDAOName(ResourceBundle applicationBundle, String application, String key, String connect) throws DataPropertyException {
        String name;

        try {
            name = applicationBundle.getString("dao.class." + key + "." + connect);
        } catch (MissingResourceException e) {
            try {
                name = applicationBundle.getString("dao.class." + key);
            } catch (MissingResourceException ex) {
                String message = null;
                try {
                    ResourceBundle messageBundle = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n");
                    message = messageBundle.getString("ResourceBundleDataPropertyHandlerUtil.param.DAOClassNotDeclared");
                } catch (MissingResourceException exc) {
                }
                throw new DataPropertyException(message + " : application = " + application + ", key = " + key + ", connect = " + connect, ex);
            }
        }

        return name;
    }

    /**
     * DAOに対するデータコネクタ名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたDAOのデータコネクタ名を取得します。
     * 対応するデータコネクタ名が指定されていない場合、nullが返ります。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param key DAOのキー
     * @param connect 接続情報
     * @return データコネクタの名前
     * @throws DataPropertyException データコネクタ名の取得時に例外が発生
     */
    public static String getConnectorName(ResourceBundle applicationBundle, String application, String key, String connect) throws DataPropertyException {
        String name;

        try {
            name = applicationBundle.getString("dao.connector." + key + "." + connect);
        } catch (MissingResourceException e) {
            try {
                name = applicationBundle.getString("dao.connector." + key);
            } catch (MissingResourceException ex) {
                try {
                    name = applicationBundle.getString("dao.connector");
                } catch (MissingResourceException exc) {
                    name = null;
                }
            }
        }

        return name;
    }

    /**
     * データコネクタのクラス名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたデータコネクタのクラス名を取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param connectorName データコネクタ名
     * @return データコネクタのクラス名
     * @throws DataPropertyException データコネクタのクラス名の取得に失敗
     */
    public static String getConnectorClassName(ResourceBundle commonBundle, String connectorName) throws DataPropertyException {
        String name;

        try {
            name = commonBundle.getString("connector.class." + connectorName);
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("ResourceBundleDataPropertyHandlerUtil.param.ConnectorClassNotDeclared");
            } catch (MissingResourceException ex) {
            }
            throw new DataPropertyException(message + " : Connector name = " + connectorName, e);
        }

        return name;
    }

    /**
     * データコネクタのリソース名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたデータコネクタのリソース名を取得します。
     * 対応するリソース名がない場合、nullを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param connectorName データコネクタ名
     * @return データコネクタのリソース名
     * @throws DataPropertyException データコネクタのリソース名の取得時に例外が発生
     */
    public static String getConnectorResource(ResourceBundle commonBundle, String connectorName) throws DataPropertyException {
        String name;

        try {
            name = commonBundle.getString("connector.resource." + connectorName);
        } catch (MissingResourceException ex) {
            name = null;
        }

        return name;
    }

    /**
     * リソースのパラメータを取得します。
     * <CODE>name</CODE>で指定されたリソースのパラメータを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param name リソース名
     * @return リソースのパラメータ
     * @throws DataPropertyException リソースのパラメータの取得時に例外が発生
     */
    public static ResourceParam[] getResourceParams(ResourceBundle commonBundle, String name) throws DataPropertyException {
        ResourceParam[] result;
        String prefix = "resource.param." + name + ".";
        Enumeration keys = commonBundle.getKeys();
        String key;
        String paramName;
        String value;
        ResourceParam param;
        Vector params = new Vector();

        while (keys.hasMoreElements()) {
            key = (String)keys.nextElement();
            if (key.startsWith(prefix)) {
                paramName = key.substring(prefix.length());
                try {
                    value = commonBundle.getString(key);
                } catch (MissingResourceException e) {
                    String message = null;
                    try {
                        message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("ResourceBundleDataPropertyHandlerUtil.param.ResourceDetailNotDeclared");
                    } catch (MissingResourceException ex) {
                    }
                    throw new DataPropertyException(message + " : resource name = " + name, e);
                }
                param = new ResourceParam();
                param.setName(paramName);
                param.setValue(value);
                params.add(param);
            }
        }

        result = new ResourceParam[params.size()];
        for (int i = 0; i < params.size(); i++) {
            result[i] = (ResourceParam)params.get(i);
        }

        return result;
    }
}
