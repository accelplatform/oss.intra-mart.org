/*
 * DefaultDataPropertyHandler.java
 *
 * Created on 2001/10/29, 19:15
 */

package org.intra_mart.framework.base.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyParam;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * デフォルトのデータプロパティハンドラです。
 * <BR>プロパティファイルはアプリケーション毎に分割されます。この場合のファイル名は「<I>プレフィックス</I>_<I>アプリケーションID</I>.properties」です。また、アプリケーションに依存しない「<I>プレフィックス</I>.properties」があります。
 * <BR>プロパティファイルのプレフィックスは{@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}でキーに{@link DataManager#DATA_PROPERTY_HANDLER_KEY}を指定したときに取得されるパラメータのうち{@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 指定しなかった場合のプロパティファイルのプレフィックスは{@link #DEFAULT_BUNDLE_NAME_PARAM}で示されるものです。
 * <BR>プロパティの検索順は、アプリケーションごとのプロパティファイル「<I>プレフィックス</I>_<I>アプリケーションID</I>.properties」が優先されます。該当するプロパティが見つからない場合、引き続いて「<I>プレフィックス</I>.properties」を検索します。
 * <BR>プロパティの設定内容は以下のとおりです。
 * <TABLE border="1">
 *    <TR>
 *        <TH nowrap>キー</TH>
 *        <TH nowrap>内容</TH>
 *        <TH nowrap>共通</TH>
 *        <TH nowrap>アプリケーション毎</TH>
 *    </TR>
 *    <TR>
 *        <TD>dao.class.<I>キー</I>.<I>接続名</I></TD>
 *        <TD><I>キー</I>と<I>接続名</I>に対応するDAOのクラス名</TD>
 *        <TD align="center">×</TD>
 *        <TD align="center">○</TD>
 *    </TR>
 *    <TR>
 *        <TD>dao.class.<I>キー</I></TD>
 *        <TD><I>キー</I>に対応するデフォルトのDAOのクラス名</TD>
 *        <TD align="center">×</TD>
 *        <TD align="center">○</TD>
 *    </TR>
 *    <TR>
 *        <TD>dao.connector.<I>キー</I>.<I>接続名</I></TD>
 *        <TD><I>キー</I>と<I>接続名</I>に対応するDAOのデータコネクタ名</TD>
 *        <TD align="center">×</TD>
 *        <TD align="center">○</TD>
 *    </TR>
 *    <TR>
 *        <TD>dao.connector.<I>キー</I></TD>
 *        <TD><I>キー</I>に対応するデフォルトのDAOのデータコネクタ名</TD>
 *        <TD align="center">×</TD>
 *        <TD align="center">○</TD>
 *    </TR>
 *    <TR>
 *        <TD>connector.class.<I>データコネクタ名</I></TD>
 *        <TD><I>データコネクタ名</I>に対応するデータコネクタのクラス名</TD>
 *        <TD align="center">○</TD>
 *        <TD align="center">×</TD>
 *    </TR>
 *    <TR>
 *        <TD>connector.resource.<I>データコネクタ名</I></TD>
 *        <TD><I>データコネクタ名</I>に対応するデータコネクタのリソース名</TD>
 *        <TD align="center">○</TD>
 *        <TD align="center">×</TD>
 *    </TR>
 *    <TR>
 *        <TD>resource.param.<I>リソース名</I>.<I>パラメータ名</I></TD>
 *        <TD><I>リソース名</I>に対応するリソースのパラメータの値</TD>
 *        <TD align="center">○</TD>
 *        <TD align="center">×</TD>
 *    </TR>
 * </TABLE>
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DefaultDataPropertyHandler implements DataPropertyHandler {

    /**
     * デフォルトのリソースバンドル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "DataConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * データリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

	/**
	 * アプリケーションごとのデータリソース情報が設定されているリソースバンドル
	 * 
	 * @uml.property name="bundles"
	 * @uml.associationEnd 
	 * @uml.property name="bundles" multiplicity="(0 1)" qualifier="application:java.lang.String
	 * result:java.util.ResourceBundle"
	 */
	private Map bundles;


    /**
     * 共通のデータリソース情報が設定されているリソースバンドル
     */
    private ResourceBundle commonBundle;

    /**
     * DefaultDataPropertyHandlerを新規に生成します。
     */
    public DefaultDataPropertyHandler() {
        setBundlePrefix(null);
        setApplicationBundles(new HashMap());
        setCommonBundle(null);
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
	 * 共通のリソースバンドルを設定します。
	 * 
	 * @param commonBundle 共通のリソースバンドル
	 * @since 3.2
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
	 * @since 3.2
	 * 
	 * @uml.property name="commonBundle"
	 */
	private ResourceBundle getCommonBundle() throws DataPropertyException {
		return this.commonBundle;
	}

    /**
     * アプリケーションIDで指定されたリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws DataPropertyException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle getResourceBundle(String application)
        throws DataPropertyException {
        ResourceBundle result;

        synchronized (this.bundles) {
            result = (ResourceBundle)getApplicationBundles().get(application);
            if (result == null) {
                result = createResourceBundle(application);
                getApplicationBundles().put(application, result);
            }
        }

        return result;
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     *
     * @param application アプリケーションID
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws DataPropertyException リソースバンドルの取得時に例外が発生
     * @since 3.2
     */
    private ResourceBundle createResourceBundle(String application)
        throws DataPropertyException {
        try {
            return ResourceBundle.getBundle(
                getPropertyPackage( application ) + getBundlePrefix() + "_" + this.getApplicationID( application ));
        } catch (MissingResourceException e) {
            throw new DataPropertyException(e.getMessage(), e);
        }
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

        try {
            setCommonBundle(ResourceBundle.getBundle(bundleName));
        } catch (MissingResourceException e) {
            throw new PropertyHandlerException(e.getMessage(), e);
        }
    }

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     * このクラスではこのメソッドは常にfalseを返します。
     *
     * @return 常にfalse
     * @throws DataPropertyException チェック時に例外が発生
     * @since 3.2
     */
    public boolean isDynamic() throws DataPropertyException {
        return false;
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
    public String getDAOName(String application, String key, String connect)
        throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getDAOName(
            getResourceBundle(application),
            application,
            key,
            connect);
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
    public String getConnectorName(
        String application,
        String key,
        String connect)
        throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getConnectorName(
            getResourceBundle(application),
            application,
            key,
            connect);
    }

    /**
     * データコネクタのクラス名を取得します。
     * <CODE>application</CODE>、<CODE>key</CODE>と<CODE>connect</CODE>で指定されたデータコネクタのクラス名を取得します。
     *
     * @param connectorName データコネクタ名
     * @return データコネクタのクラス名
     * @throws DataPropertyException データコネクタのクラス名の取得に失敗
     */
    public String getConnectorClassName(String connectorName)
        throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getConnectorClassName(
            getCommonBundle(),
            connectorName);
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
    public String getConnectorResource(String connectorName)
        throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getConnectorResource(
            getCommonBundle(),
            connectorName);
    }

    /**
     * リソースのパラメータを取得します。
     * nameで指定されたリソースのパラメータを取得します。
     *
     * @param name リソース名
     * @return リソースのパラメータ
     * @throws DataPropertyException リソースのパラメータの取得時に例外が発生
     */
    public ResourceParam[] getResourceParams(String name)
        throws DataPropertyException {
        return ResourceBundleDataPropertyHandlerUtil.getResourceParams(
            getCommonBundle(),
            name);
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
			    buf.append(File.separatorChar);
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
