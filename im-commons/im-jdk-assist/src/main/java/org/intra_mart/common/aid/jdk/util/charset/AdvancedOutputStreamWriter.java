package org.intra_mart.common.aid.jdk.util.charset;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * このクラスは、基礎となる java.io.OutputStreamWriter をフィルタリングした
 * Writer 機能を提供します。<p>
 * 通常、ASCII の範囲に含まれない文字は、java.io.OutputStreamWriter を
 * 利用して相互に文字コード変換した場合、各文字エンコーディングの
 * Unicode に対するマッピングの相違により、本来その文字エンコーディングにおいて
 * 存在する文字であってもコード不明を意味する「?」に置き換えられてしまう
 * ことがあります。
 * いわゆる、この文字化けに対して、文字コード変換前に、
 * 対象となる文字エンコーディングに対する Unicode の正しい文字に
 * 置換することで、いくつかの文字については正しく文字コード変換することが
 * できます。
 * <br>
 * このクラスでは、この方式により文字化けを回避します。
 * <p>
 * 文字化けに対する文字のマッピングおよび対応する文字エンコーディングに
 * 関しては、{@link org.intra_mart.common.aid.jdk.util.charset.CharacterEncoder
 * org.intra_mart.common.aid.jdk.util.charset.CharacterEncoder} クラスの仕様に準じます。
 *
 * @see org.intra_mart.common.aid.jdk.util.charset.CharacterEncoder org.intra_mart.common.aid.jdk.util.charset.CharacterEncoder
 */
public class AdvancedOutputStreamWriter extends FilterWriter {
	/**
	 * 文字エンコーディング名
	 */
	private String characterEncoding = null;
	/**
	 * エンコードの際のマッピングテーブル
	 */
	private char[] mappingTable = null;
	/**
	 * 基礎となる出力
	 */
	private OutputStreamWriter parentOutputStreamWriter = null;

	/**
	 * 指定された文字エンコーディングを使う AdvancedOutputStreamWriter を作成します。
	 * @param out 基礎となる出力ストリーム
	 * @param charsetName サポートされる charset の名前
	 * @throws UnsupportedEncodingException 指定された文字エンコーディングがサポートされていない場合
	 * @throws NullPointerException 引数が null の場合
	 * @throws ResourceNotFoundException 対象となる文字エンコーディングに対応する設定がない場合
	 * @throws IOException 設定の読み込みエラー
	 * @throws ResourceFormatException 設定に不整合がある場合
	 */
	public AdvancedOutputStreamWriter(OutputStream out, String charsetName) throws UnsupportedEncodingException, ResourceNotFoundException, ResourceFormatException, IOException {
		this(new OutputStreamWriter(out, charsetName));
	}

	/**
	 * デフォルトの文字エンコーディングを使う AdvancedOutputStreamWriter を作成します。
	 * @param out 基礎となる出力ストリーム
	 * @throws NullPointerException 引数が null の場合
	 * @throws ResourceNotFoundException 対象となる文字エンコーディングに対応する設定がない場合
	 * @throws IOException 設定の読み込みエラー
	 * @throws ResourceFormatException 設定に不整合がある場合
	 */
	public AdvancedOutputStreamWriter(OutputStream out) throws ResourceFormatException, ResourceNotFoundException, IOException {
		this(new OutputStreamWriter(out));
	}

	/**
	 * 与えられた文字エンコーディングを使う AdvancedOutputStreamWriter を作成します。
	 * @param out 基礎となる出力ストリーム
	 * @param cs サポートされる Charset
	 * @throws NullPointerException 引数が null の場合
	 * @throws ResourceNotFoundException 対象となる文字エンコーディングに対応する設定がない場合
	 * @throws IOException 入出力エラー
	 * @throws ResourceFormatException 設定に不整合がある場合
	 */
	public AdvancedOutputStreamWriter(OutputStream out, Charset cs) throws IOException, ResourceFormatException, ResourceNotFoundException {
		this(new OutputStreamWriter(out, cs));
	}

	/**
	 * 与えられた文字エンコーディングエンコーダを使う AdvancedOutputStreamWriter を作成します。
	 * @param out 基礎となる出力ストリーム
	 * @param enc 文字エンコーディングエンコーダ
	 * @throws NullPointerException 引数が null の場合
	 * @throws ResourceNotFoundException 対象となる文字エンコーディングに対応する設定がない場合
	 * @throws IOException 設定の読み込みエラー
	 * @throws ResourceFormatException 設定に不整合がある場合
	 */
	public AdvancedOutputStreamWriter(OutputStream out, CharsetEncoder enc) throws ResourceFormatException, IOException, ResourceNotFoundException {
		this(new OutputStreamWriter(out, enc));
	}

	/**
	 * OutputStreamWriter をフィルタリングする新しい AdvancedOutputStreamWriter を作成します。<p>
	 * なお、このオブジェクトが対応する文字エンコーディングは、
	 * java.io.OutputStreamWriter#getEncoding() の返す値によって
	 * 決定します。
	 * java.io.OutputStreamWriter#getEncoding() が null を返す場合、
	 * このコンストラクタは NUllPointerException をスローして
	 * オブジェクトの作成に失敗します。
	 * @param writer 基本となるストリームを提供する java.io.OutputStreamWriter オブジェクト
	 * @throws NullPointerException 引数が null の場合
	 * @throws ResourceNotFoundException 対象となる文字エンコーディングに対応する設定がない場合
	 * @throws ResourceFormatException 設定に不整合がある場合
	 * @throws IOException 設定の読み込みエラー
	 */
	public AdvancedOutputStreamWriter(OutputStreamWriter writer) throws ResourceNotFoundException, IOException, ResourceFormatException {
		super(writer);

		// 設定の保管
		this.parentOutputStreamWriter = writer;
		this.setCharacterEncoding(writer.getEncoding());
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
	 * @throws ResourceNotFoundException 対象となる文字エンコーディングに対応する設定がない場合
	 * @throws ResourceFormatException 設定に不整合がある場合
	 * @throws IOException 設定の読み込みエラー
	 * @see {@link CharacterMappingBuilder}
	 */
	private void setCharacterEncoding(String enc) throws ResourceNotFoundException, ResourceFormatException, IOException{
		if(enc == null){
			throw new NullPointerException("Method-parameter error at " + this.getClass().getName() + "#setCharacterEncoding(): enc is null.");
		}

		this.characterEncoding = enc;		// 値の保存

		// マッピング情報の取得
		try{
			CharacterMappingBuilder cmb = CharacterMappingBuilder.instance();
			this.mappingTable = cmb.getMapping(this.getCharacterEncoding());
		}
		catch(SAXException saxe){
			throw new ResourceFormatException("Configuration format error: resource=" + CharacterMappingBuilder.CONFIG_FILE_PATH, saxe);
		}
		catch(ParserConfigurationException pce){
			throw new ResourceFormatException("Parser error.", pce);
		}
		this.characterEncoding = enc;
	}

	/**
	 * 文字の配列の一部を書き込みます。
	 * @param cbuf 書き込む文字のバッファ
	 * @param off 文字の読み込み開始オフセット
	 * @param len 書き込む文字数
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(char[] cbuf, int off, int len) throws IOException{
		// char データをコピーするための器を作成
		int terminate = off + len;
		char[] chars = new char[terminate];

		// マッピングの修正
		for(int idx = off; idx < terminate; idx++){
			try{
				chars[idx] = this.mappingTable[cbuf[idx]];
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				chars[idx] = cbuf[idx];				// マッピングの変更はなし
			}
		}

		// 出力
		this.parentOutputStreamWriter.write(chars, off, len);
	}

	/**
	 * 単一の文字を書き込みます。
	 * @param c 書き込む文字を指定する int
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(int c) throws IOException{
		try{
			this.parentOutputStreamWriter.write(this.mappingTable[c]);
		}
		catch(ArrayIndexOutOfBoundsException aioobe){
			this.parentOutputStreamWriter.write(c);	// マッピングの変更はなし
		}
	}

	/**
	 * 文字列の一部を書き込みます。
	 * @param str 書き込まれる文字列
	 * @param off 文字の読み込み開始オフセット
	 * @param len 書き込む文字数
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(String str, int off, int len) throws IOException{
		// char データの取得
		char[] chars = str.toCharArray();

		// マッピングの修正
		int termPoint = off + len;
		for(int idx = off; idx < termPoint; idx++){
			try{
				chars[idx] = this.mappingTable[chars[idx]];
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				continue;							// マッピングの変更はなし
			}
		}

		// 出力
		this.parentOutputStreamWriter.write(chars, off, len);
	}

	/**
	 * このオブジェクトの基礎出力となっている java.io.OutputStreamWriter を返します。
	 * @return 基礎となっている java.io.OutputStreamWriter
	 */
	public OutputStreamWriter getOutputStreamWriter(){
		return this.parentOutputStreamWriter;
	}
}
