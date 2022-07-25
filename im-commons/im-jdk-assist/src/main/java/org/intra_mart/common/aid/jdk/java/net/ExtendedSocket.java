package org.intra_mart.common.aid.jdk.java.net;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream;
import org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream;
import org.intra_mart.common.aid.jdk.util.ErrorEvent;
import org.intra_mart.common.aid.jdk.util.ErrorEventListener;


/**
 * このクラスは、ソケット通信用の入出力機能を提供します。<P>
 * このクラスの提供している独自機能や、このクラスの持つメソッドから
 * 取得したオブジェクトを使っている最中に例外がスローされた場合、
 * このクラスのインスタンスは発生した例外を保存します。
 * 保存された例外は、lastError メソッドにより知ることができます。
 * <BR>
 * ただし、このクラスが継承しているスーパークラスのメソッドの
 * 呼び出し時にスローされた例外に関しては感知することはできません。
 *
 */
public class ExtendedSocket{
	// インスタンス変数
	/**
	 * ネットワーク利用中に発生した例外の履歴を保持するコレクション
	 */
	private ThrowableList BAD_BOYS;
	/**
	 * ネットワーク閉鎖時に呼び出すイベントリスナー群
	 */
	private Collection closeEventListeners = new ArrayList();
	/**
	 * 親オブジェクト（存在しない場合は null）
	 */
	private ExtendedSocket parentExtendedSocket = null;
	/**
	 * ソケット
	 */
	private Socket socket;
	/**
	 * 出力ストリーム。
	 */
	private ExtendedOutputStream output;
	/**
	 * 入力ストリーム。
	 */
	private ExtendedInputStream input;
	/**
	 * ソケットを閉じたかどうかをチェックするためのフラグ
	 */
	private boolean socketClosed = false;

	/**
	 * ソケットを構築します。<P>
	 * このコンストラクタは、通信クライアント用のインスタンス構築用として
	 * 利用します。<BR>
	 *
	 * @param address 接続先アドレス
	 * @param port 接続先ポート
	 * @exception UnknownHostException ホストの IP アドレスが判定できなかった場合にスローされます。
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	public ExtendedSocket(String address, int port) throws UnknownHostException, IOException{
		// ソケットの作成
		super();
		Socket sock = new Socket(address, port);
		try{
			this.init(sock);
		}
		catch(UnknownHostException uhe){
			sock.close();
			throw uhe;
		}
		catch(IOException ioe){
			sock.close();
			throw ioe;
		}
	}

	/**
	 * ソケットを構築します。<P>
	 * このコンストラクタは、通信クライアント用のインスタンス構築用として
	 * 利用します。<BR>
	 *
	 * @param socket ソケットオブジェクト
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	public ExtendedSocket(Socket socket) throws IOException{
		// ソケットの作成
		super();
		this.init(socket);
	}

	/**
	 * ソケットを構築します。<P>
	 * このコンストラクタは、通信クライアント用のインスタンス構築用として
	 * 利用します。<BR>
	 *
	 * @param socket ソケットオブジェクト
	 */
	protected ExtendedSocket(ExtendedSocket socket){
		// ソケットの作成
		super();
		this.socket = socket.getSocket();
		this.input = socket.getInputStream();
		this.output = socket.getOutputStream();
		this.BAD_BOYS = socket.BAD_BOYS;
		this.parentExtendedSocket = socket;
	}

	/**
	 * オブジェクトの初期化ロジック。
	 * コンストラクタからしかコールされません。
	 * @param socket ソケットオブジェクト
	 * @throws SocketException ソケットに関連するエラー
	 * @throws IOException 入出力エラー
	 */
	private void init(Socket socket) throws SocketException, IOException{
		this.socket = socket;
		this.BAD_BOYS = new ThrowableList();
		input = new ExtendedInputStream(this.socket.getInputStream(), BAD_BOYS);
		output = new ExtendedOutputStream(this.socket.getOutputStream(), BAD_BOYS);
	}

	/**
	 * このオブジェクトの持つソケットオブジェクトを返します。
	 * このメソッドから取得したソケットオブジェクトの入出力ストリームと
	 * このオブジェクト自身から取得した入出力ストリームを
	 * 同時に利用した場合、データ送受信の整合性が失われてしまう可能性が
	 * あります。
	 * @return ソケットオブジェクト
	 */
	public Socket getSocket(){
		return this.socket;
	}

	/**
	 * 入力ストリームの取得。<P>
	 *
	 * @return 入力ストリーム
	 */
	public ExtendedInputStream getInputStream(){
		return input;
	}

	/**
	 * 出力ストリームの取得。<P>
	 *
	 * @return 出力ストリーム
	 */
	public ExtendedOutputStream getOutputStream(){
		return output;
	}

	/**
	 * ソケットをクローズしたイベントを通知するためのリスナーを
	 * 設定します。
	 * このメソッドで追加したリスナーには、このソケットが
	 * クローズされたときに、そのソケットをメソッド引数として
	 * handleSocketEventメソッドが実行されます。
	 * @param listener ソケットイベントリスナー
	 */
	public void addCloseSocketEventListener(SocketEventListener listener){
		this.closeEventListeners.add(listener);
	}

	/**
	 * このソケットに登録されているクローズ・イベント・リスナーを返します。
	 * このメソッドの戻り値となる Collection の各要素は SocketEventListener
	 * 型オブジェクトです。
	 * @return SocketEventListener を要素とする Collection
	 */
	public Collection getCloseSocketEventListeners(){
		return new ArrayList(this.closeEventListeners);
	}

	/**
	 * ソケット閉鎖イベントリスナーの実行
	 */
	private void callCloseSocketEventListener() throws IOException{
		Iterator cursor = this.closeEventListeners.iterator();
		while(cursor.hasNext()){
			((SocketEventListener) cursor.next()).handleSocketEvent(this);
		}
	}

	/**
	 * このインスタンスのメソッド中で最後に発生した例外を返します。<BR>
	 *
	 * @return 最後に発生した例外
	 */
	public Throwable lastError(){
		try{
			return (Throwable) this.BAD_BOYS.getLast();
		}
		catch(NoSuchElementException nsee){
			// 空だ＝エラーは検知されなかったのだ
			return null;
		}
	}

	/**
	 * このインスタンスのメソッド実行中に発生した例外をすべて破棄します。<p>
	 * このメソッドを実行すると、次に例外が発生するまで
	 * {@link #lastError()}メソッドは
	 *  null を返し、
	 * {@link #errorIterator()}メソッドは空の反復子を返します。
	 */
	public void clearError(){
		this.BAD_BOYS.clear();
	}

	/**
	 * このインスタンスのメソッド実行中に発生した例外を返します。
	 * このメソッドの返す反復子は発生時間順に例外オブジェクトを返します。
	 * 反復子の返すオブジェクトは、Throwable クラスと代入互換のある
	 * オブジェクトになります。
	 * @return 例外の反復子
	 */
	public Iterator errorIterator(){
		return this.BAD_BOYS.iterator();
	}

	/**
	 * 入出力ストリームおよびソケットを閉じます。<BR>
	 * 入出力ストリームを閉じる前にバッファに溜められているデータをすべて
	 * 出力ストリームに書き出します。<BR>
	 *
	 * @exception IOException 入出力エラーが発生した場合
	 */
	public void close() throws IOException{
		if(! this.socketClosed){
			try{
				if(this.parentExtendedSocket == null){
					try{
						output.close();
					}
					catch(IOException ioe){
						this.BAD_BOYS.add(ioe);
						throw ioe;
					}
					finally{
						try{
							input.close();
						}
						catch(IOException ioe){
							this.BAD_BOYS.add(ioe);
							throw ioe;
						}
						finally{
							try{
								this.socket.close();
							}
							catch(IOException ioe){
								this.BAD_BOYS.add(ioe);
								throw ioe;
							}
						}
					}
				}
				else{
					this.parentExtendedSocket.close();
				}
			}
			finally{
				this.socketClosed = true;
				// イベントリスナーへのイベント通知
				this.callCloseSocketEventListener();
			}
		}
		else{
			throw new IOException("Socket is closed.");
		}
	}

	/**
	 * このソケットが閉じられているかどうかをチェックします。
	 * @return ソケットが閉じられている場合は true
	 */
	public boolean isClosed(){
		if(this.socketClosed){
			return true;
		}
		else{
			if(this.parentExtendedSocket == null){
				// ソケットの状態を調査
				try{
					Method isClosedMethod = this.socket.getClass().getMethod("isClosed", new Class[0]);
					this.socketClosed = ((Boolean) isClosedMethod.invoke(this.socket, new Object[0])).booleanValue();
					if(this.socketClosed){
						try{
							// イベントリスナーへのイベント通知
							this.callCloseSocketEventListener();
						}
						catch(IOException ioe){
							// エラー発生だ
							this.BAD_BOYS.add(ioe);
						}
						return true;
					}
					else{
						return false;
					}
				}
				catch(Exception e){
					// ちっ、JRE は 1.4 未満か(>_<)
					return false;
				}
			}
			else{
				this.socketClosed = this.parentExtendedSocket.isClosed();
				if(this.socketClosed){
					try{
						// イベントリスナーへのイベント通知
						this.callCloseSocketEventListener();
					}
					catch(IOException ioe){
						// エラー発生だ
						this.BAD_BOYS.add(ioe);
					}
					return true;
				}
				else{
					return false;
				}
			}
		}
	}

	private static class ThrowableList extends LinkedList implements ErrorEventListener{
		/**
		 * コンストラクタ
		 */
		protected ThrowableList(){
			super();
		}

		/**
		 * 発生した例外を受け取ります。
		 * @param err 発生した例外イベント
		 */
		public void handleError(ErrorEvent err){
			this.add(err.getException());
		}
	}
}


/* End of File */