package org.intra_mart.framework.base.event.container.factory;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.event.container.EventContainer;
import org.intra_mart.framework.base.event.container.EventContainerImpl;
import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.container.factory.Provider;
import org.intra_mart.framework.system.exception.IMContainerException;

/**
 * 標準的なプロバイダーです。<br>
 * クラスパス上のリソースファイルを取得し、イベントコンテナの実装を決定します。<br>
 * <br>
 * リソースファイル名 - imartContainer.properties<br>
 * キー - eventContainer<br>
 * <br>
 * キーにはEventContainerインターフェースの実装クラスをフルパッケージ名で指定してください。<br>
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class DefaultEventProvider implements Provider {
	
    private Class defaultContainer = EventContainerImpl.class;
    
    private Class containerClass = defaultContainer;

    /**
     * DefaultEventProviderを新規に生成します。
     */
    public DefaultEventProvider() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("imartContainer");
			setContainerClass(Class.forName(bundle.getString("eventContainer")));
		} catch (Exception ignore) {
		}
	}
	
	/**
	 * EventContainerを生成します。
	 * 
	 * @return イベントコンテナ
	 * @throws IMContainerException イベントコンテナ生成時に例外が発生。
	 */
    public IMContainer create() throws IMContainerException {
		try {
			EventContainer container = (EventContainer)getContainerClass().newInstance();
			container.init();
			return container;
		} catch (Exception e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.event.i18n")
                        .getString("EventContainer.FailedToCreateEventContainer");
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
