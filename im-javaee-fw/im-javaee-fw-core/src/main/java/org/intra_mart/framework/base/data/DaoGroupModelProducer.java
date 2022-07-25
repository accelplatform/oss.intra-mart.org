/*
 * 作成日: 2003/12/25
 */
package org.intra_mart.framework.base.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.framework.util.XMLDocumentProducer;
import org.intra_mart.framework.util.XMLNode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * DataModelを作成するクラスです。
 * @author INTRAMART
 * @version 1.0
 */
class DaoGroupModelProducer {

    String application;
    String key;

    /**
     * データモデルを作成します。
     * @param fileName
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    DaoGroupModel createDaoGroupModel(
        String application,
        String key,
        String prefix)
        throws
            ParserConfigurationException,
            SAXException,
            IOException,
            DataPropertyException,
            IllegalArgumentException {
        DaoGroupModel model = null;
        this.application = application;
        this.key = key;

        XMLDocumentProducer producer = new XMLDocumentProducer();

        String fileName = getPropertyPackage(application) 
                        + prefix 
                        + "-" 
                        + getApplicationID(application);
        
        Document doc = producer.getDocument(fileName);
        Node node = producer.getRoot(doc);
        XMLNode root = new XMLNode(node);
        XMLNode groupNode = selectGroupNode(root);
        model = getDaoGroupModel(groupNode);
        return model;
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
			    buf.append('/');
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
     * DaoGroupModelを取得します。
     * @param groupNode　データリソース情報を持つグループ
     * @return DaoGroupModel 
     */
    private DaoGroupModel getDaoGroupModel(XMLNode groupNode)
        throws DataPropertyException {
        DaoGroupModel model = new DaoGroupModel();
        ArrayList daos = getDaoModels(groupNode);

        for (int i = 0; i < daos.size(); i++) {
            DaoModel dao = (DaoModel) daos.get(i);
            if (dao.getConnectName() == null) {
                model.setDefaultDao(dao);
            } else {
                model.setDaoModel(dao.getConnectName(), dao);
            }
        }
        return model;
    }

    /**
     * DaoModelのリストを取得します。
     * @param nodes
     * @return ArrayList 
     */
    private ArrayList getDaoModels(XMLNode groupNode)
        throws DataPropertyException {
        ArrayList daos = new ArrayList();
        XMLNode[] daoNodes = groupNode.select(DaoModel.ID);
        for (int i = 0; i < daoNodes.length; i++) {
            DaoModel dao = getDaoModel(daoNodes[i]);
            daos.add(dao);
        }
        return daos;
    }

    /**
     * DaoModelを取得します。
     * @param daoNode
     * @version 1.0
     */
    private DaoModel getDaoModel(XMLNode daoNode)
        throws DataPropertyException {
        DaoModel dao = new DaoModel();
        dao.setConnectName(getConnectName(daoNode));
        dao.setDaoClass(getDaoClass(daoNode));
        dao.setConnectorName(getConnectorName(daoNode));
        return dao;
    }

    /**
     * DaoClass名を取得します。
     * @param xmlNode
     * @return String DaoClass名
     * @throws DataPropertyException
     */
    private String getDaoClass(XMLNode xmlNode) throws DataPropertyException {
        String daoClass = xmlNode.getString(DaoModel.P_ID_DAO_CLASS);
        if (daoClass == null || daoClass.equals("")) {
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

            String connect = xmlNode.getString(DaoModel.P_ID_CONNECT_NAME);
            if (connect == null) {
                throw new DataPropertyException(
                    message
                        + " : application = "
                        + application
                        + ", key = "
                        + key);
            } else {
                throw new DataPropertyException(
                    message
                        + " : application = "
                        + application
                        + ", key = "
                        + key
                        + " connect = "
                        + connect);
            }
        }

        return daoClass;
    }

    /**
     * ノードからコネクト名を取り出します。
     * @param daoNode
     * @return String コネクト名 
     */
    private String getConnectName(XMLNode daoNode) {
        String connectName = daoNode.getString(DaoModel.P_ID_CONNECT_NAME);
        return connectName;
    }

    /**
     * ノードからコネクタ名を取り出します。
     * @param daoNode
     * @return String コネクタ名 
     */
    private String getConnectorName(XMLNode daoNode) {
        String connectorName =
            daoNode.getString(DaoModel.P_ID_DAO_CONNECTOR_NAME);
        return connectorName;
    }

    /**
     * XMLNodeを取得します。
     * @param root
     * @return XMLNode 
     * @throws DataPropertyException
     */
    private XMLNode selectGroupNode(XMLNode root)
        throws DataPropertyException {
        XMLNode result = null;
        XMLNode[] groupNodes = root.select(DaoGroupModel.ID);

        for (int i = 0; i < groupNodes.length; i++) {
            XMLNode groupNode = groupNodes[i];
            if (groupNode.getString(DaoGroupModel.P_ID_DAO_KEY).equals(key)) {
                result = groupNode;
                break;
            }

        }
        if (result == null) {
            throw new DataPropertyException(
                "application = " + application + " : key = " + key);
        }
        return result;
    }

}
