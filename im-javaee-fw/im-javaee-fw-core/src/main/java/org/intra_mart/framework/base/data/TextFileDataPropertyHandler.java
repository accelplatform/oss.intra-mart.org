/*
 * TextFileDataPropertyHandler.java
 *
 * Created on 2002/07/08, 15:28
 */

package org.intra_mart.framework.base.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
 * ファイルの書式は{@link DefaultDataPropertyHandler}で示されるものと同じです。<BR>
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
public class TextFileDataPropertyHandler implements DataPropertyHandler {

    /**
     * プロパティファイルのファイルパスのパラメータ名
     */
    public static final String PARAM_FILE_DIR = "file_dir";

    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

    /**
     * サービスプロパティのファイルがあるディレクトリ
     */    
    private String propertyFileDir;

    /**
     * データリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

    /**
     * 共通のサービスリソース情報が設定されているリソースバンドル
     */
    private ResourceBundle commonBundle;

	/**
	 * アプリケーションごとのサービスリソース情報が設定されているリソースバンドル
	 * 
	 * @uml.property name="bundles"
	 * @uml.associationEnd 
	 * @uml.property name="bundles" multiplicity="(0 1)" qualifier="application:java.lang.String
	 * result:java.util.ResourceBundle"
	 */
	private Map bundles;


    /**
     * 再設定可能フラグ
     */
    private boolean dynamic;

    /**
     * TextFileDataPropertyHandlerを新規に生成します。
     */
    public TextFileDataPropertyHandler() {
        setApplicationBundles(new HashMap());
        setCommonBundle(null);
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
	 * 共通のリソースバンドルを設定します。
	 * 
	 * @param commonBundle 共通のリソースバンドル
	 * 
	 * @uml.property name="commonBundle"
	 */
	private void setCommonBundle(ResourceBundle commonBundle) {
		this.commonBundle = commonBundle;
	}

	/**
	 * 共通のリソースバンドルを取得します。
	 * 
	 * @return 共通のリソースバンドル
	 * @throws DataPropertyException 共通のリソースバンドル取得時に例外が発生
	 * 
	 * @uml.property name="commonBundle"
	 */
	private ResourceBundle getCommonBundle() throws DataPropertyException {
		if (isDynamic()) {
			try {
				return createCommonBundle();
			} catch (Exception e) {
				throw new DataPropertyException(e.getMessage(), e);
			}
		} else {
			return this.commonBundle;
		}
	}


    /**
     * 共通のリソースバンドルを生成します。
     *
     * @return 共通のリソースバンドル
     * @throws 共通のリソースバンドルの生成に失敗
     */
    private PropertyResourceBundle createCommonBundle() throws PropertyHandlerException {
        try {
            return createPropertyResourceBundle(getPropertyFileDir() + File.separator + getBundlePrefix() + ".properties");
        } catch (PropertyHandlerException e) {
            throw e;
        } catch (Exception e) {
            throw new PropertyHandlerException(e.getMessage(), e);
        }
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

    /** アプリケーションIDで指定されたリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws DataPropertyException リソースバンドルの取得時に例外が発生
     */
    private ResourceBundle getResourceBundle(String application) throws DataPropertyException {
        if (isDynamic()) {
            return createResourceBundle(application);
        } else {
            ResourceBundle result = null;
            synchronized (this.bundles) {
                result = (ResourceBundle)getApplicationBundles().get(application);
                if (result == null) {
                    result = createResourceBundle(application);
                    getApplicationBundles().put(application, result);
                }
            }
            return result;
        }
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws DataPropertyException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle createResourceBundle(String application) throws DataPropertyException {
        try {
            return createPropertyResourceBundle(getPropertyFileDir() + File.separator + getPropertyPackage( application ) + getBundlePrefix() + "_" + getApplicationID( application ) + ".properties");
        } catch (Exception e) {
            throw new DataPropertyException(e.getMessage(), e);
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
    private PropertyResourceBundle createPropertyResourceBundle(String path) throws PropertyHandlerException {
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
            if (params[i].getName().equals(DefaultDataPropertyHandler.DEFAULT_BUNDLE_NAME_PARAM)) {
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
            bundleName = DefaultDataPropertyHandler.DEFAULT_BUNDLE_NAME;
        }
        setBundlePrefix(bundleName);

        // 再設定可能フラグの設定
        Boolean dummyDynamic = new Boolean(dynamic);
        setDynamic(dummyDynamic.booleanValue());

        // プロパティファイルディレクトリの必須チェック
        if (fileDir == null) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("TextFileDataPropertyHandler.param.FileDirNotFound");
            } catch (MissingResourceException e) {
            }
            throw new PropertyHandlerException(message + " : " + PARAM_FILE_DIR);
        }
        this.propertyFileDir = fileDir;

        // 再設定不可の場合プロパティハンドラを設定
        if (!isDynamic()) {
            try {
                setCommonBundle(new PropertyResourceBundle(new BufferedInputStream(new FileInputStream(getPropertyFileDir() + File.separator + getBundlePrefix() + ".properties"))));
            } catch (Exception e) {
                throw new PropertyHandlerException(e.getMessage(), e);
            }
        }
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
     * DAOのクラス名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたDAOのクラス名を取得します。
     *
     * @param application アプリケーションID
     * @param key DAOのキー
     * @param connect 接続情報
     * @return DAOのクラス名
     * @throws DataPropertyException DAOのクラス名の取得に失敗
     */
    public String getDAOName(String application, String key, String connect) throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getDAOName(getResourceBundle(application), application, key, connect);
    }

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
    public String getConnectorName(String application, String key, String connect) throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getConnectorName(getResourceBundle(application), application, key, connect);
    }

    /**
     * データコネクタのクラス名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたデータコネクタのクラス名を取得します。
     *
     * @param connectorName データコネクタ名
     * @return データコネクタのクラス名
     * @throws DataPropertyException データコネクタのクラス名の取得に失敗
     */
    public String getConnectorClassName(String connectorName) throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getConnectorClassName(getCommonBundle(), connectorName);
    }

    /**
     * データコネクタのリソース名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたデータコネクタのリソース名を取得します。
     * 対応するリソース名がない場合、nullを返します。
     *
     * @param connectorName データコネクタ名
     * @return データコネクタのリソース名
     * @throws DataPropertyException データコネクタのリソース名の取得時に例外が発生
     */
    public String getConnectorResource(String connectorName) throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getConnectorResource(getCommonBundle(), connectorName);
    }

    /**
     * リソースのパラメータを取得します。
     * nameで指定されたリソースのパラメータを取得します。
     *
     * @param name リソース名
     * @return リソースのパラメータ
     * @throws DataPropertyException リソースのパラメータの取得時に例外が発生
     */
    public ResourceParam[] getResourceParams(String name) throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getResourceParams(getCommonBundle(), name);
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
