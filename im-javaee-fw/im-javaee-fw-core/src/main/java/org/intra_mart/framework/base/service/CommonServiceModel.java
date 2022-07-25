/*
 * CommonServiceModel.java
 *
 * Created on 2004/01/12, 12:00
 */

package org.intra_mart.framework.base.service;

import java.util.Collection;

/**
 * CommonService情報を管理するクラスです。
 * 
 * @author INTRAMART
 * @since 5.0
 */
class CommonServiceModel {

	static final String ID = "service-config";

	static final String P_ID_CLIENT_ENCODING = "client-encoding";

	static final String P_ID_ENCODING_ATTRIBUTE_NAME = "encoding-attribute-name";

	static final String P_ID_CLIENT_LOCALE = "client-locale";

	static final String P_ID_LOCALE_ATTRIBUTE = "locale-attribute-name";

	static final String P_ID_CONTEXT_PATH = "context-path";

	static final String P_ID_SERVICE_SERVLET = "service-servlet";

	static final String P_ID_APPLICATION_PARAM_NAME = "application-param-name";

	static final String P_ID_SERVICE_PARAM_NAME = "service-param-name";

	static final String P_ID_EXCEPTION_ATTRIBUTE_NAME = "exception-attribute-name";

	static final String P_ID_CACHE_RULE = "cache-rule";

	static final String P_ID_CACHE_CONDITION = "cache-condition";

	static final String P_ID_CACHE_CONDITION_CLASS = "cache-condition-class";

	static final String P_ID_INIT_PARAM = "init-param";

	static final String P_ID_PARAM_NAME = "param-name";

	static final String P_ID_PARAM_VALUE = "param-value";

	static final String P_ID_CACHE = "cache";

	static final String P_ID_CACHE_CLASS = "cache-class";

	private String clientEncoding;

	private String encodingAttributeName;

	private String clientLocale;

	private String localeAttributeName;

	private String contextPath;

	private String serviceServletPath;

	private String applicationParamName;

	private String serviceParamName;

	private String exceptionAttributeName;

	private ErrorPageModel inputErrorPage;

	private ErrorPageModel systemErrorPage;

	private ErrorPageModel serviceErrorPage;

	private Collection cacheRuleInfos;

	// 多言語対応
	private CommonServiceModel parent;

	/**
	 * EncodingAttributeNameを設定します。
	 * 
	 * @param encodingAttributeName
	 */
	void setEncodingAttributeName(String encodingAttributeName) {
		this.encodingAttributeName = encodingAttributeName;
	}

	/**
	 * EncodingAttributeNameを取得します。
	 * 
	 * @return String EncodingAttributeName
	 */
	String getEncodingAttributeName() {
		return encodingAttributeName;
	}

	/**
	 * ClientLocaleを設定します。
	 * 
	 * @param clientLocale
	 */
	void setClientLocale(String clientLocale) {
		this.clientLocale = clientLocale;
	}

	/**
	 * ClientLocaleを取得します。
	 * 
	 * @return String ClientLocale
	 */
	String getClientLocale() {
		return clientLocale;
	}

	/**
	 * LocaleAttributeNameを設定します。
	 * 
	 * @param localeAttributeName
	 */
	void setLocaleAttributeName(String localeAttributeName) {
		this.localeAttributeName = localeAttributeName;
	}

	/**
	 * LocaleAttributeNameを取得します。
	 * 
	 * @return String LocaleAttributeName
	 */
	String getLocaleAttributeName() {
		return localeAttributeName;
	}

	/**
	 * ContextPathを設定します。
	 * 
	 * @param contextPath
	 */
	void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * ContextPathを取得します。
	 * 
	 * @return String ContextPath
	 */
	String getContextPath() {
		return contextPath;
	}

	/**
	 * ServiceServletPathを設定します。
	 * 
	 * @param serviceServletPath
	 */
	void setServiceServletPath(String serviceServletPath) {
		this.serviceServletPath = serviceServletPath;
	}

	/**
	 * ServiceServletPathを取得します。
	 * 
	 * @return String ServiceServletPath
	 */
	String getServiceServletPath() {
		return serviceServletPath;
	}

	/**
	 * ApplicationParamNameを設定します。
	 * 
	 * @param applicationParamName
	 */
	void setApplicationParamName(String applicationParamName) {
		this.applicationParamName = applicationParamName;
	}

	/**
	 * ApplicationParamNameを取得します。
	 * 
	 * @return String ApplicationParamName
	 */
	String getApplicationParamName() {
		return applicationParamName;
	}

	/**
	 * ServiceParamNameを設定します。
	 * 
	 * @param serviceParamName
	 */
	void setServiceParamName(String serviceParamName) {
		this.serviceParamName = serviceParamName;
	}

	/**
	 * ServiceParamNameを取得します。
	 * 
	 * @return String ServiceParamName
	 */
	String getServiceParamName() {
		return serviceParamName;
	}

	/**
	 * ClientEncodingを設定します。
	 * 
	 * @param clientEncoding
	 */
	void setClientEncoding(String clientEncoding) {
		this.clientEncoding = clientEncoding;
	}

	/**
	 * ClientEncodingを取得します。
	 * 
	 * @return String ClientEncoding
	 */
	String getClientEncoding() {
		return clientEncoding;
	}

	/**
	 * ExceptionAttributeNameを設定します。
	 * 
	 * @param exceptionAttributeName
	 */
	void setExceptionAttributeName(String exceptionAttributeName) {
		this.exceptionAttributeName = exceptionAttributeName;
	}

	/**
	 * ExceptionAttributeNameを取得します。
	 * 
	 * @return String ExceptionAttributeName
	 */
	String getExceptionAttributeName() {
		return exceptionAttributeName;
	}

	/**
	 * CacheRuleInfoを設定します。
	 * 
	 * @param cacheRuleInfos
	 */
	void setCacheRuleInfos(Collection cacheRuleInfos) {
		this.cacheRuleInfos = cacheRuleInfos;
	}

	/**
	 * CacheRuleInfoを取得します。
	 * 
	 * @return Collection CacheRuleInfo
	 */
	Collection getCacheRuleInfos() {
		return cacheRuleInfos;
	}

	/**
	 * InputErrorPageを設定します。
	 * 
	 * @param inputErrorPage
	 */
	void setInputErrorPage(ErrorPageModel inputErrorPage) {
		this.inputErrorPage = inputErrorPage;
	}

	/**
	 * InputErrorPageを取得します。
	 * 
	 * @return ErrorPageModel InputErrorPage
	 */
	ErrorPageModel getInputErrorPage() {
		return inputErrorPage;
	}

	/**
	 * SystemErrorPageを設定します。
	 * 
	 * @param systemErrorPage
	 */
	void setSystemErrorPage(ErrorPageModel systemErrorPage) {
		this.systemErrorPage = systemErrorPage;
	}

	/**
	 * SystemErrorPageを取得します。
	 * 
	 * @return ErrorPageModel SystemErrorPage
	 */
	ErrorPageModel getSystemErrorPage() {
		return systemErrorPage;
	}

	/**
	 * ServiceErrorPageを設定します。
	 * 
	 * @param serviceErrorPage
	 */
	void setServiceErrorPage(ErrorPageModel serviceErrorPage) {
		this.serviceErrorPage = serviceErrorPage;
	}

	/**
	 * ServiceErrorPageを取得します。
	 * 
	 * @return ErrorPageModel ServiceErrorPage
	 */
	ErrorPageModel getServiceErrorPage() {
		return serviceErrorPage;
	}

	/**
	 * InputErrorPagePathを取得します。
	 * 
	 * @return String InputErrorPagePath
	 */
	String getInputErrorPagePath() {
		String result = null;
		if (inputErrorPage != null) {
			result = inputErrorPage.getErrorPage();
		}
		if (result == null && parent != null) {
			result = parent.getInputErrorPagePath();
		}
		return result;
	}

	/**
	 * ServiceErrorPagePathを取得します。
	 * 
	 * @return String ServiceErrorPagePath
	 */
	String getServiceErrorPagePath() {
		String result = null;
		if (serviceErrorPage != null) {
			result = serviceErrorPage.getErrorPage();
		}
		if (result == null && parent != null) {
			result = parent.getServiceErrorPagePath();
		}
		return result;
	}

	/**
	 * SystemErrorPagePathを取得します。
	 * 
	 * @return String SystemErrorPagePath
	 */
	String getSystemErrorPagePath() {
		String result = null;
		if (systemErrorPage != null) {
			result = systemErrorPage.getErrorPage();
		}
		if (result == null && parent != null) {
			result = parent.getSystemErrorPagePath();
		}
		return result;
	}

	/**
	 * CommonServiceModelを設定します。
	 * 
	 * @param parent
	 */
	void setParent(CommonServiceModel parent) {
		this.parent = parent;
	}

	/**
	 * CommonServiceModelを取得します。
	 * 
	 * @return CommonServiceModel
	 */
	CommonServiceModel getParent() {
		return parent;
	}

}