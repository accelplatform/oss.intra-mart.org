package org.intra_mart.common.aid.jdk.java.lang;



/**
 * シャットダウンイベント時に実行する任意のロジックを実装するためのインターフェースです。<P>
 * このインターフェースを実装して {@link ShutdownEventController} クラスに登録する
 * ことで、任意のロジックをシャットダウン直前に実行することができます。
 *
 * @see org.intra_mart.common.aid.jdk.java.lang.ShutdownEventController
 */
public interface ShutdownListener{

	/**
	 * シャットダウンイベントが発生したときに実行されるメソッドです。
	 * このメソッドを実装することで、
	 * シャットダウン直前に任意のロジックを実行することができます。
	 * このメソッドは、プロセスのシャットダウン直前に実行されるため、
	 * 必ずスレッドセーフであり、かつデッドロックしないように
	 * プログラムする必要があります。
	 * このメソッド実行中の {@link ShutdownEventController} に対するリスナー登録は、
	 * デッドロックの原因となるので、絶対にやってはいけません。
	 */
	public void handleShutdown();
}

