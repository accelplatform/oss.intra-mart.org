package org.intra_mart.framework.base.data.container;

import org.intra_mart.framework.base.data.DataAccessController;
import org.intra_mart.framework.base.data.DataPropertyHandler;
import org.intra_mart.framework.system.container.IMContainer;


/**
 * データコンテナのインターフェースです。<br>
 * データコンテナは{@link org.intra_mart.framework.base.data.DataManager}の振る舞いを決定します。
 * DataManagerではデータコンテナを保管し、その挙動はデータコンテナの実装クラスに依存します。
 * データコンテナの実装は通常{@link org.intra_mart.framework.base.data.container.factory.DataContainerFactory}によって生成されます。
 *
 * @author INTRAMART
 * @since 6.0
 */
public interface DataContainer extends IMContainer {
	
    /**
     * データプロパティハンドラのキー
     */
    public static final String DATA_PROPERTY_HANDLER_KEY = "data";

    /**
     * データプロパティハンドラを取得します。
     *
     * @return データプロパティハンドラ
     */
    public DataPropertyHandler getDataPropertyHandler();


    /**
     * データアクセスコントローラを取得します。
     *
     * @return データアクセスコントローラ
     */
    public DataAccessController getDataAccessController();
}
