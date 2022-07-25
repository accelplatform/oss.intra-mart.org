package org.intra_mart.framework.system.container.factory;

import org.intra_mart.framework.system.container.IMContainer;
import org.intra_mart.framework.system.exception.IMContainerException;

/**
 * IMContainerのファクトリクラスです。
 * 
 * @author INTRAMART
 * @since 6.0
 */
public interface Provider {

	/**
	 * IMContainerを生成します
	 * 
	 * @return IMContainer
	 * @throws IMContainerException IMContainer生成時に例外が発生。
	 */
	public IMContainer create() throws IMContainerException;
}
