package org.intra_mart.common.aid.jdk.java.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * このクラスは、BASE64 デコードされたデータを書き込むための
 * ストリームフィルタを実装します。<BR>
 * この出力ストリームに書き込まれたバイトは、BASE64 デコードされ
 * 基礎出力ストリームに書き込まれます。<BR>
 *
 * @see java.io.OutputStream
 */
public class BASE64DecodeOutputStream extends FilterOutputStream{
	// クラス変数
	private static final String B_ELEMENTS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	// インスタンス変数
	private int strm = 0;
	private int surplus = 0;
	private boolean ready = true;
	private boolean decoding = true;

/*
	public static void main(String[] args){
		try{
			String[] testData = {"MS-DOS","Card","Model"};

			for(int idx = 0; idx < testData.length; idx++){
				String encodedText = bEncode(testData[idx]);
				System.out.println("B-encode: " + testData[idx] + " -> " + encodedText + " -> " + bDecode(encodedText) + " ///");
			}

			System.out.println("Conguraturations !!!");
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}

	private static String bEncode(String asciiText) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BASE64EncodeOutputStream bFilter = new BASE64EncodeOutputStream(baos);
		OutputStreamWriter out = new OutputStreamWriter(bFilter);

		out.write(asciiText);

		out.flush();
		out.close();
		return baos.toString();
	}

	private static String bDecode(String bText) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BASE64DecodeOutputStream bFilter = new BASE64DecodeOutputStream(baos);
		OutputStreamWriter out = new OutputStreamWriter(bFilter);

		out.write(bText);

		out.flush();
		out.close();
		return baos.toString();
	}
*/

	/**
	 * 指定された OutputStream に書き込む BASE64デコーダーを作成します。<P>
	 *
	 * @param out 出力ストリーム
	 */
	public BASE64DecodeOutputStream(OutputStream out){
		super(out);
	}

	/**
	 * バイトをデコードして出力ストリームに書き込みます。<P>
	 *
	 * このメソッドは、デコード結果が出力可能な 1 バイトのデータになるまで、
	 * バッファに溜め込みます。
	 * バッファに溜め込まれたデータは、flush メソッドや close メソッドを
	 * 実行しても基礎出力ストリームに書き出されることはありません。
	 * バッファにデータが溜まった状態で、flush メソッドや close メソッドを
	 * 実行した場合、バッファ内のデータは破棄されます。
	 *
	 * @param b 書き込まれるバイト
	 * @exception ArithmeticException デコード中に計算結果がオーバーフローしてしまった場合
	 * @exception IllegalArgumentException バイト b が ASCII 文字の範囲に含まれない場合（ASCII のコントロールコードの場合も含む）や不正なコードが指定された場合
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	public synchronized void write(int b) throws IOException{
		writeByte(b);
	}

	/**
	 * 指定のバイト b をデコードしたバイトを基礎出力ストリームに書き込みます。
	 * @param b 書き込まれるバイト
	 * @exception ArithmeticException デコード中に計算結果がオーバーフローしてしまった場合
	 * @exception IllegalArgumentException バイト b が ASCII 文字の範囲に含まれない場合（ASCII のコントロールコードの場合も含む）や不正なコードが指定された場合
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	private void writeByte(int b) throws ArithmeticException, IllegalArgumentException, IOException{
		if(ready){
			if(b >= 0x0020 && b < 0x007f){
				if(decoding){
					// 通常のデコード処理
					if(b == (int) '='){
						switch(surplus){
							case 2:								// 終了
								ready = false;
							case 4:								// 終了処理へ
								decoding = false;
								break;
							default:							// エラー
								ready = false;
								throw new IllegalArgumentException("An inaccurate character: " + b + "/" + String.valueOf((char) b));
						}
						if(surplus == 4){ decoding = false; }	// 終了処理へ
						else{ ready = false; }					// 終了
					}
					else{
						strm <<= 6;
						surplus += 6;
						strm |= B_ELEMENTS.indexOf(b);
						if(strm >= 0){
							if(surplus >= 8){
								surplus -= 8;
								int result = strm >> surplus;
								strm ^= result << surplus;
								this.out.write(result);
							}
						}
						else{
							throw new ArithmeticException("The decoding result overflowed: " + b + "/" + Character.toString((char) b));
						}
					}
				}
				else{
					// 終端文字(=)を処理するための終了処理
					ready = false;
					if(b != (int) '='){
						throw new IllegalArgumentException("An inaccurate character: " + b + "/" + String.valueOf((char) b));
					}
				}
			}
			else{
				throw new IllegalArgumentException("It is over the range by which a byte is permitted: " + String.valueOf(b));
			}
		}
		else{
			// デコード処理は終端文字を発見済みなので続行不能
			throw new IOException("The end of a stream is written in and it is ending. A byte cannot be written in any more.");
		}
	}

	/**
	 * バイト列をエンコード出力ストリームに書き込みます。<P>
	 *
	 * オフセット off から始まる指定のバイト配列からこの出力ストリームに
	 * len バイトを書き込みます。配列 b 内の一定のバイトが出力ストリームに
	 * 順番に書き込まれます。この処理で最初に書き込まれるバイトは要素 b[off]、
	 * 最後に書き込まれるバイトは要素 b[off+len-1] です。<P>
	 * b が null の場合は、NullPointerException がスローされます。<P>
	 * off が負の場合、len が負の場合、あるいは off+len が配列 b の長さより
	 * 大きい場合は、IndexOutOfBoundsException がスローされます。<BR>
	 * このメソッドは、配列 b 内の off から len バイトについて、
	 * int 型引数をひとつだけとる write メソッドを呼び出します。
	 *
	 * @param b データ
	 * @param off データの開始オフセット
	 * @param len 書き込むバイト数
	 * @exception ArithmeticException デコード中に計算結果がオーバーフローしてしまった場合
	 * @exception IllegalArgumentException バイト b が 0 より小さいか 0x3f よりも大きい場合
	 * @exception IOException 入出力エラーが発生した場合。
	 *                        特に、出力ストリームが閉じられている場合に
	 *                        IOException がスローされる
	 */
	public synchronized void write(byte[] b, int off, int len) throws IOException{
		while(0 <= --len){ writeByte(b[off++]); }
	}
}


/* End of File */