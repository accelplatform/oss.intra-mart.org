package org.intra_mart.common.aid.jdk.java.net;

import java.io.IOException;


/**
 * このクラスは、通信ソケットをプールする機能を提供します。<P>
 * プール機能を利用することにより少ないソケットを複数のプログラムから
 * 利用できるようになります。<BR>
 * このプールは、１つのインスタンスに付き１つの専用スレッドを持ちます。
 * プールは専用のスレッドにより管理され、一定時間に１つのコネクションが
 * 順に破棄されていきます。したがって、コネクションを全く使用しないまま
 * 長い時間が経過すると、このプール中にプールされているソケット・コネクションは
 * ０個になります。
 *
 */
public interface SocketConnectionDistributor{
	/**
	 * コネクションの取得。<P>
	 * プール中にコネクションが待機している場合にはプールの中から
	 * 待機中のコネクションを返却します。
	 * プール中にコネクションが存在しない場合には、
	 * 新しいコネクションを生成して返します。
	 *
	 * @throws IOException ソケットの接続エラー
	 * @return 通信用インスタンス
	 * @see org.intra_mart.common.aid.jdk.java.net.SocketConnection
	 */
	public SocketConnection connection() throws IOException;

	/**
	 * このプールを破棄します。
	 */
	public void destroy();

	/**
	 * プール中のコネクションをすべて破棄します。
	 */
	public void clean();
}

/* End of File */