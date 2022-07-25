package org.intra_mart.common.aid.jdk.java.lang;

import java.util.Collection;
import java.util.HashSet;

import org.intra_mart.common.platform.log.Logger;

/**
 * このクラスは、サーバプロセスの基礎ロジック。<P>
 *
 * @see org.intra_mart.common.aid.jdk.java.lang.ShutdownListener
 */
public class ShutdownEventController{
	
	private static Logger _logger = Logger.getLogger();
	
	private static ShutdownEventHook eventThread = null;
	private static Collection listeners = new HashSet();

	/**
	 * シャットダウンリスナーを登録します。
	 * @param listener イベントリスナーオブジェクト
	 * @throws IllegalStateException すでにシャットダウン処理中
	 * @see org.intra_mart.common.aid.jdk.java.lang.ShutdownListener
	 */
	public static synchronized void addListener(ShutdownListener listener){
		if(listeners.add(listener)){
			_logger.info("Regist shutdown listener[exit]: " + listener.getClass().toString());
		}

		// イベント登録のチェック
		ensureShutdownHook();
	}

	/**
	 * シャットダウンリスナーを削除します。<p>
	 * ただし、すでにシャットダウンフェーズに入ってしまっている場合は、
	 * リスナーの削除に成功しても削除したリスナが実行されてしまう
	 * 可能性があります。
	 * @param listener イベントリスナーオブジェクト
	 * @return リストに、指定された要素が格納されている場合は true
	 * @see org.intra_mart.common.aid.jdk.java.lang.ShutdownListener
	 */
	public static synchronized boolean removeListener(ShutdownListener listener){
		try{
			if(listeners.remove(listener)){
				_logger.info("Remove shutdown listener[exit]: " + listener.getClass().toString());
				return true;
			}
			else{
				return false;
			}
		}
		finally{
			ensureShutdownHook();
		}
	}

	/**
	 * VM に対するイベントフック処理をチェックします。
	 */
	private static void ensureShutdownHook(){
		if(listeners.isEmpty()){
			// 処理すべきリスナーがなくなったのでイベント登録をキャンセル
			if(eventThread != null){
				if(Runtime.getRuntime().removeShutdownHook(eventThread)){
					_logger.info("Remove shutdown event hook to process.");
					try{
						eventThread.reject();		// 破棄
					}
					finally{
						eventThread = null;
					}
				}
			}
		}
		else{
			if(eventThread == null){
				// 処理すべきリスナーが存在するのでイベント登録
				eventThread = new ShutdownEventHook();
				try{
					Runtime.getRuntime().addShutdownHook(eventThread);
					_logger.info("Regist shutdown event hook to process.");
				}
				catch(RuntimeException re){
					eventThread.reject();		// 破棄
				}
				catch(Error e){
					eventThread.reject();		// 破棄
				}
			}
		}
	}

	/**
	 * 現在登録されているリスナを返します。
	 * @return 登録されているリスナ
	 */
	public static synchronized ShutdownListener[] getShutdownListeners(){
		return (ShutdownListener[]) listeners.toArray(new ShutdownListener[listeners.size()]);
	}

	/**
	 * このプロセスを終了します。
	 * このメソッドは、System.exit(0) と同様です。
	 */
	public static void shutdown(){
		shutdown(0);
	}

	/**
	 * このプロセスを終了します。
	 * 引数はステータスコードとして作用します。
	 * 通例、ゼロ以外のステータスコードは異常終了を示します。
	 * このメソッドは、正常に復帰することはありません。
	 * @param status 終了のステータス
	 */
	public static void shutdown(int status){
		// exit のコールは１度のみで他の呼び出しをブロック(復帰させない)
		System.exit(status);									// プロセス終了
	}

	/**
	 * シャットダウン中（またはシャットダウン処理終了）かどうかを判定。
	 * @return シャットダウン中（またはシャットダウン処理を終了したとみなされる）場合は、true。
	 */
	public static synchronized boolean isShutdown(){
		if(eventThread == null){
			try{
				Thread thread = new Thread();	// 検査用
				try{
					Runtime.getRuntime().removeShutdownHook(thread);
				}
				finally{
					thread.start();
				}
				return false;
			}
			catch(IllegalStateException ise){
				return true;
			}
		}
		else{
			return ! eventThread.isValid();
		}
	}


	/**
	 * 唯一のコンストラクタ。
	 * 不用意にインスタンス化されないために隠蔽化しています。
	 *
	 */
	private ShutdownEventController(){
		super();
	}

	/**
	 * VM のシャットダウンイベントをフックするスレッド
	 */
	private static class ShutdownEventHook extends Thread{
		/**
		 * シャットダウンイベントとして有効かどうかのフラグ
		 */
		private boolean isEntry = true;

		/**
		 * 唯一のコンストラクタ。
		 */
		private ShutdownEventHook(){
			super();
		}

		/**
		 * シャットダウン中かどうか判定します。
		 * @return シャットダウンフェーズに入る前の状態の場合 true
		 */
		public boolean isValid(){
			return this.isEntry;
		}

		/**
		 * このスレッドを破棄します。
		 */
		public void reject(){
			this.isEntry = false;
			this.start();
		}

		/**
		 * スレッド実行ロジック。
		 * このメソッドは、Runtime によりシャットダウンイベント発生時に
		 * スレッド実行されます。
		 * このメソッドでは、指定された ShutdownListener を順に実行していき、
		 * 登録済みのすべての ShutdownListener を実行終了と同時に終了します。
		 */
		public void run(){
			if(this.isEntry){
				try{
					this.isEntry = false;
					_logger.info("Begin shutdown phase.");

					// リスナの取得
					ShutdownListener[] listeners = ShutdownEventController.getShutdownListeners();

					// 個々のイベント処理
					for(int idx = 0; idx < listeners.length; idx++){
						try{
							_logger.info("Call shutdown-listener[exit]: " + listeners[idx].getClass().getName());
							listeners[idx].handleShutdown();
//							System.out.println("Shutdown listener is execution-completed[exit]: " + listeners[idx].getClass().getName());
						}
						catch(Throwable t){
							// イベント処理中にエラー発生
							_logger.error("Shutdown event execute error[" + listeners[idx].getClass().getName() + "]: " + t.getMessage(), t);
						}
					}
				}
				catch(Throwable t){
					t.printStackTrace();
				}
				finally{
					_logger.info("Process stops.");
				}
			}
		}
	}
}
