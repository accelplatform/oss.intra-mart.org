package org.intra_mart.framework.base.service.container;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.framework.base.service.DefaultTransition;
import org.intra_mart.framework.base.service.ServiceController;
import org.intra_mart.framework.base.service.ServiceControllerException;
import org.intra_mart.framework.base.service.ServicePropertyException;
import org.intra_mart.framework.base.service.ServicePropertyHandler;
import org.intra_mart.framework.base.service.Transition;
import org.intra_mart.framework.base.service.TransitionException;
import org.intra_mart.framework.system.container.IMContainer;

/**
 * サービスコンテナのインターフェースです。<br>
 * サービスコンテナは{@link org.intra_mart.framework.base.service.ServiceManager}の振る舞いを決定します。
 * ServiceManagerではサービスコンテナを保管し、その挙動はサービスコンテナの実装クラスに依存します。
 * サービスコンテナの実装は通常{@link org.intra_mart.framework.base.service.container.factory.ServiceContainerFactory}によって生成されます。
 *
 * @author INTRAMART
 * @since 6.0
 */
public interface ServiceContainer extends IMContainer {
	
    /**
     * サービスプロパティハンドラのキー
     */
	public static final String SERVICE_PROPERTY_HANDLER_KEY = "service";
	
    /**
     * サービスプロパティハンドラを取得します。
     *
     * @return サービスプロパティハンドラ
     */
    public ServicePropertyHandler getServicePropertyHandler();

    /**
     * サービスコントローラを取得します。
     * 指定されたアプリケーションID、サービスID、ロケールに該当するサービスコントローラを取得します。
     * 該当するサービスコントローラが存在しない場合、nullを返します。
     *
     * @param application アプリケーション
     * @param service サービス
     * @param locale ロケール
     * @return サービスコントローラ、存在しない場合はnull
     * @throws ServicePropertyException サービスプロパティの取得時に例外が発生
     * @throws ServiceControllerException サービスコントローラ取得時に例外が発生
     */
    public ServiceController getServiceController(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException, ServiceControllerException;

    /**
     * トランジションを取得します。
     * 指定されたアプリケーションID、サービスID、ロケールに該当するトランジションを取得します。
     * 該当するトランジションが存在しない場合、{@link DefaultTransition}を返します。
     *
     * @param application アプリケーション
     * @param service サービス
     * @param locale ロケール
     * @return トランジション
     * @throws ServicePropertyException サービスプロパティの取得時に例外が発生
     * @throws TransitionException トランジション取得時に例外が発生
     */
    public Transition getTransition(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException, TransitionException;
}
