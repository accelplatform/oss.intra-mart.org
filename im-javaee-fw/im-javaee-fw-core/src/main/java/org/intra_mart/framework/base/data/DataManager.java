/*
 * DataManager.java
 *
 * Created on 2001/10/29, 16:15
 */

package org.intra_mart.framework.base.data;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.data.container.DataContainer;
import org.intra_mart.framework.base.data.container.factory.DataContainerFactory;
import org.intra_mart.framework.base.service.ServiceManager;
import org.intra_mart.framework.system.exception.IMContainerException;
import org.intra_mart.framework.system.log.LogConstant;
import org.intra_mart.framework.system.log.LogManager;

/**
 * データストアとの接続を管理します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DataManager {

    /**
     * データフレームワークのログのプレフィックス
     */
    static String LOG_HEAD = "[J2EE][Data]";

    /**
     * データプロパティハンドラのキー
     */
    public static final String DATA_PROPERTY_HANDLER_KEY = DataContainer.DATA_PROPERTY_HANDLER_KEY;

    /**
     * データマネージャ取得フラグ
     */
    private static Boolean managerFlag = new Boolean(false);

    /**
     * データマネージャ
     */
    private static DataManager manager;

    /**
     * データコンテナ
     */
    private DataContainer dataContainer;

    /**
     * データマネージャを生成するコンストラクタです。
     * このコンストラクタは明示的に呼び出すことはできません。
     *
     * @throws DataManagerException データマネージャの生成に失敗した
     */
    protected DataManager() throws DataManagerException {
    	try {
			this.dataContainer = (DataContainer)new DataContainerFactory().create();
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.data.i18n")
                        .getString("DataManager.SuccessedToCreateContainer");
            } catch (MissingResourceException ignore) {
            }
            LogManager.getLogManager().getLogAgent().sendMessage(
                    ServiceManager.class.getName(),
                    LogConstant.LEVEL_INFO,
                    LOG_HEAD + message + " - " + this.dataContainer.getClass().getName());
		} catch (IMContainerException e) {
			throw new DataManagerException(e.getMessage(), e);
		}
	}

    /**
     * データマネージャを取得します。
     *
     * @return データマネージャ
     * @throws DataManagerException データマネージャの設定に失敗した
     */
    public static DataManager getDataManager() throws DataManagerException {
        if (!managerFlag.booleanValue()) {
            synchronized (managerFlag) {
                if (!managerFlag.booleanValue()) {
                    try {
                        manager = new DataManager();
                    } catch (DataManagerException e) {
                        String message = null;
                        try {
                            message =
                                ResourceBundle.getBundle(
                                    "org.intra_mart.framework.base.data.i18n")
                                        .getString(
                                    "DataManager.FailedToCreateDataManager");
                        } catch (MissingResourceException ex) {
                        }
                        LogManager.getLogManager().getLogAgent().sendMessage(
                            DataManager.class.getName(),
                            LogConstant.LEVEL_ERROR,
                            LOG_HEAD + message,
                            e);
                        //                        Report.error.write(LOG_HEAD + message, e);
                        throw e;
                    }
                    String message = null;
                    try {
                        message =
                            ResourceBundle.getBundle(
                                "org.intra_mart.framework.base.data.i18n")
                                    .getString(
                                "DataManager.SuccessToCreateDataManager");
                    } catch (MissingResourceException e) {
                    }
                    managerFlag = new Boolean(true);
                    LogManager.getLogManager().getLogAgent().sendMessage(
                        DataManager.class.getName(),
                        LogConstant.LEVEL_INFO,
                        LOG_HEAD + message);
                    //                    Report.system.write(LOG_HEAD + message);
                }
            }
        }

        return manager;
    }

    /**
     * データアクセスコントローラを取得します。
     *
     * @return データアクセスコントローラ
     */
    public DataAccessController getDataAccessController() {
        return this.dataContainer.getDataAccessController();
    }

    /**
     * データプロパティハンドラを取得します。
     *
     * @return データプロパティハンドラ
     */
    public DataPropertyHandler getDataPropertyHandler() {
        return this.dataContainer.getDataPropertyHandler();
    }
}
