package org.intra_mart.common.aid.jdk.java.net;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * このクラスは、サーバプロセスの基礎情報を管理します。<P>
 *
 */
public class Localhost implements Serializable{
	/**
	 * このクラスから生成される唯一のインスタンス。
	 */
	private static Localhost _instance = null;

	/**
	 * このクラスから生成される唯一のインスタンスを返します。
	 * このメソッドが返すインスタンスは、常に一定で、
	 * VM 内で共有されます。
	 * @return このクラスのインスタンス
	 * @throws UnknownHostException このコンピュータのＩＰアドレスが判定できなかった場合
	 */
	public static Localhost instance() throws UnknownHostException{
		if(_instance == null){
			_instance = new Localhost();
		}
		return _instance;
	}

	private byte[] localhostIPAddress = {127,0,0,1};
	private String localhostAddress = "127.0.0.1";	// ループバック
	private String localhostName = "localhost";

	/**
	 * 唯一のコンストラクタ
	 * @throws UnknownHostException このコンピュータのＩＰアドレスが判定できなかった場合
	 */
	private Localhost() throws UnknownHostException {
		super();

		// サーバのネットワーク設定(Localhost)をチェック
		InetAddress localHost = InetAddress.getLocalHost();
		localhostIPAddress = localHost.getAddress();
		localhostAddress = localHost.getHostAddress();
		localhostName = localHost.getHostName();

	}

	/**
	 * このコンピュータのＩＰアドレスを返します。
	 * @return IPアドレス
	 */
	public byte[] getAddress(){
		return localhostIPAddress;
	}

	/**
	 * このコンピュータのアドレスを返します。
	 * @return アドレス
	 */
	public String getHostAddress(){
		return localhostAddress;
	}

	/**
	 * このコンピュータのホスト名を返します。
	 * @return ホスト名
	 */
	public String getHostName(){
		return localhostName;
	}
}

