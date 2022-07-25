/*
 * ServiceController.java
 *
 * Created on 2001/12/25, 10:53
 */

package org.intra_mart.framework.base.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * クライアントからの入力に対する処理を行います。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface ServiceController {

    /**
     * リクエストを設定します。
     *
     * @param request リクエスト
     */
    public void setRequest(HttpServletRequest request);

    /**
     * リクエストを取得します。
     *
     * @return リクエスト
     */
    public HttpServletRequest getRequest();

    /**
     * レスポンスを設定します。
     *
     * @param response レスポンス
     */
    public void setResponse(HttpServletResponse response);

    /**
     * レスポンスを取得します。
     *
     * @return レスポンス
     */
    public HttpServletResponse getResponse();

    /**
     * 入力内容をチェックします。
     *
     * @throws RequestException 入力内容に誤りがある
     * @throws SystemException チェック時にシステム例外が発生
     */
    public void check() throws RequestException, SystemException;

    /**
     * 入力に対する処理を実行します。
     *
     * @return 処理結果
     * @throws SystemException 処理実行時にシステム例外が発生
     * @throws ApplicationException 処理実行時にアプリケーション例外が発生
     */
    public ServiceResult service() throws SystemException, ApplicationException;
}
