/*
 * PropertyManager.java
 *
 * Created on 2001/11/08, 10:53
 */

package org.intra_mart.framework.system.property;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import java.util.MissingResourceException;

/**
 * システム設定の情報を管理します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class PropertyManager {

    /**
     * プロパティマネージャを指定するキー
     */
    public static final String KEY = "org.intra_mart.framework.system.property.PropertyManager";

    /**
     * デフォルトのプロパティマネージャクラス
     */
    public static final String DEFAULT_SYSTEM_MANAGER = "org.intra_mart.framework.system.property.XmlPropertyManager";

    /**
     * プロパティマネージャ
     */
    private static PropertyManager manager;

    /**
     * プロパティハンドラの集合
     */
    private Map propertyHandlers;

    /**
     * プロパティマネージャのコンストラクタです。
     * コンストラクタの明示的な使用はできません。
     */
    protected PropertyManager() {
        this.propertyHandlers = new HashMap();
    }

    /**
     * プロパティマネージャを取得します。
     * プロパティマネージャは常に１つだけ存在します。
     * プロパティマネージャが未設定の状態でこのメソッドが呼ばれた場合、以下の順番でプロパティマネージャを取得します。
     * <PRE>
     * 1.システムプロパティ{@link #KEY KEY}で設定されているプロパティマネージャ
     * 2.デフォルトのプロパティマネージャ
     * </PRE>
     *
     * @return プロパティマネージャ
     * @throws PropertyManagerException プロパティマネージャの設定時に発生する例外
     */
    public static synchronized PropertyManager getPropertyManager() throws PropertyManagerException {
        String className = null;

        // プロパティマネージャが未作成の場合、新規に作成する。
        if (manager == null) {

            // システムプロパティからプロパティマネージャのクラス名を取得
            try {
                className = System.getProperty(KEY);
            } catch (Exception e) {
                String message = null;
                try {
                    message = ResourceBundle.getBundle("org.intra_mart.framework.system.property.i18n").getString("PropertyManager.FailedToGetClass");
                } catch (MissingResourceException ex) {
                }
                throw new PropertyManagerException(message + " : System property = " + KEY, e);
            }

            // システムプロパティが未設定の場合デフォルトのプロパティマネージャのクラス名を使用
            if (className == null) {
                className = DEFAULT_SYSTEM_MANAGER;
            }

            // プロパティマネージャの生成
            try {
                manager = (PropertyManager)Class.forName(className).newInstance();
            } catch (Exception e) {
                String message = null;
                try {
                    message = ResourceBundle.getBundle("org.intra_mart.framework.system.property.i18n").getString("PropertyManager.FailedToCreateManager");
                } catch (MissingResourceException ex) {
                }
                e.printStackTrace();
                throw new PropertyManagerException(message + " : class name = " + className, e);
            }
        }

        return manager;
    }

    /**
     * キーで指定されたプロパティハンドラのクラス名を取得します。
     *
     * @param key プロパティハンドラのキー
     * @return プロパティハンドラのクラス名
     * @throws PropertyHandlerException プロパティハンドラのクラス名を取得できなかった場合に発生する例外
     */
    protected abstract String getPropertyHandlerName(String key) throws PropertyHandlerException;

    /**
     * キーで指定されたプロパティハンドラの初期化データを取得します。
     * 初期化データが存在しない場合、nullが返ります。
     *
     * @param key プロパティハンドラのキー
     * @return プロパティハンドラの初期化データ
     */
    protected abstract PropertyHandlerParam[] getPropertyHandlerParams(String key);

    /**
     * キーで指定されたプロパティハンドラを取得します。
     *
     * @return プロパティハンドラ
     * @param key プロパティハンドラのキー
     * @throws PropertyHandlerException プロパティハンドラの取得に失敗
     */
    public PropertyHandler getPropertyHandler(String key) throws PropertyHandlerException {
        PropertyHandler result;
        PropertyHandlerParam[] params;
        String className;
        String option;

        // キーに一致するプロパティハンドラを検索する。
        result = (PropertyHandler)this.propertyHandlers.get(key);

        // プロパティハンドラが未登録の場合、キーに一致するプロパティハンドラのクラスを新規登録する。
        if (result == null) {
            className = getPropertyHandlerName(key);
            try {
                result = (PropertyHandler)Class.forName(className).newInstance();
            } catch (Exception e) {
                String message = null;
                try {
                    message = ResourceBundle.getBundle("org.intra_mart.framework.system.property.i18n").getString("PropertyManager.FailedToGetPropertyHandler");
                } catch (MissingResourceException ex) {
                }
                throw new PropertyHandlerException(message + " : " + className, e);
            }

            params = getPropertyHandlerParams(key);
            result.init(params);
            propertyHandlers.put(key, result);
        }

        return result;
    }
}
