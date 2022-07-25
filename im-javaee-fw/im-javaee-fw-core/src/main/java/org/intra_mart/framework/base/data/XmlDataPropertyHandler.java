/*
 * 作成日: 2003/12/25
 */
package org.intra_mart.framework.base.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;

/**
 * XMLのデータプロパティハンドラです。
 * <BR>プロパティファイルはアプリケーション毎に分割されます。この場合のファイル名は「<I>プレフィックス</I>_<I>アプリケーションID</I>.xml」です。また、アプリケーションに依存しない「<I>プレフィックス</I>.properties」があります。
 * <BR>プロパティファイルのプレフィックスは{@link org.intra_mart.framework.system.property.PropertyManager#getPropertyHandlerParams(String)}でキーに{@link DataManager#DATA_PROPERTY_HANDLER_KEY}を指定したときに取得されるパラメータのうち{@link #DEFAULT_BUNDLE_NAME}で取得されるものとなります。
 * 
 * @author INTRAMART
 * @version 1.0
 */
public class XmlDataPropertyHandler implements DataPropertyHandler {

    /**
     * デフォルトのXMLファイル名のプレフィックス
     */
    public static final String DEFAULT_BUNDLE_NAME = "data-config";

    /**
     * XMLファイル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

    /**
     * データリソース情報が設定されているXMLファイルのプレフィックス
     */
    private String xmlPrefix;

    /**
     * 再設定可能フラグ
     */
    private boolean dynamic;

    /**
     * アプリケーションごとのデータリソース情報が設定されているリソースモデル群
     */
    private Map models;

    /**
     * 共通のデータリソース情報が設定されているリソースモデル群
     */
    private Map connectors;

    /**
     * コネクタのパラメータ情報を格納しているもの。
     */
    private Map resources;

    /**
     * コンストラクタです。
     */
    public XmlDataPropertyHandler() {
        setXMLPrefix(null);
        setApplicationModels(new HashMap());
        setCommonDataModels(new HashMap());
        setResourceParams(new HashMap());
    }

    /**
     * XMLファイルのプレフィックスを設定します。
     * @param xmlPrefix XMLファイルのプレフィックス
     */
    private void setXMLPrefix(String xmlPrefix) {
        this.xmlPrefix = xmlPrefix;
    }

    /**
     * XMLファイルのプレフィックスを取得します。
     * @return　XMLファイルのプレフィックス
     */
    private String getXMLPrefix() {
        return this.xmlPrefix;
    }

    /**
     * アプリケーションごとのリソースバンドルの集合を設定します。
     *
     * @param applicationBundles アプリケーションごとのリソースバンドルの集合
     */
    private void setApplicationModels(Map applicationModels) {
        this.models = applicationModels;
    }

    /**
     * アプリケーションごとのリソースモデルの集合を取得します。
     * @return　models リソースモデル集合
     */
    private Map getApplicationModels() {
        return this.models;
    }

    /**
     * コネクタ名に該当するコネクタモデルを取得します。
     * @param connectorName
     * @return 共通リソースモデル
     * @throws コネクタモデル取得時に例外が発生
     */
    private ConnectorModel getConnectorModel(String connectorName) throws DataPropertyException {
        return (ConnectorModel) getCommonDataModels().get(connectorName);
    }

    /**
     * 共通リソースモデル群を取得します。
     * @return 共通リソースモデル群
     * @throws 共通リソース取得時に例外が発生
     */
    private Map getCommonDataModels() throws DataPropertyException {
		if (isDynamic()) {
			return createConnectorModels(getXMLPrefix());
		}
        return connectors;
    }

    /**
     * 共通のリソースモデルを設定します
     * @param commonDataModel　共通のリソースモデル
     */
    private void setCommonDataModels(Map commonDataModels) {
        this.connectors = commonDataModels;
    }

    /**
     * 共通のリソースパラメータを設定します。
     * @param resourceParams
     */
    private void setResourceParams(Map resources) {
        this.resources = resources;
    }

    /**
     * 共通のリソースパラメータを取得します。
     * @return 共通リソースパラメータのMap
     * @throws DataPropertyException 共通リソース取得時に例外が発生
     */
    private Map getResources() throws DataPropertyException {
		if (isDynamic()) {
			return createResourceModels(getXMLPrefix());
		}
        return this.resources;
    }

    /**
     * アプリケーションIDとキーに該当するリソースモデルを取得します。
     *
     * @param application アプリケーションID
     * @param key キー
     * @return アプリケーションIDのキー該当するリソースモデル
     * @throws DataPropertyException リソースバンドルの取得時に例外が発生
     */
    private DaoGroupModel getDaoGroupModel(String application, String key)
        throws DataPropertyException {
        DaoGroupModel result = null;

        synchronized (this.models) {
        	
        	Map keyModels =null;
        	if(!isDynamic()) {
                keyModels = (Map) getApplicationModels().get(application);
        	}
            if (keyModels == null) {
                keyModels = new HashMap();
                getApplicationModels().put(application, keyModels);
            }
            if(!isDynamic()) {
                result = (DaoGroupModel) keyModels.get(key);
            }
            if (result == null) {
                result = createDaoGroupModel(application, key);
                keyModels.put(key, result);
            }
        }

        return result;
    }
    /**
     * データリソース情報を持つモデルを作成します。
     * @param application
     * @param key
     * @param connect
     * @return
     * @throws DataPropertyException
     */
    private DaoGroupModel createDaoGroupModel(String application, String key)
        throws DataPropertyException {
        try {
            DaoGroupModelProducer producer = new DaoGroupModelProducer();
            DaoGroupModel dataModel =
                producer.createDaoGroupModel(application.replace('\\', '/'), key, getXMLPrefix());
            return dataModel;
        } catch (ParserConfigurationException e) {
            throw new DataPropertyException(
                e.getMessage()
                    + ": application = "
                    + application
                    + ", key = "
                    + key,
                e);
        } catch (SAXException e) {
            throw new DataPropertyException(
                e.getMessage()
                    + ": application = "
                    + application
                    + ", key = "
                    + key,
                e);
        } catch (IOException e) {
            throw new DataPropertyException(
                e.getMessage()
                    + ": application = "
                    + application
                    + ", key = "
                    + key,
                e);
        } catch (IllegalArgumentException e) {
            throw new DataPropertyException(
                e.getMessage()
                    + ": application = "
                    + application
                    + ", key = "
                    + key,
                e);
        }
    }

    /**
     * 共通データリソース情報をもつモデルを作成します。
     * @param xmlFileName　共通リソースデータが書かれたXMLファイル
     * @return 共通リソースデータのMap 
     * @throws DataPropertyException
     */
    private Map createConnectorModels(String xmlFileName)
        throws DataPropertyException {
        try {

            CommonDataModelProducer producer = new CommonDataModelProducer();
            Map result = producer.createConnectorModels(xmlFileName.replace('\\', '/'));
            return result;
        } catch (ParserConfigurationException e) {
            throw new DataPropertyException(e.getMessage(), e);
        } catch (SAXException e) {
            throw new DataPropertyException(e.getMessage(), e);
        } catch (IOException e) {
            throw new DataPropertyException(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new DataPropertyException(e.getMessage(), e);
        }
    }

    /**
     * 共通のコネクタのパラメータを持つマップを作成する
     * @param xmlFileName　共通リソースデータが書かれたXMLファイル
     * @return 共通コネクタパラメータのMap
     * @throws DataPropertyException
     */
    private Map createResourceModels(String xmlFileName)
        throws DataPropertyException {
        try {
            CommonDataModelProducer producer = new CommonDataModelProducer();
            Map result = producer.createResourceModels(xmlFileName.replace('\\', '/'));
            return result;
        } catch (ParserConfigurationException e) {
            throw new DataPropertyException(e.getMessage(), e);
        } catch (SAXException e) {
            throw new DataPropertyException(e.getMessage(), e);
        } catch (IOException e) {
            throw new DataPropertyException(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
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
        String xmlPrefix = null;
        String dynamic = null;

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].getName().equals(DEFAULT_BUNDLE_NAME_PARAM)) {
                    xmlPrefix = params[i].getValue();
                } else if (params[i].getName().equals(PARAM_DYNAMIC)) {
                    // 再設定可能フラグの場合
                    dynamic = params[i].getValue();
                }
            }
        }
        if (xmlPrefix == null) {
            xmlPrefix = DEFAULT_BUNDLE_NAME;
        }

        // プレフィックスの設定
        setXMLPrefix(xmlPrefix);

        // 再設定可能フラグの設定
        Boolean dummyDynamic = new Boolean(dynamic);
        setDynamic(dummyDynamic.booleanValue());
        
        try {
        	if(!isDynamic()) {	
	            setCommonDataModels(createConnectorModels(getXMLPrefix()));
	            setResourceParams(createResourceModels(getXMLPrefix()));
       		}
        } catch (Exception e) {
            throw new PropertyHandlerException(e.getMessage(), e);
        }
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
     * @throws DataPropertyException チェック時に例外が発生
     * @since 3.2
     */
    public boolean isDynamic() throws DataPropertyException {
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
    public String getDAOName(String application, String key, String connect)
        throws DataPropertyException {
        DaoGroupModel model = getDaoGroupModel(application, key);
        String daoName = null;

        if (connect != null) {
            daoName = model.getDAOName(connect);
        } else {
            DaoModel dao = model.getDefaultDao();
            daoName = dao.getDaoClass();
        }

        if (daoName == null || daoName.equals("")) {
            String message = null;
            try {
                ResourceBundle messageBundle =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n");
                message =
                    messageBundle.getString(
                        "ResourceBundleDataPropertyHandlerUtil.param.DAOClassNotDeclared");
            } catch (MissingResourceException exc) {
            }
            throw new DataPropertyException(
                message
                    + " : application = "
                    + application
                    + ", key = "
                    + key
                    + ", connect = "
                    + connect);
        }
        return daoName;
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
        DaoGroupModel model = getDaoGroupModel(application, key);
        return model.getConnectorName(connect);

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
        ConnectorModel connector = getConnectorModel(connectorName);
        if (connector == null) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "ResourceBundleDataPropertyHandlerUtil.param.ConnectorClassNotDeclared");
            } catch (MissingResourceException e) {
            }
            throw new DataPropertyException(
                message + " : Connector name = " + connectorName);
        }
        return connector.getConnectorClassName();
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
        ConnectorModel connector = getConnectorModel(connectorName);
        if (connector == null) {
            return null;
        }
        return connector.getConnectorResource();
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
        Map resources = getResources();

        ResourceModel resource = (ResourceModel) resources.get(name);
        if (resource == null) {
            return new ResourceParam[0];
        }
        ResourceParam[] params = resource.getParams();
        if (params == null) {
            return new ResourceParam[0];
        } else {
            return params;
        }
    }
}
