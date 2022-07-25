package org.intra_mart.common.aid.jdk.java.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * このクラスは、通信ソケットを作成する機能を提供します。<P>
 * このクラスを継承してソケットプールを作成することができます。
 *
 */
public abstract class AbstractSocketConnectionDistributor implements SocketConnectionDistributor{
	// インスタンス変数
	private String serverAddress;						// 接続先アドレス
	private int serverPort;								// 接続先ポート

	/**
	 * ソケット作成オブジェクトを構築します。<P>
	 *
	 * @param address 接続するサーバのアドレス
	 * @param port 接続するサーバがサービスを提供しているポート
	 */
	protected AbstractSocketConnectionDistributor(String address, int port){
		serverAddress = address;
		serverPort = port;
	}

	/**
	 * 新しいソケットを作成します。
	 * @return ソケット
	 * @throws UnknownHostException ホストの IP アドレスを判定できなかった場合
	 * @throws IOException ソケットの生成中に入出力エラーが発生した場合
	 */
	protected Socket createSocket() throws UnknownHostException, IOException{
		return new Socket(this.serverAddress, this.serverPort);
	}

	/**
	 * アドレスを返します。
	 * @return このプールのネットワーク接続先サーバアドレス
	 */
	public String getAddress(){
		return serverAddress;
	}

	/**
	 * ポートを返します。
	 * @return このプールのネットワーク接続先サーバポート
	 */
	public int getPort(){
		return serverPort;
	}
}

/* End of File */