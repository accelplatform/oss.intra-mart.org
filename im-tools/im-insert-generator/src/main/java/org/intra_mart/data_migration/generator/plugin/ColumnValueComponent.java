/*
 * ColumnValueComponent.java
 *
 * Created on 2006/03/07,  15:52:39
 */
package org.intra_mart.data_migration.generator.plugin;

import org.intra_mart.data_migration.generator.GenerateException;

/**
 * カラムの値を保管します。実装クラスはカラムタイプ毎に存在します。
 * 必ずデフォルトコンストラクタを定義してください。
 * 
 * @author intra-mart
 */
public interface ColumnValueComponent {
	
	/**
	 * クエリの値を返します。
	 * カラムタイプやnull値に応じた書式で返す必要があります。
	 * 
	 * @param value データベースからの取得値
	 * @return クエリの値
	 * @throws GenerateException 値生成時に例外が発生
	 */
	public String getQueryValue(String value) throws GenerateException;
}
