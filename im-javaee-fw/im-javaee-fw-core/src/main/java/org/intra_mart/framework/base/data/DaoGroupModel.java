/*
 * 作成日: 2003/12/25
 */
package org.intra_mart.framework.base.data;

import java.util.HashMap;
import java.util.Map;

/**
 * DaoGroup情報を管理するクラスです。 
 * @author INTRAMART
 * @version 1.0
 */
class DaoGroupModel {

    /**
     * 接続先別データリソース情報
     */
    private Map daoModels;
    
    private String daoKey;

    /**
     * デフォルトDao
     */
    private DaoModel defaultDao;

    public static final String P_ID_DAO_KEY = "dao-key";
    public static final String DATA_CONFIG = "data-config";
    public static final String ID = "dao-group";

    /**
     * コンストラクタです。
     *
     */
    DaoGroupModel() {
        daoModels = new HashMap();
        defaultDao = new DaoModel();
    }

    /**
     * Dao名を取得します。
     * @param connect
     * @return Dao名
     **/
    String getDAOName(String connect) {
        String result = null;

        DaoModel dao = (DaoModel) daoModels.get(connect);

        if (dao != null) {
            result = dao.getDaoClass();
        }
        if (result == null) {
            return defaultDao.getDaoClass();
        }
        return result;
    }

    /**
     * コネクト名を取得します。
     * @param connect
     * @return ConnectorName
     */
    String getConnectorName(String connect) {
        DaoModel dao = (DaoModel) daoModels.get(connect);

        String result = null;
        if (dao != null) {
            result = dao.getConnectorName();
        }

        if (result == null) {
            result = defaultDao.getConnectorName();
        }
        return result;
    }

    /**
     * 接続先別DAOデータリソース情報を設定します。
     * @param connect
     * @param connectModel
     */
    void setDaoModel(String connect, DaoModel dao) {
        daoModels.put(connect, dao);
    }

    /**
     * デフォルトDAOを設定します。
     * @param defaultDao
     */
    void setDefaultDao(DaoModel defaultDao) {
        this.defaultDao = defaultDao;
    }

    /**
     * デフォルトDAOを取得します。
     * @return DaoModel
     */
    DaoModel getDefaultDao() {
        return defaultDao;
    }

	/**
	 * Daoキーを設定します。
	 * @param daoKey
	 */
    void setDaoKey(String daoKey) {
        this.daoKey = daoKey;
    }

	/**
	 * Daoキーを取得します。
	 * @return daoKey
	 */
    String getDaoKey() {
        return daoKey;
    }

}
