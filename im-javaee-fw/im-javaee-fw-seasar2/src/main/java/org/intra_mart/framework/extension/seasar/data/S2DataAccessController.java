package org.intra_mart.framework.extension.seasar.data;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.data.DAO;
import org.intra_mart.framework.base.data.DAOException;
import org.intra_mart.framework.base.data.DataAccessController;
import org.intra_mart.framework.base.data.DataConnectException;
import org.intra_mart.framework.base.data.DataConnector;
import org.intra_mart.framework.base.data.DataConnectorException;
import org.intra_mart.framework.base.data.DataPropertyException;
import org.intra_mart.framework.base.data.DataPropertyHandler;
import org.intra_mart.framework.extension.seasar.util.ComponentNameCreator;
import org.intra_mart.framework.extension.seasar.util.ComponentUtil;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * S2DataAccessControllerはSeasar2　と連携しdaoを生成します。<br>
 * S2DataAccessControllerは要求されたdaoオブジェクトをS2Containerから取得し、未登録のdaoは自動的に登録します。<br>
 * このときS2Containerから返されるdaoオブジェクトは全て新規に生成されたものです。<br>
 * <br>
 * また、要求されたdaoがインターフェースの場合、S2Containarが保持するコンポーネントとみなし、S2Containarから直接コンポーネントを取得します。
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class S2DataAccessController extends DataAccessController {
	
	private S2Container container;

    /**
     * データアクセスコントローラを生成します。
     *
     * @param handler データプロパティハンドラ
     */
	public S2DataAccessController(DataPropertyHandler handler) {
		super(handler);
		this.container = SingletonS2ContainerFactory.getContainer();
	}

    /**
     * DAOを取得します。
     * キーと接続情報で指定されたDAOを取得します。<br>
     * <br>
     * 指定されたDAOがインターフェースの場合S2Containerからコンポーネントを取得します。<br>
     * この場合、diconファイルへの定義、dao.dicon等の設定が必要です。<br>
     * S2Containerからコンポーネントを取得する場合接続情報を指定する必要はありません。
     * 接続先データベースの解決はdiconファイルのデータソース設定に依存するため、
     * {@link org.intra_mart.framework.extension.seasar.util.AutoDetectedDataSource}等をデータソースに設定してください。
     *
     * @param application アプリケーションID
     * @param key DAOのキー
     * @param connect 接続情報
     * @return DAOまたはS2Containerから取得したオブジェクト
     * @throws DataPropertyException プロパティの取得に失敗
     * @throws DataConnectorException データコネクタの取得に失敗
     * @throws DAOException DAOの取得に失敗
     * @throws DataConnectException データリソースとの接続に失敗
     * @see org.intra_mart.framework.extension.seasar.util.AutoDetectedDataSource
     * @see org.intra_mart.framework.extension.seasar.util.LoginGroupDataSource
     * @see org.intra_mart.framework.extension.seasar.util.SystemDataSource
     */
    public Object getDAO(String application, String key, String connect) throws DataPropertyException, DataConnectorException, DAOException, DataConnectException {
    	String daoName = null;
//    	Object daoObject = null;
    	Class clazz = null;
        DAO result = null;

        // DAOの生成
        daoName = getDataPropertyHandler().getDAOName(application, key, connect);
        try {
        	clazz = Class.forName(daoName);
        	if (clazz.isInterface()) {
        		// インターフェースの場合はS2Daoのコンポーネントとみなす。
        		return this.container.getComponent(clazz);
        	}
        } catch (Exception e) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DataAccessController.FailedToCreateDAO");
            } catch (MissingResourceException ex) {
            }
            throw new DAOException(message + " : class = " + daoName + ", application = " + application + ", key = " + key + ", connect = " + connect, e);
        }
        try {
        	String componentKey = ComponentNameCreator.createDaoName(application, key);
        	ComponentUtil.register(this.container, clazz, componentKey, InstanceDefFactory.PROTOTYPE);
            result = (DAO)this.container.getComponent(componentKey);
        } catch (Exception e) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DataAccessController.FailedToCreateDAO");
            } catch (MissingResourceException ex) {
            }
            throw new DAOException(message + " : class = " + daoName + ", application = " + application + ", key = " + key + ", connect = " + connect, e);
        }
        
        Object connectorObject = null;
        DataConnector connector = null;
        String connectorName = null;
        String connectorClassName = null;
        String resource = null;

        // データコネクタ名の取得
        connectorName = getDataPropertyHandler().getConnectorName(application, key, connect);

        // データコネクタの検索
        if (connectorName == null || connectorName.equals("")) {
            connector = null;
        } else {
            connector = (DataConnector)getDataConnectors().get(connectorName);
            if (connector == null) {
                // データコネクタが存在しない場合、新規に登録
                connectorClassName = getDataPropertyHandler().getConnectorClassName(connectorName);
                try {
                    connectorObject = Class.forName(connectorClassName).newInstance();
                } catch (Exception e) {
                    String message = null;
                    try {
                        message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DataAccessController.FailedToCreateDataConnector");
                    } catch (MissingResourceException ex) {
                    }
                    throw new DataConnectorException(message + " : connector name = " + connectorName + ", class = " + connectorClassName + ", application = " + application + ", key = " + key + ", connect = " + connect, e);
                }
                if (connectorObject instanceof DataConnector) {
                    connector = (DataConnector)connectorObject;
                } else {
                    String message = null;
                    try {
                        message = ResourceBundle.getBundle("org.intra_mart.framework.base.data.i18n").getString("DataAccessController.DataConnectorNotExtended");
                    } catch (MissingResourceException e) {
                    }
                    throw new DataConnectorException(message + " : class = " + connectorClassName + ", application = " + application + ", key = " + key + ", connect = " + connect);
                }
                connector.setDataPropertyHandler(getDataPropertyHandler());
                this.getDataConnectors().put(connectorName, connector);
            }

            // データリソースの取得
            resource = getDataPropertyHandler().getConnectorResource(connectorName);
        }
        
        result.setConnectInfo(connector, resource, key, connect);
        return result;
    }
}
