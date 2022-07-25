/*
 * SampleItemIntramartDBDAO.java
 *
 * Created on 2002/03/06, 16:30
 */

package sample.shopping.model.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.intra_mart.framework.base.data.DataAccessException;
import org.intra_mart.framework.base.data.DataConnectException;
import org.intra_mart.framework.base.data.DataPropertyException;
import org.intra_mart.framework.base.data.DBDAO;
import sample.shopping.model.object.SampleItemModelObject;

/**
 * IntramartのDB接続情報を利用した、b_fw_sample_itemテーブル操作用DAOです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class SampleItemIntramartDBDAO extends DBDAO implements SampleItemDAOIF {

	// 商品を登録
	private final String SQL_INSERT = "INSERT INTO b_fw_sample_item (item_cd, name, price, simple_note, detail_note, image_path) "
												+ "VALUES (?, ?, ?, ?, ?, ?)";
	
	// 商品を更新
	private final String SQL_UPDATE = "UPDATE b_fw_sample_item SET name = ?, price = ?, simple_note = ?, detail_note = ?, image_path = ? "
												+ "WHERE item_cd = ?";
	
	// 商品を削除
	private final String SQL_DELETE = "DELETE FROM b_fw_sample_item WHERE item_cd = ?";
	
	// 商品を一件取得
	private final String SQL_SELECT = "SELECT item_cd, name, price, simple_note, detail_note, image_path FROM b_fw_sample_item WHERE item_cd = ?";

	// 商品を全て取得
	private final String SQL_SELECT_ALL = "SELECT item_cd, name, price, simple_note, detail_note, image_path FROM b_fw_sample_item";
	
	// 商品の全件数を取得
	private final String SQL_COUNT_ALL ="SELECT count(*) FROM b_fw_sample_item";
	
    /**
     * insert処理を行います。
     *
     * @param item 商品情報
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     */
    public void insert(SampleItemModelObject item)
        throws DataConnectException, DataAccessException, DataPropertyException {

    	// 商品の存在チェック
    	if (select(item.getCode()) != null) {
            ItemCodeDuplicationException e = new ItemCodeDuplicationException();
            e.setCode(item.getCode());
            throw e;
    	}
    	
    	// 商品を登録
    	Connection conn = getConnection();
    	PreparedStatement stmt = null;
        try {
        	stmt = conn.prepareStatement(SQL_INSERT);
        	stmt.setString(1, item.getCode());
        	stmt.setString(2, item.getName());
        	stmt.setInt(3, item.getPrice());
        	stmt.setString(4, item.getSimpleNote());
        	stmt.setString(5, item.getDetailNote());
        	stmt.setString(6, item.getImagePath());
        	stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
    }

    /**
     * update処理を行います。
     *
     * @param item 商品情報
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     */
    public void update(SampleItemModelObject item)
        throws DataConnectException, DataAccessException, DataPropertyException {

    	// 商品の存在チェック
    	if (select(item.getCode()) == null) {
    		ItemNotExistException e = new ItemNotExistException();
            e.setCode(item.getCode());
            throw e;
    	}

    	// 商品を更新
    	Connection conn = getConnection();
    	PreparedStatement stmt = null;
        try {
        	stmt = conn.prepareStatement(SQL_UPDATE);
        	stmt.setString(1, item.getName());
        	stmt.setInt(2, item.getPrice());
        	stmt.setString(3, item.getSimpleNote());
        	stmt.setString(4, item.getDetailNote());
        	stmt.setString(5, item.getImagePath());
        	stmt.setString(6, item.getCode());
        	stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
    }

    /**
     * delete処理を行います。
     *
     * @param code 削除を行うレコードのコード
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     */
    public void delete(String code)
        throws DataConnectException, DataAccessException, DataPropertyException {

    	// 商品を削除
    	Connection conn = getConnection();
    	PreparedStatement stmt = null;
        try {
        	stmt = conn.prepareStatement(SQL_DELETE);
        	stmt.setString(1, code);
        	stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
    }

    /**
     * select処理(1件取得)を行います。
     * データを取得できない場合はnullを返却します。
     *
     * @param code 取得するレコードのコード
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報
     */
    public SampleItemModelObject select(String code)
        throws DataConnectException, DataAccessException, DataPropertyException {

    	Connection conn = getConnection();
    	PreparedStatement stmt = null;
    	ResultSet result = null;
        SampleItemModelObject modelItem = null;
        try {
        	stmt = conn.prepareStatement(SQL_SELECT);
        	stmt.setString(1, code);
        	result = stmt.executeQuery();
        	
        	// 商品情報を取得
        	if (result.next()) {
                modelItem = new SampleItemModelObject();
                modelItem.setCode(result.getString("item_cd"));
                modelItem.setName(result.getString("name"));
                modelItem.setPrice(result.getInt("price"));
                modelItem.setSimpleNote(result.getString("simple_note"));
                modelItem.setDetailNote(result.getString("detail_note"));
                modelItem.setImagePath(result.getString("image_path"));
        	}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
        return modelItem;
    }

    /**
     * select処理(全件取得)を行います。
     *
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
    public Collection select()
        throws DataConnectException, DataAccessException, DataPropertyException {

    	Connection conn = getConnection();
    	Statement stmt = null;
    	ResultSet result = null;
    	Collection collection = new ArrayList();
        SampleItemModelObject modelItem = null;
        try {
        	stmt = conn.createStatement();
        	result = stmt.executeQuery(SQL_SELECT_ALL + " ORDER BY item_cd");

        	// 商品情報を取得
        	while (result.next()) {
        		modelItem = new SampleItemModelObject();
                modelItem.setCode(result.getString("item_cd"));
                modelItem.setName(result.getString("name"));
                modelItem.setPrice(result.getInt("price"));
                modelItem.setSimpleNote(result.getString("simple_note"));
                modelItem.setDetailNote(result.getString("detail_note"));
                modelItem.setImagePath(result.getString("image_path"));
                collection.add(modelItem);
        	}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
        return collection;
    }

    /**
     * select処理(条件検索)を行います。
     *
     * @param sort ソートするカラム名
     * @param startRow データを取得する開始位置
     * @param listNum データを取得する件数
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
    public Collection select(String sort, int startRow, int listNum)
        throws DataConnectException, DataAccessException, DataPropertyException {

    	int idx = 0;
    	Connection conn = getConnection();
    	Statement stmt = null;
    	ResultSet result = null;
    	Collection collection = new ArrayList();
        SampleItemModelObject modelItem = null;
        try {
        	stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	result = stmt.executeQuery(SQL_SELECT_ALL + " ORDER BY " + sort);

        	// 開始位置へ移動
        	if (startRow < 1) {
        		return collection;
        	} else {
        		if(!result.absolute(startRow)) {
        			return collection;
        		}
        	}
        	
        	// 商品情報を取得
        	while (idx < listNum) {
        		modelItem = new SampleItemModelObject();
                modelItem.setCode(result.getString("item_cd"));
                modelItem.setName(result.getString("name"));
                modelItem.setPrice(result.getInt("price"));
                modelItem.setSimpleNote(result.getString("simple_note"));
                modelItem.setDetailNote(result.getString("detail_note"));
                modelItem.setImagePath(result.getString("image_path"));
                collection.add(modelItem);
                
                if (!result.next()) {
                	break;
                }
                
                idx++;
        	}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
        return collection;
    }

    /**
     * select処理(条件検索)を行います。
     *
     * @param code レコードのコード（検索条件）
     * @param name レコードの名前（検索条件）
     * @param sort ソートするカラム名
     * @param startRow データを取得する開始位置
     * @param listNum データを取得する件数
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
    public Collection select(String code, String name, String sort,int startRow,int listNum)
        throws DataConnectException, DataAccessException, DataPropertyException {
    	
    	int idx = 0;
    	String sql, join;
    	Connection conn = getConnection();
    	Statement stmt = null;
    	ResultSet result = null;
    	Collection collection = new ArrayList();
        SampleItemModelObject modelItem = null;

        //SQL文作成
        sql = "SELECT item_cd, name, price, simple_note, detail_note, image_path FROM b_fw_sample_item";
        join = " WHERE ";

        if (code != null && !code.trim().equals("")) {
            sql += join;
            sql += "item_cd LIKE '" + code + "%'";
            join = " AND ";
        }
        if (name != null && !name.trim().equals("")) {
            sql += join;
            sql += "name LIKE '" + name + "%'";
        }
        if (sort != null && !sort.trim().equals("")) {
            sql += " ORDER BY " + sort + " ASC";
        }

        // TODO
        System.out.println("SELECT:" + sql);
        System.out.println("code:" + code + " name:" + name + " sort:" + sort + " startRow:" + startRow + "listNum:" + listNum);

        try {
        	stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	result = stmt.executeQuery(sql);

        	// 開始位置へ移動
        	if (startRow < 1) {
        		return collection;
        	} else {
        		if(!result.absolute(startRow)) {
        			return collection;
        		}
        	}
        	
        	// 商品情報を取得
        	while (idx < listNum) {
        		modelItem = new SampleItemModelObject();
                modelItem.setCode(result.getString("item_cd"));
                modelItem.setName(result.getString("name"));
                modelItem.setPrice(result.getInt("price"));
                modelItem.setSimpleNote(result.getString("simple_note"));
                modelItem.setDetailNote(result.getString("detail_note"));
                modelItem.setImagePath(result.getString("image_path"));
                collection.add(modelItem);
                
                if (!result.next()) {
                	break;
                }
                
                idx++;
        	}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
        return collection;

    }

    /**
     * テーブルの件数を取得します。
     *
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
	public int getCount() throws DataConnectException, DataAccessException,
			DataPropertyException {
		
		int count = 0;
    	Connection conn = getConnection();
    	Statement stmt = null;
    	ResultSet result = null;

        try {
        	stmt = conn.createStatement();
        	result = stmt.executeQuery(SQL_COUNT_ALL);
            if (result.next()) {
            	count = result.getInt(1);
            }
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
		return count;
	}
	
    /**
     * 条件に一致するレコードの件数を取得します。
     * 
     * @param code 商品コード（検索条件）
     * @param name 商品名（検索条件）
     * @throws DataConnectException データ接続に関連する例外が発生
     * @throws DataAccessException データアクセス時の例外が発生
     * @throws DataPropertyException データプロパティ取得時の例外が発生
     * @return 商品情報群
     */
	public int getCount(String code, String name) throws DataConnectException,
			DataAccessException, DataPropertyException {

		int count = 0;
		String sql;
        String join = "";
    	Connection conn = getConnection();
    	Statement stmt = null;
    	ResultSet result = null;

        //SQL文作成
        sql = "SELECT count(*) FROM b_fw_sample_item";
        join = " WHERE ";

        //条件を生成
        if (code != null && !code.trim().equals("")) {
        	sql += join;
        	sql += "item_cd LIKE '" + code + "%'";
            join = " AND ";
        }
        if (name != null && !name.trim().equals("")) {
        	sql += join;
        	sql += "name LIKE '" + name + "%'";
        }

        // TODO
        System.out.println("COUNT:" + sql);
        System.out.println("code:" + code + " name:" + name);

        try {
        	stmt = conn.createStatement();
        	result = stmt.executeQuery(sql);
            if (result.next()) {
            	count = result.getInt(1);
            }
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
			}
		}
		return count;
	}
}
