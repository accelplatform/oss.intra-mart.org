package org.intra_mart.framework.system.container.factory;

import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.exception.IMContainerException;

/**
 * ServiceContainerのクラスです。
 *
 * @author INTRAMART
 * @since 6.0
 */
public interface IMContainerFactory {

    IMContainer create() throws IMContainerException;

    Provider getProvider();

    void setProvider(Provider p);
}
