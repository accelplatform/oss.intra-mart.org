/*
 * HelperBean.java
 *
 * Created on 2002/02/28, 16:13
 */

package org.intra_mart.framework.base.web.bean;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventException;
import org.intra_mart.framework.base.event.EventManager;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.base.service.ServiceManager;
import org.intra_mart.framework.base.service.ServicePropertyHandler;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * 画面表示をする時のヘルパークラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public abstract class HelperBean implements Serializable {

    /**
     * イベントマネージャ
     */
    private EventManager eventManager;

    /**
     * サービスプロパティハンドラ
     */
    private ServicePropertyHandler serviceHandler;

    /**
     * リクエスト
     */
    private HttpServletRequest request;

    /**
     * レスポンス
     */
    private HttpServletResponse response;
    
    /**
     * HelperBeanを新規に生成します。
     *
     * @throws HelperBeanException HelperBean生成時に例外が発生
     */
    public HelperBean() throws HelperBeanException {
        setRequest(null);
        setResponse(null);
        try {
            this.eventManager = EventManager.getEventManager();
            this.serviceHandler = ServiceManager.getServiceManager().getServicePropertyHandler();
        } catch (Exception e) {
            throw new HelperBeanException(e.getMessage(), e);
        }
    }

    /**
     * HelperBeanを初期化します。
     *
     * @throws HelperBeanException 初期化時に例外が発生
     * @since 3.2
     */
    public void init() throws HelperBeanException {
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
     * アプリケーションIDとキーに対応するイベントを取得します。
     * イベントにはログイン時のログイングループIDが自動的に登録されます。
     *
     * @param application アプリケーションID
     * @param key キー
     * @return イベント
     * @throws HelperBeanException イベント生成時に例外が発生
     */
    protected Event createEvent(String application, String key) throws HelperBeanException {
        // イベントの生成
        Event event = null;
        try {
            event = this.eventManager.createEvent(application, key);
        } catch (Exception e) {
            throw new HelperBeanException(e.getMessage(), e);
        }

        return event;
    }

    /**
     * イベント処理を実行します。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws EventException イベント処理時に例外が発生
     * @throws SystemException イベント処理時にシステム例外が発生
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     */
    protected EventResult dispatchEvent(Event event) throws EventException, SystemException, ApplicationException {
        return this.eventManager.dispatch(event);
    }

    /**
     * サービスプロパティハンドラを取得します。
     *
     * @return サービスプロパティハンドラ
     */
    public ServicePropertyHandler getServicePropertyHandler() {
        return this.serviceHandler;
    }
}
