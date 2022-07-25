/*
 * XmlEventPropertyHandler.java
 *
 * 
 */

package org.intra_mart.framework.base.event;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.intra_mart.framework.system.property.PropertyParam;

import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * XMLのEventPropertyHandlerです。
 * <BR>プロパティファイルはアプリケーション毎に分割されます。この場合のファイル名は「<I>プレフィックス</I>_<I>アプリケーションID</I>.xml」です。
 * <BR>プロパティファイルのプレフィックスは{@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}でキーに{@link EventManager#EVENT_PROPERTY_HANDLER_KEY}を指定したときに取得されるパラメータのうち{@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 指定しなかった場合のプロパティファイルのプレフィックスは{@link #DEFAULT_BUNDLE_NAME_PARAM}で示されるものです。
 * <BR>プロパティの設定内容は以下のとおりです。
 * @author INTRAMART
 * @version 1.0
 */
public class XmlEventPropertyHandler implements EventPropertyHandler {

	/**
	 * デフォルトのxmlファイルのプレフィックス
	 */
	public static final String DEFAULT_BUNDLE_NAME = "event-config";

	/**
	 * xmlのパラメータ名
	 */
	public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";
	
    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

	/**
	 * イベントリソース情報が設定されているXMLファイルのプレフィックス
	 */
	private String xmlPrefix;

    /**
     * 再設定可能フラグ
     */
    private boolean dynamic;

    /**
	 * アプリケーションごとのモデル群のマップ
	 */
	private Map applicationModels;

	/**
	 * DefaultEventPropertyHandlerを新規に生成します。
	 */
	public XmlEventPropertyHandler() {
		setXMLPrefix(null);
		applicationModels = new HashMap();
	}

	/**
	 * XMLファイルのプレフィックスを設定します。
	 *
	 * @param xmlPrefix　XMLファイルのプレフィックス
	 * @since 3.2
	 */
	private void setXMLPrefix(String xmlPrefix) {
		this.xmlPrefix = xmlPrefix;
	}

	/**
	 * XMLファイルのプレフィックスを取得します。
	 *
	 * @return リソースバンドルのプレフィックス
	 * @since 3.2
	 */
	private String getXMLPrefix() {
		return this.xmlPrefix;
	}

	/**
	 * アプリケーションIDとキーに対応するイベントモデルを取得します。
	 * ハッシュマップにアクセスするので、このメソッドは排他制御を行います。
	 * @param application アプリケーションID
	 * @param key キー
	 * @return アプリケーションIDに該当するリソースバンドル
	 * @throws EventPropertyException リソースバンドルの取得時に例外が発生
	 * @since 3.2
	 */
	private synchronized EventGroupModel getEventModel(String application, String key) throws EventPropertyException {
		EventGroupModel eventModel = null;
		synchronized(this.applicationModels) {
			
			// キーの一覧を取得
			Map keyModels = null;
			if (!isDynamic()) {
				keyModels = (Map) applicationModels.get(application);
			}
			if(keyModels == null) {
				keyModels = new HashMap();
				applicationModels.put(application,keyModels);
			}

			// イベントの一覧を取得
			if (!isDynamic()) {
				eventModel = (EventGroupModel)keyModels.get(key);
			}
			if (eventModel == null) {
				eventModel = createEventModel(application, key);
				keyModels.put(key,eventModel);
			}
		}
		return eventModel;
	}

	/**
	 * アプリケーションIDとキーに対応したイベントモデルを生成します。
	 *
	 * @param application アプリケーションID
	 * @return アプリケーションIDとキーに対応するイベントモデル
	 * @throws EventPropertyException　モデル生成時の取得時に例外が発生
	 */
	private EventGroupModel createEventModel(String application,String key) throws EventPropertyException {
		try{
			EventModelProducer producer = new EventModelProducer();
			EventGroupModel eventModel = producer.createEventModel(application, key, getXMLPrefix());
			return eventModel;
		}catch (ParserConfigurationException e){
			throw new EventPropertyException(e.getMessage() + ": application = " + application + ", key = " + key,e);
		}catch (SAXException e) {
			throw new EventPropertyException(e.getMessage() + ": application = " + application + ", key = " + key,e);
		}catch (IOException e) {
			throw new EventPropertyException(e.getMessage() + ": application = " + application + ", key = " + key,e );
		}catch (IllegalArgumentException e) {
			throw new EventPropertyException(e.getMessage() + ": application = " + application + ", key = " + key,e );			
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
		String dynamic = null;

		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				if (params[i].getName().equals(DEFAULT_BUNDLE_NAME_PARAM)) {
					bundleName = params[i].getValue();
                } else if (params[i].getName().equals(PARAM_DYNAMIC)) {
                    // 再設定可能フラグの場合
                    dynamic = params[i].getValue();
				}
			}
		}
		if (bundleName == null) {
			bundleName = DEFAULT_BUNDLE_NAME;
		}

		// プレフィックスの設定
		setXMLPrefix(bundleName);
		
        // 再設定可能フラグの設定
        Boolean dummyDynamic = new Boolean(dynamic);
        setDynamic(dummyDynamic.booleanValue());

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
	 * このクラスではこのメソッドは常にfalseを返します。
	 *
	 * @return 常にfalse
	 * @throws EventPropertyException チェック時に例外が発生
	 */
	public boolean isDynamic() throws EventPropertyException {
		return this.dynamic;
	}

	/**
	 * アプリケーションIDとキーに該当するイベントのクラス名を取得します。
	 * 該当するイベントが存在しない場合、nullを返します。
	 *
	 * @param application アプリケーションID
	 * @param key イベントのキー
	 * @return イベントのクラス名
	 * @throws EventPropertyException イベントのクラス名の取得に失敗
	 */
	public String getEventName(String application, String key) throws EventPropertyException {
		EventGroupModel eventModel = getEventModel(application,key);
		return eventModel.getEventName();	
	}

	/**
	 * アプリケーションIDとキーに該当するイベントリスナファクトリのクラス名を取得します。
	 *
	 * @param application アプリケーションID
	 * @param key イベントリスナファクトリのキー
	 * @return イベントリスナファクトリのクラス名
	 * @throws EventPropertyException イベントリスナファクトリのクラス名の取得に失敗
	 */
	public String getEventListenerFactoryName(String application, String key) throws EventPropertyException {
		EventGroupModel eventModel = getEventModel(application,key);
		return eventModel.getEventFactory().getFactoryName();

	}

	/**
	 * アプリケーションIDとキーに該当するイベントリスナファクトリの初期パラメータを取得します。
	 *
	 * @param application アプリケーションID
	 * @param key イベントのキー
	 * @return イベントリスナファクトリの初期パラメータ
	 * @throws EventPropertyException イベントリスナファクトリの初期パラメータの取得に失敗
	 */
	public EventListenerFactoryParam[] getEventListenerFactoryParams(String application,String key) throws EventPropertyException {
		EventGroupModel eventModel = getEventModel(application,key);
		return eventModel.getEventFactory().getFactoryParams();
	}

	/**
	 * アプリケーションIDとキーに該当するイベントのイベントトリガ情報をすべて取得します。
	 * <CODE>application</CODE>と<CODE>key</CODE>で定義される{@link EventTrigger}を定義された順番でソートしたCollectionとして取得します。
	 *
	 * @param application アプリケーションID
	 * @param key イベントのキー
	 * @return イベントトリガ情報のコレクション
	 * @throws EventPropertyException イベントトリガ情報の取得に失敗
	 * @see EventListener
	 */
	public Collection getEventTriggerInfos(String application, String key) throws EventPropertyException {
		EventGroupModel eventModel = getEventModel(application,key);
		return eventModel.getPreTriggerInfos();
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
		EventGroupModel eventModel = getEventModel(application,key);
		return eventModel.getPostTriggerInfos();
    }
    

}
