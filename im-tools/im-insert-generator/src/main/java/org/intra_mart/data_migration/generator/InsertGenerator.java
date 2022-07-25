/*
 * InsertGenerator.java
 *
 * Created on 2006/03/07,  15:52:39
 */
package org.intra_mart.data_migration.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.intra_mart.data_migration.common.read.database.DatabaseReader;
import org.intra_mart.data_migration.common.util.IOUtil;

import org.intra_mart.data_migration.common.util.DbUtil;

/**
 * 指定したテーブルから全てのデータを読み込み、INSERT文を生成します。
 * 
 * @author intra-mart
 */
public class InsertGenerator extends DatabaseReader implements Generator {
	
    // コンフィグパス
    private static final String INSERT_GENERATOR_CONFIG_PATH = "conf/insertGenerator.properties";
    
    private GeneratorContext context = null;

	public void execute(GeneratorContext context) throws GenerateException {
		this.context = context;
		
		// テーブル名のチェック
		if (context.getTableName() == null ||context.getTableName().trim().length() == 0) {
			throw new GenerateException("Please set 'table_name'");
		}
		
		// 設定ファイル読込み
        int maxRow;
        try {
        	Properties configProperties = new Properties();
        	configProperties.load(new FileInputStream(INSERT_GENERATOR_CONFIG_PATH));
            maxRow = Integer.parseInt(configProperties.getProperty("rotateRow"));
        } catch (Exception e) {
            throw new GenerateException(e);
        }

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			// クエリーの発行
			connection = getConnection(context.getInput());
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM " + context.getTableName());
			
			// カラムデータの解析
			ColumnConfigHandler handler = new ColumnConfigHandler();
			ResultSetMetaData metaDate = resultSet.getMetaData();
			int columnCount = metaDate.getColumnCount();
			for (int i = 1; i <= columnCount; i ++) {
				if (!handler.isSupportedColumnType(metaDate.getColumnType(i))) {
					throw new GenerateException("ColumnType is Not Supported: " + metaDate.getColumnName(i));
				}
			}

			// 出力ファイル
			StringBuffer result = new StringBuffer();
			
			// INSERT INTO tableName(columnNames... )
			StringBuffer prefix = new StringBuffer();
			prefix.append("INSERT INTO " + context.getTableName() + "(");
			prefix.append(metaDate.getColumnName(1));
			for (int i = 2; i <= columnCount; i ++) {
				prefix.append(", ").append(metaDate.getColumnName(i));
			}
			prefix.append(") ");
			
			int total = 0;
			int countRow = 0;;
			while(true) {

				// 最終行
				if (!resultSet.next()) {
					if (result.length() != 0) {
						write(result.toString().getBytes());
					}
					break;
				}
				
				// 一行読込み
				result.append(prefix);
				result.append("VALUES(");
				result.append(handler.getComponent(metaDate.getColumnType(1))
						.getQueryValue(resultSet.getString(1)));
				for (int i = 2; i <= columnCount; i ++) {
					result.append(", ");
					result.append(handler.getComponent(metaDate.getColumnType(i))
							.getQueryValue(resultSet.getString(i)));
				}
				result.append(");\n");
				total ++;
				countRow ++;
				
				// ローテイト
				if (maxRow <= countRow) {
					write(result.toString().getBytes());
					result = new StringBuffer();
					fileCount ++;
					countRow = 0;
				}
			}

			// 結果をログに出力
			context.getLogger().info("[Success] Total " + total + " rows.");
			
		} catch (IOException e) {
			throw new GenerateException(e);
		} catch (SQLException e) {
			throw new GenerateException(e);
		} catch (ClassNotFoundException e) {
			throw new GenerateException(e);
		} catch (ColumnConfigException e) {
			throw new GenerateException(e);
		} finally {
			try {
				DbUtil.close(statement, resultSet);
			} catch (SQLException sqle) {
			}
			try {
				DbUtil.close(connection);
			} catch (SQLException sqle) {
			}
		}
	}

	int fileCount = 1;
	
	/**
	 * ファイルに出力します。
	 * 
	 * @param byts 出力データ
	 * @param fileName ファイル名
	 * @throws IOException ファイル出力時に例外が発生
	 */
	private void write(byte[] byts) throws IOException {
		InputStream inputStream = null;
		try {
			File file = new File(context.getOutput(), context.getTableName() + "_" + fileCount + ".sql");
			inputStream = new ByteArrayInputStream(byts);
			IOUtil.write(inputStream, file);
		} catch(IOException e) {
			throw e;
		} finally {
			try {
				IOUtil.close(inputStream);
			} catch (IOException ioe) {
			}
		}
	}
}
