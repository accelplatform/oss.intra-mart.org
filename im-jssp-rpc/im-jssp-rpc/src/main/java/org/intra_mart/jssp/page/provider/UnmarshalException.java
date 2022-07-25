package org.intra_mart.jssp.page.provider;

/**
 * JSON形式の文字列表現からJavaScriptオブジェクトの生成に失敗した場合にスローされます。
 */
public class UnmarshalException extends Exception {

	public UnmarshalException() {
		super();
	}

	public UnmarshalException(String message) {
		super(message);
	}

	public UnmarshalException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnmarshalException(Throwable cause) {
		super(cause);
	}

}
