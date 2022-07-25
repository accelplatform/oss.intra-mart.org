package org.intra_mart.common.aid.jdk.java.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.intra_mart.common.aid.jdk.util.ErrorEvent;
import org.intra_mart.common.aid.jdk.util.ErrorEventListener;


/**
 * このクラスは、標準の出力ストリームを拡張した機能を提供します。<P>
 * このクラスの各メソッドは、データをストリームに書き出すときに、
 * java.io.DataOutputStream または java.io.ObjectOutputStream の
 * 適切なメソッドを使います。<BR>
 * <B>writeObject</B> メソッドを使ってオブジェクトをストリームに書き出す
 * 場合、ストリームに書き込めるのは java.io.Serializable インタフェースを
 * サポートするオブジェクトに限ります。それ以外のオブジェクトを
 * ストリームに書き込もうとした場合、例外がスローされます。<P>
 * この出力ストリームに書き込んだデータを入力する場合、
 * org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream を利用する必要があります。
 * 他の入力ストリームから読み込んだ場合例外がスローされることがあります。
 *
 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream
 */
public class ExtendedOutputStream extends OutputStream{
	// インスタンス変数
	private ErrorEventListener listener = null;
	private ByteArrayOutputStream RESERVOIR;
	private ObjectOutputStream oos;
	private OutputStream output;
	private boolean isAlive = true;

	/**
	 * 出力ストリームを構築します。<P>
	 *
	 * @param out 基礎出力ストリーム
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	public ExtendedOutputStream(OutputStream out) throws IOException{
		super();
		//出力バッファ作成
		output = out;
		RESERVOIR = new ByteArrayOutputStream();
	}

	/**
	 * 出力ストリームを構築します。<P>
	 *
	 * @param out 基礎出力ストリーム
	 * @param listener エラーイベントのリスナーオブジェクト
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	public ExtendedOutputStream(OutputStream out, ErrorEventListener listener) throws IOException{
		this(out);
		this.listener = listener;
	}

	/**
	 * 指定されたバイトを書き込みます。<BR>
	 *
	 * @param data バイトデータ
	 * @exception IOException 入出力エラーが発生した場合
	 * @see java.io.OutputStream#write(int)
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream#read()
	 */
	public void write(int data) throws IOException{
		synchronized(this){
			RESERVOIR.write(data);
		}
	}

	/**
	 * オフセット off から始まる指定のバイト配列から
	 * この出力ストリームに len バイトを書き込みます。<BR>
	 *
	 * @param b データ
	 * @param off データの開始オフセット
	 * @param len 書き込むバイト数
	 * @exception IOException 入出力エラーが発生した場合
	 * @see java.io.OutputStream#write(byte[],int,int)
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream#read()
	 */
	public void write(byte[] b, int off, int len) throws IOException{
		synchronized(this){
			RESERVOIR.write(b, off, len);
		}
	}

	/**
	 * int 型データの送信。<BR>
	 * 基本データ型の整数データを基礎出力ストリームに書き込みます。
	 *
	 * @param data データ
	 * @exception IOException 入出力エラーが発生した場合
	 * @see java.io.DataOutputStream#writeInt(int)
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream#readInt()
	 */
	public void writeInt(int data) throws IOException{
		try{
			synchronized(this){
				this.getObjectOutputStream().writeInt(data);
			}
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * long 型データの送信。<BR>
	 * 基本データ型の長整数データを基礎出力ストリームに書き込みます。
	 *
	 * @param data データ
	 * @exception IOException 入出力エラーが発生した場合
	 * @see java.io.DataOutputStream#writeLong(long)
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream#readLong()
	 */
	public void writeLong(long data) throws IOException{
		try{
			synchronized(this){
				this.getObjectOutputStream().writeLong(data);
			}
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * boolean 型データの送信。<BR>
	 * 基本データ型の真偽値データを基礎出力ストリームに書き込みます。
	 *
	 * @param data データ
	 * @exception IOException 入出力エラーが発生した場合
	 * @see java.io.DataOutputStream#writeBoolean(boolean)
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream#readBoolean()
	 */
	public void writeBoolean(boolean data) throws IOException{
		try{
			synchronized(this){
				this.getObjectOutputStream().writeBoolean(data);
			}
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * オブジェクトの送信。<P>
	 * java.io.Serializable インタフェースを実装している
	 * オブジェクトを基礎出力ストリームに書き込みます。<BR>
	 * このメソッドはオブジェクトをストリームに書き込む際に、
	 * java.io.ObjectOutputStream の writeObject メソッドを使います。
	 *
	 * @param data オブジェクト
	 * @exception InvalidClassException 直列化で使用されるクラスになんらかの不具合があった場合
	 * @exception NotSerializableException 直列化の対象が java.io.Serializable インタフェースを実装していない場合
	 * @exception IOException 基礎の OutputStream に例外が発生した場合
	 * @see java.io.ObjectOutputStream#writeObject(Object)
	 * @see java.io.Serializable
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream#readObject()
	 */
	public void writeObject(Object data) throws IOException{
		try{
			synchronized(this){
				this.getObjectOutputStream().writeObject(data);
			}
		}
		catch(InvalidClassException ice){
			if(listener != null){
				listener.handleError(new ErrorEvent(ice, this));
			}
			throw ice;
		}
		catch(NotSerializableException nse){
			if(listener != null){
				listener.handleError(new ErrorEvent(nse, this));
			}
			throw nse;
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * 文字列の送信。<BR>
	 * このメソッドは、writeObject() メソッドを使い文字列をストリームに
	 * 書き込みます。
	 * java.io.DataOutputStream や java.io.ObjectOutputStream の
	 * writeUTF メソッドとの違いは、ストリームに書き込める文字列の
	 * 長さに制約がないことです。
	 *
	 * @param data ストリームに書き込む文字列
	 * @exception InvalidClassException 直列化で使用されるクラスになんらかの不具合があった場合
	 * @exception NotSerializableException 直列化の対象が java.io.Serializable インタフェースを実装していない場合
	 * @exception IOException 基礎の OutputStream に例外が発生した場合
	 * @see java.io.DataOutputStream#writeUTF(String)
	 * @see java.io.ObjectOutputStream#writeUTF(String)
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedInputStream#readString()
	 */
	public void writeString(String data) throws IOException{
		writeObject(data.toCharArray());
	}

	/**
	 * オブジェクト出力ストリームを取得
	 */
	private ObjectOutputStream getObjectOutputStream() throws IOException{
		if(oos != null){ oos.reset(); }					// 再利用
		else{ oos = new ObjectOutputStream(RESERVOIR); }	// 新生
		return oos;
	}

	/**
	 * 書き出しストリーム内のデータをすべて出力。<BR>
	 *
	 * @exception IOException 入出力エラーが発生した場合
	 */
	public void flush() throws IOException{
		try{
			synchronized(this){
				if(oos != null){ oos.flush(); }
				if(RESERVOIR.size() > 0){
					RESERVOIR.writeTo(output);	// 送信
					RESERVOIR.reset();			// 再利用処理
					output.flush();				// 基礎ストリームのフラッシュ
				}
			}
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * 入出力ストリームおよびソケットを閉じます。<BR>
	 * 入出力ストリームを閉じる前にバッファに溜められているデータをすべて
	 * 出力ストリームに書き出します。<BR>
	 *
	 * @exception IOException 入出力エラーが発生した場合
	 */
	public void close() throws IOException{
		try{
			try{
				flush();
			}
			finally{
				isAlive = false;
				output.close();
			}
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * この入出力ストリームが閉じられているかチェックします。<BR>
	 *
	 * @return すでに閉じられている場合 true、そうでない場合 false。
	 * @see #close()
	 */
	public boolean isClosed(){
		return ! isAlive;
	}
}


/* End of File */