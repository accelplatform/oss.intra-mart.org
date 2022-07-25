/*
 * ServiceControllerAdapter.java
 *
 * Created on 2001/12/25, 11:01
 */

package org.intra_mart.framework.base.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventException;
import org.intra_mart.framework.base.event.EventManager;
import org.intra_mart.framework.base.event.EventManagerException;
import org.intra_mart.framework.base.event.EventResult;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * いくつかのユーティリティメソッドと空のメソッドを実装したサービスコントローラです。
 * このクラスにはいくつかのユーティリティメソッドがあります。
 * このクラスを継承し、必要に応じて{@link #check()}や{@link #service()}を実装することでコーディング量を減らすことが可能です。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ServiceControllerImple implements ServiceController {

    /**
     * リクエスト
     */
    private HttpServletRequest request;

    /**
     * レスポンス
     */
    private HttpServletResponse response;

    /**
     * ServiceControllerAdapterを新規に生成します。
     */
    public ServiceControllerImple() {
        setRequest(null);
        setResponse(null);
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
     * 入力内容をチェックします。
     *
     * @throws RequestException 入力内容に誤りがある
     * @throws SystemException チェック時にシステム例外が発生
     */
    public void check() throws RequestException, SystemException {
    }

    /**
     * 入力に対する処理を実行します。
     * このサービスコントローラでは何もせず、nullを返します。
     *
     * @return 処理結果(null)
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     */
    public ServiceResult service()
        throws SystemException, ApplicationException {

        return null;
    }

    /**
     * アプリケーションIDとキーに対応するイベントを取得します。
     * イベントにはログイン時のログイングループIDが自動的に登録されます。
     *
     * @param application アプリケーションID
     * @param key キー
     * @return イベント
     * @throws ServiceControllerException イベント取得時に例外が発生
     */
    protected Event createEvent(String application, String key)
        throws ServiceControllerException {

        // イベントの生成
        Event event = null;
        try {
            event =
                EventManager.getEventManager().createEvent(
                    application,
                    key);
        } catch (Exception e) {
            throw new ServiceControllerException(e.getMessage(), e);
        }

        return event;
    }

    /**
     * イベント処理を実行します。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws EventManagerException イベントマネージャの生成に失敗
     * @throws EventException イベント処理実行に失敗
     * @throws SystemException イベント処理時にシステム例外が発生
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     */
    protected EventResult dispatchEvent(Event event)
        throws
            EventManagerException,
            EventException,
            SystemException,
            ApplicationException {

        return EventManager.getEventManager().dispatch(event);
    }
}
