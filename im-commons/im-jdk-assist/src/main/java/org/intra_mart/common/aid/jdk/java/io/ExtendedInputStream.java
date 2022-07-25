package org.intra_mart.common.aid.jdk.java.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;

import org.intra_mart.common.aid.jdk.util.ErrorEvent;
import org.intra_mart.common.aid.jdk.util.ErrorEventListener;


/**
 * このクラスは、標準の入力ストリームを拡張した機能を提供します。<P>
 * このクラスは、org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream により
 * 書き出されたストリームを読み込むための実装を提供します。<BR>
 * このクラスの各メソッドは、データをストリームから読み込むときに、
 * java.io.DataInputStream または java.io.ObjectInputStream の
 * 適切なメソッドを使います。<P>
 * データをストリームに書き出したときに使ったメソッドに対応した
 * 適切なメソッドを利用することにより、データを読み込むことができます。<P>
 * このクラスを org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream と対で使うことにより、
 * データをファイルに保存したり、ネットワークにより他のプロセスへ
 * データを転送することができます。<P>
 * org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream の writeObject メソッドにより
 * 書き出されたオブジェクトは、readObject メソッドにより
 * インスタンス化することができます。
 * この時、インスタンスに必要なクラスが存在している必要があります。
 *
 */
public class ExtendedInputStream extends InputStream{
	// インスタンス変数
	private ErrorEventListener listener = null;
	private BufferedInputStream FOUNTAIN;
	private ObjectInputStream ois;
	private InputStream input;
	private boolean isAlive = true;

	/**
	 * 入力ストリームを構築します。<P>
	 *
	 * @param in 基礎入力ストリーム
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	public ExtendedInputStream(InputStream in) throws IOException{
		super();
		//入力バッファ作成
		input = in;
		FOUNTAIN = new BufferedInputStream(input);
	}

	/**
	 * 入力ストリームを構築します。<P>
	 *
	 * @param in 基礎入力ストリーム
	 * @param listener エラーイベントのリスナーオブジェクト
	 * @exception IOException 入出力ストリームの作成時エラー
	 */
	public ExtendedInputStream(InputStream in, ErrorEventListener listener) throws IOException{
		this(in);
		this.listener = listener;
	}

	/**
	 * データ取得。<P>
	 * 入力ストリームから次のバイトのデータを取得します。<BR>
	 *
	 * @exception IOException 入出力エラーが発生した場合
	 * @return 取得したバイトデータ
	 * @see java.io.InputStream#read()
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream#write(int)
	 */
	public int read() throws IOException{
		try{
			return FOUNTAIN.read();
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * データ取得。<P>
	 * 入力ストリームから len バイトまでのデータをバイト配列に
	 * 読み込みます。<BR>
	 *
	 * @param b バイト配列
	 * @param off データの開始オフセット
	 * @param len 読み込むバイト数
	 * @exception IOException 入出力エラーが発生した場合
	 * @return 取得したバイト数
	 * @see java.io.InputStream#read(byte[], int, int)
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream#write(int)
	 */
	public int read(byte[] b, int off, int len) throws IOException{
		try{
			return FOUNTAIN.read(b, off, len);
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * int 型データ取得。<P>
	 * 基本データ型の整数データを読み込みます。
	 *
	 * @exception EOFException データ受信完了前にストリームの終わりに達した場合
	 * @exception IOException 入出力エラーが発生した場合
	 * @return int 型数値データ
	 * @see java.io.DataInputStream#readInt()
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream#writeInt(int)
	 */
	public int readInt() throws IOException{
		try{
			// Open the ObjectInputStream
			if(ois == null){ ois = new ObjectInputStream(FOUNTAIN); }
			return ois.readInt();
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * long 型データ取得。<P>
	 * 基本データ型の長整数データを読み込みます。
	 *
	 * @exception EOFException データ受信完了前にストリームの終わりに達した場合
	 * @exception IOException 入出力エラーが発生した場合
	 * @return long 型数値データ
	 * @see java.io.DataInputStream#readLong()
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream#writeLong(long)
	 */
	public long readLong() throws IOException{
		try{
			// Open the ObjectInputStream
			if(ois == null){ ois = new ObjectInputStream(FOUNTAIN); }
			return ois.readLong();
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * boolean 型データ取得。<P>
	 * 基本データ型の真偽値データを読み込みます。
	 *
	 * @exception EOFException データ受信完了前にストリームの終わりに達した場合
	 * @exception IOException 入出力エラーが発生した場合
	 * @return long 型数値データ
	 * @see java.io.DataInputStream#readBoolean()
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream#writeBoolean(boolean)
	 */
	public boolean readBoolean() throws IOException{
		try{
			// Open the ObjectInputStream
			if(ois == null){ ois = new ObjectInputStream(FOUNTAIN); }
			return ois.readBoolean();
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * Object の取得。<P>
	 *
	 * @exception ClassNotFoundException 直列化されたオブジェクトのクラスが見つからなかった場合
	 * @exception InvalidClassException 直列化で使用されるクラスになんらかの不具合があった場合
	 * @exception StreamCorruptedException ストリームの制御情報に一貫性がない場合
	 * @exception OptionalDataException プリミティブデータが、オブジェクトではなくストリームに見つかった場合
	 * @exception IOException 通常の入出力関連の例外
	 * @return 入力して復元したオブジェクト
	 * @see java.io.ObjectInputStream#readObject()
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream#writeObject(Object)
	 */
	public Object readObject() throws OptionalDataException, ClassNotFoundException, IOException{
		try{
			// Open the ObjectInputStream
			if(ois == null){ ois = new ObjectInputStream(FOUNTAIN); }
			return ois.readObject();
		}
		catch(ClassNotFoundException cnfe){
			if(listener != null){
				listener.handleError(new ErrorEvent(cnfe, this));
			}
			throw cnfe;
		}
		catch(IOException ioe){
			if(listener != null){
				listener.handleError(new ErrorEvent(ioe, this));
			}
			throw ioe;
		}
	}

	/**
	 * 文字列の取得。<P>
	 * このメソッドは、org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream クラスの
	 * writeString メソッドを使って書き込んだデータを読み込むための
	 * メソッドです。他の方法で書き込んだデータを読み込むことはできません。
	 *
	 * @exception ClassNotFoundException 直列化されたオブジェクトのクラスが見つからなかった場合
	 * @exception InvalidClassException 直列化で使用されるクラスになんらかの不具合があった場合
	 * @exception StreamCorruptedException ストリームの制御情報に一貫性がない場合
	 * @exception OptionalDataException プリミティブデータが、オブジェクトではなくストリームに見つかった場合
	 * @exception IOException 通常の入出力関連の例外
	 * @return 入力した文字列
	 * @see org.intra_mart.common.aid.jdk.java.io.ExtendedOutputStream#writeString(String)
	 */
	public String readString() throws OptionalDataException, ClassNotFoundException, IOException{
		return new String((char[]) readObject());
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
			isAlive = false;
			input.close();
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