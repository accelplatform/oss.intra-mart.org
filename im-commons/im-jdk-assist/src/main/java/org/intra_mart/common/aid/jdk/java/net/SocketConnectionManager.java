package org.intra_mart.common.aid.jdk.java.net;

import java.io.IOException;


/**
 * 通信ソケットをプールする機能のインターフェースです。<P>
 *
 */
public interface SocketConnectionManager extends SocketConnectionDistributor{
	/**
	 * コネクション作成最大数の取得。<P>
	 *
	 * @return このプールにより作成されるコネクションの最大数
	 */
	public int getCapacity();

	/**
	 * このプールが現在管理しているコネクション数です。<p>
	 * この数は、最小が 0 で最大が getCapacity() メソッドにより得られる値の
	 * 間で変動します。
	 *
	 * @return 有効なコネクション数
	 */
	public int getSize();

	/**
	 * 現在待機中のコネクションをです。
	 * @return 待機中のコネクション数
	 */
	public int usableConnection();

	/**
	 * コネクションを新規に作成したイベントを通知するためのリスナーを
	 * 設定します。
	 * このメソッドで追加したリスナーには、このプールで新規にソケットが
	 * 作成されたときに、そのソケットをメソッド引数として
	 * handleSocketEventメソッドが実行されます。
	 * @param listener ソケットイベントリスナー
	 */
//	public void addCreateSocketEventListener(SocketEventListener listener);
}

/* End of File */