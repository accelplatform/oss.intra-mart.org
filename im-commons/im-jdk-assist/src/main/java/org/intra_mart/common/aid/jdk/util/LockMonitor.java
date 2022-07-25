package org.intra_mart.common.aid.jdk.util;


/**
 * このクラスはモニター機能を提供します。<BR>
 * このクラスでは、複数個のモニターを管理することができます。
 * アプリケーションでは、entry() メソッドによりモニターを取得し、
 * release() メソッドでモニターを開放します。
 *
 */
public class LockMonitor{
	private int THREAD_LIMIT = 1;							// 最大並列処理数
	private volatile int THREAD_ACTIVE = 0;
	private volatile int totalCounter = 0;
	private Object standbyMonitor = new Object();
	private Object entryMonitor = new Object();
	private Object countMonitor = new Object();

	/**
	 * モニターを構築します。<P>
	 *
	 * @param max 管理するモニターの個数
	 */
	public LockMonitor(int max){
		this.THREAD_LIMIT = Math.max(max, THREAD_LIMIT);
	}

	/**
	 * モニターの取得。<P>
	 * モニターを取得できるまでブロックします。
	 * モニターの取得＝動作の権利を取得
	 */
	public void entry(){
		synchronized(this.countMonitor){
			this.totalCounter++;
		}
		try{
			synchronized(this.entryMonitor){
				// 空き待ち
				while(THREAD_LIMIT <= THREAD_ACTIVE){
					try{
						synchronized(this.standbyMonitor){
							this.standbyMonitor.wait(1000);
						}
					}
					catch(InterruptedException ie){
						continue;
					}
				}
				synchronized(this.countMonitor){
					// カウンタ変更処理を同期化
					this.THREAD_ACTIVE++;						// 確保
				}
			}
		}
		catch(RuntimeException re){
			synchronized(this.countMonitor){
				this.totalCounter--;
			}
			throw re;
		}
		catch(Error e){
			synchronized(this.countMonitor){
				this.totalCounter--;
			}
			throw e;
		}
	}

	/**
	 * このオブジェクトの管理するモニターの数を取得します。
	 * @return モニターの数
	 */
	public int size(){
		return THREAD_LIMIT;
	}

	/**
	 * 現在取得済みモニターの数を取得します。<P>
	 * @return 取得済みモニターの数
	 */
	public int activeCount(){
		return THREAD_ACTIVE;
	}

	/**
	 * モニターを利用中、または利用しようとしているスレッドの数です。
	 * @return entry() メソッドを実行して release() メソッドを実行していないスレッドの数
	 */
	public int total(){
		return this.totalCounter;
	}

	/**
	 * モニターを取得するための待ち行列に入っているスレッドの数です。
	 * @return entry() メソッドを実行して、ブロック状態になっているスレッドの数
	 */
	public int queue(){
		return Math.max(this.totalCounter - THREAD_ACTIVE, 0);
	}

	/**
	 * モニターの開放。<P>
	 */
	public void release(){
		try{
			synchronized(this.countMonitor){
				// カウンタ変更処理を同期化
				this.THREAD_ACTIVE--;								// 開放
			}
		}
		finally{
			try{
				synchronized(this.standbyMonitor){
					this.standbyMonitor.notifyAll();				// 通知
				}
			}
			finally{
				synchronized(this.countMonitor){
					this.totalCounter--;
				}
			}
		}
	}
}

/* END OF FILE */