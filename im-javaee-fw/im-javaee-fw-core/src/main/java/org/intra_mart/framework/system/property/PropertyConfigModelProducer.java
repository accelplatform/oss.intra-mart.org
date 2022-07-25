/*
 * ServiceConfigModelProducer.java
 *
 * Created on 2004/01/12, 14:15
 */
package org.intra_mart.framework.system.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.framework.util.XMLDocumentProducer;
import org.intra_mart.framework.util.XMLNode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * プロパティコンフィグモデルを作成するクラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
class PropertyConfigModelProducer {

    /**
     * PropertyConfigModelを取得します。
     * @param fileName XMLリソース名
     * @return ServiceConfigModel 
     * @throws IllegalArgumentException 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    protected PropertyConfigModel createPropertyConfigModel(String fileName)
        throws
            IllegalArgumentException,
            ParserConfigurationException,
            SAXException,
            IOException,
            Exception {

        PropertyConfigModel model = null;
        XMLDocumentProducer producer = new XMLDocumentProducer();
        Document doc = producer.getDocument(fileName);
        Node node = producer.getRoot(doc);
        XMLNode root = new XMLNode(node);
        model = getPropertyConfigModel(root);
        return model;
    }

    /**
     * PropertyConfigModelを取得します。
     * @param rootNode
     * @return PropertyConfigModel
     */
    private PropertyConfigModel getPropertyConfigModel(XMLNode node) {
        PropertyConfigModel model = new PropertyConfigModel();
        Map map = new HashMap();
	    map.put(PropertyConfigModel.SERVICE_ID, getPropertyModel(node, PropertyConfigModel.SERVICE_ID)); 
	    map.put(PropertyConfigModel.EVENT_ID, getPropertyModel(node, PropertyConfigModel.EVENT_ID));
	    map.put(PropertyConfigModel.DATA_ID, getPropertyModel(node, PropertyConfigModel.DATA_ID));
//	    map.put(PropertyConfigModel.SESSION_ID, getPropertyModel(node, PropertyConfigModel.SESSION_ID)); 
//	    map.put(PropertyConfigModel.MESSAGE_ID, getPropertyModel(node, PropertyConfigModel.MESSAGE_ID));
	    map.put(PropertyConfigModel.I18NMESSAGE_ID, getPropertyModel(node, PropertyConfigModel.I18NMESSAGE_ID)); 
	    map.put(PropertyConfigModel.LOG_ID, getPropertyModel(node, PropertyConfigModel.LOG_ID));
        model.setProperties(map);
        return model;
    }

    /**
     * プロパティモデルを取得します
     * 
     * @param node ノード
     * @param propId ノード
     * @return プロパティモデル
     */
    private PropertyModel getPropertyModel(XMLNode node, String propId) {
        PropertyModel model = new PropertyModel();
        XMLNode subNode = node.lookup(propId);
        if (subNode != null) {
            String handler = subNode.getString(PropertyConfigModel.HANDLER_ID);
            model.setPropertyHandlerName(handler);
            List list = getInitParamProperties(subNode);
            PropertyHandlerParam[] param = new PropertyHandlerParam[list.size()];
            for ( int i = 0; i < list.size(); i++ ) {
                param[i] = (PropertyHandlerParam) list.get(i); 
            }
            model.setPropertyHandlerParams(param);
        }
        return model;
    }
    
    /**
     * 初期化パラメータのプロパティを取得します。
     * @param node
     * @return
     */
    private List getInitParamProperties(XMLNode node) {
        XMLNode[] paramNode = node.select(PropertyModel.INIT_ID);
        List list = new ArrayList();
        for ( int i = 0; i < paramNode.length; i++ ) {
	        PropertyHandlerParam param = new PropertyHandlerParam();
            param.setName( paramNode[i].getString(PropertyModel.PARAM_NAME_ID) );
            param.setValue( paramNode[i].getString(PropertyModel.PARAM_VALUE_ID) );
            list.add(param);
        }
		return list;
    }
}
