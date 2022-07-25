package org.intra_mart.framework.base.data.container.factory;

import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.container.factory.IMContainerFactory;
import org.intra_mart.framework.system.container.factory.Provider;
import org.intra_mart.framework.system.exception.IMContainerException;

/**
 * DataContainerのファクトリクラスです。
 * 生成されるデータコンテナはプロバイダーにより決定されます。
 *
 * @author INTRAMART
 * @since 6.0
 */
public class DataContainerFactory implements IMContainerFactory{

    protected static Provider provider = new DefaultDataProvider();

    /**
     * DataContainerを生成します。
     * 
     * @return データコンテナ
     * @throws IMContainerException データコンテナ生成時に例外が発生。
     */
    public IMContainer create() throws IMContainerException {
        return getProvider().create();
    }

    /**
     * プロバイダを返します。
     * 
     * @return データコンテナのプロバイダ
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * プロバイダを設定します。
     * 
     * @param p データコンテナのプロバイダ
     */
    public void setProvider(Provider p) {
        provider = p;
    }
}
