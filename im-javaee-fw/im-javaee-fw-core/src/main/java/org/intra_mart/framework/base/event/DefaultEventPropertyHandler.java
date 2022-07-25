/*
 * DefaultEventPropertyHandler.java
 *
 * Created on 2001/11/29, 14:18
 */

package org.intra_mart.framework.base.event;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyParam;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * デフォルトのEventPropertyHandlerです。
 * <BR>プロパティファイルはアプリケーション毎に分割されます。この場合のファイル名は「<I>プレフィックス</I>_<I>アプリケーションID</I>.properties」です。
 * <BR>プロパティファイルのプレフィックスは{@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}でキーに{@link EventManager#EVENT_PROPERTY_HANDLER_KEY}を指定したときに取得されるパラメータのうち{@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 指定しなかった場合のプロパティファイルのプレフィックスは{@link #DEFAULT_BUNDLE_NAME_PARAM}で示されるものです。
 * <BR>プロパティの設定内容は以下のとおりです。
 * <TABLE border="1">
 *    <TR>
 *        <TH nowrap>キー</TH>
 *        <TH nowrap>内容</TH>
 *    </TR>
 *    <TR>
 *        <TD>event.class.<I>キー</I></TD>
 *        <TD><I>キー</I>に対応する{@link Event}のクラス名</TD>
 *    </TR>
 *    <TR>
 *        <TD>factory.class.<I>キー</I></TD>
 *        <TD><I>キー</I>に対応する{@link EventListenerFactory}のクラス名</TD>
 *    </TR>
 *    <TR>
 *        <TD>factory.param.<I>キー</I>.<I>パラメータ名</I></TD>
 *        <TD><I>キー</I>に対応する{@link EventListenerFactory}のパラメータの値</TD>
 *    </TR>
 *    <TR>
 *        <TD>trigger.class.<I>キー</I>.<I>ソート番号</I>[.pre]</TD>
 *        <TD><I>キー</I>に対応する{@link EventTrigger}のクラス名<BR>複数指定した場合、トリガは<I>ソート番号</I>の順番に処理されます。<BR>このトリガは該当するイベントリスナが処理される前に起動します。</TD>
 *    </TR>
 *    <TR>
 *        <TD>trigger.class.<I>キー</I>.<I>ソート番号</I>.post</TD>
 *        <TD><I>キー</I>に対応する{@link EventTrigger}のクラス名<BR>複数指定した場合、トリガは<I>ソート番号</I>の順番に処理されます。<BR>このトリガは該当するイベントリスナが処理された後に起動します。</TD>
 *    </TR>
 * </TABLE>
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DefaultEventPropertyHandler implements EventPropertyHandler {

    /**
     * デフォルトのリソースバンドル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "EventConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * イベントリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

	/**
	 * アプリケーションごとのイベントリソース情報が設定されているリソースバンドル
	 * 
	 * @uml.property name="bundles"
	 * @uml.associationEnd 
	 * @uml.property name="bundles" multiplicity="(0 1)" qualifier="application:java.lang.String
	 * result:java.util.ResourceBundle"
	 */
	private Map bundles;

	/**
	 * 前処理用イベントトリガ情報のマップ
	 * 
	 * @uml.property name="eventTriggers"
	 * @uml.associationEnd 
	 * @uml.property name="eventTriggers" multiplicity="(0 1)" qualifier="application:java.lang.String
	 * infos:java.util.Collection"
	 */
	private Map eventTriggers;

	/**
	 * 後処理用イベントトリガ情報のマップ
	 * 
	 * since 4.3
	 * 
	 * @uml.property name="postEventTriggers"
	 * @uml.associationEnd 
	 * @uml.property name="postEventTriggers" multiplicity="(0 1)" qualifier="application:java.lang.String
	 * infos:java.util.Collection"
	 */
	private Map postEventTriggers;


    /**
     * DefaultEventPropertyHandlerを新規に生成します。
     */
    public DefaultEventPropertyHandler() {
        setBundlePrefix(null);
        setApplicationBundles(new HashMap());
        setEventTriggers(new HashMap());
        setPostEventTriggers(new HashMap());
    }

	/**
	 * リソースバンドルのプレフィックスを設定します。
	 * 
	 * @param bundlePrefix リソースバンドルのプレフィックス
	 * @since 3.2
	 * 
	 * @uml.property name="bundlePrefix"
	 */
	private void setBundlePrefix(String bundlePrefix) {
		this.bundlePrefix = bundlePrefix;
	}

	/**
	 * リソースバンドルのプレフィックスを取得します。
	 * 
	 * @return リソースバンドルのプレフィックス
	 * @since 3.2
	 * 
	 * @uml.property name="bundlePrefix"
	 */
	private String getBundlePrefix() {
		return this.bundlePrefix;
	}


    /**
     * アプリケーションごとのリソースバンドルの集合を設定します。
     *
     * @param applicationBundles アプリケーションごとのリソースバンドルの集合
     * @since 3.2
     */
    private void setApplicationBundles(Map applicationBundles) {
        this.bundles = applicationBundles;
    }

    /**
     * アプリケーションごとのリソースバンドルの集合を取得します。
     *
     * @return アプリケーションごとのリソースバンドルの集合
     * @since 3.2
     */
    private Map getApplicationBundles() {
        return this.bundles;
    }

	/**
	 * イベントトリガ情報のマップを設定します。
	 * 
	 * @param eventTriggers イベントトリガ情報のマップ
	 * @since 3.2
	 * 
	 * @uml.property name="eventTriggers"
	 */
	private void setEventTriggers(Map eventTriggers) {
		this.eventTriggers = eventTriggers;
	}

	/**
	 * イベントトリガ情報のマップを取得します。
	 * 
	 * @return イベントトリガ情報のマップ
	 * @since 3.2
	 * 
	 * @uml.property name="eventTriggers"
	 */
	private Map getEventTriggers() {
		return this.eventTriggers;
	}

	/**
	 * イベントトリガ情報のマップを設定します。
	 * 
	 * @param eventTriggers イベントトリガ情報のマップ
	 * @since 4.3
	 * 
	 * @uml.property name="postEventTriggers"
	 */
	private void setPostEventTriggers(Map eventTriggers) {
		this.postEventTriggers = eventTriggers;
	}

	/**
	 * イベントトリガ情報のマップを取得します。
	 * 
	 * @return イベントトリガ情報のマップ
	 * @since 4.3
	 * 
	 * @uml.property name="postEventTriggers"
	 */
	private Map getPostEventTriggers() {
		return this.postEventTriggers;
	}

    /**
     * アプリケーションIDで指定されたリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws EventPropertyException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle getResourceBundle(String application)
        throws EventPropertyException {

        ResourceBundle result;

        result = (ResourceBundle)getApplicationBundles().get(application);
        if (result == null) {
            synchronized (this) {
                result =
                    (ResourceBundle)getApplicationBundles().get(application);
                if (result == null) {
                    result = createResourceBundle(application);
                    getApplicationBundles().put(application, result);
                }
            }
        }

        return result;
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws EventPropertyException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle createResourceBundle(String application)
        throws EventPropertyException {

        try {
            return  ResourceBundle.getBundle(
				getPropertyPackage( application ) + getBundlePrefix() + "_" + getApplicationID( application ));
        } catch (MissingResourceException e) {
            throw new EventPropertyException(e.getMessage(), e);
        }
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルからキーで指定されたプロパティを取得します。
     *
     * @param application アプリケーションID
     * @param key キー
     * @return 指定されたキーに該当するプロパティ
     * @throws MissingResourceException プロパティの取得時に例外が発生
     * @throws EventPropertyException プロパティの取得に失敗
     */
    private String getString(String application, String key)
        throws MissingResourceException, EventPropertyException {

        ResourceBundle bundle;
        String result;

        bundle = getResourceBundle(application);
        result = bundle.getString(key);

        return result;
    }

    /**
     * プロパティハンドラを初期化します。
     *
     * @param params 初期パラメータ
     * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
     */
    public void init(PropertyParam[] params) throws PropertyHandlerException {
        String bundleName = null;

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].getName().equals(DEFAULT_BUNDLE_NAME_PARAM)) {
                    bundleName = params[i].getValue();
                }
            }
        }
        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }

        setBundlePrefix(bundleName);
    }

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     * このクラスではこのメソッドは常にfalseを返します。
     *
     * @return 常にfalse
     * @throws EventPropertyException チェック時に例外が発生
     * @since 3.2
     */
    public boolean isDynamic() throws EventPropertyException {
        return false;
    }

    /**
     * キーに該当するイベントのクラス名を取得します。
     * 該当するイベントが存在しない場合、nullを返します。
     *
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントのクラス名
     * @throws EventPropertyException イベントのクラス名の取得に失敗
     */
    public String getEventName(String application, String key)
        throws EventPropertyException {

        return ResourceBundleEventPropertyHandlerUtil.getEventName(
            getResourceBundle(application),
            application,
            key);
    }

    /**
     * キーに該当するイベントリスナファクトリのクラス名を取得します。
     *
     * @param application アプリケーション
     * @param key イベントリスナファクトリのキー
     * @return イベントリスナファクトリのクラス名
     * @throws EventPropertyException イベントリスナファクトリのクラス名の取得に失敗
     */
    public String getEventListenerFactoryName(String application, String key)
        throws EventPropertyException {

        return ResourceBundleEventPropertyHandlerUtil
            .getEventListenerFactoryName(
            getResourceBundle(application),
            application,
            key);
    }

    /**
     * キーに該当するイベントリスナファクトリの初期パラメータを取得します。
     *
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントリスナファクトリの初期パラメータ
     * @throws EventPropertyException イベントリスナファクトリの初期パラメータの取得に失敗
     */
    public EventListenerFactoryParam[] getEventListenerFactoryParams(
        String application,
        String key)
        throws EventPropertyException {

        return ResourceBundleEventPropertyHandlerUtil
            .getEventListenerFactoryParams(
            getResourceBundle(application),
            application,
            key);
    }

    /**
     * キーに該当するイベントのイベントトリガ情報をすべて取得します。
     * <CODE>application</CODE>と<CODE>key</CODE>で定義される{@link EventTrigger}を定義された順番でソートしたCollectionとして取得します。
     *
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントトリガ情報のコレクション
     * @throws EventPropertyException イベントトリガ情報の取得に失敗
     * @see EventListener
     */
    public Collection getEventTriggerInfos(String application, String key)
        throws EventPropertyException {

        Map infoCollections = null;
        Collection infos = null;

        if (isDynamic()) {
            infos =
                ResourceBundleEventPropertyHandlerUtil.getEventTriggerInfos(
                    getResourceBundle(application),
                    application,
                    key);
        } else {
            // キーに該当するイベントトリガのコレクションを取得する
            synchronized (this.eventTriggers) {
                infoCollections = (Map)getEventTriggers().get(application);
                if (infoCollections == null) {
                    infoCollections = new HashMap();
                    getEventTriggers().put(application, infoCollections);
                }
                infos = (Collection)infoCollections.get(key);
                if (infos == null) {
                    // イベントトリガのコレクションが存在しない場合新たに取得する
                    infos =
                        ResourceBundleEventPropertyHandlerUtil
                            .getEventTriggerInfos(
                            getResourceBundle(application),
                            application,
                            key);

                    // 新規に生成されたイベントトリガ群を登録する
                    infoCollections.put(key, infos);
                }
            }
        }

        return infos;
    }

    /**
     * キーに該当するイベントのイベントトリガ情報をすべて取得します。
     * ここで取得されるイベントトリガはイベントの処理後に実行されます。
     * <CODE>application</CODE>と<CODE>key</CODE>で定義される{@link EventTrigger}を定義された順番でソートしたCollectionとして取得します。
     *
     * @param application アプリケーション
     * @param key イベントのキー
     * @return イベントトリガ情報のコレクション
     * @throws EventPropertyException イベントトリガ情報の取得に失敗
     * @see EventListener
     */
    public Collection getPostEventTriggerInfos(String application, String key)
        throws EventPropertyException {

        Map infoCollections = null;
        Collection infos = null;

        if (isDynamic()) {
            infos =
                ResourceBundleEventPropertyHandlerUtil
                    .getPostEventTriggerInfos(
                    getResourceBundle(application),
                    application,
                    key);
        } else {
            // キーに該当するイベントトリガのコレクションを取得する
            synchronized (this.eventTriggers) {
                infoCollections = (Map)getPostEventTriggers().get(application);
                if (infoCollections == null) {
                    infoCollections = new HashMap();
                    getPostEventTriggers().put(application, infoCollections);
                }
                infos = (Collection)infoCollections.get(key);
                if (infos == null) {
                    // イベントトリガのコレクションが存在しない場合新たに取得する
                    infos =
                        ResourceBundleEventPropertyHandlerUtil
                            .getPostEventTriggerInfos(
                            getResourceBundle(application),
                            application,
                            key);

                    // 新規に生成されたイベントトリガ群を登録する
                    infoCollections.put(key, infos);
                }
            }
        }

        return infos;
    }
    
    /**
	 * propertiesファイルが存在するパッケージを取得します。
	 * パッケージ化されていない場合は空文字を返却します。
	 *  
	 * @param application
	 * @return パッケージ
     * @since 2004.09.13
	 */
    private String getPropertyPackage( String application ) {
        String[] paramAry = application.split("[.]");
		StringBuffer buf = new StringBuffer();
		if ( paramAry.length > 1 ) {
			for ( int i = 0; i < paramAry.length - 1; i++ ) {
			    buf.append(paramAry[i]);
			    buf.append(File.separator);
			}
		}
		return buf.toString();
	}

    /**
	 * アプリケーションIDを取得します。
	 * 
	 * @param application
	 * @return アプリケーションID
     * @since 2004.09.13
	 */
    private String getApplicationID( String application ) {
        String[] paramAry = application.split("[.]");
	    String id = paramAry[paramAry.length - 1];
		return id;
	}
    
}
