package org.intra_mart.framework.base.event.container.factory;

import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.container.factory.IMContainerFactory;
import org.intra_mart.framework.system.container.factory.Provider;
import org.intra_mart.framework.system.exception.IMContainerException;

/**
 * EventContainerのファクトリクラスです。
 * 生成されるイベントコンテナはプロバイダーにより決定されます。
 *
 * @author INTRAMART
 * @since 6.0
 */
public class EventContainerFactory implements IMContainerFactory{

    protected static Provider provider = new DefaultEventProvider();

    /**
     * EventContainerを生成します。
     * 
     * @return イベントコンテナ
     * @throws IMContainerException イベントコンテナ生成時に例外が発生。
     */
    public IMContainer create() throws IMContainerException {
        return getProvider().create();
    }

    /**
     * プロバイダを返します。
     * 
     * @return イベントコンテナのプロバイダ
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * プロバイダを設定します。
     * 
     * @param p イベントコンテナのプロバイダ
     */
    public void setProvider(Provider p) {
        provider = p;
    }
}
