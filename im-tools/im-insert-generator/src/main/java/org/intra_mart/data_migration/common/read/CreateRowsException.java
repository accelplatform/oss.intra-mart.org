/*
 * CreateRowsException.java
 *
 * Created on 2005/05/20,  13:17:44
 */
package org.intra_mart.data_migration.common.read;

/**
 * 表形式データの作成に失敗した場合にスローされます。
 *
 * @author intra-mart
 * 
 */
public class CreateRowsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
     * コンストラクタ
     * 
     * @param message 詳細メッセージ
     */
    public CreateRowsException(String message) {
        super(message);
    }
    /**
     * コンストラクタ
     * 
     * @param cause 原因
     */
    public CreateRowsException(Throwable cause) {
        super(cause);
    }
    /**
     * コンストラクタ
     * 
     * @param message 詳細メッセージ
     * @param cause 原因
     */
    public CreateRowsException(String message, Throwable cause) {
        super(message, cause);
    }
}
