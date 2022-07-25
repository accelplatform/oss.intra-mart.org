package org.intra_mart.jssp.source;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;


import org.intra_mart.common.aid.jdk.util.charset.CharacterSetManager;
import org.intra_mart.jssp.source.property.SourceProperties;
import org.xml.sax.SAXException;

/**
 * ソースファイルのデータを持つクラスです。
 */
public class SourceFile implements Serializable{
	/**
	 * ソースデータ
	 */
	private char[] srcChars = null;

	/**
	 * ファイルの絶対パス名
	 */
	private String absolutePath = null;

	/**
	 * 設定
	 */
	private SourceProperties sourceProperties = null;

	/**
	 * 指定のソースデータを持つ新しいオブジェクトを作成します。
	 * 
	 * @param file ソースのファイルパスを表すオブジェクト
	 * @param sourceProperties 設定情報
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public SourceFile(File file, SourceProperties sourceProperties) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		super();

		// 情報の保存
		this.absolutePath = file.getAbsolutePath();
		this.sourceProperties = sourceProperties;

		// ソースの読み込み
		if(file.isFile()){
			FileInputStream fis = new FileInputStream(file);
			Reader in = null;
			String enc = this.getCharacterEncoding();
			if(enc != null){
				in = new InputStreamReader(fis, CharacterSetManager.toJDKName(enc));
			}
			else{
				in = new InputStreamReader(fis);
			}

			CharArrayWriter out = new CharArrayWriter();
			while(true){
				int chr = in.read();
				if(chr != -1){
					out.write(chr);
				}
				else{
					break;
				}
			}

			out.flush();
			this.srcChars = out.toCharArray();		// データ確保
			in.close();
			out.close();
		}
		else{
			throw new FileNotFoundException("File is not found: " + file.getAbsolutePath());
		}
	}

	/**
	 * ソースを読み込んだときの文字エンコーディング名を返します。
	 */
	public String getCharacterEncoding(){
		return this.getSourceProperties().getCharacterEncoding();
	}

	/**
	 * ソースの絶対パスを返します。
	 */
	public String getAbsolutePath(){
		return this.absolutePath;
	}

	/**
	 * ソースデータを返します。
	 */
	public char[] getCharArray(){
		return this.srcChars;
	}

	/**
	 * ソースデータを読み込むためのストリームを返します。
	 * @return ソースデータを読み込む入力ストリーム
	 */
	public Reader getReader(){
		return new CharArrayReader(this.getCharArray());
	}

	/**
	 * 指定の出力に対してソースデータを書き出します。
	 * @param out 出力先ストリーム
	 */
	public void writeTo(Writer out) throws IOException{
		out.write(this.getCharArray());
	}

	/**
	 * このソースに関する設定情報を返却します。
	 */
	public SourceProperties getSourceProperties(){
		return this.sourceProperties;
	}
}

