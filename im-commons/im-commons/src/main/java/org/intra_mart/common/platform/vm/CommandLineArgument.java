package org.intra_mart.common.platform.vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * このクラスはコマンド実引数を解釈するための共通実装です。<p>
 * このクラスは、インスタンス生成時にコマンドライン引数を解析し、
 * オプションと引数に選別します。
 *    コマンド [-オプション名 [値]] [引数]
 * オプションは "-" から始まる名称文字列と値から成り立ちます。
 * 最後尾のオプション指定以降のトークンを引数として解釈します。
 * オプションはオプション名("-" から始まるトークン)をキーとして
 * 値がマッピングされます。
 */
public class CommandLineArgument{
	/**
	 * このインスタンスが司る引数情報
	 */
	private List argumentList = null;
	/**
	 * オプションが格納されています。
	 */
	private Map options = new HashMap();
	/**
	 * コマンドライン引数からオプションを除いた引数が格納されています。
	 */
	private List arguments = new ArrayList();

	/**
	 * Java-VM から最初に実行される public かつ static な
	 * void main(String[]) メソッドの引数を受け取り解析します。<p>
	 * 実引数(command-line-arguments)の格納されている配列の構造は変化しません。<br>
	 *     コマンド [-オプション名 [値]] [引数]
	 * オプションと引数を分割します。
	 * オプションは、値のない単独指定も可能です。
	 * @param args コマンド実行時のコマンドライン引数(main メソッドの引数)
	 * @throws IllegalArgumentException 引数が null の場合
	 */
	public CommandLineArgument(String[] args){
		if(args == null){ throw new IllegalArgumentException("args is null"); }

		// 引数の解析
		this.argumentList = Arrays.asList(args);

		ListIterator cursor = this.argumentList.listIterator();
		while(cursor.hasNext()){
			String data = (String) cursor.next();
			if(data.startsWith("-")){
				List optionValues = (List) options.get(data);
				if(optionValues == null){
					optionValues = new ArrayList();
					options.put(data, optionValues);
				}
				// 値チェック
				if(cursor.hasNext()){
					String attributeValue = (String) cursor.next();
					if(! attributeValue.startsWith("-")){
						optionValues.add(attributeValue);
					}
					else{
						cursor.previous();			// カーソルポイントを戻す
					}
				}
			}
			else{
				// 残りはすべて引数となる
				arguments.add(data);
				while(cursor.hasNext()){ arguments.add(cursor.next()); }
				break;	// ループの終了
			}
		}
	}

	/**
	 * 引数情報を取得します。
	 * このメソッドは、このインスタンス生成時にコンストラクタに指定した
	 * 値を返します。
	 * @return 引数情報
	 */
	public String[] getQuery(){
		return (String[]) this.argumentList.toArray(new String[this.argumentList.size()]);
	}

	/**
	 * オプションが指定されているかどうか判定します。<p>
	 * @param name オプション名
	 * @return オプションが指定されている場合 true、そうでない場合 false。
	 */
	public boolean hasOption(String name){
		return options.containsKey(name);
	}

	/**
	 * オプションの値を返します。
	 * オプション name が値を持たない単独指定の場合、null を返します。
	 * @param name オプション名
	 * @return オプションの値
	 * @throws IllegalArgumentException 指定のオプション名が定義されていない場合
	 */
	public String getOption(String name) throws IllegalArgumentException{
		String[] values = this.getOptions(name);
		if(values != null){
			return values[0];						// 値の返却
		}
		else{
			return null;							// 単独属性
		}
	}

	/**
	 * オプションの値を返します。
	 * オプション name が値を持たない単独指定の場合、null を返します。
	 * また、指定のオプション名が未定義だった場合、デフォルト値 def を
	 * 返します。
	 * @param name オプション名
	 * @param def オプションが未指定だった場合のデフォルト値
	 * @return オプションの値
	 */
	public String getOption(String name, String def){
		try{
			return this.getOption(name);
		}
		catch(IllegalArgumentException iae){
			return def;								// 未定義→デフォルト値
		}
	}

	/**
	 * オプションの値を返します。
	 * オプション name が値を持たない単独指定の場合、null を返します。
	 * @param name オプション名
	 * @return オプションの値
	 * @throws IllegalArgumentException 指定のオプション名が定義されていない場合
	 */
	public String[] getOptions(String name) throws IllegalArgumentException{
		Collection collection = (Collection) options.get(name);
		if(collection != null){
			if(collection.size() > 0){
				return (String[]) collection.toArray(new String[collection.size()]);
			}
			else{
				return null;
			}
		}
		else{
			throw new IllegalArgumentException(name + " is not defined.");
		}
	}

	/**
	 * 指定されたすべてのオプションの名前を返します。
	 * 返却値のセットの各要素は String インスタンスです。
	 * @return オプション名一覧のセット
	 */
	public Set getOptionNames(){
		return new HashSet(options.keySet());
	}

	/**
	 * 指定されたすべてのコマンドライン引数の引数を返します。
	 * ここで返される引数は、コマンドライン引数のうちの
	 * オプション指定ではない引数です。
	 * 返却値のセットの各要素は String インスタンスです。
	 * @return 引数のセット
	 */
	public String[] getArguments(){
		return (String[]) this.arguments.toArray(new String[this.arguments.size()]);
	}

	/**
	 * 指定されたすべてのコマンドライン引数の引数を返します。
	 * ここで返される引数は、コマンドライン引数のうちの
	 * オプション指定ではない引数です。
	 * 返却値のセットの各要素は String インスタンスです。
	 * @return 引数のセット
	 * @throws ArrayIndexOutOfBoundsException インデックスが範囲外の場合（引数の個数以上、または負数）
	 */
	public String getArgument(int index) throws ArrayIndexOutOfBoundsException{
		return (String) this.arguments.get(index);
	}

	/**
	 * コマンドライン引数があるかどうか判定します。
	 * ここでチェックされる引数は、コマンドライン引数のうちの
	 * オプション指定ではない引数です。
	 * @return オプションではない引数がある場合 true、そうでない場合 false。
	 */
	public boolean hasArguments(){
		return ! arguments.isEmpty();
	}
}

/* End of File */