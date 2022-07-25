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

import org.intra_mart.framework.util.XMLDocumentProducer;
import org.intra_mart.framework.util.XMLNode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * CommonDataModelを生成します。
 * @author INTRAMART
 * @version 1.0
 */
class CommonDataModelProducer {

    /**
     * コネクターモデルを作成します。
     * @param fileName　XMLファイル
     * @return Map 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws EventPropertyException
     * @throws IllegalArgumentException
     */
    Map createConnectorModels(String fileName)
        throws
            ParserConfigurationException,
            SAXException,
            IOException,
            DataPropertyException,
            IllegalArgumentException {

        Map models = null;
        XMLDocumentProducer producer = new XMLDocumentProducer();
        Document doc = null;
        doc = producer.getDocument(fileName);
        Node node = producer.getRoot(doc);
        XMLNode root = new XMLNode(node);
        //ノードからコネクタノード群を取り出す

        models = createModels(root);
        
        return models;
    }

    /**
     * 共通リソース情報を持つモデル群を作成します。
     * @param root
     * @return
     * @throws DataPropertyException
     */
    private Map createModels(XMLNode root) throws DataPropertyException {
        HashMap models = new HashMap();
        XMLNode[] nodes = root.select(ConnectorModel.ID);
        for (int i = 0; i < nodes.length; i++) {
            ConnectorModel connector = getConnectorModel(nodes[i]);
            models.put(connector.getConnectorName(), connector);
        }
        return models;
    }

    /**
     * コネクターモデルを取得します。
     * @param connectorNode ノード 
     * @return ConnectorModel コネクターモデル 
     * @throws DataPropertyException
     */
    private ConnectorModel getConnectorModel(XMLNode connectorNode)
        throws DataPropertyException {
        ConnectorModel model = new ConnectorModel();

        model.setConnectorName(getConnectorName(connectorNode));
        model.setConnectorClassName(getConnectorClassName(connectorNode));
        model.setConnectorResource(getConnectorResource(connectorNode));

        return model;
    }

    /**
     * コネクタ名を取得します。
     * @param connectorNode
     * @return String コネクタ名 
     * @throws DataPropertyException
     */
    private String getConnectorName(XMLNode connectorNode)
        throws DataPropertyException {
        String connectorName =
            connectorNode.getString(ConnectorModel.P_ID_CONNECTOR_NAME);
        if (connectorName == null || connectorName.equals("")) {
            throw new DataPropertyException();
        }
        return connectorName;
    }

    /**
     * コネクタクラス名を取得します。
     * @param connectorNode
     * @return String コネクタクラス名 
     * @throws DataPropertyException
     */
    private String getConnectorClassName(XMLNode connectorNode)
        throws DataPropertyException {
        String connectorClass =
            connectorNode.getString(ConnectorModel.P_ID_CONNECTOR_CLASS);
        if (connectorClass == null || connectorClass.equals("")) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "ResourceBundleDataPropertyHandlerUtil.param.ConnectorClassNotDeclared");
            } catch (MissingResourceException e) {
            }
            throw new DataPropertyException(
                message
                    + " : Connector name = "
                    + connectorNode.getString(
                        ConnectorModel.P_ID_CONNECTOR_NAME));
        }
        return connectorClass;
    }

    /**
     * コネクタノードからリソース名を取得します。
     * @param connectorNode
     * @return String リソース名 
     * @throws DataPropertyException
     */
    private String getConnectorResource(XMLNode connectorNode)
        throws DataPropertyException {
        return connectorNode.getString(ConnectorModel.P_ID_RESOURCE_NAME);

    }

    /**
     * リソースモデルを作成します。
     * 
     * @param fileName
     * @return Map リソースモデル 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws DataPropertyException
     * @throws IllegalArgumentException
     */
    Map createResourceModels(String fileName)
        throws
            ParserConfigurationException,
            SAXException,
            IOException,
            DataPropertyException,
            IllegalArgumentException {
        Map models = null;
        XMLDocumentProducer producer = new XMLDocumentProducer();
        Document doc = producer.getDocument(fileName);
        Node node = producer.getRoot(doc);
        XMLNode root = new XMLNode(node);
        //ノードからリソースノード群を取り出す

        models = createResourceModels(root);
        return models;
    }

    /**
     * 共通リソース情報を持つモデル群を作成します。
     * 
     * @param root
     * @return Map 共通リソース情報を持つモデル群
     * @throws DataPropertyException
     */
    private Map createResourceModels(XMLNode root)
        throws DataPropertyException {
        HashMap models = new HashMap();
        XMLNode[] nodes = root.select(ResourceModel.ID);

        for (int i = 0; i < nodes.length; i++) {
            ResourceModel resource = getResourceModel(nodes[i]);
            models.put(resource.getConnectorResource(), resource);
        }
        return models;
    }

    /**
     * 共通リソース情報を持つモデル群を取得します。
     * 
     * @param resourceNode
     * @return ResourceModel 
     * @throws DataPropertyException
     */
    private ResourceModel getResourceModel(XMLNode resourceNode) throws DataPropertyException {
        ResourceModel resourceModel = new ResourceModel();
        String resourceName =
            resourceNode.getString(ResourceModel.P_ID_RESOURCE_NAME);
        resourceModel.setConnectorResource(resourceName);

        if (resourceName == null || resourceName.equals("")) {
            throw new DataPropertyException();
        }

        ResourceParam[] initParams = getResourceParams(resourceNode);
        if (initParams == null || initParams.length == 0) {
            resourceModel.setParams(new ResourceParam[0]);
        } else {
            resourceModel.setParams(initParams);
        }
        return resourceModel;
    }

    /**
     * リソースパラメータを作成します。
     * 
     * @param resourceNode
     * @return ResourceParam[] 
     * @throws DataPropertyException
     */
    private ResourceParam[] getResourceParams(XMLNode resourceNode)
        throws DataPropertyException {
        XMLNode[] initParamNodes =
            resourceNode.select(ResourceModel.P_ID_RESOURCE_PARAM);
        ResourceParam[] params = new ResourceParam[initParamNodes.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = getResourceParam(initParamNodes[i]);

        }
        return params;
    }

    /**
     * リソースパラメータオブジェクトにパラメータノードの値を格納します。
     * 
     * @param initParam
     * @return ResourceParam 
     * @throws DataPropertyException
     */
    private ResourceParam getResourceParam(XMLNode initParam)
        throws DataPropertyException {
        ResourceParam param = new ResourceParam();
        param.setName(getResourceParamName(initParam));
        param.setValue(getResourceParamValue(initParam));
        return param;
    }

    /**
     * パラメータノードからリソース名を取得します。
     * 
     * @param initParam
     * @return String リソース名 
     * @throws DataPropertyException
     */
    private String getResourceParamName(XMLNode initParam)
        throws DataPropertyException {
        String paramName = initParam.getString(ResourceModel.P_ID_PARAM_NAME);
        if (paramName == null || paramName.equals("")) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "ResourceBundleDataPropertyHandlerUtil.param.ResourceDetailNotDeclared");
            } catch (MissingResourceException ex) {
            }
            throw new DataPropertyException(message);
        }
        return paramName;
    }

    /**
     * パラメータノードからパラメータ値を取得します。
     * 
     * @param initParam
     * @return String 
     * @throws DataPropertyException
     */
    private String getResourceParamValue(XMLNode initParam)
        throws DataPropertyException {
        String paramValue = initParam.getString(ResourceModel.P_ID_PARAM_VALUE);
        if (paramValue == null || paramValue.equals("")) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "ResourceBundleDataPropertyHandlerUtil.param.ResourceDetailNotDeclared");
            } catch (MissingResourceException ex) {
            }
            throw new DataPropertyException(
                message
                    + " : resource name = "
                    + initParam.getString(ResourceModel.P_ID_PARAM_NAME));
        }
        return paramValue;

    }
}