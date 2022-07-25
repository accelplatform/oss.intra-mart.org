/*
 * CommonServiceModelProducer.java
 *
 * Created on 2004/01/12, 17:48
 */

package org.intra_mart.framework.base.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.framework.system.property.PropertyParam;
import org.intra_mart.framework.util.XMLDocumentProducer;
import org.intra_mart.framework.util.XMLNode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 共通リソースモデルを作成するクラスです。
 * 
 * @author INTRAMART
 * @since 5.0
 */
class CommonServiceModelProducer {

	/**
	 * 共通リソースモデルを作成します。
	 * 
	 * @param fileName
	 *            ファイル名
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	CommonServiceModel createCommonServiceModel(String fileName)
			throws ParserConfigurationException, SAXException, IOException {

		CommonServiceModel model = null;
		XMLDocumentProducer producer = new XMLDocumentProducer();
		Document doc = producer.getDocument(fileName);
		Node node = producer.getRoot(doc);
		XMLNode root = new XMLNode(node);
		model = getCommonServiceModel(root);

		return model;
	}

	/**
	 * 共通リソースモデルを取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return CommonServiceModel
	 */
	private CommonServiceModel getCommonServiceModel(XMLNode root) {
		CommonServiceModel model = new CommonServiceModel();

		// 入力エラーの取得
		XMLNode inputErrorPage = root.lookup(ErrorPageModel.P_ID_INPUT_ERROR);
		model.setInputErrorPage(getInputErrorPage(inputErrorPage));

		// サービスエラーの取得
		XMLNode serviceErrorPage = root
				.lookup(ErrorPageModel.P_ID_SERVICE_ERROR);
		model.setServiceErrorPage(getServiceErrorPage(serviceErrorPage));

		// システムエラーの取得
		XMLNode systemErrorPage = root.lookup(ErrorPageModel.P_ID_SYSTEM_ERROR);
		model.setSystemErrorPage(getSystemErrorPage(systemErrorPage));

		// モデルに値を設定
		model.setClientEncoding(getClientEncoding(root));
		model.setEncodingAttributeName(getEncodingAttributeName(root));
		model.setClientLocale(getClientLocale(root));
		model.setLocaleAttributeName(getLocaleAttributeName(root));
		model.setContextPath(getContextPath(root));
		model.setServiceServletPath(getServiceServlet(root));
		model.setApplicationParamName(getApplicationParamName(root));
		model.setServiceParamName(getServiceParamName(root));
		model.setExceptionAttributeName(getExceptionAttributeName(root));
//		model.setCacheRuleInfos(getCacheRuleInfos(root));

		return model;
	}

	/**
	 * 例外属性名を取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String 例外属性名
	 */
	private String getExceptionAttributeName(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_EXCEPTION_ATTRIBUTE_NAME);
	}

	/**
	 * サービスパラメータ名を取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String サービスパラメータ名
	 */
	private String getServiceParamName(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_SERVICE_PARAM_NAME);
	}

	/**
	 * アプリケーションパラメータ名を取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String アプリケーションパラメータ名
	 */
	private String getApplicationParamName(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_APPLICATION_PARAM_NAME);
	}

	/**
	 * サービスサーブレットパスを読み込みます。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String サービスサーブレットパス
	 */
	private String getServiceServlet(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_SERVICE_SERVLET);
	}

	/**
	 * コンテキストパスを取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String コンテキストパス
	 */
	private String getContextPath(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_CONTEXT_PATH);
	}

	/**
	 * ロケール属性名を取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String ロケール属性名
	 */
	private String getLocaleAttributeName(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_LOCALE_ATTRIBUTE);
	}

	/**
	 * クライアントローケルを取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String クライアントローケル
	 */
	private String getClientLocale(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_CLIENT_LOCALE);
	}

	/**
	 * エンコーディング属性名を取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String エンコーディング属性名
	 */
	private String getEncodingAttributeName(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_ENCODING_ATTRIBUTE_NAME);
	}

	/**
	 * クライアントエンコーディングを取得します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return String クライアントエンコーディング
	 */
	private String getClientEncoding(XMLNode root) {
		return root.getString(CommonServiceModel.P_ID_CLIENT_ENCODING);
	}

	/**
	 * キャッシュルール情報コレクションを取得します。 書かれていないときは空のコレクションを返します。
	 * 
	 * @param root
	 *            ルートノード
	 * @return Collection キャッシュルール情報
	 */
//	private Collection getCacheRuleInfos(XMLNode root) {
//		XMLNode[] cacheRuleNodes = root
//				.select(CommonServiceModel.P_ID_CACHE_RULE);
//		ArrayList list = new ArrayList();
//		for (int i = 0; i < cacheRuleNodes.length; i++) {
//			CacheRuleInfo info = getCacheRuleInfo(cacheRuleNodes[i]);
//			list.add(info);
//		}
//
//		return list;
//	}

	/**
	 * キャッシュルール情報を取得します。
	 * 
	 * @param node
	 *            キャッシュルールノード
	 * @return CacheRuleInfo キャッシュルール情報
	 */
//	private CacheRuleInfo getCacheRuleInfo(XMLNode cacheRuleNode) {
//		CacheRuleInfo info = new CacheRuleInfo();
//		String cacheConditionClass = getCacheConditionClass(cacheRuleNode);
//		String cacheClass = getCacheClass(cacheRuleNode);
//		PropertyParam[] conditionParams = getConditionParams(cacheRuleNode);
//		PropertyParam[] cacheParams = getCacheParams(cacheRuleNode);
//
//		info.setCondition(cacheConditionClass);
//		info.setCache(cacheClass);
//		info.setConditionParameters(conditionParams);
//		info.setCacheParameters(cacheParams);
//
//		return info;
//	}

	/**
	 * キャッシュ条件クラスを取得します。
	 * 
	 * @param node
	 *            キャッシュルールノード
	 * @return String キャッシュ条件クラス
	 */
	private String getCacheConditionClass(XMLNode cacheRuleNode) {

		StringBuffer path = new StringBuffer();
		path.append(CommonServiceModel.P_ID_CACHE_CONDITION).append("/")
				.append(CommonServiceModel.P_ID_CACHE_CONDITION_CLASS);

		String cacheConditionClass = cacheRuleNode.getString(path.toString());
		return cacheConditionClass;

	}

	/**
	 * キャッシュクラスを取得します。
	 * 
	 * @param node
	 *            キャッシュルールノード
	 * @return String キャッシュクラス
	 */
	private String getCacheClass(XMLNode cahceRuleNode) {
		StringBuffer path = new StringBuffer();
		path.append(CommonServiceModel.P_ID_CACHE).append("/").append(
				CommonServiceModel.P_ID_CACHE_CLASS);
		String cacheClass = cahceRuleNode.getString(path.toString());
		return cacheClass;
	}

	/**
	 * キャッシュ条件クラスのパラメータを取得します。
	 * 
	 * @param cacheRuleNode
	 *            キャッシュルールノード
	 * @return PropertyParam[] キャッシュ条件クラスのパラメータ
	 */
	private PropertyParam[] getConditionParams(XMLNode cacheRuleNode) {
		StringBuffer initParamPath = new StringBuffer();
		initParamPath.append(CommonServiceModel.P_ID_CACHE_CONDITION).append(
				"/").append(CommonServiceModel.P_ID_INIT_PARAM);

		XMLNode[] initParamNodes = cacheRuleNode.select(initParamPath
				.toString());
		PropertyParam[] params = new PropertyParam[initParamNodes.length];

		for (int i = 0; i < initParamNodes.length; i++) {
			params[i] = getInitParam(initParamNodes[i]);
		}
		return params;
	}

	/**
	 * キャッシュクラスのパラメータを取得します。
	 * 
	 * @param cacheRuleNode
	 *            キャッシュルールノード
	 * @return PropertyParam[] キャッシュクラスのパラメータ
	 */
	private PropertyParam[] getCacheParams(XMLNode cacheRuleNode) {
		StringBuffer initParamPath = new StringBuffer();
		initParamPath.append(CommonServiceModel.P_ID_CACHE).append("/").append(
				CommonServiceModel.P_ID_INIT_PARAM);

		XMLNode[] initParamNodes = cacheRuleNode.select(initParamPath
				.toString());
		PropertyParam[] params = new PropertyParam[initParamNodes.length];

		for (int i = 0; i < initParamNodes.length; i++) {
			params[i] = getInitParam(initParamNodes[i]);
		}
		return params;
	}

	/**
	 * パラメータを取得します。
	 * 
	 * @param initNode
	 * @return PropertyParam パラメータ
	 */
	private PropertyParam getInitParam(XMLNode initNode) {
		PropertyParam param = new PropertyParam();
		param.setName(initNode.getString(CommonServiceModel.P_ID_PARAM_NAME));
		param.setValue(initNode.getString(CommonServiceModel.P_ID_PARAM_VALUE));
		return param;
	}

	/**
	 * 入力エラーページを取得します。
	 * 
	 * @param node
	 * @return ErrorPageModel 入力エラーページ
	 */
	private ErrorPageModel getInputErrorPage(XMLNode node) {
		return getErrorPage(node);
	}

	/**
	 * サービスエラーページを取得します。
	 * 
	 * @param node
	 * @return ErrorPageModel サービスエラーページ
	 */
	private ErrorPageModel getServiceErrorPage(XMLNode node) {
		return getErrorPage(node);
	}

	/**
	 * システムエラーページを取得
	 * 
	 * @param node
	 * @return ErrorPageModel システムエラーページ
	 */
	private ErrorPageModel getSystemErrorPage(XMLNode node) {
		return getErrorPage(node);
	}

	/**
	 * エラーページのモデルを取得
	 * 
	 * @param node
	 * @return ErrorPageModel エラーページ
	 */
	private ErrorPageModel getErrorPage(XMLNode node) {
		ErrorPageModel errorPage = new ErrorPageModel();
		String page = null;

		if (node != null) {
			page = node.getString(ErrorPageModel.P_ID_ERROR_PAGE);
		}
		errorPage.setErrorPage(page);

		return errorPage;
	}
}