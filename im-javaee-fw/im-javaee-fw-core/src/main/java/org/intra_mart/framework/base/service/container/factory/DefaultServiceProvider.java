package org.intra_mart.framework.base.service.container.factory;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.service.container.ServiceContainer;
import org.intra_mart.framework.base.service.container.ServiceContainerImpl;
import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.container.factory.Provider;
import org.intra_mart.framework.system.exception.IMContainerException;

/**
 * 標準的なプロバイダーです。<br>
 * クラスパス上のリソースファイルを取得し、サービスコンテナの実装を決定します。<br>
 * <br>
 * リソースファイル名 - imartContainer.properties<br>
 * キー - serviceContainer<br>
 * <br>
 * キーにはServiceContainerインターフェースの実装クラスをフルパッケージ名で指定してください。<br>
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class DefaultServiceProvider implements Provider {
	
    private Class defaultContainer = ServiceContainerImpl.class;
    
    private Class containerClass = defaultContainer;

    /**
     * DefaultServiceProviderを新規に生成します。
     */
	public DefaultServiceProvider() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("imartContainer");
			setContainerClass(Class.forName(bundle.getString("serviceContainer")));
		} catch (Exception ignore) {
		}
	}
	
	/**
	 * ServiceContainerを生成します。
	 * 
	 * @return サービスコンテナ
	 * @throws IMContainerException サービスコンテナ生成時に例外が発生。
	 */
	public IMContainer create() throws IMContainerException {
		try {
			ServiceContainer container = (ServiceContainer)getContainerClass().newInstance();
			container.init();
			return container;
		} catch (Exception e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ServiceContainer.FailedToCreateServiceContainer");
            } catch (MissingResourceException ex) {
            }
			throw new IMContainerException(message, e);
		}
	}

	private Class getContainerClass() {
		return containerClass;
	}

	private void setContainerClass(Class containerClass) {
		this.containerClass = containerClass;
	}
}
