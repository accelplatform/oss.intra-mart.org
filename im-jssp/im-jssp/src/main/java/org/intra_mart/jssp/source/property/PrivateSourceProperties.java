package org.intra_mart.jssp.source.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * ソースファイルのデータを持つクラスです。
 */
public class PrivateSourceProperties extends AbstractSourceProperties{
	/**
	 * 設定データを作成します。
	 * @param f 設定ファイル（xml）のパス
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public PrivateSourceProperties(File f) throws FileNotFoundException, IOException{
		this(new FileInputStream(f));
	}

	/**
	 * 設定データを作成します。
	 * @param in XML ソースを読み込むための入力ストリーム
	 * @throws IOException
	 */
	public PrivateSourceProperties(InputStream in) throws IOException{
		super();

		this.load(in);
	}

	/**
	 * 設定データを作成します。
	 * @param in XML ソースを読み込むための入力ストリーム
	 * @param parent 基礎データ
	 * @throws IOException
	 */
	public PrivateSourceProperties(InputStream in, SourceProperties parent) throws IOException{
		super(parent);

		this.load(in);
	}

	/**
	 * 文字エンコーディングの設定値がマッピングされているキー名を返します。
	 * @return キー名
	 */
	protected String getKey4characterEncoding(){
		return "charset";
	}

	/**
	 * 最適化レベルの設定値がマッピングされているキー名を返します。
	 * @return キー名
	 */
	protected String getKey4javaScriptOptimizationLevel(){
		return "javascript.optimize.level";
	}

	/**
	 * JavaScript のコンパイル機能が有効かどうかの設定値がマッピングされている
	 * キー名を返します。
	 * @return キー名
	 */
	protected String getKey4javaScriptCompiler(){
		return "javascript.compile.enable";
	}

	/**
	 * View ソースのキャッシュ機能が有効かどうかの設定値がマッピングされている
	 * キー名を返します。
	 * @return キー名
	 */
	protected String getKey4viewCompiler(){
		return "view.compile.enable";
	}
}

