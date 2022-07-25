package org.intra_mart.common.aid.jdk.java.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * 通信ソケットに関するイベント発生を受け取るためのスケルトン実装です。<P>
 * ソケットに関するイベント通知側と組み合わせて利用することにより、
 * ソケットに関する特定のイベント発生時に独自の処理を実行することができます。
 *
 */
public interface SocketEventListener{
	/**
	 * ソケットに関するイベント発生時に呼び出されるメソッド。
	 * サブクラスは、このメソッドを実装することにより、特定のソケットに関する
	 * イベントの発生を知ることができます。
	 * @param socket イベントの発生したソケット
	 * @throws IOException 入出力エラー
	 */
	public void handleSocketEvent(ExtendedSocket socket) throws IOException;
}

