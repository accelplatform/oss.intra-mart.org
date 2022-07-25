package org.intra_mart.common.aid.jdk.util;

/**
 * 「サロゲートペア文字」を扱うためのユーティリティクラスです。<br/>
 * <br/>
 * 「サロゲートペア文字」とは、
 * Unicodeの U+FFFF よりも大きいコードポイントを持つ文字である「補助文字」のことを指しています。<br/>
 * <br/>
 * 言い換えると、<br/>
 * 「上位サロゲート」範囲 (&#x5c;uD800-&#x5c;uDBFF) からの最初の値と、<br/>
 * 「下位サロゲート」範囲 (&#x5c;uDC00-&#x5c;uDFFF) からの第２の値のペアで表現される文字のことを指しています。<br/>
 * 
 * @author NTTDATA intra-mart
 * 
 * @see <a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/java/lang/Character.html#unicode">Unicode 文字表現(JDK 5.0 ドキュメント - Java 2 プラットフォーム API 仕様)</a>
 * @see <a href="http://ja.wikipedia.org/wiki/Unicode#.E3.82.B5.E3.83.AD.E3.82.B2.E3.83.BC.E3.83.88.E3.83.9A.E3.82.A2">サロゲートペア (Wikipedia)</a>
 * @see <a href="http://java.sun.com/developer/technicalArticles/Intl/Supplementary/index_ja.html">Java プラットフォームにおける補助文字のサポート(Sun Developer Network)</a>
 * @see <a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/java/lang/Character.html">Characterクラス(JDK 5.0 ドキュメント - Java 2 プラットフォーム API 仕様)</a>
 * @see <a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/java/lang/String.html">Stringクラス(JDK 5.0 ドキュメント - Java 2 プラットフォーム API 仕様)</a>
 * 
 */
public class CodePointUtil {
	
	/**
	 * 引数 target の 文字数を返します。<br/>
	 * サロゲートペア文字は１文字としてカウントされます。<br/>
	 * <br/>
	 * 例えば、「α」をサロゲートペア文字とすると、<br/>
	 * <tt>"0123αあいう".length()</tt>               は、「<b>9</b>」を返却しますが、<br/>
	 * <tt> CodePointUtil.length("0123αあいう")</tt> は、「<b>8</b>」を返却します。<br/>
	 * <br/>
	 * このメソッドを呼び出すことは、
	 * {@link java.lang.String#codePointCount(int, int)
	 * <tt>target.codePointCount(0, target.length())</tt>}
	 * を呼び出すことに相当します。
	 * 
	 * @param target 対象文字列
	 * @return 引数 target に指定された文字列の文字数
	 */
	public static int length(final String target){
		return target.codePointCount(0, target.length());
	}
	
	
	/**
	 * 引数 target の codePointIndex番目の文字(Unicode コードポイント)を返します。<br/>
	 * 最初の文字を0番目とします。また、サロゲートペア文字は１文字としてカウントされます。<br/>
	 * <br/>
	 * 例えば、「α」をサロゲートペア文字とすると、<br/>
	 * <tt>"0123αあいう".charAt(4)</tt>                 は、<b><u>「α」</u>の「上位サロゲート範囲の値」</b>を返却しますが、<br/>
	 * <tt> CodePointUtil.charAt("0123αあいう", 4)</tt> は、<b><u>「α」</u>の「Unicode コードポイント」</b>を返却します。<br/>
	 * <br/>
	 * <tt>"0123αあいう".charAt(5)</tt>                 は、<b><u>「α」</u>の「下位サロゲート範囲の値」</b>を返却しますが、<br/>
	 * <tt> CodePointUtil.charAt("0123αあいう", 5)</tt> は、<b><u>「あ」</u>の「Unicode コードポイント」</b>を返却します。<br/>
	 * <br/>
	 * 
	 * @param target 対象文字列
	 * @param codePointIndex インデックス (最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント）
	 * @return 指定された位置の文字 (Unicode コードポイント)
	 */
	public static int charAt(final String target, final int codePointIndex){
		
		int index = codePointIndex;

		// サロゲートペア文字なし 
		if(!CodePointUtil.containsSurrogatePair(target)){
			return target.charAt(index);
		}
		// サロゲートペア文字あり
		else{
			index = CodePointUtil.convCodePointIndex2Index(target, codePointIndex);
			return Character.codePointAt(target.toCharArray(), index);
		}
	}

	
	/**
	 * 引数 target の 先頭から後方に検索し、最初に str が現れる位置を返します。見つからない場合は -1 を返します。<br/>
	 * サロゲートペア文字は１文字としてカウントされます。<br/>
	 * <br/>
	 * このメソッドを呼び出すことは、
	 * {@link CodePointUtil#indexOf(String, String, int)
	 * <tt>CodePointUtil.indexOf(target, str, 0)</tt>}
	 * を呼び出すことに相当します。
	 * 
	 * @param target 対象文字列
	 * @param str 検索する文字列
	 * @return 最初に str が現れる位置(最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント)。見つからない場合は -1 が返されます。
	 */
	public static int indexOf(final String target, final String str){
		return CodePointUtil.indexOf(target, str, 0);
	}

	
	/**
	 * 引数 target の fromCodePointIndex番目（最初の文字を0番目とする）から後方に検索し、
	 * 最初に str が現れる位置を返します。見つからない場合は -1 を返します。<br/>
	 * サロゲートペア文字は１文字としてカウントされます。<br/>
	 * <br/>
	 * 例えば、「α」をサロゲートペア文字とすると、<br/>
	 * <tt>"0123αあいう".indexOf("あ", 0)</tt>                 は、「<b>6</b>」を返却しますが、<br/>
	 * <tt> CodePointUtil.indexOf("0123αあいう", "あ", 0)</tt> は、「<b>5</b>」を返却します。<br/>
	 * <br/>
	 * 
	 * @param target 対象文字列
	 * @param str 検索する文字列
	 * @param fromCodePointIndex インデックス (最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント）
	 * @return 最初に str が現れる位置(最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント)。見つからない場合は -1 が返されます。
	 */
	public static int indexOf(final String target, final String str, final int fromCodePointIndex){
		
		int fromIndex = fromCodePointIndex;

		// サロゲートペア文字なし 
		if(!CodePointUtil.containsSurrogatePair(target)){
			return target.indexOf(str, fromIndex);
		}
		// サロゲートペア文字あり
		else{
			fromIndex = CodePointUtil.convCodePointIndex2Index(target, fromCodePointIndex);
			
			// indexOf()実行
			int foundIndex = target.indexOf(str, fromIndex);

			if(foundIndex == -1){
				return foundIndex;
			}
			else{
				int foundCodePointIndex = CodePointUtil.convIndex2CodePointIndex(target, foundIndex);
				return foundCodePointIndex;
			}
		}
	}


	/**
	 * 引数 target の 末尾から前方に検索し、最初に str が現れる位置を返します。見つからない場合は -1 を返します。<br/>
	 * サロゲートペア文字は１文字としてカウントされます。<br/>
	 * <br/>
	 * このメソッドを呼び出すことは、<br/>
	 * {@link CodePointUtil#convIndex2CodePointIndex(String, int)
	 * <tt>
	 * CodePointUtil.convIndex2CodePointIndex(target, target.lastIndexOf(str));
	 * <tt>}<br/>
	 * を呼び出すことに相当します。
	 * 
	 * @param target 対象文字列
	 * @param str 検索する文字列
	 * @return 末尾から前方に検索し、最初に str が現れる位置(最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント)。見つからない場合は -1 が返されます。
	 */
	public static int lastIndexOf(final String target, final String str){
		int index = target.lastIndexOf(str);
		return CodePointUtil.convIndex2CodePointIndex(target, index);
	}

	
	/**
	 * 引数 target の fromCodePointIndex番目（最初の文字を0番目とする）から前方に検索し、
	 * 最初に str が現れる位置を返します。見つからない場合は -1 を返します。<br/>
	 * サロゲートペア文字は１文字としてカウントされます。<br/>
	 * <br/>
	 * 例えば、「α」をサロゲートペア文字とすると、<br/>
	 * <tt>"0123αあいう".lastIndexOf("あ", 7)</tt>                 は、「<b>6</b>」を返却しますが、<br/>
	 * <tt> CodePointUtil.lastIndexOf("0123αあいう", "あ", 7)</tt> は、「<b>5</b>」を返却します。<br/>
	 * <br/>
	 * 
	 * @param target 対象文字列
	 * @param str 検索する文字列
	 * @param fromCodePointIndex  インデックス (最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント）
	 * @return 末尾から前方に検索し、最初に str が現れる位置(最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント)。見つからない場合は -1 が返されます。
	 */
	public static int lastIndexOf(final String target, final String str, final int fromCodePointIndex){
		
		int fromIndex = fromCodePointIndex;

		// サロゲートペア文字なし
		if(!CodePointUtil.containsSurrogatePair(target)){
			return target.lastIndexOf(str, fromIndex);
		}
		// サロゲートペア文字あり
		else{
			fromIndex = CodePointUtil.convCodePointIndex2Index(target, fromCodePointIndex);
			
			// lastIndexOf()実行
			int foundIndex = target.lastIndexOf(str, fromIndex);

			if(foundIndex == -1){
				return foundIndex;
			}
			else{
				int foundCodePointIndex = CodePointUtil.convIndex2CodePointIndex(target, foundIndex);
				return foundCodePointIndex;
			}
		}
		
	}

	
	/**
	 * サロゲートペア文字を<b>２文字</b>としてカウントするインデックスを、
	 * サロゲートペア文字を<b>１文字</b>としてカウントするインデックスに変換します。
	 * 
	 * @param target 対象文字列
	 * @param codePointIndex サロゲートペア文字を<b>２文字</b>としてカウントするインデックス
	 * @return サロゲートペア文字を<b>１文字</b>としてカウントするインデックス
	 */
	public static int convCodePointIndex2Index(final String target, final int codePointIndex) {

		int index = codePointIndex;
		
		for(int idx = 0; idx < index; idx++){

			char high = target.charAt(idx);
			char low  = ( idx + 1 < target.length() ) ? target.charAt(idx + 1) : 0;

			if(Character.isSurrogatePair(high, low)){
				index++;
				idx++;
			}
		}
		return index;
	}

	
	/**
	 * サロゲートペア文字を<b>１文字</b>としてカウントするインデックスを、
	 * サロゲートペア文字を<b>２文字</b>としてカウントするインデックスに変換します。
	 * 
	 * @param target 対象文字列
	 * @param index サロゲートペア文字を<b>１文字</b>としてカウントするインデックス
	 * @return サロゲートペア文字を<b>２文字</b>としてカウントするインデックス
	 */
	public static int convIndex2CodePointIndex(final String target, final int index) {
		
		int codePointIndex = index;
		
		for(int idx = 0; idx < index; idx++){

			char high = target.charAt(idx);
			char low  = ( idx + 1 < target.length() ) ? target.charAt(idx + 1) : 0;

			if(Character.isSurrogatePair(high, low)){
				codePointIndex--;
				idx++;
			}
		}
		return codePointIndex;
	}
	
	
	/**
	 * 引数 target の 「beginCodePointIndex」番目 から 末尾（最初の文字を0番目とする）の文字列を返します。
	 * サロゲートペア文字は１文字としてカウントされます。<br/>
	 * <br/>
	 * 
	 * @param target 対象文字列
	 * @param beginCodePointIndex 開始インデックス (この値を含む。最初の文字を0番目とし、サロゲートペア文字は１文字としてカウントします。）
	 * @return 指定された部分文字列
	 * 
	 * @throws IndexOutOfBoundsException 	<code>beginCodePointIndex</code> が負の値である場合、
	 * 										あるいは、<code>beginCodePointIndex</code> が target の文字列長より大きい場合
	 */
	public static String substring(final String target, final int beginCodePointIndex){
		return CodePointUtil.substring(target, beginCodePointIndex, CodePointUtil.length(target));
	}

	
	/**
	 * 引数 target の 「beginCodePointIndex」番目 から「endCodePointIndex - 1」番目（最初の文字を0番目とする）の文字列を返します。
	 * サロゲートペア文字は１文字としてカウントされます。<br/>
	 * <br/>
	 * 例えば、「α」をサロゲートペア文字とすると、<br/>
	 * <tt>"0123αあいう".substring(0, 6)</tt>                 は、「<b>0123α</b>」を返却しますが、<br/>
	 * <tt> CodePointUtil.substring("0123αあいう", 0, 6)</tt> は、「<b>0123αあ</b>」を返却します。<br/>
	 * <br/>
	 * 
	 * @param target 対象文字列
	 * @param beginCodePointIndex 開始インデックス (この値を含む。最初の文字を0番目とし、サロゲートペア文字は１文字としてカウントします。）
	 * @param endCodePointIndex 終了インデックス (この値を含まない。最初の文字を0番目とし、サロゲートペア文字は１文字としてカウントします。）
	 * @return 指定された部分文字列
	 * 
	 * @throws IndexOutOfBoundsException 	<code>beginCodePointIndex</code> が負の値である場合、
	 * 										<code>endCodePointIndex</code> が target の文字列長より大きい場合、
	 * 										あるいは <code>beginCodePointIndex</code> が <code>endCodePointIndex</code> より大きい場合
	 */
	public static String substring(final String target, final int beginCodePointIndex, final int endCodePointIndex){
		
		// サロゲートペア文字なし
		if(!CodePointUtil.containsSurrogatePair(target)){
			return target.substring(beginCodePointIndex, endCodePointIndex);
		}
		// サロゲートペア文字あり
		else{
			int beginIndex = CodePointUtil.convCodePointIndex2Index(target, beginCodePointIndex);
            int endIndex = CodePointUtil.convCodePointIndex2Index(target, endCodePointIndex);
			
			return target.substring(beginIndex, endIndex);
		}
	}

	/**
	 * 引数 target に、サロゲートペア文字が含まれているかを判定します。<br/>
	 * <br/>
	 * 
	 * 具体的には、引数 target に
	 * 「上位サロゲート」範囲 (&#x5c;uD800-&#x5c;uDBFF) または「下位サロゲート」範囲 (&#x5c;uDC00-&#x5c;uDFFF)の char が存在する場合は true、
	 * それ以外は、false を返却します。
	 * 
	 * @param target 対象文字列
	 * @return 引数 target に指定された文字列内に、サロゲートペア文字が含まれている場合はtrue、それ以外は false を返却します。
	 */
	public static boolean containsSurrogatePair(final String target){

		for(int idx = 0; idx < target.length(); idx++){
			char ch = target.charAt(idx);
			if(Character.isHighSurrogate(ch) || Character.isLowSurrogate(ch)){
				return true;
			}
		}
		
		return false;
	}
	
}
