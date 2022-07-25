package org.intra_mart.common.aid.jdk.java.lang;

import java.util.LinkedList;

import org.intra_mart.common.platform.log.Logger;

public class ExtendedThread extends Thread {
	private static Logger _logger = Logger.getLogger();
	
	/**
	 * スレッド実行するロジックを持ったオブジェクト
	 */
	private Runnable runner = null;
	/**
	 * スレッド停止リスナーを保管しておく領域
	 */
	private LinkedList listeners = new LinkedList();

	/**
	 * Constructor for ExtendedThread.
	 */
	public ExtendedThread() {
		super();
	}

	/**
	 * Constructor for ExtendedThread.
	 * @param target
	 */
	public ExtendedThread(Runnable target) {
		this();
		this.runner = target;
	}

	/**
	 * Constructor for ExtendedThread.
	 * @param group
	 * @param target
	 */
	public ExtendedThread(ThreadGroup group, Runnable target) {
		super(group, target);
		this.runner = target;
	}

	/**
	 * Constructor for ExtendedThread.
	 * @param name
	 */
	public ExtendedThread(String name) {
		super(name);
	}

	/**
	 * Constructor for ExtendedThread.
	 * @param group
	 * @param name
	 */
	public ExtendedThread(ThreadGroup group, String name) {
		super(group, name);
	}

	/**
	 * Constructor for ExtendedThread.
	 * @param target
	 * @param name
	 */
	public ExtendedThread(Runnable target, String name) {
		this(name);
		this.runner = target;
	}

	/**
	 * Constructor for ExtendedThread.
	 * @param group
	 * @param target
	 * @param name
	 */
	public ExtendedThread(ThreadGroup group, Runnable target, String name) {
		this(group, name);
		this.runner = target;
	}

	/**
	 * Constructor for ExtendedThread.
	 * @param group
	 * @param target
	 * @param name
	 * @param stackSize
	 */
//	public ExtendedThread(ThreadGroup group, Runnable target, String name, long stackSize) {
//		super(group, target, name, stackSize);
//		this.runner = target;
//	}

	/**
	 * このオブジェクトのスレッド実行ロジックです。
	 * このメソッドをサブクラスがオーバーライドすることはできません。
	 * サブクラスがスレッド実行ロジックを定義するためには、fireメソッドを
	 * オーバーライドしてください。
	 */
	public final void run(){
		try{
			this.fire();
		}
		catch(Throwable t){
			String className = this.getClass().getName();
			if(this.runner != null){
				className = this.runner.getClass().getName();
			}
			_logger.error("Thread runtime error[" + className + "]: " + t.getMessage(), t);
			
		}
		finally{
			LinkedList list = listeners;
			listeners = new LinkedList();
			try{
				while(! list.isEmpty()){
					ThreadStopListener tsl = (ThreadStopListener) list.removeFirst();
					try{
						tsl.handleThreadStop();
					}
					catch(Exception e){
						_logger.error("Thread stop listener error[" + tsl.getClass().getName() + "]: " + e.getMessage(), e);
					}
				}
			}
			catch(Throwable t){
				String className = this.getClass().getName();
				if(this.runner != null){
					className = this.runner.getClass().getName();
				}
				_logger.error("Thread stop event error[" + className + "]: " + t.getMessage(), t);
			}
		}
	}

	/**
	 * スレッド実行されるロジック。
	 * Thread クラスを継承したサブクラスにおいて、通常 run メソッドに
	 * 記述する内容をこのメソッドに定義してください。
	 */
	public void fire(){
		if(this.runner != null){ this.runner.run(); }
	}

	/**
	 * このスレッドが終了したときに呼び出されるイベントリスナーを登録します。
	 * @param listener スレッド終了イベントリスナー
	 */
	public void addThreadStopListener(ThreadStopListener listener){
		listeners.add(listener);
	}
}
