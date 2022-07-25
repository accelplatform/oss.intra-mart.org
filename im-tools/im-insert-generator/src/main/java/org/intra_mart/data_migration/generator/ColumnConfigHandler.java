/*
 * ColumnConfigHandler.java
 *
 * Created on 2006/03/07,  15:52:39
 */
package org.intra_mart.data_migration.generator;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.intra_mart.data_migration.generator.plugin.ColumnValueComponent;

public class ColumnConfigHandler {

	/**
	 * カラムタイプのコンフィグ
	 */
	private static final String COLUMN_CONFIG_PATH = "conf/columnType.properties";

	/**
	 * カラム毎の出力コンポーネント
	 */
	private Map columnMap = null;
	
	/**
	 * JDBCカラムタイプ
	 */
	private Map columnTypes = null;

	/**
	 * ColumnConfigHandlerを新規に生成します。
	 */
	public ColumnConfigHandler() throws ColumnConfigException {
		init();
	}
	
	/**
	 * 初期化処理
	 * 
	 * @throws ColumnConfigException 初期化処理で例外が発生
	 */
	private void init() throws ColumnConfigException {
		// カラムタイプ設定読込み
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(COLUMN_CONFIG_PATH));
		} catch (Exception e) {
			throw new ColumnConfigException(e.getMessage(), e);
		}
		
		// 対応しているカラムタイプのインスタンスを格納
		columnMap = new HashMap();
		Iterator iterator = properties.keySet().iterator();
		try {
			while (iterator.hasNext()) {
				String name = (String)iterator.next();
				String className = properties.getProperty(name);
				
				if (className == null ||className.trim().length() == 0) {
					continue;
				}
				
				Class clazz = Class.forName(className);
				if (clazz.isAssignableFrom(ColumnValueComponent.class)) {
					throw new ColumnConfigException("Illegal class type: " + clazz.getName());
				}
				columnMap.put(name, clazz.newInstance());
			}
		} catch (Exception e) {
			throw new ColumnConfigException(e.getMessage(), e);
		}
		
		// JDBCのカラムタイプ
		columnTypes = new HashMap();
		try {
			Class clazz = Class.forName("java.sql.Types");
			Field[] field = clazz.getFields();
			for (int i = 0; i < field.length; i ++) {
				columnTypes.put(field[i].getName(), new Integer(field[i].getInt(null)));
			}
		} catch (Exception e) {
			throw new ColumnConfigException(e.getMessage(), e);
		}
	}

	/**
	 * 指定されたカラムタイプが使用可能かを判定します。
	 * 
	 * @param columnType カラムタイプ
	 * @return 可能:true 不可能:false
	 * @throws ColumnConfigException カラム設定で例外が発生
	 */
	public boolean isSupportedColumnType(int columnType) throws ColumnConfigException {
		String name = getColumnTypeName(columnType);
		if (name == null) {
			return false;
		}
		if (!columnMap.containsKey(name)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 指定した値のJDBCカラムタイプ名を取得します。
	 * 存在しない場合はnullを返します。
	 * 
	 * @param columnType カラムタイプの値
	 * @return カラムタイプ名
	 * @throws ColumnConfigException カラムタイプ名取得時に例外が発生
	 */
	public String getColumnTypeName(int columnType) throws ColumnConfigException {
		Iterator iterator = columnTypes.keySet().iterator();
		while (iterator.hasNext()) {
			String name = (String)iterator.next();
			Integer num = (Integer)columnTypes.get(name);
			if (num.intValue() == columnType) {
				return name;
			}
		}
		return null;
	}
	
	/**
	 * 指定した型のColumnValueComponentを返します。
	 * 
	 * @param columnType カラムタイプ
	 * @return カラムコンポーネント
	 * @throws ColumnConfigException ColumnValueComponent取得時に例外が発生
	 */
	public ColumnValueComponent getComponent(int columnType) throws ColumnConfigException {
		if(!isSupportedColumnType(columnType)) {
			return null;
		}
		return (ColumnValueComponent)columnMap.get(getColumnTypeName(columnType));
	}
}
