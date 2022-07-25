/*
 * DefaultTransition.java
 *
 * Created on 2001/12/25, 11:24
 */

package org.intra_mart.framework.base.service;

import java.util.Locale;

import javax.servlet.RequestDispatcher;

import org.intra_mart.framework.system.exception.SystemException;

/**
 * デフォルトの画面遷移をします。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DefaultTransition extends Transition {

    /**
     * DefaultTransitionを新規に生成します。
     */
    public DefaultTransition() {
        super();
    }

    /**
     * 入力エラーのページを取得します。
     *
     * @param e 例外
     * @return 入力エラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getInputErrorPage(RequestException e)
        throws ServicePropertyException, TransitionException {

        Locale locale =
            getServiceManager().getLocale(getRequest(), getResponse());

        return getServiceManager()
            .getServicePropertyHandler()
            .getInputErrorPagePath(
            getApplication(),
            getService(),
            e.getClass().getName(),
            locale);
    }

    /**
     * システムエラーのページを取得します。
     *
     * @param e 処理時の例外
     * @return システムエラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getSystemErrorPage(Exception e)
        throws ServicePropertyException, TransitionException {

        Locale locale =
            getServiceManager().getLocale(getRequest(), getResponse());

        return getServiceManager()
            .getServicePropertyHandler()
            .getSystemErrorPagePath(
            getApplication(),
            getService(),
            e.getClass().getName(),
            locale);
    }

    /**
     * 処理エラーのページを取得します。
     *
     * @param e 処理時の例外
     * @return 処理エラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getServiceErrorPage(Exception e)
        throws ServicePropertyException, TransitionException {

        Locale locale =
            getServiceManager().getLocale(getRequest(), getResponse());

        return getServiceManager()
            .getServicePropertyHandler()
            .getServiceErrorPagePath(
            getApplication(),
            getService(),
            e.getClass().getName(),
            locale);
    }

    /**
     * 次の遷移先に渡す情報を設定します。
     * このクラスでは何も行いません。
     *
     * @throws TransitionException 情報設定時に例外が発生
     */
    public void setInformation() throws TransitionException {
    }

    /**
     * 次に遷移するページを取得します。
     *
     * @return 次に遷移するページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getNextPage()
        throws ServicePropertyException, TransitionException {
        return getNextPagePath();
    }

    /**
     * 次のページに遷移します。
     * {@link #getNextPage()}で取得されるページにforwardされます。
     *
     * @throws SystemException システム例外が発生
     * @since 3.2
     */
    public void transfer() throws SystemException {
        String nextPage = null;
        try {
            nextPage = getNextPage();
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }

        // 他のサーブレットによってincludeされているかどうかチェック
        String requestURI =
            (String)getRequest().getAttribute(
                "javax.servlet.include.request_uri");
        String contextPath =
            (String)getRequest().getAttribute(
                "javax.servlet.include.context_path");
        String servletPath =
            (String)getRequest().getAttribute(
                "javax.servlet.include.servlet_path");
        RequestDispatcher dispatcher =
            getRequest().getRequestDispatcher(nextPage);
        try {
            if (requestURI != null
                && !requestURI.equals("")
                || contextPath != null
                && !contextPath.equals("")
                || servletPath != null
                && !servletPath.equals("")) {

                // 他のサーブレットによってincludeされている場合
                dispatcher.include(getRequest(), getResponse());

            } else {
                // 他のサーブレットによってincludeされていない場合
                dispatcher.forward(getRequest(), getResponse());
            }
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
}
