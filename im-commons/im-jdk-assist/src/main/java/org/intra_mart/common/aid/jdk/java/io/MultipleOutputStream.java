package org.intra_mart.common.aid.jdk.java.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 複数の出力ストリームにバイトを書き出すためのフィルタ実装です。<p>
 * このフィルタを利用する事によって、
 * 同じバイト情報を全く異なる複数の出力先へ、同時に書き出すことができます。
 *
 */
public class MultipleOutputStream extends OutputStream{
	private Collection streams = new HashSet();

	/**
	 * 唯一のコンストラクタ
	 */
	public MultipleOutputStream(){
		super();
	}

	/**
	 * メッセージの出力先を追加します。
	 * @param output 出力ストリーム
	 */
	public synchronized void addOutputStream(OutputStream output){
		if(this.streams != null){ this.streams.add(output); }
	}

	/**
	 * メッセージの出力先を削除します。
	 * @param output 出力ストリーム
	 * @return 削除に成功した場合は true。
	 */
	public synchronized boolean removeOutputStream(OutputStream output){
		if(this.streams != null){
			return this.streams.remove(output);
		}
		else{
			return false;
		}
	}

	/**
	 * 指定のバイトを
	 * 設定されている各出力先へ書き出します。
	 * @param b バイト
	 * @throws IOException 入出力エラー
	 */
	public synchronized void write(int b) throws IOException{
		ensureOpen();		// 準備ＯＫ？
		Iterator iterator = this.streams.iterator();
		while(iterator.hasNext()){
			((OutputStream) iterator.next()).write(b);
		}
	}

	/**
	 * 指定のバイトを
	 * 設定されている各出力先へ書き出します。
	 * @param b バイト
	 * @throws IOException 入出力エラー
	 */
	public synchronized void write(byte[] b, int off, int len) throws IOException{
		ensureOpen();		// 準備ＯＫ？
		Iterator iterator = this.streams.iterator();
		while(iterator.hasNext()){
			((OutputStream) iterator.next()).write(b, off, len);
		}
	}

	/**
	 * 指定のバイトを
	 * 設定されている各出力先へ書き出します。
	 * @param b バイト
	 * @throws IOException 入出力エラー
	 */
	public synchronized void write(byte[] b) throws IOException{
		ensureOpen();		// 準備ＯＫ？
		Iterator iterator = this.streams.iterator();
		while(iterator.hasNext()){
			((OutputStream) iterator.next()).write(b);
		}
	}

	/**
	 * この出力ストリームをフラッシュします。<p>
	 * まず、この出力ストリーム内のデータをフラッシュします。
	 * その後、設定されている各出力先の {@link java.io.OutputStream#flush()} メソッドを
	 * 順に呼び出します。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void flush() throws IOException{
		ensureOpen();		// 準備ＯＫ？
		Iterator iterator = this.streams.iterator();
		while(iterator.hasNext()){
			((OutputStream) iterator.next()).flush();
		}
	}

	/**
	 * このストリームを閉じます。<p>
	 * このメソッドは、自分をクローズ処理すると同時に基礎ストリームも閉じます。
	 * 設定されている各出力先の {@link java.io.OutputStream#close()} メソッドを
	 * 順に呼び出します。
	 * @throws IOException 入出力エラー
	 */
	public synchronized void close() throws IOException{
		if(this.streams != null){
			try{
				Iterator iterator = this.streams.iterator();
				while(iterator.hasNext()){
					((OutputStream) iterator.next()).close();
				}
			}
			finally{
				this.streams = null;
			}
		}
	}

	/**
	 * このオブジェクトをクリーンアップします。
	 * このオブジェクトへの参照はもうないとガベージコレクションによって
	 * 判断されたときに、ガベージコレクタによって呼び出されます。
	 */
	protected void finalize() throws Throwable{
		if(this.streams != null){
			try{
				this.flush();
			}
			finally{
				this.streams = null;
			}
		}
	}

	// 基礎ストリームのチェック
	private void ensureOpen() throws IOException {
		if(this.streams == null){ throw new IOException("Stream closed"); }
	}
}

