package org.intra_mart.common.aid.jdk.java.net;

import java.io.IOException;
import java.net.Socket;

/**
 * ソケットプール用のスケルトン実装を提供します。<P>
 *
 */
public abstract class SocketConnection extends ExtendedSocket{
	/**
	 * ソケット入出力インスタンスを構築します。<P>
	 * このコンストラクタは、通信クライアント用のインスタンス構築用として
	 * 利用します。<BR>
	 *
	 * @param address 接続先アドレス
	 * @param port 接続先ポート
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	protected SocketConnection(String address, int port) throws IOException{
		//入出力バッファ作成
		super(address, port);
	}

	/**
	 * ソケット入出力インスタンスを構築します。<P>
	 * このコンストラクタは、通信クライアント用のインスタンス構築用として
	 * 利用します。<BR>
	 *
	 * @param socket ソケットオブジェクト
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	protected SocketConnection(Socket socket) throws IOException{
		super(socket);
	}

	/**
	 * ソケット入出力インスタンスを構築します。<P>
	 * このコンストラクタは、通信クライアント用のインスタンス構築用として
	 * 利用します。<BR>
	 *
	 * @param socket ソケットオブジェクト
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	protected SocketConnection(ExtendedSocket socket) throws IOException{
		super(socket);
	}

	/**
	 * ソケットおよび入出力ストリームの利用を中止します。<P>
	 * このインスタンスの利用が終了したら、必ず呼び出して下さい。<BR>
	 * このメソッドを呼び出さないと、プール領域中のリソースが完全には
	 * 開放されません。<BR>
	 * また、スーパークラス ExtendedSocket の close メソッドを呼び出した場合も
	 * プール中のリソースが完全に開放されない可能性があります。<P>
	 * リソースの開放は、プールオブジェクトが必要に応じて行います。<BR>
	 *
	 * @exception IOException 入出力エラー
	 */
	public abstract void release() throws IOException;
}

/* End of File */