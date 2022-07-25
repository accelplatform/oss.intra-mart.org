package org.intra_mart.common.aid.jdk.util.charset;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * このクラスは、文字エンコードの機能を提供します。<P>
 * このクラスは、Java runtime が提供する通常の文字コード変換において
 * 文字のマッピング定義の相違に起因して文字化けしてしまう現象を解決するための
 * 一つの方法となります。
 * 実際には、定義ファイルに設定されている文字をネイティブコードへ変換する前に
 * 他の文字へ変更する事により、文字化けを防ぎます。
 * <p>
 * 例えば、EUC_JP における文字「～」は、
 * Unicode へ変換した際 0x301C(Wave Dash) にマッピングされます。
 * これに対して、SJIS  では文字「～」は、
 * Unicode の 0xFF5E(Fullwidth Tilde) にマッピングされています。
 * このため、相互に文字コード変換をすると、
 * 文字「?」に置き換わると言う現象(いわゆる文字化け)が発生します。<br>
 * このクラスでは、Unicode から SJIS  へ変換する際に
 * Unicode の文字 0x301C(Wave Dash) を 0xFF5E(Fullwidth Tilde) へ変更してから
 * 文字コード変換を行います。
 * <p>
 * 定義ファイルは、クラスローダを利用して検索および読み込まれます。
 * したがって、定義ファイルはクラスパスに設定されているディレクトリまたは
 * アーカイブファイル内に配置する必要があります。<br>
 * 各文字エンコーディングと文字の変換テーブルの関連については、
 * 定義ファイル <code>org/intra_mart/resources/charset/encoding/mapping.xml</code> に記載されています。
 * <br>
 *
 */
public class CharacterEncoder{
	/**
	 * 単体試験用
	 */
/*
	public static void main(String[] args){
		try{
			String enc = "SJIS";
			CharacterEncoder ce = new CharacterEncoder(enc);

			String str = " － ～ ∥ ￠ ￡ ￢ ― ";
			String encodedString = ce.newString(str);

			File f = new File("E:/work/test.txt");
			FileOutputStream out = new FileOutputStream(f);
			OutputStreamWriter writer = new OutputStreamWriter(out, "JIS");
			writer.write(str);
			writer.write("\r\n");
			writer.write(encodedString);
			ce = new CharacterEncoder("MS932");
			writer.write("\r\n");
			writer.write(ce.newString(encodedString));
			writer.flush();

			writer.close();
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}
*/

	/**
	 * 文字エンコーディング名
	 */
	private String characterEncoding = null;
	/**
	 * エンコードの際のマッピングテーブル
	 */
	private char[] mappingTable = null;

	/**
	 * 文字コード変換のためのエンコーダを作成します。<p>
	 * @param enc エンコーディング名
	 * @throws NullPointerException 引数が null の場合
	 * @throws ResourceNotFoundException 設定が見つからなかった場合
	 * @throws ResourceFormatException 設定情報が不適切だった場合
	 * @throws IOException 入出力エラー
	 */
	public CharacterEncoder(String enc) throws ResourceNotFoundException, ResourceFormatException, IOException{
		super();

		// 設定の保管
		this.setCharacterEncoding(enc);
	}

	/**
	 * このオブジェクトが対象としている文字エンコーディング名を返します。
	 * @return 文字エンコーディング名
	 */
	public String getCharacterEncoding(){
		return this.characterEncoding;
	}

	/**
	 * このオブジェクトが対象とする文字エンコーディングを設定します。
	 * @param enc エンコーディング名
	 * @throws NullPointerException 引数が null の場合
	 * @throws ResourceNotFoundException 設定が見つからなかった場合
	 * @throws ResourceFormatException 設定情報が不適切だった場合
	 * @throws IOException 入出力エラー
	 */
	protected void setCharacterEncoding(String enc) throws ResourceFormatException, ResourceNotFoundException, IOException{
		if(enc == null){
			throw new NullPointerException("Method-parameter error at " + this.getClass().getName() + "#setCharacterEncoding(): enc is null.");
		}

		// マッピング情報の取得
		try{
			CharacterMappingBuilder cmb = CharacterMappingBuilder.instance();
			this.mappingTable = cmb.getMapping(enc);
		}
		catch(SAXException saxe){
			throw new ResourceFormatException("Configuration format error: resource=" + CharacterMappingBuilder.CONFIG_FILE_PATH, saxe);
		}
		catch(ParserConfigurationException pce){
			throw new ResourceFormatException("Parser error.", pce);
		}
		this.characterEncoding = enc;		// 保管
	}

	/**
	 * 指定の文字を {@link #getCharacterEncoding()} で表される文字コードへ
	 * エンコードします。
	 * @param c 文字
	 * @return エンコードされたバイト列
	 */
	public byte[] encode(char c) throws UnsupportedEncodingException, IOException{
		// 文字コード変換
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(baos, this.getCharacterEncoding());
		try{
			out.write(this.mappingTable[c]);
		}
		catch(ArrayIndexOutOfBoundsException aioobe){
			out.write(c);							// マッピングの変更はなし
		}
		out.close();

		return baos.toByteArray();		// 返却
	}

	/**
	 * 指定の文字を {@link #getCharacterEncoding()} で表される文字コードへ
	 * エンコードします。
	 * @param c 文字配列
	 * @return エンコードされたバイト列
	 */
	public byte[] encode(char[] c) throws UnsupportedEncodingException, IOException{
		if(c == null){
			throw new NullPointerException("Method-parameter error at " + this.getClass().getName() + "#encode(char[]): c is null.");
		}

		// 文字配列の修正
		char[] chars = new char[c.length];
		for(int idx = 0; idx < c.length; idx++){
			try{
				chars[idx] = this.mappingTable[c[idx]];
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				chars[idx] = c[idx];				// マッピングの変更はなし
			}
		}

		// 文字コード変換
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(baos, this.getCharacterEncoding());
		out.write(chars);
		out.close();

		return baos.toByteArray();		// 返却
	}

	/**
	 * 指定の文字列を {@link #getCharacterEncoding()} で表される文字コードへ
	 * エンコードします。
	 * @param str 文字列
	 * @return エンコードされたバイト列
	 */
	public byte[] encode(String str) throws UnsupportedEncodingException, IOException{
		if(str == null){
			throw new NullPointerException("Method-parameter error at " + this.getClass().getName() + "#encode(String): str is null.");
		}

		// 文字の置換
		char[] chars = str.toCharArray();
		for(int idx = chars.length - 1; idx >= 0; idx--){
			try{
				chars[idx] = this.mappingTable[chars[idx]];
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				continue;							// マッピングの変更はなし
			}
		}

		// 文字コード変換
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(baos, this.getCharacterEncoding());
		out.write(chars);
		out.close();

		return baos.toByteArray();		// 返却
	}

	/**
	 * {@link #getCharacterEncoding()} で表される文字コードに対応した
	 * 新しい文字列を作成します。<p>
	 * 返される文字列は、引数 str のマッピングを修正した
	 * 新しい文字列です。
	 *
	 * @param str 文字列
	 * @return マッピングを修正された新しい文字列
	 */
	public String newString(String str){
		if(str == null){
			throw new NullPointerException("Method-parameter error at " + this.getClass().getName() + "#newString(String): str is null.");
		}

		// 文字の置換
		char[] chars = str.toCharArray();
		for(int idx = chars.length - 1; idx >= 0; idx--){
			try{
				chars[idx] = this.mappingTable[chars[idx]];
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				continue;							// マッピングの変更はなし
			}
		}

		return new String(chars);
	}

	/**
	 * {@link #getCharacterEncoding()} で表される文字コードに対応した
	 * 新しい文字列を作成します。<p>
	 * 返される文字列は、引数 c のマッピングを修正した
	 * 新しい文字列です。
	 * <br>
	 * 引数の配列 c は変化しません。
	 *
	 * @param c 文字配列
	 * @return 新しい文字列
	 */
	public String newString(char[] c){
		if(c == null){
			throw new NullPointerException("Method-parameter error at " + this.getClass().getName() + "#newString(char[]): c is null.");
		}

		// 文字配列の修正
		char[] chars = new char[c.length];
		for(int idx = 0; idx < c.length; idx++){
			try{
				chars[idx] = this.mappingTable[c[idx]];
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				chars[idx] = c[idx];				// マッピングの変更はなし
			}
		}

		return new String(chars);
	}
}

