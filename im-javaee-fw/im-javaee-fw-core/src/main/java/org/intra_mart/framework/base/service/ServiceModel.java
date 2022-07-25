/*
 * ServiceModel.java
 *
 * Created on 2004/01/12, 14:15
 */
package org.intra_mart.framework.base.service;

import java.util.HashMap;

/**
 * サービスの情報を管理するクラスです。
 * 
 * @author INTRAMART
 * @since 5.0
 */
class ServiceModel {

    static final String ID = "service";

    static final String P_ID_SERVICE_ID = "service-id";

    static final String P_ID_SERVICE_CONTROLL = "controller-class";

    static final String P_ID_SERVICE_TRANSITION = "transition-class";

    private NextPageModel defaultNextPage;

    private HashMap nextPages = new HashMap();

    private ErrorPageModel defaultInputErrorPage;

    private HashMap inputErrorPages = new HashMap();

    private ErrorPageModel defaultSystemErrorPage;

    private HashMap systemErrorPages = new HashMap();

    private ErrorPageModel defaultServiceErrorPage;

    private HashMap serviceErrorPages = new HashMap();

    private String serviceControllerName;

    private String transitionName;

    private String serviceId;

    /**
     * Transition名を取得します。
     * 
     * @return String Transition名
     */
    String getTransitionName() {
        return transitionName;
    }

    /**
     * Transition名を設定します。
     * 
     * @param transitionName
     */
    void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
    }

    /**
     * DefaultNextPageを設定します。
     * 
     * @param defaultNextPage
     */
    void setDefaultNextPage(NextPageModel defultNextPage) {
        this.defaultNextPage = defultNextPage;
    }

    /**
     * DefaultNextPageを取得します。
     * 
     * @return NextPageModel
     */
    NextPageModel getDefaultNextPage() {
        return defaultNextPage;
    }

    /**
     * NextPageを設定します。
     * 
     * @param nextPages
     */
    void setNextPages(HashMap nextPages) {
        this.nextPages = nextPages;
    }

    /**
     * NextPageを設定します。
     * 
     * @param key
     * @param nextPage
     */
    void setNextPage(String key, NextPageModel nextPage) {
        this.nextPages.put(key, nextPage);
    }

    /**
     * NextPageを取得します。
     * 
     * @return HashMap
     */
    HashMap getNextPages() {
        return nextPages;
    }

    /**
     * DefaultInputErrorPageを設定します。
     * 
     * @param defaultInputErrorPage
     */
    void setDefaultInputErrorPage(ErrorPageModel defaultInputErrorPage) {
        this.defaultInputErrorPage = defaultInputErrorPage;
    }

    /**
     * DefaultInputErrorPageを取得します。
     * 
     * @return ErrorPageModel
     */
    ErrorPageModel getDefaultInputErrorPage() {
        return defaultInputErrorPage;
    }

    /**
     * InputErrorPageを設定します。
     * 
     * @param inputErrorPages
     */
    void setInputErrorPages(HashMap inputErrorPages) {
        this.inputErrorPages = inputErrorPages;
    }

    /**
     * InputErrorPageを設定します。
     * 
     * @param key
     * @param errorPage
     */
    void setInputErrorPage(String key, ErrorPageModel errorPage) {
        this.inputErrorPages.put(key, errorPage);
    }

    /**
     * InputErrorPageを取得します。
     * 
     * @return HashMap InputErrorPages
     */
    HashMap getInputErrorPages() {
        return inputErrorPages;
    }

    /**
     * DefaultSystemErrorPageを設定します。
     * 
     * @param defaultSystemErrorPage
     */
    void setDefaultSystemErrorPage(ErrorPageModel defaultSystemErrorPage) {
        this.defaultSystemErrorPage = defaultSystemErrorPage;
    }

    /**
     * DefaultSystemErrorPageを取得します。
     * 
     * @return ErrorPageModel DefaultSystemErrorPage
     */
    ErrorPageModel getDefaultSystemErrorPage() {
        return defaultSystemErrorPage;
    }

    /**
     * SystemErrorPageを設定します。
     * 
     * @param systemErrorPages
     */
    void setSystemErrorPages(HashMap systemErrorPages) {
        this.systemErrorPages = systemErrorPages;
    }

    /**
     * SystemErrorPageを設定します。
     * 
     * @param key
     * @param errorPage
     */
    void setSystemErrorPage(String key, ErrorPageModel errorPage) {
        this.systemErrorPages.put(key, errorPage);
    }

    /**
     * SystemErrorPageを取得します。
     * 
     * @return HashMap SystemErrorPage
     */
    HashMap getSystemErrorPages() {
        return systemErrorPages;
    }

    /**
     * DefaultServiceErrorPageを設定します。
     * 
     * @param defaultServiceErrorPage
     */
    void setDefaultServiceErrorPage(ErrorPageModel defaultServiceErrorPage) {
        this.defaultServiceErrorPage = defaultServiceErrorPage;
    }

    /**
     * DefaultServiceErrorPageを設定します。
     * 
     * @return ErrorPageModel
     */
    ErrorPageModel getDefaultServiceErrorPage() {
        return defaultServiceErrorPage;
    }

    /**
     * ServiceErrorPageを設定します。
     * 
     * @param serviceErrorPages
     */
    void setServiceErrorPages(HashMap serviceErrorPages) {
        this.serviceErrorPages = serviceErrorPages;
    }

    /**
     * ServiceErrorPageを設定します。
     * 
     * @param key
     * @param errorPage
     */
    void setServiceErrorPage(String key, ErrorPageModel errorPage) {
        this.serviceErrorPages.put(key, errorPage);
    }

    /**
     * ServiceErrorPagesを取得します。
     * 
     * @return HashMap ServiceErrorPage
     */
    HashMap getServiceErrorPages() {
        return serviceErrorPages;
    }

    /**
     * ServiceIDを設定します。
     * 
     * @param serviceId
     */
    void setServiceId(String sericeId) {
        this.serviceId = sericeId;
    }

    /**
     * ServiceIDを取得します。
     * 
     * @return
     */
    String getServiceId() {
        return serviceId;
    }

    /**
     * ServiceControllerNameを設定します。
     * 
     * @param serviceControllerName
     */
    void setServiceControllerName(String serviceControllerName) {
        this.serviceControllerName = serviceControllerName;
    }

    /**
     * ServiceControllerNameを取得します。
     * 
     * @return
     */
    String getServiceControllerName() {
        return serviceControllerName;
    }

    /**
     * InputErrorPagePathを取得します。
     * 
     * @param key
     * @return String InputErrorPagePath
     */
    String getInputErrorPagePath(String key) {
        String result = null;

        ErrorPageModel errorPage = (ErrorPageModel) inputErrorPages.get(key);
        if (errorPage != null) {
            result = errorPage.getErrorPage();
        }

        return result;
    }

    /**
     * InputErrorPagePathを取得します。
     * 
     * @return String InputErrorPagePath
     */
    String getInputErrorPagePath() {
        if (defaultInputErrorPage != null) {
            return defaultInputErrorPage.getErrorPage();
        }
        return null;
    }

    /**
     * ServiceErrorPagePathを取得します。
     * 
     * @param key
     * @return String ServiceErrorPagePath
     */
    String getServiceErrorPagePath(String key) {
        String result = null;

        ErrorPageModel errorPage = (ErrorPageModel) serviceErrorPages.get(key);
        if (errorPage != null) {
            result = errorPage.getErrorPage();
        }

        return result;
    }

    /**
     * ServiceErrorPagePathを取得します。
     * 
     * @return String ServiceErrorPagePath
     */
    String getServiceErrorPagePath() {
        if (defaultServiceErrorPage != null) {
            return defaultServiceErrorPage.getErrorPage();
        }
        return null;
    }

    /**
     * SystemErrorPagePathを取得します。
     * 
     * @param key
     * @return String SystemErrorPagePath
     */
    String getSystemErrorPagePath(String key) {
        String result = null;

        ErrorPageModel errorPage = (ErrorPageModel) systemErrorPages.get(key);
        if (errorPage != null) {
            result = errorPage.getErrorPage();
        }
        return result;
    }

    /**
     * SystemErrorPagePathを取得します。
     * 
     * @return String SystemErrorPagePath
     */
    String getSystemErrorPagePath() {
        if (defaultSystemErrorPage != null) {
            return defaultSystemErrorPage.getErrorPage();
        }
        return null;
    }

    /**
     * NextPagePathを取得します。
     * 
     * @param key
     * @return String NextPagePath
     */
    String getNextPagePath(String key) {
        String result = null;

        NextPageModel nextPage = (NextPageModel) nextPages.get(key);
        if (nextPage != null) {
            result = nextPage.getPagePath();
        }

        return result;
    }

    /**
     * NextPagePathを取得します。
     * 
     * @return String NextPagePath
     */
    String getNextPagePath() {
        if (defaultNextPage != null) {
            return defaultNextPage.getPagePath();
        }
        return null;
    }

}