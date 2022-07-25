package org.intra_mart.jssp.page.provider;

/**
 * JSON形式の文字列表現からJavaScriptオブジェクトの生成に失敗した場合にスローされます。
 */
public class UnmarshalJSONStringException extends Exception {

	public UnmarshalJSONStringException() {
		super();
	}

	public UnmarshalJSONStringException(String message) {
		super(message);
	}

	public UnmarshalJSONStringException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnmarshalJSONStringException(Throwable cause) {
		super(cause);
	}

}
