package org.intra_mart.common.aid.jdk.java.lang;


/**
 * このインタフェースは、例外情報を出力するためのものです。<p>
 * {@link StdErr} クラスと合わせて利用します。
 *
 */
public interface ThrowablePrintListener{
	/**
	 * 例外のメッセージを出力します。
	 * @param t 例外
	 */
	public void printMessage(Throwable t);

	/**
	 * 例外のスタックトレースを出力します。
	 * @param t 例外
	 */
	public void printStackTrace(Throwable t);
}

