package org.intra_mart.common.aid.jdk.java.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;
import java.util.WeakHashMap;

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
public class SocketPool extends AbstractSocketConnectionDistributor implements SocketConnectionManager{
	// インスタンス変数
	private ConnectionStore history = new ConnectionStore();
	private IdleCollection sockets = new IdleCollection(history);
	private int CONNECT_LIMIT = 1;						// 最大並列処理数
	private boolean active = true;
	private Collection createEventListeners = new ArrayList();
	private Collection closeEventListeners = new ArrayList();

	/**
	 * ソケットプールを構築します。<P>
	 * このプールで管理できるソケットコネクションは 1 個になります。
	 *
	 * @param address 接続するサーバのアドレス
	 * @param port 接続するサーバがサービスを提供しているポート
	 */
	public SocketPool(String address, int port){
		super(address, port);
		this.addCloseSocketEventListener(history);
	}

	/**
	 * ソケットプールを構築します。<P>
	 * コネクションは動的に生成されます。
	 * capacity 個よりも多くのコネクション要求があった場合、
	 * コネクションが開放されるまで待ち状態になります。<br>
	 * capacity にはプールの性質上、 1 以上の値を指定して下さい。
	 * capacity に 1 より小さな値を指定した場合、プール数は 1 になります。
	 *
	 * @param address 接続するサーバのアドレス
	 * @param port 接続するサーバがサービスを提供しているポート
	 * @param capacity 接続コネクションの最大数
	 */
	public SocketPool(String address, int port, int capacity){
		this(address, port);
		CONNECT_LIMIT = Math.max(CONNECT_LIMIT, capacity);
	}

	/**
	 * ソケットを新規に作成したイベントを通知するためのリスナーを
	 * 設定します。
	 * このメソッドで追加したリスナーには、このプールで新規にソケットが
	 * 作成されたときに、そのソケットをメソッド引数として
	 * handleSocketEventメソッドが実行されます。
	 * @param listener ソケットイベントリスナー
	 */
	public void addCreateSocketEventListener(SocketEventListener listener){
		synchronized(createEventListeners){
			createEventListeners.add(listener);
		}
	}

	/**
	 * このプールに登録されている
	 * ソケットを新規に作成したイベントを通知するためのリスナーを
	 * 返します。
	 * このメソッドの戻り値となる Collection の各要素は SocketEventListener
	 * 型オブジェクトです。
	 * @return SocketEventListener を要素とする Collection
	 */
	public Collection getCreateSocketEventListeners(){
		synchronized(createEventListeners){
			return new ArrayList(createEventListeners);
		}
	}

	/**
	 * ソケットを新規に作成したイベントのリスナーを実行します。
	 */
	private void addCreateSocketEventListener(ExtendedSocket socket) throws IOException{
		// ソケット作成イベントリスナーの実行
		synchronized(createEventListeners){
			Iterator cursor = createEventListeners.iterator();
			while(cursor.hasNext()){
				((SocketEventListener) cursor.next()).handleSocketEvent(socket);
			}
		}
	}

	/**
	 * ソケットをクローズしたイベントを通知するためのリスナーを
	 * 設定します。
	 * このメソッドで追加したリスナーには、このプールのソケットが
	 * クローズされたときに、そのソケットをメソッド引数として
	 * handleSocketEventメソッドが実行されます。
	 * @param listener ソケットイベントリスナー
	 */
	public void addCloseSocketEventListener(SocketEventListener listener){
		synchronized(closeEventListeners){
			closeEventListeners.add(listener);
		}
	}

	/**
	 * このプールに登録されている
	 * ソケットをクローズしたイベントを通知するためのリスナーを
	 * 返します。
	 * このメソッドの戻り値となる Collection の各要素は SocketEventListener
	 * 型オブジェクトです。
	 * @return SocketEventListener を要素とする Collection
	 */
	public Collection getCloseSocketEventListeners(){
		synchronized(closeEventListeners){
			return new ArrayList(closeEventListeners);
		}
	}

	/**
	 * コネクション作成最大数の取得。<P>
	 *
	 * @return このプールにより作成されるコネクションの最大数
	 */
	public int getCapacity(){
		return CONNECT_LIMIT;
	}

	/**
	 * このプールが現在管理しているコネクション数です。<p>
	 * この数は、最小が 0 で最大が getCapacity() メソッドにより得られる値の
	 * 間で変動します。
	 *
	 * @return 有効なコネクション数
	 */
	public int getSize(){
		return this.history.size();
	}

	/**
	 * 現在待機中のコネクションをです。
	 * @return 待機中のコネクション数
	 */
	public int usableConnection(){
		return this.sockets.size();
	}

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
	public synchronized SocketConnection connection() throws IOException{
		while(this.active){
			try{
				return this.sockets.popConnection();
			}
			catch(EmptyStackException ese){
				// ストックがない！
				if(getSize() >= CONNECT_LIMIT){
					if(this.sockets.wait4entry(1000)){
						continue;
					}
					else{
						Thread.yield();
					}
				}
				else{
					SocketConnection sc = new SocketConnectionImpl(this.createSocket(), this.sockets);
					sc.addCloseSocketEventListener(this.sockets);
					synchronized(closeEventListeners){
						// ソケット閉鎖リスナーの登録
						Iterator cursor = closeEventListeners.iterator();
						while(cursor.hasNext()){
							sc.addCloseSocketEventListener((SocketEventListener) cursor.next());
						}
					}
					// ソケット作成イベントリスナーの実行
					this.addCreateSocketEventListener(sc);
					if(! sc.isClosed()){
						this.history.entry(sc);
						if(this.active){ return sc; }
					}
					else{
						// コネクションは何故か既に閉じられている
						throw new IOException("Network connection is closed.");
					}
				}
			}
		}

		// このプールは既に無効
		throw new IOException("Network connection pool is closed.");
	}

	/**
	 * 指定コネクションをこのプールの管理から切り離します。<p>
	 * 指定コネクションがこのプールに管理されていたものなら、
	 * このメソッド実行後はこのプールの管理するコネクションが１つ減少するので、
	 * 新たにコネクションを作成することができるようになります。<p>
	 * 指定のコネクションをこのプールが管理していて他のスレッドによって
	 * 使用（connection() メソッドにより取得）されていない場合 true を
	 * 返します。指定のコネクションがこのプールにより管理されているもの
	 * であった場合でも、他のスレッドにより使用中の場合は false を返します。
	 * @param connection 管理から切り離すコネクション
	 * @return コネクションの切り離しに成功した場合 true
	 */
	public boolean eject(SocketConnection connection){
		if(this.history.release(connection)){
			this.sockets.release(connection);
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * このプールを破棄します。
	 */
	public void destroy(){
		this.active = false;
		clean();
	}

	/**
	 * プール中のコネクションをすべて破棄します。
	 */
	public void clean(){
		// プール中のすべてのコネクションも破棄(同様のエラー発生の可能性)
		this.sockets.reset();
	}

	/**
	 * このオブジェクトへの参照はもうないとガーベージコレクションによって
	 * 判断されたときに、ガーベージコレクタによって呼び出されます。
	 * SocketPool クラスは、このメソッドによりオブジェクトの破棄前に
	 * リソースのクリーンアップをします。
	 */
	protected void finalize() throws Throwable{
		this.destroy();
	}

	/**
	 * 待機コネクションを保管するオブジェクト
	 * また、このオブジェクトはソケット閉鎖イベントに反応して閉鎖される
	 * コネクションを破棄するためのリスナー機能も持っています。
	 */
	private static class IdleCollection extends Stack implements SocketEventListener{
		private Thread sleepingThread;
		private ConnectionStore connectionStore;

		protected IdleCollection(ConnectionStore connectionStore){
			super();
			this.connectionStore = connectionStore;
		}

		/**
		 * 保存します。
		 */
		public synchronized void entry(SocketConnection connection){
			try{
				if(this.connectionStore.isEntry(connection)){
					this.push(connection);
				}
				else{
					this.release(connection);
				}
			}
			finally{
				try{
					this.sleepingThread.interrupt();
				}
				catch(Exception e){
					Thread.yield();
				}
			}
		}

		/**
		 * 取り出します。
		 */
		public synchronized SocketConnection popConnection() throws EmptyStackException{
			return (SocketConnection) this.pop();
		}

		/**
		 * エントリから削除します。
		 */
		public synchronized boolean release(SocketConnection connection){
			try{
				return this.remove(connection);
			}
			finally{
				try{
					this.connectionStore.release(connection);
				}
				finally{
					try{
						this.sleepingThread.interrupt();
					}
					catch(Exception e){
						Thread.yield();
					}
				}
			}
		}

		/**
		 * すべての要素を破棄します。
		 */
		public void reset(){
			try{
				this.connectionStore.release();
			}
			finally{
				try{
					while(! this.isEmpty()){
						try{
							this.popConnection().close();
						}
						catch(EmptyStackException ese){
							break;			// もうプールは空らしい。
						}
						catch(IOException ioe){
							continue;
						}
					}
				}
				finally{
					try{
						this.sleepingThread.interrupt();
					}
					catch(Exception e){
						Thread.yield();
					}
				}
			}
		}

		/**
		 * entry されるのを待ちます。
		 * @param timeout entry の最大待ち時間
		 * @return entry を確認した場合 true を返します。
		 */
		public boolean wait4entry(long timeout){
			if(this.isEmpty()){
				this.sleepingThread = Thread.currentThread();
				try{
					Thread.sleep(timeout);
				}
				catch(InterruptedException ie){
					Thread.yield();
				}
				finally{
					this.sleepingThread = null;
				}
				return ! this.isEmpty();
			}
			else{
				return true;
			}
		}

		/**
		 * ソケットに関するイベント発生時に呼び出されるメソッド。
		 * サブクラスは、このメソッドを実装することにより、特定のソケットに関する
		 * イベントの発生を知ることができます。
		 * @param socket イベントの発生したソケット
		 * @throws IOException 入出力エラー
		 */
		public void handleSocketEvent(ExtendedSocket socket) throws IOException{
			this.release((SocketConnection) socket);
		}
	}

	/**
	 * 作成されたコネクションを保管するリストオブジェクト
	 */
	private static class ConnectionStore extends WeakHashMap implements SocketEventListener{
		protected ConnectionStore(){
			super();
		}

		/**
		 * 作成されたコネクションを保管します。
		 */
		public synchronized void entry(ExtendedSocket socket){
			if(! socket.isClosed()){
				// コネクション自身をキーとして現在時刻(エントリ時間)を保存
				this.put(socket, new Long(System.currentTimeMillis()));
			}
		}

		/**
		 * 指定のコネクションがエントリされているかどうかをチェックします。
		 */
		public synchronized boolean isEntry(ExtendedSocket socket){
			return this.containsKey(socket);
		}

		/**
		 * 指定されたコネクションを管理から外します。
		 * @param socket コネクション
		 * @return 指定コネクションが保管されていて削除に成功した場合 true。
		 */
		public synchronized boolean release(ExtendedSocket socket){
			// コネクション自身をキーとして現在時刻(エントリ時間)を保存
			return this.remove(socket) != null;
		}

		/**
		 * このマップの管理するすべてのコネクションを破棄します。
		 */
		public synchronized void release(){
			this.clear();
		}

		/**
		 * ソケットに関するイベント発生時に呼び出されるメソッド。
		 * サブクラスは、このメソッドを実装することにより、特定のソケットに関する
		 * イベントの発生を知ることができます。
		 * @param socket イベントの発生したソケット
		 * @throws IOException 入出力エラー
		 */
		public void handleSocketEvent(ExtendedSocket socket) throws IOException{
			this.release(socket);		// リストから削除
		}
	}

	/**
	 * このクラスは、ソケット通信用の入出力機能を提供します。<P>
	 *
	 */
	private static class SocketConnectionImpl extends SocketConnection{
		// インスタンス変数
		private IdleCollection idleCollection;

		/**
		 * ソケット入出力インスタンスを構築します。<P>
		 * このコンストラクタは、通信クライアント用のインスタンス構築用として
		 * 利用します。<BR>
		 *
		 * @exception IOException 入出力ストリームの作成時エラー
		 */
		protected SocketConnectionImpl(Socket socket, IdleCollection idleCollection) throws IOException{
			//入出力バッファ作成
			super(socket);
			this.idleCollection = idleCollection;
		}

		/**
		 * ソケットおよび入出力ストリームを閉じます。<P>
		 * このインスタンスの利用が終了したら、必ず呼び出して下さい。<BR>
		 * このメソッドを呼び出さないと、プール領域中のリソースが完全には
		 * 開放されません。<BR>
		 *
		 * @exception IOException 入出力エラー
		 */
		public synchronized void release() throws IOException{
			if(! this.isClosed()){
				if(this.lastError() == null){
					// プールに保管
					this.idleCollection.entry(this);
				}
				else{
					try{
						// エラー発生を確認→破棄
						this.close();
					}
					finally{
						// すべての接続が同様の問題の疑いアリ→すべて破棄
						this.idleCollection.reset();
					}
				}
			}
		}

		/**
		 * 入出力ストリームおよびソケットを閉じます。<BR>
		 * 入出力ストリームを閉じる前にバッファに溜められているデータをすべて
		 * 出力ストリームに書き出します。<BR>
		 * このメソッドを呼び出してソケットを閉じた場合でも、
		 *
		 * @exception IOException 入出力エラーが発生した場合
		 */
		public synchronized void close() throws IOException{
			try{
				this.idleCollection.release(this);
			}
			finally{
				super.close();
			}
		}
	}
}

/* End of File */