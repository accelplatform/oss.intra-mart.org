/*
 * GenerateException.java
 *
 * Created on 2006/03/07,  15:53:16
 */
package org.intra_mart.data_migration.generator;

/**
 * SQLデータの抽出時に例外が発生した場合にスローされます。
 *
 * @author intra-mart
 * 
 */
public class GenerateException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
     * コンストラクタ
     * 
     * @param message 詳細メッセージ
     */
    public GenerateException(String message) {
        super(message);
    }
    /**
     * コンストラクタ
     * 
     * @param cause 原因
     */
    public GenerateException(Throwable cause) {
        super(cause);
    }
    /**
     * コンストラクタ
     * 
     * @param message 詳細メッセージ
     * @param cause 原因
     */
    public GenerateException(String message, Throwable cause) {
        super(message, cause);
    }
}
