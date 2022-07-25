/*
 * DefaultPropertyManager.java
 *
 * Created on 2001/11/08, 11:17
 */

package org.intra_mart.framework.system.property;

import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * デフォルトのプロパティマネージャです。
 * リソースバンドルで設定されたプロパティ情報を管理します。
 * プロパティ情報のリソースバンドルは以下の順番で決定されます。
 * <PRE>
 * 1.システムプロパティ{@link #KEY}で設定されているリソースバンドル
 * 2.デフォルトのプロパティマネージャ{@link #DEFAULT_BUNDLE_NAME}で設定されているリソースバンドル
 * </PRE>
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DefaultPropertyManager extends PropertyManager {

    /**
     * プロパティマネージャを指定するキー
     */
    public static final String KEY = "org.intra_mart.framework.system.property.DefaultPropertyManager";

    /**
     * デフォルトのリソースバンドル名
     */
    public static final String DEFAULT_BUNDLE_NAME = "/PropertyConfig.properties";

    /**
     * システム情報が設定されているリソースバンドル
     */
    private ResourceBundle bundle;

    /**
     * コンストラクタです。
     *
     * @throws PropertyManagerException プロパティマネージャの設定に失敗
     */
    public DefaultPropertyManager() throws PropertyManagerException {
        super();

        String bundleName = System.getProperty(KEY, DEFAULT_BUNDLE_NAME);
        try {
            this.bundle = new PropertyResourceBundle(this.getClass().getResourceAsStream(bundleName));
        } catch (Exception e) {
            String message = null;
            try {
                message = java.util.ResourceBundle.getBundle("org.intra_mart.framework.system.property.i18n").getString("DefaultPropertyManager.FailedToCreateManager");
            } catch (MissingResourceException ex) {
            }
            throw new PropertyManagerException(message + " : resource bundle = " + bundleName, e);
        }
    }

    /**
     * キーで指定されたプロパティハンドラのクラス名を取得します。
     *
     * @return プロパティハンドラのクラス名
     * @param key プロパティハンドラのキー
     * @throws PropertyHandlerException クラス名の取得に失敗
     */
    protected String getPropertyHandlerName(String key) throws PropertyHandlerException {
        String keyName = key + ".class";
        String result;

        try {
            result = this.bundle.getString(keyName);
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message = java.util.ResourceBundle.getBundle("org.intra_mart.framework.system.property.i18n").getString("DefaultPropertyManager.NoSuchKey");
            } catch (MissingResourceException ex) {
            }
            throw new PropertyHandlerException(message + " : key = " + keyName, e);
        }

        return result;
    }

    /**
     * キーで指定されたプロパティハンドラの初期化データを取得します。
     * 初期化データが存在しない場合、nullが返ります。
     *
     * @param key プロパティハンドラのキー
     * @return プロパティハンドラの初期化データ
     */
    protected PropertyHandlerParam[] getPropertyHandlerParams(String key) {
        Enumeration keys = this.bundle.getKeys();
        String prefix = key + ".param.";
        String currentKey;
        String name;
        String value;
        PropertyHandlerParam param;
        Vector params = new Vector();
        PropertyHandlerParam[] result;

        if (keys != null) {
            while (keys.hasMoreElements()) {
                currentKey = (String)keys.nextElement();
                if (currentKey.startsWith(prefix)) {
                    param = new PropertyHandlerParam();
                    param.setName(currentKey.substring(prefix.length()));
                    try {
                        param.setValue((String)this.bundle.getString(currentKey));
                    } catch (MissingResourceException e) {
                        param.setValue("");
                    }
                    params.add(param);
                }
            }
        }

        result = new PropertyHandlerParam[params.size()];
        for (int i = 0; i < params.size(); i++) {
            result[i] = (PropertyHandlerParam)params.elementAt(i);
        }

        return result;
    }
}
