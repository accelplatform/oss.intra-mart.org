package org.intra_mart.framework.base.data.container;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.data.DataAccessController;
import org.intra_mart.framework.base.data.DataPropertyHandler;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManager;
import org.intra_mart.framework.system.property.PropertyManagerException;

/**
 * DataContainerの標準的な実装クラスです。
 * 
 * @author INTRAMART
 */
public class DataContainerImpl implements DataContainer {

    /**
     * データプロパティハンドラ
     */
    private DataPropertyHandler dataPropertyHandler;

    /**
	 * DataContainerImplを生成します。
	 */
	public DataContainerImpl() {
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
        return new DataAccessController(this.dataPropertyHandler);
    }
}
