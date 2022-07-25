package org.intra_mart.common.aid.jdk.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class UniqueIdGeneratorTest extends TestCase {

	private static Set<String> SET_for_ID = Collections.synchronizedSet(new HashSet<String>(500 * 1000));
	private static Collection<String> COL_for_executingThread = new Vector<String>();
	private static Collection<Throwable> COL_for_ERROR = new Vector<Throwable>();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		COL_for_executingThread = new Vector<String>();
		COL_for_ERROR = new Vector<Throwable>();
	}
	
	/**
	 * @throws Exception
	 */
	public void testGetUniqueId_スレッド数1_ループ数10000の場合() throws Exception{

		int maxThread = 1;
		int loopCount = 10000;
		
		testExecutor(maxThread, loopCount);
	}

	/**
	 * @throws Exception
	 */
	public void testGetUniqueId_スレッド数500_ループ数1000の場合() throws Exception{

		int maxThread = 500;
		int loopCount = 1000;
		
		testExecutor(maxThread, loopCount);
	}

	//======================================================================
	
	
	private void testExecutor(int maxThread, int loopCount) throws Exception {
		
		for(int threadCount = 0 ; threadCount < maxThread; threadCount++){
			
			Thread t =new UniqueIdGenThread(loopCount);
			t.start();
			
			COL_for_executingThread.add(t.getName());
		}
		
		// スレッド達が終了するまで待つ。
		while(!COL_for_executingThread.isEmpty()){
			zzzz(1000);
		}
		
		if(!COL_for_ERROR.isEmpty()){
			Iterator<Throwable> it = COL_for_ERROR.iterator();
			while(it.hasNext()){
				it.next().printStackTrace();
			}
			
			fail("失敗");
		}
	}
	
	private static void zzzz(long sleepMillis){
		try {
			Thread.sleep(sleepMillis);
		}
		catch (InterruptedException e) {
			// 無視
		}
	}
	
	
	public static class UniqueIdGenThread extends Thread {
		private int loopCount;
		
		public UniqueIdGenThread(int loopCount){
			this.loopCount = loopCount;
		}
		
		public void run() {
			int idx = 0;
			
			try{
				for(idx = 0; idx < loopCount; idx++){
					// ID生成
					String id = UniqueIdGenerator.getUniqueId();
					
					String errMsg = " (" + Thread.currentThread().getName() + " - " + idx + "回目のID生成時に失敗しました。 loopCount=" + loopCount + ", id=" + id + ")";
					
					if(id.length() != 13){
						throw new AssertionFailedError("13桁ではありません。→" + errMsg);
					}
					
					if(!SET_for_ID.add(id)){
						throw new AssertionFailedError("重複したIDが生成されました。→" + errMsg);
					}
					
					if(idx % 100 == 0){
						// デバッグ
						System.out.println(Thread.currentThread().getName() + " - [" + idx + "]: " + id);
					}
				}
				
				if(idx != loopCount){
					String errMsg = " (" + Thread.currentThread().getName() + " - " + idx + "回目のID生成時に失敗しました。 loopCount=" + loopCount + ")";
					throw new AssertionFailedError("ループの途中で失敗しました。→" + errMsg);
				}
				
			}
			catch (Throwable e) {
				COL_for_ERROR.add(e);
			}
			finally{
				System.out.println(Thread.currentThread().getName() + " - ************ Loop Count: " + idx + " ************");
				COL_for_executingThread.remove(this.getName());
			}
		}
	}
}
