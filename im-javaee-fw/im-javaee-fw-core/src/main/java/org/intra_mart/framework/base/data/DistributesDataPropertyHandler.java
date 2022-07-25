/*
 * DefaultDataPropertyHandler.java
 *
 * Created on 2001/10/29, 19:15
 */

package org.intra_mart.framework.base.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyParam;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * アプリケーション毎のプロパティハンドラを呼び出します。
 * <BR>プロパティファイルはアプリケーション毎に分割されます。
 * <BR>この場合のファイル名は「<I>プレフィックス</I>_<I>アプリケーションID</I>.properties」、「<I>プレフィックス</I>_<I>アプリケーションID</I>.xml」です。
 * <BR>また、アプリケーションに依存しない「<I>プレフィックス</I>.properties」があります。
 * <BR>プロパティファイルのプレフィックスは{@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}でキーに{@link DataManager#DATA_PROPERTY_HANDLER_KEY}を指定したときに取得されるパラメータのうち{@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 指定しなかった場合のプロパティファイルのプレフィックスは{@link #DEFAULT_BUNDLE_NAME_PARAM}で示されるものです。
 *  
 * @author INTRAMART
 * @version 1.0
 */
public class DistributesDataPropertyHandler implements DataPropertyHandler {

    /**
     * デフォルトのリソースバンドル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "DataConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * プロパティファイルのデフォルトのパラメータ名
     */
    public static final String PARAM_DEFAULT = "default";

    /**
     * プロパティファイルのアプリケーションのパラメータ名
     */
    public static final String PARAM_APPLICATION = "application";

    /**
     * プロパティファイルのパラメータのパラメータ名
     */
    public static final String PARAM_APPLICATION_PARAM = "param";

    /**
     * プロパティファイルのクラスのパラメータ名
     */
    public static final String PARAM_APPLICATION_CLASS = "class";
    
    /**
     * プロパティファイルのファイルパスのパラメータ名
     */
    public static final String PARAM_FILE_DIR = "file_dir";

    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

    /**
     * プロパティファイルのバンドルのパラメータ名
     */
    public static final String PARAM_BUNDLE = "bundle";

    /**
     * データリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

	/**
	 * アプリケーションごとのデータリソース情報が設定されているリソースバンドル
	 */
	private Map bundles;


    /**
     * 共通のデータリソース情報が設定されているリソースバンドル
     */
    private ResourceBundle commonBundle;

    /**
     * DefaultDataPropertyHandlerを新規に生成します。
     */
    public DistributesDataPropertyHandler() {
        setBundlePrefix(null);
        setApplicationBundles(new HashMap());
        setCommonBundle(null);
    }

	/**
	 * リソースバンドルのプレフィックスを設定します。
	 * 
	 * @param bundlePrefix リソースバンドルのプレフィックス
	 * @since 3.2
	 */
	private void setBundlePrefix(String bundlePrefix) {
		this.bundlePrefix = bundlePrefix;
	}

	/**
	 * リソースバンドルのプレフィックスを取得します。
	 * 
	 * @return リソースバンドルのプレフィックス
	 * @since 3.2
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
	 * アプリケーションバンドルからハンドラのリソース情報を取得します。
	 * 
	 * @return ハンドラのリソース
	 * 
	 */
	private Object getApplicationBundle( String application ) {
        Object obj = this.bundles.get( application );
        if ( obj == null ) {
            obj = this.bundles.get(PARAM_DEFAULT);
        }
        return obj;
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
            // デフォルトのHandlerクラスを取得します。
            getDefaultHandler(params);

            // アプリケーション毎のHandlerクラスを取得します。
			getApplicationHandler(params);
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
     * DefaultのHandler情報を取得します。 
     *
     * @param params 初期パラメータ
     */
    private void getDefaultHandler(PropertyParam[] params) throws PropertyHandlerException{

		List keys = new ArrayList();
		HashMap values = new HashMap();

		try {
			// "default"で絞り込みます。
			for ( int i = 0; i < params.length; i++ ) {
				if ( params[i].getName().indexOf(PARAM_DEFAULT) != -1 ) {
				    keys.add(params[i].getName());
					values.put(params[i].getName(), params[i].getValue());
				}
			}

			setApplicationBundles( PARAM_DEFAULT, keys, values );

		} catch (Exception e) {
			throw new PropertyHandlerException();		    
		}
	}

    /**
     * Application毎のHandler情報を取得します。 
     *
     * @param params 初期パラメータ
     */
    private void getApplicationHandler(PropertyParam[] params) throws PropertyHandlerException{

		List keys = new ArrayList();
		Map values = new HashMap();

		// "application"で絞り込みます。
		for ( int i = 0; i < params.length; i++ ) {
			if ( params[i].getName().indexOf(PARAM_APPLICATION) != -1 ) {
			    keys.add( params[i].getName() ); 
				values.put( params[i].getName(), params[i].getValue() );
			}
		}

		// applicationIDのリストを取得します
		String[] appIds = getApplicationIdList(keys);
		
		// applicationIDをもとに、class、propertyを取得します
		for ( int i = 0; i < appIds.length; i++ ) {

		    setApplicationBundles( appIds[i], keys, values );

		}

    }

	/**
	 * Handlerクラスを生成し、Mapに保持します。
	 * 
	 * @param id
	 * @param list
	 * @param map
	 */
	private void setApplicationBundles(String id, List keys, Map values) throws PropertyHandlerException{
	    
		// ハンドラクラス名を取得します。
	    String className = getApplicationHandlerClass(id, keys, values);

	    Object obj = null;
	    try {
	        // クラスを生成します。
            obj = Class.forName(className).newInstance();
	    } catch (Exception ex) {
			throw new PropertyHandlerException();		    
        }

		// パラメータを取得します。
		PropertyParam[] params = getApplicationHandlerProperty(id, keys, values);
		
	    // Handlerクラスを生成しMapに保持します。
	    if ( obj instanceof DefaultDataPropertyHandler ) {

	        DefaultDataPropertyHandler handler = (DefaultDataPropertyHandler) obj;
            handler.init( params );

            // Mapに保持します。
			this.bundles.put(id, handler);
	        
		} else if ( obj instanceof TextFileDataPropertyHandler ) {

		    TextFileDataPropertyHandler handler = (TextFileDataPropertyHandler) obj;
            handler.init( params );
			// Mapに保持します。
			this.bundles.put(id, handler);

		} else if ( obj instanceof XmlDataPropertyHandler ) {

		    XmlDataPropertyHandler handler = (XmlDataPropertyHandler) obj;
            handler.init( params );
			// Mapに保持します。
			this.bundles.put(id, handler);

		}
		
	}
    
    /**
     * ApplicationIDのリストを取得します	
     *
     * @param params 初期パラメータ
     */
	private String[] getApplicationIdList(List keys) {

		// applicationIDのリストを取得します
		HashSet set = new HashSet();
		for ( int i = 0; i < keys.size(); i++ ) {
		    String str = (String)keys.get(i);
		    String[] paramArray = str.split("[.]");
			set.add( getApplications(paramArray) );
		}

		return (String[]) set.toArray(new String[0]);
		
	}

    /**
     * ApplicationIDのリストを取得します	
     *
     * @param params 初期パラメータ
     */
	private String getApplications(String[] paramArray) {

		String aplid = new String(); 
		int lastpos = 0;
		int firstpos = 0;
		
	    if (paramArray[0].equals(PARAM_APPLICATION)){
	    	firstpos = 1;	
	    }

	    if (paramArray[paramArray.length - 1].equals(PARAM_APPLICATION_CLASS)){
	    	lastpos = 1;	
	    } else if (paramArray[paramArray.length - 2].equals(PARAM_APPLICATION_PARAM) 
	        && (paramArray[paramArray.length - 1].equals(PARAM_DYNAMIC) 
	        || paramArray[paramArray.length - 1].equals(PARAM_FILE_DIR)
	        || paramArray[paramArray.length - 1].equals(PARAM_BUNDLE)) ){
	    	lastpos = 2;	
	    }

		for ( int i = firstpos; i < paramArray.length - lastpos; i++ ) {
		    if ( i == firstpos ) {
				aplid += paramArray[i];
		    } else {
				aplid += "." + paramArray[i];
		    }
		}
	    
		return aplid;
		
	}

	/**
     * Handlerクラス名を取得します。
     * 
     * @param applicationId
     * @param list
     * @param map
     * @return
     */
	private String getApplicationHandlerClass( String id, List list, Map map ) {

		Iterator listite = list.iterator();
		String name = new String();

		// applicationIdが一致するものだけ取り出します
		while ( listite.hasNext() ) {

			String key = ( String ) listite.next();
			
			// "<applicationID>.class"の値を取得します。
			if ( key.indexOf( id + "." + PARAM_APPLICATION_CLASS ) != -1 ) {
				name = (String) map.get(key); 
			}

		}

		return name;
		
	}

    /**
     * Handlerに引き渡すパラメータを取得します。
     * 
     * @param applicationId
     * @param list
     * @param map
     * @return
     */
	private PropertyParam[] getApplicationHandlerProperty( String applicationId, List list, Map map ) {

		Iterator iterator = list.iterator();
		List keys = new ArrayList();
		Map properties = new HashMap();
		
		while ( iterator.hasNext() ) {

			String key = ( String ) iterator.next();

			// "<applicationID>.param.dynamic"の値を取得します。
			if ( key.indexOf( applicationId + "." + PARAM_APPLICATION_PARAM + "." + PARAM_DYNAMIC ) != -1 ) {
			    keys.add(PARAM_DYNAMIC);
			    properties.put(PARAM_DYNAMIC, (String) map.get( key ));

			// "<applicationID>.param.file_dir"の値を取得します。
			} else if ( key.indexOf( applicationId + "." + PARAM_APPLICATION_PARAM + "." + PARAM_FILE_DIR ) != -1 ) {
			    keys.add(PARAM_FILE_DIR);
			    properties.put(PARAM_FILE_DIR, (String) map.get( key ));

			// "<applicationID>.param.bundle"の値を取得します。
			} else if ( key.indexOf( applicationId + "." + PARAM_APPLICATION_PARAM + "." + PARAM_BUNDLE ) != -1 ) {
			    keys.add(PARAM_BUNDLE);
			    properties.put(PARAM_BUNDLE, (String) map.get( key ));
			}

		}

		// PropertyParamを生成します。 
		PropertyParam[] param = new PropertyParam[keys.size()];
		for (int i = 0; i < keys.size(); i++ ) {
			param[i] = new PropertyParam(); 
		    param[i].setName( (String) keys.get(i) );
		    param[i].setValue( (String) properties.get( keys.get(i) ) );
		}
		
		return param;
		
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
        String ret = new String();

        Object obj = getApplicationBundle( application );
        
		if ( obj instanceof DefaultDataPropertyHandler ) {
	        
	        DefaultDataPropertyHandler handler = (DefaultDataPropertyHandler) obj;
	    	ret = handler.getDAOName( application, key, connect );

		} else if ( obj instanceof TextFileDataPropertyHandler ) {

	    	TextFileDataPropertyHandler handler = (TextFileDataPropertyHandler) obj;
	    	ret = handler.getDAOName( application, key, connect );

		} else if ( obj instanceof XmlDataPropertyHandler ) {
	        
	    	XmlDataPropertyHandler handler = (XmlDataPropertyHandler) obj;
	    	ret = handler.getDAOName( application, key, connect );

		}

		return ret;

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

        String ret = new String();
        Object obj = getApplicationBundle( application );
        
		if ( obj instanceof DefaultDataPropertyHandler ) {
	        
	        DefaultDataPropertyHandler handler = (DefaultDataPropertyHandler) obj;
	    	ret = handler.getConnectorName( application, key, connect );

		} else if ( obj instanceof TextFileDataPropertyHandler ) {

	    	TextFileDataPropertyHandler handler = (TextFileDataPropertyHandler) obj;
	    	ret = handler.getConnectorName( application, key, connect );

		} else if ( obj instanceof XmlDataPropertyHandler ) {
	        
	    	XmlDataPropertyHandler handler = (XmlDataPropertyHandler) obj;
	    	ret = handler.getConnectorName( application, key, connect );

		}

		return ret;

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
    
}