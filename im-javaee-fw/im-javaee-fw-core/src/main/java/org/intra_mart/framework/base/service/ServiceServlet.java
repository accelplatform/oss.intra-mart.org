/*
 * ServiceServlet.java
 *
 * Created on 2001/12/25, 15:16
 */

package org.intra_mart.framework.base.service;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.log.LogConstant;
import org.intra_mart.framework.system.log.LogManager;

/**
 * サービスを振り分けるサーブレットです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class ServiceServlet extends HttpServlet {

    /**
     * サービスマネージャ
     */
    private ServiceManager manager;

    /**
     * サービスプロパティハンドラ
     */
    private ServicePropertyHandler handler;

    /**
     * ServiceServletを新規に生成します。
     */
    public ServiceServlet() {
        super();
    }

    /**
     * リクエストの情報を分割するセパレータ
     * @since 5.0
     */
    public static final String REQUEST_SEPARATOR = "-";
    
    /**
     * サーブレットを初期化します。
     *
     * @param servletConfig ServletConfig
     * @throws ServletException サーブレット初期化時に例外が発生
     */
    public void init(ServletConfig servletConfig) throws ServletException {

        // ServletConfigの設定
        super.init(servletConfig);

        // サービスマネージャの設定
        try {
            this.manager = ServiceManager.getServiceManager();
        } catch (ServiceManagerException e) {
            throw new ServletException(e.getMessage(), e);
        }

        // サービスプロパティハンドラの設定
        this.handler = this.manager.getServicePropertyHandler();
    }

    /**
     * GETメソッドに対するサービスを行います。
     * このメソッドでは対応するサービスコントローラを呼び、次の画面に遷移します。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @throws ServletException サーブレット例外が発生
     * @throws IOException 入出力例外が発生
     */
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {
        try {
            execute(request, response);
        } catch (ServiceServletException e) {
            throw new ServletException(e.getMessage(), e);
        } catch (ServicePropertyException e) {
            throw new ServletException(e.getMessage(), e);
        } catch (ServiceControllerException e) {
            throw new ServletException(e.getMessage(), e);
        } catch (TransitionException e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    /**
     * POSTメソッドに対するサービスを行います。
     * このメソッドでは対応するサービスコントローラを呼び、次の画面に遷移します。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @throws ServletException サーブレット例外が発生
     * @throws IOException 入出力例外が発生
     */
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {
        try {
            execute(request, response);
        } catch (ServiceServletException e) {
            throw new ServletException(e.getMessage(), e);
        } catch (ServicePropertyException e) {
            throw new ServletException(e.getMessage(), e);
        } catch (ServiceControllerException e) {
            throw new ServletException(e.getMessage(), e);
        } catch (TransitionException e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    /**
     * リクエストに対する処理を行います。
     * このメソッドでは対応するサービスコントローラを呼び、次の画面に遷移します。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @throws ServiceServletException サービスに関連する例外が発生
     * @throws ServicePropertyException サービスプロパティ取得時に例外が発生
     * @throws ServiceControllerException サービスコントローラ取得時に例外が発生
     * @throws TransitionException トランジション例外が発生
     * @throws ServletException サーブレット例外が発生
     * @throws IOException サーブレット例外が発生
     * @see ServiceController
     * @see Transition
     */
    private void execute(
        HttpServletRequest request,
        HttpServletResponse response)
        throws
            ServiceServletException,
            ServicePropertyException,
            ServiceControllerException,
            TransitionException,
            ServletException,
            IOException {

        String application = null;
        String service = null;
        ServiceController controller = null;
        Transition transition = null;
        ServiceResult result = null;
        String nextPage = null;
        Locale locale = this.manager.getLocale(request, response);

        // アプリケーションIDの取得
        application = this.manager.getApplication(request, response);
        if (application == null || application.equals("")) {
            String errorMessage = null;
            try {
                errorMessage =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ServiceServlet.ApplicationNotRequested")
					 + " - "
					 + request.getServletPath();
            } catch (MissingResourceException e) {
            }
            ServiceServletException exception =
                new ServiceServletException(errorMessage);
            LogManager.getLogManager().getLogAgent().sendMessage(
                ServiceServlet.class.getName(),
                LogConstant.LEVEL_ERROR,
                ServiceManager.LOG_HEAD + errorMessage,
                exception);
            throw exception;
        }

        // サービスIDの取得
        service = this.manager.getService(request, response);
        if (service == null || service.equals("")) {
            String errorMessage = null;
            try {
                errorMessage =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ServiceServlet.ServiceNotRequested")
				 + " - "
				 + request.getServletPath();
            } catch (MissingResourceException e) {
            }
            ServiceServletException exception =
                new ServiceServletException(errorMessage);
            LogManager.getLogManager().getLogAgent().sendMessage(
                ServiceServlet.class.getName(),
                LogConstant.LEVEL_ERROR,
                ServiceManager.LOG_HEAD + errorMessage,
                exception);
            throw exception;
        }

        // サービスコントローラの取得
        controller =
            this.manager.getServiceController(application, service, locale);

        // トランジションの取得
        transition = this.manager.getTransition(application, service, locale);
        transition.setRequest(request);
        transition.setResponse(response);

        // サービスコントローラが存在する場合のみ処理を実行する
        if (controller != null) {

            // リクエスト、レスポンスの設定
            controller.setRequest(request);
            controller.setResponse(response);

            // 入力チェック
            try {
                controller.check();
            } catch (RequestException e) {
                nextPage = transition.getInputErrorPage(e);
                request.setAttribute(
                    this.handler.getExceptionAttributeName(),
                    e);
                getServletConfig().getServletContext().getRequestDispatcher(
                    nextPage).forward(
                    request,
                    response);
                return;
            } catch (SystemException e) {
                // システム例外が発生した場合
                LogManager.getLogManager().getLogAgent().sendMessage(
                    ServiceServlet.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    ServiceManager.LOG_HEAD + e.getMessage(),
                    e);
                nextPage = transition.getSystemErrorPage(e);
                request.setAttribute(
                    this.handler.getExceptionAttributeName(),
                    e);
                getServletConfig().getServletContext().getRequestDispatcher(
                    nextPage).forward(
                    request,
                    response);
                return;
            }

            // 処理実行
            try {
                result = controller.service();
            } catch (SystemException e) {
                // システム例外が発生した場合
                LogManager.getLogManager().getLogAgent().sendMessage(
                    ServiceServlet.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    ServiceManager.LOG_HEAD + e.getMessage(),
                    e);
                nextPage = transition.getSystemErrorPage(e);
                request.setAttribute(
                    this.handler.getExceptionAttributeName(),
                    e);
                getServletConfig().getServletContext().getRequestDispatcher(
                    nextPage).forward(
                    request,
                    response);
                return;
            } catch (ApplicationException e) {
                // アプリケーション例外が発生した場合
                LogManager.getLogManager().getLogAgent().sendMessage(
                    ServiceServlet.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    ServiceManager.LOG_HEAD + e.getMessage(),
                    e);
                nextPage = transition.getServiceErrorPage(e);
                request.setAttribute(
                    this.handler.getExceptionAttributeName(),
                    e);
                getServletConfig().getServletContext().getRequestDispatcher(
                    nextPage).forward(
                    request,
                    response);
                return;
            }
        }

        // 次のページへの情報を設定する
        transition.setResult(result);
        transition.setInformation();

        // 次のページに遷移する
        try {
            transition.transfer();
        } catch (SystemException e) {
            // システム例外が発生した場合
            LogManager.getLogManager().getLogAgent().sendMessage(
                ServiceServlet.class.getName(),
                LogConstant.LEVEL_ERROR,
                ServiceManager.LOG_HEAD + e.getMessage(),
                e);
            nextPage = transition.getSystemErrorPage(e);
            request.setAttribute(this.handler.getExceptionAttributeName(), e);
            getServletConfig().getServletContext().getRequestDispatcher(
                nextPage).forward(
                request,
                response);
            return;
        }
    }

    /**
     * サーブレットを解放します。
     */
    public void destroy() {
        this.manager = null;
        super.destroy();
    }
}
