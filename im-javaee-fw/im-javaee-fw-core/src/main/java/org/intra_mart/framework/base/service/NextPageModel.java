/*
 * PageModel.java
 * 
 * Created on 2004/09/11 ,23:38:59
 */
package org.intra_mart.framework.base.service;

/**
 * NextPage情報を管理するクラスです。
 * 
 * @author INTRAMART
 * @since 5.0
 */
class NextPageModel {

    static final String P_ID_NEXT_PAGE = "next-page";

    static final String P_ID_PAGE_KEY = "page-key";

    static final String P_ID_PAGE_PATH = "page-path";

    private String pageKey;

    private String pagePath;

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
     * @return String PageKey
     */
    String getPageKey() {
        return pageKey;
    }

    /**
     * PagePathを設定します。
     * 
     * @param pagePath
     */
    void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    /**
     * PagePathを取得します。
     * 
     * @return String PagePath
     */
    String getPagePath() {
        return pagePath;
    }

}