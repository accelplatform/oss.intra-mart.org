/*
 * PageModel.java
 * 
 * Created on 2004/09/11 ,23:38:59
 */
package org.intra_mart.framework.base.service;

/**
 * ErrorPage情報を管理するクラスです。
 * 
 * @author INTRAMART
 * @since 5.0
 */
class ErrorPageModel {

    static final String P_ID_PAGE_KEY = "page-key";

    static final String P_ID_ERROR_PAGE = "page-path";

    static final String P_ID_INPUT_ERROR = "input-error";

    static final String P_ID_SERVICE_ERROR = "service-error";

    static final String P_ID_SYSTEM_ERROR = "system-error";

    private String pageKey;

    private String errorPage;

    /**
     * PageKeyを設定します。
     * 
     * @param pageKey
     */
    void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    /**
     * PageKeyを取得します。
     * 
     * @return
     */
    String getPageKey() {
        return pageKey;
    }

    /**
     * ErrorPageを設定します。
     * 
     * @param errorPage
     */
    void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    /**
     * ErrorPageを取得します。
     * 
     * @return
     */
    String getErrorPage() {
        return errorPage;
    }
}