/*
 * ColumnConfigException.java
 *
 * Created on 2006/03/10,  10:33:10
 */
package org.intra_mart.data_migration.generator;

/**
 * カラムタイプ設定時に例外が発生した場合にスローされます。
 *
 * @author intra-mart
 * 
 */
public class ColumnConfigException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
     * コンストラクタ
     * 
     * @param message 詳細メッセージ
     */
    public ColumnConfigException(String message) {
        super(message);
    }
    /**
     * コンストラクタ
     * 
     * @param cause 原因
     */
    public ColumnConfigException(Throwable cause) {
        super(cause);
    }
    /**
     * コンストラクタ
     * 
     * @param message 詳細メッセージ
     * @param cause 原因
     */
    public ColumnConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
