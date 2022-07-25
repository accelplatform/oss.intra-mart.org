/*
 * MigrationException.java
 *
 * Created on 2005/05/16,  15:53:16
 */
package org.intra_mart.data_migration.common;

/**
 * 移行データの抽出時に例外が発生した場合にスローされます。
 *
 * @author intra-mart
 * 
 */
public class MigrationException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
     * コンストラクタ
     * 
     * @param message 詳細メッセージ
     */
    public MigrationException(String message) {
        super(message);
    }
    /**
     * コンストラクタ
     * 
     * @param cause 原因
     */
    public MigrationException(Throwable cause) {
        super(cause);
    }
    /**
     * コンストラクタ
     * 
     * @param message 詳細メッセージ
     * @param cause 原因
     */
    public MigrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
