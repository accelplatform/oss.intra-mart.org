/*
 * EncodingFilter.java
 *
 * Created on 2001/12/17, 13:59
 */

package org.intra_mart.framework.base.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * リクエストのエンコードを設定します。
 * リクエストに対するエンコードは
 * <OL>
 * <LI>{@link ServicePropertyHandler#getClientEncoding()}で取得されるもの
 * <LI>{@link ServiceManager#getLocale(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * </OL>
 * の順位で決定されます。<BR><BR>
 * この時に取得されたエンコードは{@link javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)}に設定され、
 * さらに{@link javax.servlet.http.HttpSession}が存在すればその中にも設定されます。この場合の属性名は{@link ServicePropertyHandler#getEncodingAttributeName()}で取得されるものです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EncodingFilter implements Filter {

    /**
     * Filter Config
     */
    private FilterConfig filterConfig;

    /**
     * サービスマネージャ
     */
    private ServiceManager manager;

    /**
     * サービスプロパティハンドラ
     */
    private ServicePropertyHandler handler;

    /**
     * エンコーディング用のフィルタを初期化します。
     *
     * @param filterConfig FilterConfig
     * @throws ServletException フィルタ内で例外が発生
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

        // サービスマネージャの取得
        try {
            this.manager = ServiceManager.getServiceManager();
        } catch (ServiceManagerException e) {
            throw new ServletException(e.getMessage(), e);
        }

        // サービスプロパティハンドラの取得
        this.handler = this.manager.getServicePropertyHandler();
    }

    /**
     * リクエストのエンコードを設定します。
     *
     * @param servletRequest リクエスト
     * @param servletResponse レスポンス
     * @param filterChain フィルタ
     * @throws IOException フィルタ処理実行時にIOExceptionが発生
     * @throws ServletException フィルタ処理実行時にServletExceptionが発生
     */
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain)
        throws IOException, ServletException {

        String encoding = null;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession(false);

        // エンコーディングを取得
        try {
            encoding = this.manager.getEncoding(request, response);
        } catch (ServicePropertyException e) {
            throw new ServletException(e.getMessage(), e);
        }

        // セッションにエンコーディングを登録
        if (session != null) {
            try {
                session.setAttribute(
                    this.handler.getEncodingAttributeName(),
                    encoding);
            } catch (ServicePropertyException e) {
                throw new ServletException(e.getMessage(), e);
            }
        }

        // リクエストにクライアントエンコーディングを設定
        if(Charset.isSupported(encoding)) {
            try {
                servletRequest.setCharacterEncoding(encoding);
            } catch (UnsupportedEncodingException e) {
                throw new ServletException(e.getMessage(), e);
            }
        }

        // 次のフィルタに遷移
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * フィルタを破棄します。
     */
    public void destroy() {
        this.handler = null;
        this.filterConfig = null;
    }
}
