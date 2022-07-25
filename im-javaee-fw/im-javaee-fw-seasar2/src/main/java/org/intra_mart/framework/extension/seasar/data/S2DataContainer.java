package org.intra_mart.framework.extension.seasar.data;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.data.DataAccessController;
import org.intra_mart.framework.base.data.DataPropertyHandler;
import org.intra_mart.framework.base.data.container.DataContainer;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManager;
import org.intra_mart.framework.system.property.PropertyManagerException;

/**
 * S2DataContainer　は　Seasar2　と連携するためのDataContainer実装です。<br>
 * 実際の連携はS2DataContainerが保持する{@link S2DataAccessController}が行います。
 * 
 * @see S2DataAccessController
 * @author INTRAMART
 * @since 6.0
 */
public class S2DataContainer implements DataContainer {

    /**
     * データプロパティハンドラ
     */
    private DataPropertyHandler dataPropertyHandler;

    /**
	 * S2DataContainerを生成します。
	 */
	public S2DataContainer() {
	}

	/**
	 * DataContainerを初期化します。
	 * 
	 * @throws IMContainerException
	 */
	public void init() throws IMContainerException {
        PropertyManager propertyManager;

        // プロパティマネージャの取得
        try {
            propertyManager = PropertyManager.getPropertyManager();
        } catch (PropertyManagerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "DataManager.FailedToGetPropertyManager");
            } catch (MissingResourceException ex) {
            }
            throw new IMContainerException(message, e);
        }

        // データプロパティハンドラの取得
        try {
            this.dataPropertyHandler =
                (DataPropertyHandler)propertyManager.getPropertyHandler(
                    DATA_PROPERTY_HANDLER_KEY);
        } catch (PropertyHandlerException e) {
            String message = null;
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.base.data.i18n").getString(
                        "DataManager.FailedToGetDataPropertyHandler");
            } catch (MissingResourceException ex) {
            }
            throw new IMContainerException(
                message + " : key = " + DATA_PROPERTY_HANDLER_KEY,
                e);
        }
	}
	
    /**
     * データプロパティハンドラを取得します。
     *
     * @return データプロパティハンドラ
     */
	public DataPropertyHandler getDataPropertyHandler() {
        return this.dataPropertyHandler;
	}

    /**
     * データアクセスコントローラを取得します。
     *
     * @return データアクセスコントローラ
     */
    public DataAccessController getDataAccessController() {
        return new S2DataAccessController(this.dataPropertyHandler);
    }
}
