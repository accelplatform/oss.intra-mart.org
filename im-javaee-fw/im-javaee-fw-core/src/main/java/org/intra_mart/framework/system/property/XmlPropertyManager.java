/*
 * DefaultPropertyManager.java
 *
 * Created on 2001/11/08, 11:17
 */

package org.intra_mart.framework.system.property;

import java.io.IOException;
import java.util.MissingResourceException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * xmlのプロパティマネージャです。
 * プロパティ情報を管理します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class XmlPropertyManager extends PropertyManager {

    /**
     * プロパティマネージャを指定するキー
     */
    public static final String KEY = "org.intra_mart.framework.system.property.XmlPropertyManager";

    /**
     * デフォルトのリソースバンドル名
     */
    public static final String DEFAULT_BUNDLE_NAME = "property-config.xml";

    /**
     * プロパティコンフィグ情報
     */
    private PropertyConfigModel config;

    /**
     * コンストラクタです。
     *
     * @throws PropertyManagerException プロパティマネージャの設定に失敗
     */
    public XmlPropertyManager() throws PropertyManagerException {
        super();

        String bundleName = System.getProperty(KEY, DEFAULT_BUNDLE_NAME);
		if ( bundleName == null ) {
		    bundleName = DEFAULT_BUNDLE_NAME;
		}
		
		this.config = getPropertyConfigModel( bundleName );
    }

    /**
     * キーで指定されたプロパティハンドラのクラス名を取得します。
     *
     * @return プロパティハンドラのクラス名
     * @param key プロパティハンドラのキー
     * @throws PropertyHandlerException クラス名の取得に失敗
     */
    protected String getPropertyHandlerName(String key) throws PropertyHandlerException {
        String result;
		try {
	        result = config.getPropertyHandlerName( key );
	        if ( result == null ) {
	            throw new PropertyHandlerException();
	        }
		} catch ( Exception e ) {
            String message = null;
            try {
                message = java.util.ResourceBundle.getBundle("org.intra_mart.framework.system.property.i18n").getString("DefaultPropertyManager.NoSuchKey");
            } catch (MissingResourceException ex) {
            }
            throw new PropertyHandlerException(message + " : key = " + key);
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
        PropertyHandlerParam[] result;

        result = config.getPropertyParams( key );

        return result;
    }

	/**
	 * プロパティモデルを生成します。
	 *
	 * @param application アプリケーションID
	 * @return アプリケーションIDとキーに対応するイベントモデル
	 * @throws EventPropertyException　モデル生成時の取得時に例外が発生
	 */
	private PropertyConfigModel getPropertyConfigModel( String bundleName ) throws PropertyManagerException {
		try{
			PropertyConfigModelProducer producer = new PropertyConfigModelProducer();
			PropertyConfigModel model = producer.createPropertyConfigModel( bundleName );
			return model;
		}catch (ParserConfigurationException e){
			throw new PropertyManagerException(e.getMessage(), e);
		}catch (SAXException e) {
			throw new PropertyManagerException(e.getMessage(), e);
		}catch (IOException e) {
			throw new PropertyManagerException(e.getMessage(), e);
		}catch (IllegalArgumentException e) {
			throw new PropertyManagerException(e.getMessage(), e);
		}catch (Exception e) {
			throw new PropertyManagerException(e.getMessage(), e);
		}
	}
}
