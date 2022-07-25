package org.intra_mart.jssp.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * プロセスからの出力を受け取る入力受付専用クラス(MultiThread 対応)<br>
 * 
 *  コンストラクタで受け付けた入力ストリームからデータを取得。<br>
 *  スレッドとして独立して動作するため、他のスレッドとの並列処理が可能。<br>
 *  この処理はデーモン・スレッドとして動作するので、万一無限ループになった
 *  場合においても、システム全体をフリーズさせることはない。
 */
public class BufferedInputStreamThread extends Thread {

	private BufferedInputStream bis;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private boolean error = false;
	private String message = null;


	/**
	 * 指定の入力ストリームからデータを取得するためのスレッド<br>
	 * このスレッドはデーモン・スレッドとして動作
	 * @param is 入力ストリーム
	 */
	public BufferedInputStreamThread(InputStream is){
		this.setDaemon(true);						// デーモン化
		this.bis = new BufferedInputStream(is);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		byte[] buf = new byte[256];
		int max;

		try{
			while(true){
				max = bis.read(buf, 0, 256);
				if(max != -1){
					write(buf, 0, max);
				}
				else{
					break;
				}
			}
		}
		catch(Throwable t){
			System.out.println(t.getMessage());
			this.error = true;
			this.message = t.getMessage();
		}
	}


	/**
	 * スレッド処理中にエラーが発生したかどうかを判定します。
	 * 
	 * @return スレッド処理中にIOException または InterruptedException など例外が発生した場合に true、<br>
	 * 			それ以外の場合に false を返却します。
	 */
	public boolean isError(){
		return this.error;
	}


	/**
	 * エラーメッセージを返却します。
	 * 
	 * @return エラー内容のメッセージ文字列<br>
	 * 			エラーが発生していない状態で呼び出した場合は、null を返却します。
	 */
	public String getMessage(){
		return this.message;
	}


	/**
	 * このインスタンスで確保した入出力ストリームを閉じて、
	 * 関連するすべてのシステムリソースを開放します。<br>
	 * ストリームを閉じると、このインスタンスで入出力処理を
	 * 実行できず、もう一度開く事もできません。
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException{
		try{
			bis.close(); 
		}
		finally{
			baos.close(); 
		}
	}


	/**
	 * バッファに対して、データを書き出します。
	 * @param stream 挿入データ
	 */
	public void write(byte[] stream){
		write(stream, 0, stream.length);
	}

	
	/**
	 * バッファに対して、データを書き出します。
	 * 
	 * @param stream 挿入データ
	 * @param off データの開始オフセット
	 * @param len 書き込むバイト数
	 */
	public synchronized void write(byte[] stream, int off, int len){
		baos.write(stream, off, len);
	}


	/**
	 * 指定の InputStream から取得された文字列データを
	 * 指定の文字エンコーディングに従ってバイトを文字に変換しながら、
	 * バッファを文字列に変換します。
	 * 
	 * @param enctype 文字エンコーディング
	 * @return 指定の InputStream から取得された文字列データ
	 * @throws UnsupportedEncodingException 文字列変換失敗時
	 */
	public String getString(String enctype) throws UnsupportedEncodingException{
		return baos.toString(enctype);
	}


	/**
	 * 指定の InputStream から取得された文字列データを
	 * プラットフォームのデフォルトの文字エンコーディングに従ってバイトを文字に変換しながら、
	 * バッファを文字列に変換します。
	 * 
	 * @return 指定の InputStream から取得された文字列データ
	 */
	public String getString(){
		return baos.toString();
	}
	

	/**
	 * 指定の InputStream から取得された文字列データをメモリを割り当てて
	 * バイト配列を新しく作成します。
	 * 配列のサイズは現在の出力ストリームと同じで、バッファの中の有効データはここにコピーされます。
	 * 
	 * @return 指定の InputStream から取得されたバイトデータ
	 */
	public byte[] toByteArray(){
		return baos.toByteArray();
	}


	/**
	 * バッファ領域取得メソッド<br>
	 * 
	 * これまでに蓄積されたデータを持つバッファ領域を返却。<br>
	 * このメソッド呼び出しの後に指定の InputStream から入力した
	 * データは新規に作成されたバッファに格納されます。
	 * 
	 * @return バッファ領域(バイト配列)
	 */
	public byte[] reset(){
		
		ByteArrayOutputStream current = baos;
		baos = new ByteArrayOutputStream();
		byte[] buf = current.toByteArray();
		
		try{
			current.close();
		}
		catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
		return buf;
	}
}