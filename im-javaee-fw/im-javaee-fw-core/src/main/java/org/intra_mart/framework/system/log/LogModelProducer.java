/*
 * 作成日: 2003/12/25
 */
package org.intra_mart.framework.system.log;

import java.io.IOException;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.framework.util.XMLDocumentProducer;
import org.intra_mart.framework.util.XMLNode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * ログモデルを作成するクラスです。
 * @author INTRAMART
 * @version 1.0
 */
class LogModelProducer {

    /**
     * ログモデルを作成します。
     * @param fileName　XMLファイル
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws LogPropertyException
     * @throws IllegalArgumentException
     */
    LogModel createLogModel(String fileName)
        throws
            ParserConfigurationException,
            SAXException,
            LogPropertyException,
            IOException,
            IllegalArgumentException {
        XMLDocumentProducer producer = new XMLDocumentProducer();
        Document doc = producer.getDocument(fileName);
        Node node = producer.getRoot(doc);
        XMLNode root = new XMLNode(node);
        LogModel model = getLogModel(root);
        return model;
    }

    /**
     * ノードから値を取り出し、その値をLogModelにセットします。
     * @param node
     * @param model
     * @throws LogPropertyException
     */
    private LogModel getLogModel(XMLNode node)
        throws LogPropertyException {
        LogModel model = new LogModel();
        model.setLogAgentName(getLogAgentName(node));
        model.setLogAgentParams(getLogAgentParams(node));
        return model;
    }

    /**
     * ノードからLogAgent名を取得します。
     * @param node
     * @return
     * @throws LogPropertyException
     */
    private String getLogAgentName(XMLNode node) throws LogPropertyException {
        String logAgentName = node.getString(LogModel.P_ID_AGENT_NAME);
        if (logAgentName == null || logAgentName.equals("")) {
            return null;
        }
        return logAgentName;
    }

    /**
     * LogAgentParamを取得します。
     * @param root　XMLファイルのルートノード
     * @return
     * @throws LogPropertyException
     */
    private LogAgentParam[] getLogAgentParams(XMLNode root)
        throws LogPropertyException {
        XMLNode[] paramNodes = root.select(LogModel.P_ID_INIT_PARAM);
        LogAgentParam[] params = new LogAgentParam[paramNodes.length];

        for (int i = 0; i < params.length; i++) {
            params[i] = getLogAgentParam(paramNodes[i]);
        }
        return params;
    }

    /**
     * LogAgentParamを取得します。
     * @param paramNode　パラメータノード
     * @param param　LogAgentParamオブジェクト
     */
    private LogAgentParam getLogAgentParam(XMLNode paramNode)
        throws LogPropertyException {
        LogAgentParam param = new LogAgentParam();
        param.setName(getLogAgentParamName(paramNode));
        param.setValue(getLogAgentParamValue(paramNode));
        return param;
    }

    /**
     * パラメータノードからパラメータ名を取得します。
     * @param  paramNode　パラメータノード
     * @return String パラメータ名
     * @throws DataPropertyException
     */
    private String getLogAgentParamName(XMLNode paramNode)
        throws LogPropertyException {
        String paramName = paramNode.getString(LogModel.P_ID_PARAM_NAME);
        if (paramName == null || paramName.equals("")) {
            String message = null;
            try {
                message =
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.system.log.i18n")
                            .getString("LogManager.FailedToGetAgentParameters");
            } catch (MissingResourceException ex) {
            }
            throw new LogPropertyException(message);
        }
        return paramName;
    }

    /**
     * パラメータノードからパラメータ値を取得します。
     * @param paramNode パラメータノード
     * @return String パラメータ値
     * @throws LogPropertyException
     */
    private String getLogAgentParamValue(XMLNode paramNode)
        throws LogPropertyException {
        String paramValue = paramNode.getString(LogModel.P_ID_PARAM_VALUE);
        if (paramValue == null || paramValue.equals("")) {
            String message = null;
            try {
                message =
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.system.log.i18n")
                            .getString("LogManager.FailedToGetAgentParameters");
            } catch (MissingResourceException ex) {
            }
            throw new LogPropertyException(message);
        }
        return paramValue;
    }
}
