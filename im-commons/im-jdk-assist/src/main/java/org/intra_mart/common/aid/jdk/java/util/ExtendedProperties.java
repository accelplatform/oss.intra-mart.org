package org.intra_mart.common.aid.jdk.java.util;

import java.util.Properties;

/**
 * java.util.Properties を、より便利に利用するためのインタフェースを提供します。<P>
 *
 */
public class ExtendedProperties extends Properties{
	/**
	 * デフォルト値を持たない空のプロパティリストを作成します。
	 */
	public ExtendedProperties(){
		super();
	}

	/**
	 * 指定されたデフォルト値を持つ空のプロパティリストを作成します。
	 * @param defaults デフォルト値
	 */
	public ExtendedProperties(Properties defaults){
		super(defaults);
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を真偽判定します。
	 * @param key プロパティの名前
	 * @return プロパティの文字列値が "true" の場合(大文字と小文字は区別しない) true、そうでない場合 false。
	 */
	public boolean isTrue(String key){
		return Boolean.valueOf(this.getProperty(key)).booleanValue();
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を真偽判定します。
	 * @param key プロパティの名前
	 * @return プロパティの文字列値が "false" の場合(大文字と小文字は区別しない) true、そうでない場合 false。
	 */
	public boolean isFalse(String key){
		return ! Boolean.valueOf(this.getProperty(key)).booleanValue();
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を
	 * 数値として取得します。
	 * @param key プロパティの名前
	 * @return プロパティの文字列値を数値として表現した値。そのキーにプロパティがない場合(設定値が不適切なフォーマットも含む)は 0。
	 */
	public int intValue(String key){
		return this.intValue(key, 0);
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を
	 * 数値として取得します。
	 * @param key プロパティの名前
	 * @param defaultValue デフォルト値
	 * @return プロパティの文字列値を数値として表現した値。そのキーにプロパティがない場合(設定値が不適切なフォーマットも含む)はデフォルト値。
	 */
	public int intValue(String key, int defaultValue){
		try{
			return this.parseInt(key, 10);
		}
		catch(NumberFormatException e){
			return defaultValue;
		}
	}

	/**
	 * 2 番目の引数に指定された基数を元にして、
	 * 指定されたキーによって示されるプロパティ値を
	 * 符号付き整数として解析します。
	 * このメソッドは、java.lang.Integer.parseInt(String s, int radix)
	 * メソッドの仕様に準じます。
	 * @param key プロパティの名前
	 * @param radix 使用される基数
	 * @return プロパティの文字列値を数値として表現した値。
	 * @throws NumberFormatException 文字列が解析可能な整数を含まない場合
	 */
	public int parseInt(String key, int radix) throws NumberFormatException{
		return Integer.parseInt(this.getProperty(key), radix);
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を
	 * 長整数として取得します。
	 * @param key プロパティの名前
	 * @return プロパティの文字列値を数値として表現した値。そのキーにプロパティがない場合(設定値が不適切なフォーマットも含む)は 0。
	 */
	public long longValue(String key){
		return this.longValue(key, 0);
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を
	 * 長整数として取得します。
	 * @param key プロパティの名前
	 * @param defaultValue デフォルト値
	 * @return プロパティの文字列値を数値として表現した値。そのキーにプロパティがない場合(設定値が不適切なフォーマットも含む)はデフォルト値。
	 */
	public long longValue(String key, long defaultValue){
		try{
			return this.parseLong(key, 10);
		}
		catch(NumberFormatException e){
			return defaultValue;
		}
	}

	/**
	 * 2 番目の引数に指定された基数を元にして、
	 * 指定されたキーによって示されるプロパティ値を
	 * 符号付き長整数として解析します。
	 * このメソッドは、java.lang.Long.parseLong(String s, int radix)
	 * メソッドの仕様に準じます。
	 * @param key プロパティの名前
	 * @param radix 使用される基数
	 * @return プロパティの文字列値を数値として表現した値。
	 * @throws NumberFormatException 文字列が解析可能な整数を含まない場合
	 */
	public long parseLong(String key, int radix) throws NumberFormatException{
		return Long.parseLong(this.getProperty(key), radix);
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を
	 * 浮動小数点数(float)表現された数値として取得します。
	 * @param key プロパティの名前
	 * @return プロパティの文字列値を数値として表現した値。そのキーにプロパティがない場合(設定値が不適切なフォーマットも含む)は float 型の非数 (NaN) 値。
	 */
	public float floatValue(String key){
		return this.floatValue(key, Float.NaN);
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を
	 * 浮動小数点数(float)表現された数値として取得します。
	 * @param key プロパティの名前
	 * @param defaultValue デフォルト値
	 * @return プロパティの文字列値を数値として表現した値。そのキーにプロパティがない場合(設定値が不適切なフォーマットも含む)はデフォルト値。
	 */
	public float floatValue(String key, float defaultValue){
		try{
			return Float.parseFloat(this.getProperty(key));
		}
		catch(NumberFormatException e){
			return defaultValue;
		}
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を
	 * 浮動小数点数(double)表現された数値として取得します。
	 * @param key プロパティの名前
	 * @return プロパティの文字列値を数値として表現した値。そのキーにプロパティがない場合(設定値が不適切なフォーマットも含む)は double 型の非数 (NaN) 値。
	 */
	public double doubleValue(String key){
		return this.doubleValue(key, Double.NaN);
	}

	/**
	 * 指定されたキーによって示されるプロパティ値を
	 * 浮動小数点数(double)表現された数値として取得します。
	 * @param key プロパティの名前
	 * @param defaultValue デフォルト値
	 * @return プロパティの文字列値を数値として表現した値。そのキーにプロパティがない場合(設定値が不適切なフォーマットも含む)はデフォルト値。
	 */
	public double doubleValue(String key, double defaultValue){
		try{
			return Double.parseDouble(this.getProperty(key));
		}
		catch(NumberFormatException e){
			return defaultValue;
		}
	}
}

