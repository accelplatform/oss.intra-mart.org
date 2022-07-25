/*
 * 作成日: 2004/01/06
 */
package org.intra_mart.framework.system.log;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;

/**
 * リソースからログプロパティ情報を取得するプロパティハンドラです。
 * @author INTRAMART
 * @version 1.0
 */
public class XmlLogPropertyHandler implements LogPropertyHandler {

	/**
	 * デフォルトのXMLファイル名
	 */
	public static final String DEFAULT_BUNDLE_NAME = "log-config";

	/**
	 * XMLファイル名のパラメータ名
	 */
	public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

	/**
	 * ログリソース情報が設定されているモデル
	 */
	private LogModel model;

	/**
	 * XMLLogPropertyHandlerを新規に生成します。
	 */
	public XmlLogPropertyHandler() {
		this.model = null;
	}

	/**
	 * プロパティハンドラを初期化します。
	 *
	 * @param params 初期パラメータ
	 * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
	 */
	public void init(PropertyParam[] params) throws PropertyHandlerException {
		String xmlName = null;

		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				if (params[i].getName().equals(DEFAULT_BUNDLE_NAME_PARAM)) {
					xmlName = params[i].getValue();
				}
			}
		}
		if (xmlName == null) {
			xmlName = DEFAULT_BUNDLE_NAME;
		}

		this.model = createLogModel(xmlName);
	}

	/**
	 * LogModelを生成します。
	 * @param xmlName
	 * @return LogModel 
	 * @throws PropertyHandlerException
	 */
	private LogModel createLogModel(String xmlName) throws PropertyHandlerException{
		try{
			LogModelProducer producer = new LogModelProducer();
			LogModel result = producer.createLogModel(xmlName);
			return result;
		}catch (ParserConfigurationException e){
			throw new PropertyHandlerException(e.getMessage() ,e);
		}catch (SAXException e) {
			throw new PropertyHandlerException(e.getMessage() ,e);
		}catch (IOException e) {
			throw new PropertyHandlerException(e.getMessage() ,e );
		}catch (IllegalArgumentException e) {
			throw new PropertyHandlerException(e.getMessage() ,e );			
		}catch(LogPropertyException e) {
			throw new PropertyHandlerException(e.getMessage() ,e );
		}
	}
	/**
	 * ログエージェントのクラス名を取得します。
	 * 何も設定されていない場合nullが返ります。
	 * @return ログエージェントのクラス名（設定されていない場合null）
	 * @throws LogPropertyException ログエージェントのクラス名の取得時に例外が発生
	 */
	public String getLogAgentName() throws LogPropertyException {
		return model.getLogAgentName();
	}

	/**
	 * ログエージェントのパラメータを取得します。
	 * 何も設定されていない場合大きさが0の配列が返ります。
	 *
	 * @return ログエージェントのパラメータの配列
	 * @throws LogPropertyException ログエージェントのパラメータの取得時に例外が発生
	 */
	public LogAgentParam[] getLogAgentParams() throws LogPropertyException {
		return model.getLogAgetnParams();
	}
}

