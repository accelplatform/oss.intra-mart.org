/*
 * TextFileEventPropertyHandler.java
 *
 * Created on 2002/07/08, 14:27
 */

package org.intra_mart.framework.base.event;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyParam;

import java.io.IOException;
import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * 指定されたファイルからプロパティ情報を取得するサービスプロパティハンドラです。
 * ファイルの書式は{@link DefaultEventPropertyHandler}で示されるものと同じです。<BR>
 * ファイルが存在するディレクトリ名は{@link #PARAM_FILE_DIR}で示されるパラメータ名で指定します。<BR>
 * {@link #PARAM_DYNAMIC}で示されるパラメータにtrueを指定した場合
 * アプリケーション実行時にもプロパティの変更を動的に反映させることが可能となりますが、
 * パラメータの取得時に毎回ファイル操作を行うためパフォーマンスに悪影響を与える可能性があります。
 * このオプションは、開発時やデバッグ時のようにパラメータを頻繁に変更する必要がある場合のみtrueとし、
 * 通常はfalseに設定しておいてください。
 *
 * @author INTRAMART
 * @since 3.2
 */
public class TextFileEventPropertyHandler implements EventPropertyHandler {

    /**
     * プロパティファイルのファイルパスのパラメータ名
     */
    public static final String PARAM_FILE_DIR = "file_dir";

    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

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
	 * イベントトリガ情報のマップ
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
	 * @since 4.3
	 * 
	 * @uml.property name="postEventTriggers"
	 * @uml.associationEnd 
	 * @uml.property name="postEventTriggers" multiplicity="(0 1)" qualifier="application:java.lang.String
	 * infos:java.util.Collection"
	 */
	private Map postEventTriggers;


    /**
     * サービスプロパティのファイルがあるディレクトリ
     */
    private String propertyFileDir;

    /**
     * 再設定可能フラグ
     */
    private boolean dynamic;

    /**
     * TextFileEventPropertyHandlerを新規に生成します。
     */
    public TextFileEventPropertyHandler() {
        setBundlePrefix(null);
        setApplicationBundles(new HashMap());
        setEventTriggers(new HashMap());
        setPostEventTriggers(new HashMap());
        setPropertyFileDir(null);
        setDynamic(false);
    }

	/**
	 * リソースバンドルのプレフィックスを設定します。
	 * 
	 * @param bundlePrefix リソースバンドルのプレフィックス
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
     */
    private void setApplicationBundles(Map applicationBundles) {
        this.bundles = applicationBundles;
    }

    /**
     * アプリケーションごとのリソースバンドルの集合を取得します。
     *
     * @return アプリケーションごとのリソースバンドルの集合
     */
    private Map getApplicationBundles() {
        return this.bundles;
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws EventPropertyException リソースバンドルの取得時に例外が発生
     */
    private ResourceBundle getResourceBundle(String application)
        throws EventPropertyException {

        ResourceBundle result;

        if (isDynamic()) {
            result = createResourceBundle(application);
        } else {
            synchronized (this.bundles) {
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
     */
    private ResourceBundle createResourceBundle(String application)
        throws EventPropertyException {

        try {
            String path = getPropertyPackage( application );
            String applicationId = getApplicationID( application );
            return createPropertyResourceBundle(
                getPropertyFileDir()
                	+ File.separator
                    + path
                    + getBundlePrefix()
                    + "_"
                    + applicationId
                    + ".properties");
        } catch (Exception e) {
            throw new EventPropertyException(e.getMessage(), e);
        }
    }

    /**
     * パスで指定されたリソースバンドルを生成します。
     *
     * @param path 指定するファイルのパス
     * @return ファイルのパスに該当するリソースバンドル
     * @throws PropertyHandlerException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private PropertyResourceBundle createPropertyResourceBundle(String path)
        throws PropertyHandlerException {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        PropertyResourceBundle resultBundle = null;

        // ファイルのオープン
        try {
            fis = new FileInputStream(path);
        } catch (IOException e) {
            throw new PropertyHandlerException(e.getMessage(), e);
        }

        // バッファのオープン
        bis = new BufferedInputStream(fis);

        // バンドルの取得
        try {
            resultBundle = new PropertyResourceBundle(bis);
        } catch (IOException e) {
            throw new PropertyHandlerException(e.getMessage(), e);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
            }
        }

        return resultBundle;
    }

	/**
	 * イベントトリガ情報のマップを設定します。
	 * 
	 * @param eventTriggers イベントトリガ情報のマップ
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
	 * 
	 * @uml.property name="eventTriggers"
	 */
	private Map getEventTriggers() {
		return this.eventTriggers;
	}

	/**
	 * 後処理用のイベントトリガ情報のマップを設定します。
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
	 * 後処理用のイベントトリガ情報のマップを取得します。
	 * 
	 * @return イベントトリガ情報のマップ
	 * @since 4.3
	 * 
	 * @uml.property name="postEventTriggers"
	 */
	private Map getPostEventTriggers() {
		return this.postEventTriggers;
	}


    /** プロパティハンドラを初期化します。
     *
     * @param params 初期パラメータ
     * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
     */
    public void init(PropertyParam[] params) throws PropertyHandlerException {
        String bundleName = null;
        String fileDir = null;
        String dynamic = null;

        // パラメータのパース
        for (int i = 0; i < params.length; i++) {
            if (params[i]
                .getName()
                .equals(
                    DefaultEventPropertyHandler.DEFAULT_BUNDLE_NAME_PARAM)) {
                // リソースバンドルのファイル名の場合
                bundleName = params[i].getValue();
            } else if (params[i].getName().equals(PARAM_FILE_DIR)) {
                // ファイルディレクトリの場合
                fileDir = params[i].getValue();
            } else if (params[i].getName().equals(PARAM_DYNAMIC)) {
                // 再設定可能フラグの場合
                dynamic = params[i].getValue();
            }
        }

        // リソースバンドルプレフィックスの設定
        if (bundleName == null) {
            bundleName = DefaultEventPropertyHandler.DEFAULT_BUNDLE_NAME;
        }
        setBundlePrefix(bundleName);

        // 再設定可能フラグの設定
        Boolean dummyDynamic = new Boolean(dynamic);
        setDynamic(dummyDynamic.booleanValue());

        // プロパティファイルディレクトリの必須チェック
        if (fileDir == null) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("TextFileEventPropertyHandler.param.FileDirNotFound");
            } catch (MissingResourceException e) {
            }
            throw new PropertyHandlerException(
                message + " : " + PARAM_FILE_DIR);
        }
        this.propertyFileDir = fileDir;
    }

	/**
	 * プロパティファイルがあるディレクトリを設定します。
	 * 
	 * @param propertyFileDir  プロパティファイルがあるディレクトリ
	 * 
	 * @uml.property name="propertyFileDir"
	 */
	private void setPropertyFileDir(String propertyFileDir) {
		this.propertyFileDir = propertyFileDir;
	}

	/**
	 * プロパティファイルがあるディレクトリを取得します。
	 * 
	 * @return プロパティファイルがあるディレクトリ
	 * 
	 * @uml.property name="propertyFileDir"
	 */
	private String getPropertyFileDir() {
		return this.propertyFileDir;
	}

	/**
	 * 再設定可能／不可能を設定します。
	 * 
	 * @param dynamic true 再設定可能、false 再設定不可
	 * 
	 * @uml.property name="dynamic"
	 */
	private void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     *
     * @return true：プロパティの動的読み込みが可能、false：プロパティの動的読み込み不可
     */
    public boolean isDynamic() {
        return this.dynamic;
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
