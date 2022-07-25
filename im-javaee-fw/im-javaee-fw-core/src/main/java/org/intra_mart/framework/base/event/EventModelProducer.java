/*
 * 作成日: 2003/12/23
 */
package org.intra_mart.framework.base.event;

import java.io.IOException;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.framework.util.XMLDocumentProducer;
import org.intra_mart.framework.util.XMLNode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * イベント情報を作成するクラスです。
 * @author INTRAMART
 * @version 1.0
 * 
 */
public class EventModelProducer {
    private String application;
    private String key;

    /**
     * イベントモデルを作成します。
     * @param fileName
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
     EventGroupModel createEventModel(
        String application,
        String key,
        String prefix)
        throws
            ParserConfigurationException,
            SAXException,
            IOException,
            EventPropertyException,
            IllegalArgumentException {
        this.application = application;
        this.key = key;

        EventGroupModel model = null;
        XMLDocumentProducer producer = new XMLDocumentProducer();
        
        String fileName = (getPropertyPackage(application)
			+ prefix + "-"
			+ getApplicationID(application));
        
        Document doc = producer.getDocument(fileName);
        Node node = producer.getRoot(doc);
        XMLNode root = new XMLNode(node);
        XMLNode groupNode = selectXmlNodeByKey(root, key);
        model = getEventGroupModel(groupNode);
        return model;
    }

	/**
	 * EventGroupModelを取得します。
	 * @param groupNode
	 * @return EventGroupModel 
	 * @throws EventPropertyException
	 */
    private EventGroupModel getEventGroupModel(XMLNode groupNode)
        throws EventPropertyException {
        EventGroupModel model = new EventGroupModel();
        model.setEventKey(groupNode.getString(EventGroupModel.P_ID_EVENT_KEY));
        model.setEventName(
            groupNode.getString(EventGroupModel.P_ID_EVENT_NAME));

        model.setEventFactory(getEventFactoryModel(groupNode));
        model.setPreTriggerInfos(getPreTriggerInfos(application, groupNode));
        model.setPostTriggerInfos(getPostTriggerInfos(application, groupNode));
        return model;
    }

    /**
     * ノードの中からevent-keyの値が、targetKeyの値を一致する物を取り出します。
     * @param node xmlファイルのルートノード
     * @param targetKey	イベントキー
     * @return	result イベントグループ
     */
    private XMLNode selectXmlNodeByKey(XMLNode node, String targetKey)
        throws EventPropertyException {
        XMLNode[] groups = node.select(EventGroupModel.ID);
        XMLNode groupNode = null;
        for (int i = 0; i < groups.length; i++) {
            String key = groups[i].getString(EventGroupModel.P_ID_EVENT_KEY);
            if (targetKey.equals(key)) {
                groupNode = groups[i];
                break;
            }
        }
        if (groupNode == null) {
            throw new EventPropertyException(
                "application = " + application + ", key = " + targetKey);
        }
        return groupNode;
    }

    /**
     * EventFactoryModelを取得します。
     * @param eventGroupNode イベントグループノード
     * @return EventFactoryModel イベントファクトリ
     * @throws EventPropertyException
     */
    private EventFactoryModel getEventFactoryModel(XMLNode eventGroupNode)
        throws EventPropertyException {
        EventFactoryModel factory = new EventFactoryModel();
        factory.setFactoryName(getFactoryName(eventGroupNode));
        factory.setFactoryParams(getEventFactoryParams(eventGroupNode));
        return factory;
    }

    /**
     * グループノードがらイベントファクトリーリスナーの初期パラメータを取り出します。
     * @param groupNode　グループノード
     * @return	factoryParams イベントファクトリーリスナーの初期パラメータ。初期パラメータが設定されていないときは長さ０の配列を返す。
     * @throws EventPropertyException
     */
    private EventListenerFactoryParam[] getEventFactoryParams(XMLNode groupNode)
        throws EventPropertyException {
        EventListenerFactoryParam[] factoryParams;

        //グループノードからファクトリーパラメータノードの一覧を取り出す。
        XMLNode[] paramNodes =
            groupNode.select(
                EventFactoryModel.ID
                    + "/"
                    + EventFactoryModel.P_ID_FACTORY_PARAM);

        if (paramNodes != null) {
            factoryParams = new EventListenerFactoryParam[paramNodes.length];
            for (int i = 0; i < paramNodes.length; i++) {
                factoryParams[i] = new EventListenerFactoryParam();
                factoryParams[i].setName(
                    paramNodes[i].getString(EventFactoryModel.P_ID_PARAM_NAME));
                factoryParams[i].setValue(
                    paramNodes[i].getString(
                        EventFactoryModel.P_ID_PARAM_VALUE));

                //パラメータ名かパラメータ値のどちらかがnullの時は例外を発生させる。
                if (factoryParams[i].getName() == null
                    || factoryParams[i].getValue() == null) {
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("ResourceBundleEventPropertyHandlerUtil.FailedToGetFactoryParameter");
                    } catch (MissingResourceException ex) {
                    }
                    throw new EventPropertyException(
                        message
                            + " : application = "
                            + application
                            + ", key = "
                            + groupNode.getString(
                                EventGroupModel.P_ID_EVENT_KEY));
                }
            }
        } else {
            //初期パラメータが設定されていないときは、長さゼロの配列を返す.
            factoryParams = new EventListenerFactoryParam[0];
        }
        return factoryParams;
    }

    /**　
     * イベント前トリガ情報を取得します。
     * @param groupNode
     * @return イベント前トリガーの情報。設定されていない場合は、内容がからのCollectionが返される。
     * @throws EventPropertyException
     */
    private Collection getPreTriggerInfos(String application, XMLNode groupNode)
        throws EventPropertyException {
        TreeMap infos = new TreeMap();
        XMLNode[] triggerNodes = groupNode.select(EventGroupModel.P_ID_PRE_TRIGGER);
        if (triggerNodes != null) {
            for (int i = 0; i < triggerNodes.length; i++) {
                EventTriggerInfo info = new EventTriggerInfo();
                info.setNumber(i + 1);
                // イベントトリガのクラス名を設定する
                String triggerClass =
                    triggerNodes[i].getString(
                        EventGroupModel.P_ID_TRIGGER_CLASS);
                if (triggerClass == null || triggerClass.equals("")) {
                    // イベントトリガ名を取得できない場合例外を発生させる。
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("ResourceBundleEventPropertyHandlerUtil.TriggerNotDeclared");
                    } catch (MissingResourceException ex) {
                    }
                    throw new EventPropertyException(
                        message
                            + " : application = "
                            + application
                            + ", key = "
                            + groupNode.getString(
                                EventGroupModel.P_ID_EVENT_KEY));
                }
                info.setName(triggerClass);

                //イベントトリガーに情報を追加する。
                infos.put(new Integer(i), info);
            }
        }
        return infos.values();
    }

    /**　
     * イベント後トリガ情報を取得します。
     * @param groupNode
     * @return イベント後トリガーの情報。設定されていない場合は、内容がからのCollectionが返される。
     * @throws EventPropertyException
     */
    private Collection getPostTriggerInfos(String application, XMLNode groupNode)
        throws EventPropertyException {
        TreeMap infos = new TreeMap();
        XMLNode[] triggerNodes = groupNode.select(EventGroupModel.P_ID_POST_TRIGGER);
        if (triggerNodes != null) {
            for (int i = 0; i < triggerNodes.length; i++) {
                EventTriggerInfo info = new EventTriggerInfo();
                info.setNumber(i + 1);
                // イベントトリガのクラス名を設定する
                String triggerClass =
                    triggerNodes[i].getString(
                        EventGroupModel.P_ID_TRIGGER_CLASS);
                if (triggerClass == null || triggerClass.equals("")) {
                    // イベントトリガ名を取得できない場合例外を発生させる。
                    String message = null;
                    try {
                        message =
                            ResourceBundle
                                .getBundle("org.intra_mart.framework.base.event.i18n")
                                .getString("ResourceBundleEventPropertyHandlerUtil.TriggerNotDeclared");
                    } catch (MissingResourceException ex) {
                    }
                    throw new EventPropertyException(
                        message
                            + " : application = "
                            + application
                            + ", key = "
                            + groupNode.getString(
                                EventGroupModel.P_ID_EVENT_KEY));
                }
                info.setName(triggerClass);

                //イベントトリガーに情報を追加する。
                infos.put(new Integer(i), info);
            }
        }
        return infos.values();
    }
    
    /**
     * イベントリスナファクトリ名を取得します。 
     * @param groupNode
     * @return	イベントリスナーファクトリーのクラス名を返す。
     * @throws EventPropertyException イベントリスナーファクトリーが設定されたな場合は、例外を発生させる。
     */
    private String getFactoryName(XMLNode groupNode)
        throws EventPropertyException {
        String name =
            groupNode.getString(
                EventFactoryModel.ID
                    + "/"
                    + EventFactoryModel.P_ID_FACTORY_CLASS);
        if (name == null || name.trim().equals("")) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("ResourceBundleEventPropertyHandlerUtil.FacotryClassNotDeclared");
            } catch (MissingResourceException e) {
            }
            throw new EventPropertyException(
                message
                    + " : application = "
                    + application
                    + ", key = "
                    + groupNode.getString(EventGroupModel.P_ID_EVENT_KEY));
        }
        return name;

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
}
