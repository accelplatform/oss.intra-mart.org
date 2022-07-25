/*
 * 作成日： 2004/09/15
 */
package org.intra_mart.framework.base.event;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;

/**
 * アプリケーション毎のEventPropertyHandlerを呼び出します。
 * <BR>プロパティファイルはアプリケーション毎に分割されます。この場合のファイル名は「<I>プレフィックス</I>_<I>アプリケーションID</I>.properties」、または「<I>プレフィックス</I>_<I>アプリケーションID</I>.xml」です。
 * <BR>プロパティファイルのプレフィックスは{@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}でキーに{@link EventManager#EVENT_PROPERTY_HANDLER_KEY}を指定したときに取得されるパラメータのうち{@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 指定しなかった場合のプロパティファイルのプレフィックスは{@link #DEFAULT_BUNDLE_NAME_PARAM}で示されるものです。
 * @author INTRAMART
 * @version 1.0
 */
public class DistributesEventPropertyHandler implements EventPropertyHandler {

    /**
     * デフォルトのリソースバンドル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "EventConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * デフォルト
     */
    public static final String PARAM_DEFAULT = "default";

    /**
     * アプリケーション
     */
    public static final String PARAM_APPLICATION = "application";

    /**
     * パラメータ
     */
    public static final String PARAM_APPLICATION_PARAM = "param";

    /**
     * クラス
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
     * イベントリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

	/**
	 * アプリケーションごとのイベントリソース情報が設定されているリソースバンドル
	 */
	private Map bundles;

	/**
	 * 前処理用イベントトリガ情報のマップ
	 */
	private Map eventTriggers;

	/**
	 * 後処理用イベントトリガ情報のマップ
	 * 
	 * since 4.3
	 */
	private Map postEventTriggers;


    /**
     * DistributesEventPropertyHandlerを新規に生成します。
     */
    public DistributesEventPropertyHandler() {
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
	 * アプリケーションバンドルからハンドラのリソース情報を取得します。
	 * 
	 * @return ハンドラのリソース情報
	 * @since
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
	 * イベントトリガ情報のマップを設定します。
	 * 
	 * @param eventTriggers イベントトリガ情報のマップ
	 * @since 3.2
	 * 
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
            // デフォルトのHandlerクラスを取得します。
            getDefaultHandler(params);

            // アプリケーション毎のHandlerクラスを取得します。
			getApplicationHandler(params);

        }
        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }

        setBundlePrefix(bundleName);
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
		Map returnMap = new HashMap();
	    if ( obj instanceof DefaultEventPropertyHandler ) {

	        DefaultEventPropertyHandler handler = (DefaultEventPropertyHandler) obj;
            handler.init( params );

            // Mapに保持します。
			this.bundles.put(id, handler);
	        
		} else if ( obj instanceof TextFileEventPropertyHandler ) {

		    TextFileEventPropertyHandler handler = (TextFileEventPropertyHandler) obj;
            handler.init( params );
			// Mapに保持します。
			this.bundles.put(id, handler);

		} else if ( obj instanceof XmlEventPropertyHandler ) {

		    XmlEventPropertyHandler handler = (XmlEventPropertyHandler) obj;
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

        String ret = new String();

        Object obj = getApplicationBundle( application );
        
        if ( obj instanceof DefaultEventPropertyHandler ) {
	        
	        DefaultEventPropertyHandler handler = (DefaultEventPropertyHandler) obj;
	    	ret = handler.getEventName( application, key );

		} else if ( obj instanceof TextFileEventPropertyHandler ) {
	        
	    	TextFileEventPropertyHandler handler = (TextFileEventPropertyHandler) obj;
	    	ret = handler.getEventName( application, key );

		} else if ( obj instanceof XmlEventPropertyHandler ) {
	        
	    	XmlEventPropertyHandler handler = (XmlEventPropertyHandler) obj;
	    	ret = handler.getEventName( application, key );

		}

		return ret;
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

        String ret = new String();

        Object obj = getApplicationBundle( application );
        
		if ( obj instanceof DefaultEventPropertyHandler ) {
	        
	        DefaultEventPropertyHandler handler = (DefaultEventPropertyHandler) obj;
	    	ret = handler.getEventListenerFactoryName( application, key );

		} else if ( obj instanceof TextFileEventPropertyHandler ) {

	        
	    	TextFileEventPropertyHandler handler = (TextFileEventPropertyHandler) obj;
	    	ret = handler.getEventListenerFactoryName( application, key );

		} else if ( obj instanceof XmlEventPropertyHandler ) {
	        
	    	XmlEventPropertyHandler handler = (XmlEventPropertyHandler) obj;
	    	ret = handler.getEventListenerFactoryName( application, key );

		}

		return ret;
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

        EventListenerFactoryParam[] ret =null;

        Object obj = getApplicationBundle( application );
        
		if ( obj instanceof DefaultEventPropertyHandler ) {
	        
	        DefaultEventPropertyHandler handler = (DefaultEventPropertyHandler) obj;
	    	ret = handler.getEventListenerFactoryParams( application, key );

		} else if ( obj instanceof TextFileEventPropertyHandler ) {

	        
	    	TextFileEventPropertyHandler handler = (TextFileEventPropertyHandler) obj;
	    	ret = handler.getEventListenerFactoryParams( application, key );

		} else if ( obj instanceof XmlEventPropertyHandler ) {
	        
	    	XmlEventPropertyHandler handler = (XmlEventPropertyHandler) obj;
	    	ret = handler.getEventListenerFactoryParams( application, key );

		}

		return ret;
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

        Collection infos = null;

        Object obj = getApplicationBundle( application );
        
		if ( obj instanceof DefaultEventPropertyHandler ) {
	        
	        DefaultEventPropertyHandler handler = (DefaultEventPropertyHandler) obj;
	    	infos = handler.getEventTriggerInfos( application, key );

		} else if ( obj instanceof TextFileEventPropertyHandler ) {

	        
	    	TextFileEventPropertyHandler handler = (TextFileEventPropertyHandler) obj;
	    	infos = handler.getEventTriggerInfos( application, key );

		} else if ( obj instanceof XmlEventPropertyHandler ) {
	        
	    	XmlEventPropertyHandler handler = (XmlEventPropertyHandler) obj;
	    	infos = handler.getEventTriggerInfos( application, key );

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

        Collection infos = null;

        Object obj = getApplicationBundle( application );
        
		if ( obj instanceof DefaultEventPropertyHandler ) {
	        
	        DefaultEventPropertyHandler handler = (DefaultEventPropertyHandler) obj;
	    	infos = handler.getPostEventTriggerInfos( application, key );

		} else if ( obj instanceof TextFileEventPropertyHandler ) {

	        
	    	TextFileEventPropertyHandler handler = (TextFileEventPropertyHandler) obj;
	    	infos = handler.getPostEventTriggerInfos( application, key );

		} else if ( obj instanceof XmlEventPropertyHandler ) {
	        
	    	XmlEventPropertyHandler handler = (XmlEventPropertyHandler) obj;
	    	infos = handler.getPostEventTriggerInfos( application, key );

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
