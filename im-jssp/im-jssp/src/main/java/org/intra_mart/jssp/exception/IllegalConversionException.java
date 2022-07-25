package org.intra_mart.jssp.exception;

/**
 *　不適切な変換処理が行われたことを示すためにスローされます。
 */
public class IllegalConversionException extends JSSPSystemException {
	/**
	 * 新しい例外を作成します。
	 */
	public IllegalConversionException(){
		super();
	}

	/**
	 * 新しい例外を作成します。
	 * @param message 例外のメッセージ
	 */
	public IllegalConversionException(String message){
		super(message);
	}

	/**
	 * 新しい例外を作成します。
	 * @param message 例外のメッセージ
	 * @param cause 原因
	 */
	public IllegalConversionException(String message, Throwable cause){
		super(message, cause);
	}

	/**
	 * 新しい例外を作成します。
	 * @param cause 原因
	 */
	public IllegalConversionException(Throwable cause){
		super(cause);
	}
}
