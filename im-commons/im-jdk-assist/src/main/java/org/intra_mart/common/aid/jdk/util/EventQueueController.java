package org.intra_mart.common.aid.jdk.util;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.intra_mart.common.platform.log.Logger;

/**
 * このクラスは、イベントリスナーを実行するイベント機能を実装します。<p>
 * イベントコントローラは、指定された時間間隔に従ってリスナー登録状況を確認し、
 * リスナーの登録が１つ以上あった場合、これらを専用スレッドで実行します。
 * このとき、イベントリスナーの実行順と実行の直列化が保証されます。<br>
 * これらの機能により、通常の関数実行や java.lang.Thread クラスを利用した
 * 並列処理などと異なった機能を実装することができます。<p>
 * このクラスを利用することにより、現在の処理とイベント処理を並列化することが
 * できます。また、一つのコントローラは同時に一つのイベントリスナーしか
 * 実行しないため、それぞれのリスナーの処理が並列処理されることはありません。
 * 従って、リスナーはマルチスレッドを意識したプログラムにする必要がありますが、
 * リスナー間でマルチスレッドを意識する必要はありません。
 *
 * @see org.intra_mart.common.aid.jdk.util.EventListener
 */
public class EventQueueController{
	
	private static Logger _logger = Logger.getLogger();
	
	private int MAX_POOL_SIZE = Integer.MAX_VALUE;			// プール限界値
	private EventQueueControllerAgent POOL;					// エントリ格納庫

	/**
	 * イベントリスナーを管理・実行するためのコントローラ・オブジェクトを
	 * 構築します。
	 * コントローラがリスナー登録状況を監視する時間間隔はデフォルト値(60秒)が
	 * 適用されます。
	 */
	public EventQueueController(){
		this(60000);			// 標準は１分間隔でエントリーの確認処理
	}
	/**
	 * イベントリスナーを管理・実行するためのコントローラ・オブジェクトを
	 * 構築します。
	 * millis は、リスナーの登録状況を監視する時間間隔です。
	 * コントローラは、リスナーが一つも登録されていない状態になると、
	 * millis ミリ秒間隔でリスナー登録の有無を確認します。
	 *
	 * @param millis イベントの監視間隔(ミリ秒)
	 * @throws IllegalArgumentException millis が 0 以下の場合
	 */
	public EventQueueController(long millis) throws IllegalArgumentException{
		super();
		if(millis > 0){
			this.POOL = new EventQueueControllerAgent(millis);
		}
		else{
			throw new IllegalArgumentException("The value besides the range was specified");
		}
	}

	/**
	 * キューの容量を変更します。
	 * @param max イベントをプールできる最大量
	 * @throws IllegalStateException max 個以上のリスナーが登録されている場合
	 * @throws IllegalArgumentException max が 0 以下の場合
	 */
	public synchronized void setCapacity(int max) throws IllegalStateException, IllegalArgumentException{
		if(max > 0){
			if(this.POOL.size() <= max){
				this.MAX_POOL_SIZE = max;
			}
			else{
				throw new IllegalStateException("A value cannot be changed");
			}
		}
		else{
			throw new IllegalArgumentException("The value besides the range was specified");
		}
	}

	/**
	 * イベントリスナーの登録。<br>
	 * イベントリスナーは、一旦キューに溜められます。
	 * キューに保管されたリスナーは、
	 * このコントローラにより現在実行中のスレッドとは
	 * 別のスレッドで登録順に呼び出されます。<p>
	 * 実行されたリスナーは、処理が終了すると破棄されます。<p>
	 * 通常のスレッドクラスを利用した並列処理との違いは、
	 * イベントリスナーが登録順に実行される点と、
	 * イベントリスナーの実行が直列化される点にあります。
	 *
	 * @param listener イベントリスナー
	 * @throws NullPointerException listener が null の場合
	 * @throws IndexOutOfBoundsException キューが飽和状態の場合
	 * @throws IllegalStateException コントローラが破棄処理済の場合
	 */
	public synchronized void entry(EventListener listener) throws NullPointerException, IndexOutOfBoundsException, IllegalStateException{
		if(listener != null){
			if(this.POOL.size() <= this.MAX_POOL_SIZE){
				this.POOL.entry(listener);
			}
			else{
				throw new IndexOutOfBoundsException("It cannot register with cue any more");
			}
		}
		else{
			throw new NullPointerException("listener is null");
		}
	}

	/**
	 * キューに溜まっているすべてのイベントリスナーを実行します。<p>
	 * このメソッドは、現在実行中のスレッドで
	 * すべてのイベント処理を行います。
	 */
	public void flush(){
		this.POOL.flush();
	}

	/**
	 * キューに登録されているイベントリスナーを一つ実行します。
	 * 実行されるリスナーは、現在キューに保管されているリスナーの中で
	 * 最も過去に登録されたリスナーです。
	 * このメソッドは、現在実行中のスレッドで
	 * 登録順で最も過去のイベントを１つ実行します。
	 * @throws NoSuchElementException 登録リスナーがない場合
	 */
	public void eventGenerating() throws NoSuchElementException{
		this.POOL.eventGenerating();						// イベントの実行
	}

	/**
	 * このオブジェクトがガーベージコレクションによって破棄される時に、
	 * ガーベージコレクタによって呼び出されます。<br>
	 * このオブジェクトの全てのリソースを破棄します。
	 */
	protected void finalize() throws Throwable{
		this.flush();
	}



	/**
	 * イベントを実行するための専用スレッド
	 */
	private static class EventQueueControllerAgent extends LinkedList implements Runnable{
		private long sleepingTime;

		/**
		 * イベントリスナーを実行するための専用スレッドを構築します。
		 * このオブジェクトにエントリされたイベントリスナーを実行します。
		 * @param millis エントリがない時の休眠時間(監視間隔ともいう)
		 */
		public EventQueueControllerAgent(long millis){
			this.sleepingTime = millis;					// 休眠時間
		}

		/**
		 * スレッド処理の開始。
		 */
		private void start(){
			Thread t = new Thread(this);
			t.setDaemon(true);							// デーモンスレッド
			try{
				t.setPriority(Thread.MIN_PRIORITY);		// 優先度の設定
			}
			catch(IllegalArgumentException iae){
				// 有り得ない
			}
			t.start();									// 実行
			Thread.yield();
		}

		/**
		 * イベントリスナーの登録。<br>
		 * イベントリスナーは、一旦キューに溜められます。
		 * キューに保管されたリスナーは、
		 * このコントローラにより現在実行中のスレッドとは
		 * 別のスレッドで登録順に呼び出されます。<p>
		 * 実行されたリスナーは、処理が終了すると破棄されます。<p>
		 * 通常のスレッドクラスを利用した並列処理との違いは、
		 * イベントリスナーが登録順に実行される点と、
		 * イベントリスナーの実行が直列化される点にあります。
		 *
		 * @param listener イベントリスナー
		 * @throws IllegalStateException コントローラが破棄処理済の場合
		 */
		public synchronized void entry(EventListener listener) throws IllegalStateException{
			// キューによる処理
			if(this.isEmpty()){
				this.add(listener);
				this.start();
			}
			else{
				this.add(listener);
			}
		}

		/**
		 * キューに登録されているイベントリスナーを一つ実行します。
		 * 実行されるリスナーは、現在キューに保管されているリスナーの中で
		 * 最も過去に登録されたリスナーです。
		 * このメソッドは、現在実行中のスレッドで
		 * 登録順で最も過去のイベントを１つ実行します。
		 * @throws NoSuchElementException 登録リスナーがない場合
		 */
		public synchronized void eventGenerating() throws NoSuchElementException{
			// イベントの実行
			((EventListener) this.removeFirst()).handleEvent();
		}

		/**
		 * キューに溜まっているすべてのイベントリスナーを実行します。<p>
		 * このメソッドは、現在実行中のスレッドで
		 * すべてのイベント処理を行います。
		 */
		public void flush(){
			while(! this.isEmpty()){
				try{
					this.eventGenerating();
				}
				catch(NoSuchElementException nsee){
					break;		// もう処理すべきリスナーがないので終了
				}
			}
		}

		/**
		 * スレッド実行のロジック。
		 */
		public void run(){
			try{
				// 最初は仮眠だっ！うたた寝だっ！
				try{
					// 監視を休息
					if(sleepingTime > 0){
						Thread.sleep(sleepingTime);
					}
					else{
						Thread.yield();
					}
				}
				catch(InterruptedException ie){
					// 有り得ないと思われるが、一度休眠してみるか
					Thread.yield();
				}

				while(! this.isEmpty()){
					try{
						// イベントの実行
						this.eventGenerating();
						Thread.yield();						// 休息タイム
					}
					catch(NoSuchElementException nsee){
						// キューが空らしいので実行ループ終了
						break;
					}
				}
			}
			catch(Throwable t){
				// 予期せぬエラー -> 新しい監視スレッドを準備＆開始
				_logger.error("Event-quere runtime error: " + t.getMessage(), t);
				if(! this.isEmpty()){ this.start(); }
			}
		}
	}
}
