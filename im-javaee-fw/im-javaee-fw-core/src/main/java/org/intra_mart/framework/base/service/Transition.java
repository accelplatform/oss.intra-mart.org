/*
 * Transition.java
 *
 * Created on 2001/12/25, 11:16
 */

package org.intra_mart.framework.base.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * 画面遷移に関連するクラスのインタフェースです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class Transition {

    /**
     * サービスマネージャ
     */
    private ServiceManager manager;

    /**
     * アプリケーションID
     */
    private String application;

    /**
     * サービスID
     */
    private String service;

    /**
     * リクエスト
     */
    private HttpServletRequest request;

    /**
     * レスポンス
     */
    private HttpServletResponse response;

    /**
     * サービス処理結果
     */
    private ServiceResult result;

    /**
     * Transitionを新規に生成します。
     */
    public Transition() {
        setApplication(null);
        setService(null);
        setServiceManager(null);
        setRequest(null);
        setResponse(null);
        setResult(null);
    }

    /**
     * サービスマネージャを設定します。
     *
     * @param manager サービスマネージャ
     */
    protected void setServiceManager(ServiceManager manager) {
        this.manager = manager;
    }

    /**
     * サービスマネージャを取得します。
     *
     * @return サービスマネージャ
     */
    protected ServiceManager getServiceManager() {
        return this.manager;
    }

    /**
     * アプリケーションIDを設定します。
     *
     * @param application アプリケーションID
     */
    void setApplication(String application) {
        this.application = application;
    }

    /**
     * アプリケーションIDを取得します。
     *
     * @return アプリケーションID
     */
    protected String getApplication() {
        return this.application;
    }

    /**
     * サービスIDを設定します。
     *
     * @param service サービスID
     */
    void setService(String service) {
        this.service = service;
    }

    /**
     * サービスIDを取得します。
     *
     * @return サービスID
     */
    protected String getService() {
        return this.service;
    }

    /**
     * リクエストを設定します。
     *
     * @param request リクエスト
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * リクエストを取得します。
     *
     * @return リクエスト
     */
    public HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * レスポンスを設定します。
     *
     * @param response レスポンス
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * レスポンスを取得します。
     *
     * @return レスポンス
     */
    public HttpServletResponse getResponse() {
        return this.response;
    }

    /**
     * サービス処理結果を設定します。
     *
     * @param result サービス処理結果
     */
    public void setResult(ServiceResult result) {
        this.result = result;
    }

    /**
     * サービス処理結果を取得します。
     *
     * @return サービス処理結果
     */
    public ServiceResult getResult() {
        return this.result;
    }

    /**
     * このトランジションに設定されているアプリケーションIDとサービスIDに該当する次ページのパスを取得します。
     *
     * @return 次ページのパス
     * @throws ServicePropertyException プロパティの取得時に例外が発生
     */
    protected String getNextPagePath() throws ServicePropertyException {
        Locale locale =
            getServiceManager().getLocale(getRequest(), getResponse());
        
        return getServiceManager().getServicePropertyHandler().getNextPagePath(
            getApplication(),
            getService(),
            locale);
    }

    /**
     * このトランジションに設定されているアプリケーションIDとサービスIDと、指定されたキーに該当する次ページのパスを取得します。
     *
     * @param key キー
     * @return 次ページのパス
     * @throws ServicePropertyException プロパティの取得時に例外が発生
     */
    protected String getNextPagePath(String key)
        throws ServicePropertyException {

        Locale locale =
            getServiceManager().getLocale(getRequest(), getResponse());

        return getServiceManager().getServicePropertyHandler().getNextPagePath(
            getApplication(),
            getService(),
            key,
            locale);
    }

    /**
     * 入力エラーのページを取得します。
     *
     * @param e 入力時の例外
     * @return 入力エラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public abstract String getInputErrorPage(RequestException e)
        throws ServicePropertyException, TransitionException;

    /**
     * システムエラーのページを取得します。
     *
     * @param e 処理時の例外
     * @return システムエラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public abstract String getSystemErrorPage(Exception e)
        throws ServicePropertyException, TransitionException;

    /**
     * 処理エラーのページを取得します。
     *
     * @param e 処理時の例外
     * @return 処理エラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public abstract String getServiceErrorPage(Exception e)
        throws ServicePropertyException, TransitionException;

    /**
     * 次の遷移先に渡す情報を設定します。
     *
     * @throws TransitionException 情報設定時に例外が発生
     */
    public abstract void setInformation() throws TransitionException;

    /**
     * 次に遷移するページを取得します。
     *
     * @return 次に遷移するページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public abstract String getNextPage()
        throws ServicePropertyException, TransitionException;

    /**
     * 次のページに遷移します。
     * 遷移する方法は次のページで要求される形式に依存します。
     *
     * @throws SystemException システム例外が発生
     * @since 3.2
     */
    public abstract void transfer() throws SystemException;
}
