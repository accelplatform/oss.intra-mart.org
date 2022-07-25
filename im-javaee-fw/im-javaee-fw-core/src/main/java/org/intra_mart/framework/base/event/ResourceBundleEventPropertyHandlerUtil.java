/*
 * ResourceBundleEventPropertyHandlerUtil.java
 *
 * Created on 2002/08/13, 13:24
 */

package org.intra_mart.framework.base.event;

import java.util.Collection;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.Vector;

import java.util.MissingResourceException;

/**
 * 指定されたリソースバンドルからイベントプロパティを取得するユーティリティです。
 *
 * @author INTRAMART
 * @since 3.2
 */
class ResourceBundleEventPropertyHandlerUtil {

    /**
     * キーに該当するイベントのクラス名を取得します。
     * 該当するイベントが存在しない場合、nullを返します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントのクラス名
     * @throws EventPropertyException イベントのクラス名の取得に失敗
     */
    public static String getEventName(
        ResourceBundle applicationBundle,
        String application,
        String key)
        throws EventPropertyException {

        String name = null;

        try {
            name = applicationBundle.getString("event.class." + key);
        } catch (MissingResourceException e) {
            name = null;
        }

        return name;
    }

    /**
     * キーに該当するイベントリスナファクトリのクラス名を取得します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーション
     * @param key イベントリスナファクトリのキー
     * @return イベントリスナファクトリのクラス名
     * @throws EventPropertyException イベントリスナファクトリのクラス名の取得に失敗
     */
    public static String getEventListenerFactoryName(
        ResourceBundle applicationBundle,
        String application,
        String key)
        throws EventPropertyException {

        String name;

        try {
            name = applicationBundle.getString("factory.class." + key);
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("ResourceBundleEventPropertyHandlerUtil.FailedToGetFactory");
            } catch (MissingResourceException ex) {
            }
            throw new EventPropertyException(
                message + " : application = " + application + ", key = " + key,
                e);
        }
        if (name == null || name.trim().equals("")) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("ResourceBundleEventPropertyHandlerUtil.FacotryClassNotDeclared");
            } catch (MissingResourceException e) {
            }
            throw new EventPropertyException(
                message + " : application = " + application + ", key = " + key);
        }

        return name;
    }

    /**
     * キーに該当するイベントリスナファクトリの初期パラメータを取得します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントリスナファクトリの初期パラメータ
     * @throws EventPropertyException イベントリスナファクトリの初期パラメータの取得に失敗
     */
    public static EventListenerFactoryParam[] getEventListenerFactoryParams(
        ResourceBundle applicationBundle,
        String application,
        String key)
        throws EventPropertyException {

        Vector params = new Vector();
        String prefix = "factory.param." + key + ".";
        String propertyName;
        EventListenerFactoryParam[] result;
        Enumeration enu;
        EventListenerFactoryParam param;

        enu = applicationBundle.getKeys();
        while (enu.hasMoreElements()) {
            propertyName = (String)enu.nextElement();
            if (propertyName.startsWith(prefix)) {
                param = new EventListenerFactoryParam();
                param.setName(propertyName.substring(prefix.length()));
                try {
                    param.setValue(applicationBundle.getString(propertyName));
                } catch (MissingResourceException e) {
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("ResourceBundleEventPropertyHandlerUtil.FailedToGetFactoryParameter");
                    } catch (MissingResourceException ex) {
                    }
                    throw new EventPropertyException(
                        message
                            + " : application = "
                            + application
                            + ", key = "
                            + key,
                        e);
                }
                params.add(param);
            }
        }
        result = new EventListenerFactoryParam[params.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (EventListenerFactoryParam)params.elementAt(i);
        }

        return result;
    }

    /**
     * キーに該当するイベントのイベントトリガ情報をすべて取得します。
     * <CODE>application</CODE>と<CODE>key</CODE>で定義される{@link EventTrigger}を定義された順番でソートしたCollectionとして取得します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントトリガ情報のコレクション
     * @throws EventPropertyException イベントトリガ情報の取得に失敗
     * @see EventListener
     */
    public static Collection getEventTriggerInfos(
        ResourceBundle applicationBundle,
        String application,
        String key)
        throws EventPropertyException {

        Enumeration enu = null;
        EventTriggerInfo info = null;
        TreeMap infos = new TreeMap();
        String prefix = "trigger.class." + key + ".";
        String resourceKey = null;
        String name = null;
        int num = 0;

        // リソースからすべてのキーを取得する
        enu = applicationBundle.getKeys();

        // イベントトリガのキーに該当するプロパティすべてに対して繰り返し処理をする
        while (enu.hasMoreElements()) {
            resourceKey = (String)enu.nextElement();
            if (resourceKey.startsWith(prefix)) {
                int period = resourceKey.indexOf(".", prefix.length() + 1);

                // ソート番号を取得する
                if (period != -1) {
                    if (resourceKey.substring(period).equals(".pre")) {
                        num =
                            Integer.parseInt(
                                resourceKey.substring(
                                    prefix.length(),
                                    resourceKey.length() - ".pre".length()));
                    } else {
                        continue;
                    }
                } else {
                    num =
                        Integer.parseInt(
                            resourceKey.substring(prefix.length()));
                }
                info = new EventTriggerInfo();

                // ソート番号を設定する
                info.setNumber(num);

                // イベントトリガのクラス名を設定する
                try {
                    name = applicationBundle.getString(resourceKey);
                } catch (MissingResourceException e) {
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("ResourceBundleEventPropertyHandlerUtil.FailedToGetTrigger");
                    } catch (MissingResourceException ex) {
                    }
                    throw new EventPropertyException(
                        message
                            + " : application = "
                            + application
                            + ", key = "
                            + key,
                        e);
                }
                if (name == null || name.equals("")) {
                    // イベントトリガ名を取得できない場合例外をthrow
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("ResourceBundleEventPropertyHandlerUtil.TriggerNotDeclared");
                    } catch (MissingResourceException ex) {
                    }
                    throw new EventPropertyException(
                        message
                            + " : application = "
                            + application
                            + ", key = "
                            + key);
                }
                info.setName(name);

                // イベントトリガ情報を追加する
                infos.put(new Integer(num), info);
            }
        }

        return infos.values();
    }

    /**
     * キーに該当するイベントのイベントトリガ情報をすべて取得します。
     * <CODE>application</CODE>と<CODE>key</CODE>で定義される{@link EventTrigger}を定義された順番でソートしたCollectionとして取得します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントトリガ情報のコレクション
     * @throws EventPropertyException イベントトリガ情報の取得に失敗
     * @see EventListener
     */
    public static Collection getPostEventTriggerInfos(
        ResourceBundle applicationBundle,
        String application,
        String key)
        throws EventPropertyException {

        Enumeration enu = null;
        EventTriggerInfo info = null;
        TreeMap infos = new TreeMap();
        String prefix = "trigger.class." + key + ".";
        String resourceKey = null;
        String name = null;
        int num = 0;

        // リソースからすべてのキーを取得する
        enu = applicationBundle.getKeys();

        // イベントトリガのキーに該当するプロパティすべてに対して繰り返し処理をする
        while (enu.hasMoreElements()) {
            resourceKey = (String)enu.nextElement();
            if (resourceKey.startsWith(prefix)) {
                int period = resourceKey.indexOf(".", prefix.length() + 1);

                // ソート番号を取得する
                if (period != -1) {
                    if (resourceKey.substring(period).equals(".post")) {
                        num =
                            Integer.parseInt(
                                resourceKey.substring(
                                    prefix.length(),
                                    resourceKey.length() - ".post".length()));
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                info = new EventTriggerInfo();

                // ソート番号を設定する
                info.setNumber(num);

                // イベントトリガのクラス名を設定する
                try {
                    name = applicationBundle.getString(resourceKey);
                } catch (MissingResourceException e) {
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("ResourceBundleEventPropertyHandlerUtil.FailedToGetTrigger");
                    } catch (MissingResourceException ex) {
                    }
                    throw new EventPropertyException(
                        message
                            + " : application = "
                            + application
                            + ", key = "
                            + key,
                        e);
                }
                if (name == null || name.equals("")) {
                    // イベントトリガ名を取得できない場合例外をthrow
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("ResourceBundleEventPropertyHandlerUtil.TriggerNotDeclared");
                    } catch (MissingResourceException ex) {
                    }
                    throw new EventPropertyException(
                        message
                            + " : application = "
                            + application
                            + ", key = "
                            + key);
                }
                info.setName(name);

                // イベントトリガ情報を追加する
                infos.put(new Integer(num), info);
            }
        }

        return infos.values();
    }
}
