package org.intra_mart.framework.system.container;

import org.intra_mart.framework.system.exception.IMContainerException;
/**
 * イントラマートコンテナのインターフェースです。
 * 
 * @author INTRAMART
 */
public interface IMContainer {

    /**
	 * IMContainerを初期化します。
	 * 
	 * @throws IMContainerException
	 */
	public void init () throws IMContainerException;
}
