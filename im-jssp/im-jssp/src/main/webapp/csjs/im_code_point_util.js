/**
 * 「サロゲートペア文字」を扱うためのユーティリティクラスです。
 *
 * @fileoverview 「サロゲートペア文字」を扱うためのユーティリティクラスです。
 *
 * @class 「サロゲートペア文字」を扱うためのユーティリティクラスです。<br/>
 * <br/>
 * 「サロゲートペア文字」とは、
 * Unicodeの U+FFFF よりも大きいコードポイントを持つ文字である「補助文字」のことを指しています。<br/>
 * <br/>
 * 言い換えると、<br/>
 * 「上位サロゲート」範囲 (&#x5c;uD800-&#x5c;uDBFF) からの最初の値と、<br/>
 * 「下位サロゲート」範囲 (&#x5c;uDC00-&#x5c;uDFFF) からの第２の値のペアで表現される文字のことを指しています。<br/>
 * <br/>
 * <br/>
 * <b>関連項目:</b><br/>
 * <a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/java/lang/Character.html#unicode">Unicode 文字表現(JDK 5.0 ドキュメント - Java 2 プラットフォーム API 仕様)</a>, 
 * <a href="http://ja.wikipedia.org/wiki/Unicode#.E3.82.B5.E3.83.AD.E3.82.B2.E3.83.BC.E3.83.88.E3.83.9A.E3.82.A2">サロゲートペア (Wikipedia)</a>, 
 * <a href="http://java.sun.com/developer/technicalArticles/Intl/Supplementary/index_ja.html">Java プラットフォームにおける補助文字のサポート(Sun Developer Network)</a>, 
 * <br/>
 *
 * @constructor
 *
 * @author INTRAMART
 *
 */
function ImCodePointUtil(){}


/**
 * UTF-16 エンコーディングでの Unicode 上位サロゲートコード単位の最小値。
 *
 * @final
 * @type Number
 */
ImCodePointUtil.CHAR_CODE_4_MIN_HIGH_SURROGATE = 0xD800;

/**
 * UTF-16 エンコーディングでの Unicode 上位サロゲートコード単位の最大値。
 *
 * @final
 * @type Number
 */
ImCodePointUtil.CHAR_CODE_4_MAX_HIGH_SURROGATE = 0xDBFF;

/**
 * UTF-16 エンコーディングでの Unicode 下位サロゲートコード単位の最小値。
 *
 * @final
 * @type Number
 */
ImCodePointUtil.CHAR_CODE_4_MIN_LOW_SURROGATE  = 0xDC00;

/**
 * UTF-16 エンコーディングでの Unicode 下位サロゲートコード単位の最大値。
 *
 * @final
 * @type Number
 */
ImCodePointUtil.CHAR_CODE_4_MAX_LOW_SURROGATE  = 0xDFFF;

/**
 * 補助コードポイントの最小値。
 *
 * @final
 * @type Number
 */
ImCodePointUtil.MIN_SUPPLEMENTARY_CODE_POINT   = 0x010000;




/**
 * @private
 * Unicode 上位サロゲートコード単位の最小値の文字列表現。（正規表現の文字列パターンでのみ利用します）
 *
 * @final
 * @type String
 */
ImCodePointUtil.STRING_4_MIN_HIGH_SURROGATE = String.fromCharCode(ImCodePointUtil.CHAR_CODE_4_MIN_HIGH_SURROGATE);

/**
 * @private
 * Unicode 上位サロゲートコード単位の最大値の文字列表現。（正規表現の文字列パターンでのみ利用します）
 *
 * @final
 * @type String
 */
ImCodePointUtil.STRING_4_MAX_HIGH_SURROGATE = String.fromCharCode(ImCodePointUtil.CHAR_CODE_4_MAX_HIGH_SURROGATE);

/**
 * @private
 * Unicode 下位サロゲートコード単位の最小値の文字列表現。（正規表現の文字列パターンでのみ利用します）
 *
 * @final
 * @type String
 */
ImCodePointUtil.STRING_4_MIN_LOW_SURROGATE  = String.fromCharCode(ImCodePointUtil.CHAR_CODE_4_MIN_LOW_SURROGATE);

/**
 * @private
 * Unicode 下位サロゲートコード単位の最大値の文字列表現。（正規表現の文字列パターンでのみ利用します）
 *
 * @final
 * @type String
 */
ImCodePointUtil.STRING_4_MAX_LOW_SURROGATE  = String.fromCharCode(ImCodePointUtil.CHAR_CODE_4_MAX_LOW_SURROGATE);

/**
 * @private
 * サロゲートペア文字が含まれているかを判定するために利用するRegExpオブジェクト。
 *
 * @final
 * @type RegExp
 */
ImCodePointUtil.SURROGATE_PAIR_REGEXP = new RegExp("");

ImCodePointUtil
    .SURROGATE_PAIR_REGEXP
        .compile("[" + ImCodePointUtil.STRING_4_MIN_HIGH_SURROGATE + "-" + ImCodePointUtil.STRING_4_MAX_HIGH_SURROGATE + "]" + 
                 "|" + 
                 "[" + ImCodePointUtil.STRING_4_MIN_LOW_SURROGATE  + "-" + ImCodePointUtil.STRING_4_MAX_LOW_SURROGATE  + "]",
                 "mi"); // gフラグ不可（∵正規表現での検索結果のindexを取得したいため）


/**
 * 引数 target に、サロゲートペア文字が含まれているかを判定します。<br/>
 * <br/>
 *
 * @param {String} target 対象文字列
 * @return 引数 target に指定された文字列内に、サロゲートペア文字が含まれている場合はtrue, それ以外は false を返却します。
 * @type Boolean
 */
ImCodePointUtil.containsSurrogatePair = function(target) {
	
	var idx = target.search(ImCodePointUtil.SURROGATE_PAIR_REGEXP);
	
	if(idx != -1){
		return true;
	}
	else{
		return false;
	}
}


/**
 * 引数 target の 文字数を返します。<br/>
 * サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * 例えば、「α」をサロゲートペア文字とすると、<br/>
 * <tt>  "0123αあいう".length</tt>                    は、「<b>9</b>」ですが、<br/>
 * <tt> ImCodePointUtil.getLength("0123αあいう")</tt> は、「<b>8</b>」を返却します。<br/>
 * <br/>
 * 
 * @param {String} target 対象文字列
 * @return 引数 target に指定された文字列の文字数
 * @type Number
 */
ImCodePointUtil.getLength = function(target){	
	return ImCodePointUtil.convIndex2CodePointIndex(target, target.length);
}

/**
 * 引数 target の codePointIndex番目の文字を返します。<br/>
 * 最初の文字を0番目とします。また、サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * 例えば、「α」をサロゲートペア文字とすると、<br/>
 * <tt>  "0123αあいう".charAt(4)</tt>                 は、<b><u>「α」</u>の「上位サロゲート範囲の値の文字（文字化けが発生します）」</b>を返却しますが、<br/>
 * <tt> ImCodePointUtil.charAt("0123αあいう", 4)</tt> は、<b><u>「α」</u></b>を返却します。<br/>
 * <br/>
 * <tt>  "0123αあいう".charAt(5)</tt>                 は、<b><u>「α」</u>の「下位サロゲート範囲の値の文字（文字化けが発生します）」</b>を返却しますが、<br/>
 * <tt> ImCodePointUtil.charAt("0123αあいう", 5)</tt> は、<b><u>「あ」</u></b>を返却します。<br/>
 * <br/>
 * 
 * @param {String} target 対象文字列
 * @param {Number} codePointIndex インデックス（最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント）
 * @return 指定された位置の文字
 * @type String
 */
ImCodePointUtil.charAt = function(target, codePointIndex){
	var index = codePointIndex;
	
	// サロゲートペア文字なし 
	if(!ImCodePointUtil.containsSurrogatePair(target)){
		return target.charAt(index);
	}
	
	// サロゲートペア文字あり
	else{
		index = ImCodePointUtil.convCodePointIndex2Index(target, codePointIndex);
		
		var high = target.charCodeAt(index);
		var low  = ( index + 1 < target.length ) ? target.charCodeAt(index + 1) : 0;
		
		if(!ImCodePointUtil.isSurrogatePair(high, low)){
			return String.fromCharCode(high);
		}
		else{
			return String.fromCharCode(high, low);
		}
	}
}


/**
 * 引数 target の codePointIndex番目の文字(Unicode コードポイント)を返します。<br/>
 * 最初の文字を0番目とします。また、サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * 例えば、「α」をサロゲートペア文字とすると、<br/>
 * <tt>  "0123αあいう".charCodeAt(4)</tt>                 は、<b><u>「α」</u>の「上位サロゲート範囲の値」</b>を返却しますが、<br/>
 * <tt> ImCodePointUtil.charCodeAt("0123αあいう", 4)</tt> は、<b><u>「α」</u>の「Unicode コードポイント」</b>を返却します。<br/>
 * <br/>
 * <tt>  "0123αあいう".charCodeAt(5)</tt>                 は、<b><u>「α」</u>の「下位サロゲート範囲の値」</b>を返却しますが、<br/>
 * <tt> ImCodePointUtil.charCodeAt("0123αあいう", 5)</tt> は、<b><u>「あ」</u>の「Unicode コードポイント」</b>を返却します。<br/>
 * <br/>
 * 
 * @param {String} target 対象文字列
 * @param {Number} codePointIndex インデックス（最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント）
 * @return 指定された位置の文字 (Unicode コードポイント)
 * @type Number
 */
ImCodePointUtil.charCodeAt = function(target, codePointIndex){
	var index = codePointIndex;

	// サロゲートペア文字なし 
	if(!ImCodePointUtil.containsSurrogatePair(target)){
		return target.charCodeAt(index);
	}
	
	// サロゲートペア文字あり
	else{
		index = ImCodePointUtil.convCodePointIndex2Index(target, codePointIndex);
		
		var high = target.charCodeAt(index);
		var low  = ( index + 1 < target.length ) ? target.charCodeAt(index + 1) : 0;

		if(!ImCodePointUtil.isSurrogatePair(high, low)){
			return high;
		}
		else{
			return ImCodePointUtil.toCodePoint(high, low);
		}
	}
}


/**
 * 文字コード num1, num2, ..., numN で表される文字列を返します。<br/>
 * 引数には、補助コードポイント（Unicodeの U+FFFF よりも大きいコードポイント）を指定することが可能です。<br/>
 * <br/>
 * 例えば、<br/>
 * <tt>String.fromCharCode(0x61, 0x62, 0x63, 0x2000B, 0x2123D, 0x20B9F)</tt> は、文字化けが発生しますが、<br/>
 * <br/>
 * <tt>ImCodePointUtil.fromCharCode(0x61, 0x62, 0x63, 0x2000B, 0x2123D, 0x20B9F)</tt> は、
 * <b>文字列<u>「&#x61;&#x62;&#x63;&#x2000B;&#x2123D;&#x20B9F;」</u></b>を返却します。<br/>
 * <br/>
 * 
 * @param {Number} num1[, num2, ..., numN] 文字コード
 * @return 文字コード num1[, num2, ..., numN] で表される文字列
 * @type String
 */
ImCodePointUtil.fromCharCode = function(num1, num2, numN){
	
	var result = [];
	
	for(var idx = 0, max = arguments.length; idx < max; idx++){
		var target = arguments[idx];
		    target -= 0x10000;
		
		var high  = Math.floor(target / 0x400);
		    high += ImCodePointUtil.CHAR_CODE_4_MIN_HIGH_SURROGATE;

		var low   = target % 0x400;
		    low  += ImCodePointUtil.CHAR_CODE_4_MIN_LOW_SURROGATE;

		// サロゲートペア文字ではない 
		if(!ImCodePointUtil.isSurrogatePair(high, low)){
			result.push( String.fromCharCode(arguments[idx]) );
		}
		// サロゲートペア文字である
		else{
			result.push( String.fromCharCode(high, low)      );
		}
	}
	
	return result.join("");
}


/**
 * 引数 target の fromCodePointIndex番目（最初の文字を0番目とする）から後方に検索し、
 * 最初に str が現れる位置を返します。見つからない場合は -1 を返します。<br/>
 * サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * 例えば、「α」をサロゲートペア文字とすると、<br/>
 * <tt>  "0123αあいう".indexOf("あ", 0)</tt>                 は、「<b>6</b>」を返却しますが、<br/>
 * <tt> ImCodePointUtil.indexOf("0123αあいう", "あ", 0)</tt> は、「<b>5</b>」を返却します。<br/>
 * <br/>
 * 
 * @param {String} target 対象文字列
 * @param {String} str 検索する文字列
 * @param {Number} fromCodePointIndex [option] インデックス（最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント）
 * 											  省略した場合は、0。
 * @return 最初に str が現れる位置(最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント)。見つからない場合は -1 が返されます。
 * @type Number
 */
ImCodePointUtil.indexOf = function(target, str, fromCodePointIndex){
	
	var fromIndex = (fromCodePointIndex == undefined) ? 0 : fromCodePointIndex;

	// サロゲートペア文字なし 
	if(!ImCodePointUtil.containsSurrogatePair(target)){
		return target.indexOf(str, fromIndex);
	}
	
	// サロゲートペア文字あり
	else{
		fromIndex = ImCodePointUtil.convCodePointIndex2Index(target, fromCodePointIndex);
		
		// indexOf()実行
		var foundIndex = target.indexOf(str, fromIndex);

		if(foundIndex == -1){
			return foundIndex;
		}
		else{
			var foundCodePointIndex = ImCodePointUtil.convIndex2CodePointIndex(target, foundIndex);
			return foundCodePointIndex;
		}
	}
}


/**
 * 引数 target の fromCodePointIndex番目（最初の文字を0番目とする）から前方に検索し、
 * 最初に str が現れる位置を返します。見つからない場合は -1 を返します。<br/>
 * サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * 例えば、「α」をサロゲートペア文字とすると、<br/>
 * <tt>  "0123αあいう".lastIndexOf("あ", 7)</tt>                 は、「<b>6</b>」を返却しますが、<br/>
 * <tt> ImCodePointUtil.lastIndexOf("0123αあいう", "あ", 7)</tt> は、「<b>5</b>」を返却します。<br/>
 * <br/>
 * 
 * @param {String} target 対象文字列
 * @param {String} str 検索する文字列
 * @param {Number} fromCodePointIndex [option] インデックス（最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント）
 *											  省略した場合は、target の文字数。
 * @return 末尾から前方に検索し、最初に str が現れる位置(最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント)。見つからない場合は -1 が返されます。
 * @type Number
 */
ImCodePointUtil.lastIndexOf = function (target, str, fromCodePointIndex){

	// サロゲートペア文字なし
	if(!ImCodePointUtil.containsSurrogatePair(target)){
		var fromIndex = (fromCodePointIndex == undefined) ? target.length : fromCodePointIndex;

		// lastIndexOf()実行
		return target.lastIndexOf(str, fromIndex);
	}
	
	// サロゲートペア文字あり
	else{
		var fromIndex = (fromCodePointIndex == undefined) ? target.length : ImCodePointUtil.convCodePointIndex2Index(target, fromCodePointIndex);
		
		// lastIndexOf()実行
		var foundIndex = target.lastIndexOf(str, fromIndex);

		if(foundIndex == -1){
			return foundIndex;
		}
		else{
			var foundCodePointIndex = ImCodePointUtil.convIndex2CodePointIndex(target, foundIndex);
			return foundCodePointIndex;
		}
	}
	
}

/**
 * サロゲートペア文字を<b>２文字</b>としてカウントするインデックスを、
 * サロゲートペア文字を<b>１文字</b>としてカウントするインデックスに変換します。
 * 
 * @param {String} target 対象文字列
 * @param {Number} codePointIndex サロゲートペア文字を<b>２文字</b>としてカウントするインデックス
 * @return サロゲートペア文字を<b>１文字</b>としてカウントするインデックス
 * @type Number
 */
ImCodePointUtil.convCodePointIndex2Index = function(target, codePointIndex) {

	var index  = codePointIndex;
	
	var result = ImCodePointUtil.getSurrogatePairStringInfo(target);
	
	for(var i = 0, max = result.length; i < max; i++){
		if(index <= result[i].index){
			break;
		}
		else{
			index++;
		}
		
	}
	
	return index;	
}

/**
 * サロゲートペア文字を<b>１文字</b>としてカウントするインデックスを、
 * サロゲートペア文字を<b>２文字</b>としてカウントするインデックスに変換します。
 * 
 * @param {String} target 対象文字列
 * @param {Number} index サロゲートペア文字を<b>１文字</b>としてカウントするインデックス
 * @return サロゲートペア文字を<b>２文字</b>としてカウントするインデックス
 * @type Number
 */
ImCodePointUtil.convIndex2CodePointIndex = function(target, index) {

	var codePointIndex = index;
	
	var result = ImCodePointUtil.getSurrogatePairStringInfo(target);
	
	for(var i = 0, max = result.length; i < max; i++){
		if(index <= result[i].index){
			break;
		}
		else{
			codePointIndex--;
		}
	}

	return codePointIndex;
}


/**
 * 引数 target の 「beginCodePointIndex」番目 から「endCodePointIndex - 1」番目（最初の文字を0番目とする）の文字列を返します。<br/>
 * beginCodePointIndex に負の値を指定すると 0 番目と見なされます。endCodePointIndex を省略すると残りのすべてを返します。<br/>
 * beginCodePointIndex が endCodePointIndex より大きいならば、それらは交換されます。<br/>
 * サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * 例えば、「α」をサロゲートペア文字とすると、<br/>
 * <tt>  "0123αあいう".substring(0, 6)</tt>                 は、「<b>0123α</b>」を返却しますが、<br/>
 * <tt> ImCodePointUtil.substring("0123αあいう", 0, 6)</tt> は、「<b>0123αあ</b>」を返却します。<br/>
 * <br/>
 * 
 * @param {String} target 対象文字列
 * @param {Number} beginCodePointIndex 開始インデックス（この値を含む。最初の文字を0番目とし、サロゲートペア文字は１文字としてカウントします。）
 * @param {Number} endCodePointIndex [option] 終了インデックス（この値を含まない。最初の文字を0番目とし、サロゲートペア文字は１文字としてカウントします。）
 * 											 省略した場合は、target の文字数。
 * @return 指定された部分文字列
 * @type String
 */
ImCodePointUtil.substring = function(target, beginCodePointIndex, endCodePointIndex){
	endCodePointIndex = (endCodePointIndex == undefined) ? ImCodePointUtil.convCodePointIndex2Index(target, target.length) : endCodePointIndex;
	
	// 15.5.4.15 String.prototype.substring (start, end)
	//    → start が end より大きいならば、それらは交換される。
	if(beginCodePointIndex < endCodePointIndex){
		var temp = beginCodePointIndex;
		beginCodePointIndex = endCodePointIndex;
		endCodePointIndex = temp;
	}
	
	// サロゲートペア文字なし
	if(!ImCodePointUtil.containsSurrogatePair(target)){
		return target.substring(beginCodePointIndex, endCodePointIndex);
	}
	
	// サロゲートペア文字あり
	else{
		var beginIndex = ImCodePointUtil.convCodePointIndex2Index(target, beginCodePointIndex);
		var endIndex   = ImCodePointUtil.convCodePointIndex2Index(target, endCodePointIndex);
		
		return target.substring(beginIndex, endIndex);
	}
}


/**
 * 引数 target の 「beginCodePointIndex」番目 から「endCodePointIndex - 1」番目（最初の文字を0番目とする）の文字列を返します。<br/>
 * beginCodePointIndex に負の値を指定すると後ろから数えます。endCodePointIndex を省略すると残りのすべてを返します。<br/>
 * サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * 例えば、「α」をサロゲートペア文字とすると、<br/>
 * <tt>  "0123αあいう".slice(0, 6)</tt>                 は、「<b>0123α</b>」を返却しますが、<br/>
 * <tt> ImCodePointUtil.slice("0123αあいう", 0, 6)</tt> は、「<b>0123αあ</b>」を返却します。<br/>
 * <br/>
 * 
 * @param {String} target 対象文字列
 * @param {Number} beginCodePointIndex 開始インデックス（この値を含む。最初の文字を0番目とし、サロゲートペア文字は１文字としてカウントします。）
 * @param {Number} endCodePointIndex [option] 終了インデックス（この値を含まない。最初の文字を0番目とし、サロゲートペア文字は１文字としてカウントします。）
 * 											 省略した場合は、target の文字数。
 * @return 指定された部分文字列
 * @type String
 */
ImCodePointUtil.slice = function(target, beginCodePointIndex, endCodePointIndex){

	// サロゲートペア文字なし
	if(!ImCodePointUtil.containsSurrogatePair(target)){
		if(endCodePointIndex == undefined){
			return target.slice(beginCodePointIndex);
		}
		else{
			return target.slice(beginCodePointIndex, endCodePointIndex);
		}
	}
	
	// サロゲートペア文字あり
	else{
        if (beginCodePointIndex != undefined || endCodePointIndex != undefined) {
	        var begin = beginCodePointIndex;
            var end;
            var length = ImCodePointUtil.getLength(target);
            if (begin < 0) {
                begin += length;
                if (begin < 0)
                    begin = 0;
            } else if (begin > length) {
                begin = length;
            }

            if (endCodePointIndex == undefined) {
                end = length;
            } else {
                end = endCodePointIndex;
                if (end < 0) {
                    end += length;
                    if (end < 0)
                        end = 0;
                } else if (end > length) {
                    end = length;
                }
                if (end < begin)
                    end = begin;
            }

	        return ImCodePointUtil.substring(target, begin, end);
        }
        return target;
	}	

}

/**
 * 引数 target の 「beginCodePointIndex」番目（最初の文字を0番目とする）から「len」文字分の文字列を返します。<br/>
 * beginCodePointIndex に負の値を指定すると後ろから数えます。len を省略すると残りのすべてを返します。<br/>
 * サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * 例えば、「α」をサロゲートペア文字とすると、<br/>
 * <tt>  "0123αあいう".substr(0, 6)</tt>                 は、「<b>0123α</b>」を返却しますが、<br/>
 * <tt> ImCodePointUtil.substr("0123αあいう", 0, 6)</tt> は、「<b>0123αあ</b>」を返却します。<br/>
 * <br/>
 * 
 * @param {String} target 対象文字列
 * @param {Number} beginCodePointIndex 開始インデックス（この値を含む。最初の文字を0番目とし、サロゲートペア文字は１文字としてカウントします。）
 * @param {Number} len [option] 取得する文字数（サロゲートペア文字は１文字としてカウントします。）
 * 											 省略した場合は、target の文字数。
 * @return 指定された部分文字列
 * @type String
 */
ImCodePointUtil.substr = function(target, beginCodePointIndex, len){
	
	// サロゲートペア文字なし
	if(!ImCodePointUtil.containsSurrogatePair(target)){

		// IE の場合
		if( !!(window.attachEvent && !window.opera) ){
	        var begin = beginCodePointIndex;
	        var length = target.length;
	        	
	        if (begin < 0) {
	            begin += length;
	            if (begin < 0)
	                begin = 0;
	        } else if (begin > length) {
	            begin = length;
	        }
	
			return target.substr(begin, len);
		}
		else{
			if(len == undefined){
		        return target.substr(beginCodePointIndex);
			}
			else{
				return target.substr(beginCodePointIndex, len);
			}
		}
	}
	
	// サロゲートペア文字あり
	else{
        var begin = beginCodePointIndex;
        var end;
        var length = ImCodePointUtil.getLength(target);

        if (begin < 0) {
            begin += length;
            if (begin < 0)
                begin = 0;
        } else if (begin > length) {
            begin = length;
        }

        if (len == undefined) {
            end = length;
        } else {
            end = len;
            if (end < 0)
                end = 0;
            end += begin;
            if (end > length)
                end = length;
        }

        return ImCodePointUtil.substring(target, begin, end);
	}
}


/**
 * 引数 target から regexp にマッチする部分の位置を返します。<br/>
 * 見つからない場合は -1 を返します。<br/>
 * サロゲートペア文字は１文字としてカウントされます。<br/>
 * 
 * @param {String} target 対象文字列
 * @param {RegExp} regexp 正規表現オブジェクト
 * @return regexp にマッチする部分の位置(最初の文字を0番目とし、サロゲートペア文字は１文字としてカウント)。見つからない場合は -1 が返されます。
 * @type Number
 */
ImCodePointUtil.search = function(target, regexp){
	var index = target.search(regexp);
	
	if(index == -1 || !ImCodePointUtil.containsSurrogatePair(target)){
		return index;
	}
	else{
		return ImCodePointUtil.convIndex2CodePointIndex(target, index);
	}
}


/**
 * 引数 target から regexp にマッチした部分の文字列の情報を返します。<br/>
 * <br/>
 * 見つからない場合は null を返します。<br/>
 * サロゲートペア文字は１文字としてカウントされます。<br/>
 * <br/>
 * regexp にマッチした部分の文字列の情報は、ブラウザによって異なります。
 * 詳しくは、JavaScriptの正規表現に関するリファレンスを参照してください。
 * 
 * @param {String} target 対象文字列
 * @param {RegExp} regexp 正規表現オブジェクト
 * @return regexp にマッチした部分の文字列情報。見つからない場合は null を返します。
 * @type Object/Array
 */
ImCodePointUtil.match = function(target, regexp){
	var result = target.match(regexp);
	
	if(result != null && ImCodePointUtil.containsSurrogatePair(target) && result.index != undefined){
		result.index = ImCodePointUtil.convIndex2CodePointIndex(target, result.index);
	}

	return result;
}


/**
 * 指定されたサロゲートペアをその補助コードポイント値に変換します。<br/>
 * この関数は、指定されたサロゲートペアを検証しません。<br/>
 * 呼び出し元は、必要に応じて、isSurrogatePair を使ってサロゲートペアを検証する必要があります。 
 *
 * @param {Number} high 上位サロゲートコード単位
 * @param {Number} low 下位サロゲートコード単位
 * @return 指定されたサロゲートペアから作成された補助コードポイント
 * @type Number
 */
ImCodePointUtil.toCodePoint = function (high, low) {
	return ( (high - ImCodePointUtil.CHAR_CODE_4_MIN_HIGH_SURROGATE) << 10)
			+
			 (low  - ImCodePointUtil.CHAR_CODE_4_MIN_LOW_SURROGATE )
			+
			ImCodePointUtil.MIN_SUPPLEMENTARY_CODE_POINT;
}


/**
 * 指定された値のペアが有効なサロゲートペアであるかどうかを判定します。<br/>
 * このメソッドの呼び出しは以下の式と等価です。<br/>
 * <tt>ImCodePointUtil.isHighSurrogate(high) && ImCodePointUtil.isLowSurrogate(low)</tt>
 *
 * @param {Number} high 判定対象の上位サロゲートコード値
 * @param {Number} low 判定対象の下位サロゲートコード値 
 * @return 指定された上位/下位サロゲートコード値が有効なサロゲートペアを表す場合は true、そうでない場合は false。
 * @type Boolean
 */
ImCodePointUtil.isSurrogatePair = function(high, low) {
	return ImCodePointUtil.isHighSurrogate(high) && ImCodePointUtil.isLowSurrogate(low);
}


/**
 * 指定された値が上位サロゲートコード単位であるかどうかを判定します。<br/>
 * これらの値は、それ自体で文字を表しませんが、UTF-16エンコーディングの補助文字の表現で使用されます。
 *
 * @param {Number} high 判定対象の値 
 * @return 値が「0xD800」から「0xDBFF」までの範囲にある場合は true、そうでない場合は false。
 * @type Boolean
 */
ImCodePointUtil.isHighSurrogate = function(high){
	return ImCodePointUtil.CHAR_CODE_4_MIN_HIGH_SURROGATE <= high && high <= ImCodePointUtil.CHAR_CODE_4_MAX_HIGH_SURROGATE;
}


/**
 * 指定された値が下位サロゲートコード単位であるかどうかを判定します。<br/>
 * これらの値は、それ自体で文字を表しませんが、UTF-16エンコーディングの補助文字の表現で使用されます。
 *
 * @param {Number} high 判定対象の値 
 * @return 値が「0xDC00」から「0xDFFF」までの範囲にある場合は true、そうでない場合は false。
 * @type Boolean
 */
ImCodePointUtil.isLowSurrogate = function(low) {
	return ImCodePointUtil.CHAR_CODE_4_MIN_LOW_SURROGATE  <= low  && low  <= ImCodePointUtil.CHAR_CODE_4_MAX_LOW_SURROGATE;
}


/**
 * @private
 * 引数 target に含まれるサロゲートペア文字の情報を格納した配列を返却します。<br/>
 * 具体的には、以下の形式のオブジェクトを要素とした配列が返却されます。<br/>
 *
 * <pre>
 * /&#x2A; Object &#x2A;/
 * {
 *     /&#x2A; Number &#x2A;/
 *     "index" : 99,  // サロゲートペア文字が出現するインデックス値
 * 
 *     /&#x2A; String &#x2A;/
 *     "value" : "α" // サロゲートペア文字
 * }
 * </pre>
 * 
 * 上記オブジェクトの「index」プロパティは、サロゲートペア文字は<b>２文字</b>としてカウントされています。
 * つまり、通常のインデックスです。<br/>
 * <br/>
 * 引数 target にサロゲートペア文字が含まれない場合は、空の配列が返却されます。
 *
 * @param {String} target 対象文字列
 * @return 引数 target に含まれるサロゲートペア文字の情報を格納した配列
 * @type Array
 */
ImCodePointUtil.getSurrogatePairStringInfo = function (target) {
	
	var ary = new Array();

	var index = 0;
	var max = target.length;
	
	while(index < max){
		var matchResult = ImCodePointUtil.getMatchResult4SurrogatePair(target, index);
		
		if(matchResult == null){
			break;
		}
		else{
			ary.push(matchResult);
			index = matchResult.index + 2;
		}
	}
	
	return ary;
}


/**
 * @private
 * 引数 target の beginIndex 以降に含まれる最初のサロゲートペア文字の情報を返却します。<br/>
 * 具体的には、以下の形式のオブジェクトが返却されます。<br/>
 *
 * <pre>
 * /&#x2A; Object &#x2A;/
 * {
 *     /&#x2A; Number &#x2A;/
 *     "index" : 99,  // サロゲートペア文字が出現するインデックス値
 * 
 *     /&#x2A; String &#x2A;/
 *     "value" : "α" // サロゲートペア文字
 * }
 * </pre>
 * 
 * 上記オブジェクトの「index」プロパティは、サロゲートペア文字は<b>２文字</b>としてカウントされています。
 * つまり、通常のインデックスです。<br/>
 * <br/>
 * 引数 target にサロゲートペア文字が含まれない場合は、null が返却されます。
 *
 * @param {String} targetString 対象文字列
 * @param {Number} beginIndex インデックス（サロゲートペア文字は<b>２文字</b>としてカウントされる＝通常のインデックス）
 * @return 引数 target の beginIndex 以降に含まれる最初のサロゲートペア文字の情報
 * @type Object
 */
ImCodePointUtil.getMatchResult4SurrogatePair = function (targetString, beginIndex){

	var sub = targetString.substring(beginIndex);

	// 上位サロゲートコードの出現位置を検索
	var result = sub.match(ImCodePointUtil.SURROGATE_PAIR_REGEXP);
	
	if(result == null){
		return null;
	}
	else{
		var high  = result[0].charCodeAt(0);
		var index = result["index"];

		if( index + 1 < sub.length ){
			var low = sub.charCodeAt(index + 1);

			return { 
				"index" : beginIndex + index,
				"value" : String.fromCharCode(high, low)
			};
			
		}
		else{
			throw new Error("Unicode high-surrogate code and low-surrogate code have to be sequential.");
		}
	}

}