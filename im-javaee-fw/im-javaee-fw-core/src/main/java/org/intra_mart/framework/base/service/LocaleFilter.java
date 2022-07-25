/*
 * LocaleFilter.java
 *
 * Created on 2003/08/26, 19:00
 */

package org.intra_mart.framework.base.service;

import java.io.IOException;
import java.util.Locale;

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
 * リクエストのロケールを設定します。
 * リクエストに対するロケールの決定方法は{@link ServiceManager#getLocale(HttpServletRequest, HttpServletResponse)}と同じです。<BR><BR>
 * この時に取得されたロケールはHttpSessionが存在すればその中に設定されます。この場合の属性名は{@link ServicePropertyHandler#getLocaleAttributeName()}で取得されるものです。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class LocaleFilter implements Filter {

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
     * ロケール用のフィルタを初期化します。
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
     * リクエストのロケールを設定します。
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

        Locale locale = null;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession(false);

        // ロケールの取得
        try {
            locale = this.manager.getLocale(request, response);
        } catch (ServicePropertyException e) {
            throw new ServletException(e.getMessage(), e);
        }

        // ロケールの設定
        if (session != null) {
            try {
                session.setAttribute(
                    this.handler.getLocaleAttributeName(),
                    locale);
            } catch (ServicePropertyException e) {
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
        this.manager = null;
        this.filterConfig = null;
    }
}
