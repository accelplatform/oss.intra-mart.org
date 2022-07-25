package org.intra_mart.jssp.source.property;

import java.io.Serializable;

import org.intra_mart.common.aid.jdk.java.util.ExtendedProperties;

/**
 * ソースファイルに関する設定情報の共通インタフェースに関する抽象実装です。
 */
public abstract class AbstractSourceProperties extends ExtendedProperties implements SourceProperties, Serializable{
	private SourceProperties parentProperties = null;

	/**
	 * 新しい設定オブジェクトを作成します。
	 */
	protected AbstractSourceProperties(SourceProperties properties){
		super();
		this.parentProperties = properties;
	}

	/**
	 * 新しい設定オブジェクトを作成します。
	 */
	protected AbstractSourceProperties(){
		this(new SourcePropertiesImpl());
	}

	/**
	 * ソースの文字エンコーディングを返します。
	 * 文字エンコーディングが不明な場合、null を返します。
	 * @return 文字エンコーディング名
	 */
	public String getCharacterEncoding(){
		String enc = this.getProperty(this.getKey4characterEncoding());
		if(enc != null){
			return enc;
		}
		else{
			return this.parentProperties.getCharacterEncoding();
		}
	}

	/**
	 * JavaScript 解析（コンパイル）時の最適化レベルを返します。
	 * @return 最適化レベル（-1 <= level <= 9）
	 */
	public int getJavaScriptOptimizationLevel(){
		String key = this.getKey4javaScriptOptimizationLevel();
		String value = this.getProperty(key);
		if(value != null){
			return this.intValue(key);
		}
		else{
			try{
				return this.parentProperties.getJavaScriptOptimizationLevel();
			}
			catch(UnsupportedOperationException uoe){
				throw new UnsupportedOperationException("Property is not found: " + key + " (-1 <= level <= 9)");
			}
		}
	}

	/**
	 * JavaScript を Java のクラスにコンパイルする機能が有効かどうかを
	 * 判定します。
	 * @throws UnsupportedOperationException 判定するための設定情報が見つからない場合
	 */
	public boolean enableJavaScriptCompile() throws UnsupportedOperationException{
		String key = this.getKey4javaScriptCompiler();
		boolean trueValue = this.isTrue(key);
		boolean falseValue = this.isFalse(key);
		if(trueValue != falseValue){
			return trueValue;
		}
		else{
			try{
				return this.parentProperties.enableJavaScriptCompile();
			}
			catch(UnsupportedOperationException uoe){
				throw new UnsupportedOperationException("Property is not found: " + key + " (true|false)");
			}
		}
	}

	/**
	 * PresentationPage のソース(html)をキャッシュする機能が有効かどうかを
	 * 判定します。
	 * @return 有効という設定になっている場合 ture。
	 * @throws UnsupportedOperationException 判定するための設定情報が見つからない場合
	 */
	public boolean enableViewCompile() throws UnsupportedOperationException{
		String key = this.getKey4viewCompiler();
		boolean trueValue = this.isTrue(key);
		boolean falseValue = this.isFalse(key);
		if(trueValue != falseValue){
			return trueValue;
		}
		else{
			try{
				return this.parentProperties.enableViewCompile();
			}
			catch(UnsupportedOperationException uoe){
				throw new UnsupportedOperationException("Property is not found: " + key + " (true|false)");
			}
		}
	}

	/**
	 * 文字エンコーディングの設定値がマッピングされているキー名を返します。
	 * @return キー名
	 */
	protected abstract String getKey4characterEncoding();

	/**
	 * 最適化レベルの設定値がマッピングされているキー名を返します。
	 * @return キー名
	 */
	protected abstract String getKey4javaScriptOptimizationLevel();

	/**
	 * JavaScript のコンパイル機能が有効かどうかの設定値がマッピングされている
	 * キー名を返します。
	 * @return キー名
	 */
	protected abstract String getKey4javaScriptCompiler();

	/**
	 * View ソースのキャッシュ機能が有効かどうかの設定値がマッピングされている
	 * キー名を返します。
	 * @return キー名
	 */
	protected abstract String getKey4viewCompiler();
}
