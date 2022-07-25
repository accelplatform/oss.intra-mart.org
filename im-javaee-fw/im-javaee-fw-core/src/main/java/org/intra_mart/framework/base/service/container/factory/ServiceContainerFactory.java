package org.intra_mart.framework.base.service.container.factory;

import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.container.factory.IMContainerFactory;
import org.intra_mart.framework.system.container.factory.Provider;
import org.intra_mart.framework.system.exception.IMContainerException;

/**
 * ServiceContainerのファクトリクラスです。
 * 生成されるサービスコンテナはプロバイダーにより決定されます。
 *
 * @author INTRAMART
 * @since 6.0
 */
public class ServiceContainerFactory implements IMContainerFactory{

    protected Provider provider = new DefaultServiceProvider();

    /**
     * ServiceContainerを生成します。
     * 
     * @return サービスコンテナ
     * @throws IMContainerException サービスコンテナ生成時に例外が発生。
     */
    public IMContainer create() throws IMContainerException {
        return getProvider().create();
    }

    /**
     * プロバイダを返します。
     * 
     * @return サービスコンテナのプロバイダ
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * プロバイダを設定します。
     * 
     * @param p サービスコンテナのプロバイダ
     */
    public void setProvider(Provider p) {
        provider = p;
    }
}
