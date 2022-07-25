package org.intra_mart.framework.base.data.container.factory;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.data.container.DataContainer;
import org.intra_mart.framework.base.data.container.DataContainerImpl;
import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.container.factory.Provider;
import org.intra_mart.framework.system.exception.IMContainerException;

/**
 * 標準的なプロバイダーです。<br>
 * クラスパス上のリソースファイルを取得し、データコンテナの実装を決定します。<br>
 * <br>
 * リソースファイル名 - imartContainer.properties<br>
 * キー - dataContainer<br>
 * <br>
 * キーにはDataContainerインターフェースの実装クラスをフルパッケージ名で指定してください。<br>
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class DefaultDataProvider implements Provider {
	
    private Class defaultContainer = DataContainerImpl.class;
    
    private Class containerClass = defaultContainer;

    /**
     * DefaultDataProviderを新規に生成します。
     */
    public DefaultDataProvider() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("imartContainer");
			setContainerClass(Class.forName(bundle.getString("dataContainer")));
		} catch (Exception ignore) {
		}
	}

	/**
	 * DataContainerを生成します。
	 * 
	 * @return データコンテナ
	 * @throws IMContainerException データコンテナ生成時に例外が発生。
	 */
	public IMContainer create() throws IMContainerException {
		try {
			DataContainer container = (DataContainer)getContainerClass().newInstance();
			container.init();
			return container;
		} catch (Exception e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.data.i18n")
                        .getString("DataContainer.FailedToCreateDataContainer");
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
