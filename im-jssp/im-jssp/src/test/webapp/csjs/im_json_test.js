/**
 * @fileoverview ImJsonのテストケース
 */

/**
 *
 */
function testToJSONString001_JSON文字列のエスケープ文字対応() {
	var input = "\",\\,\\/,\\b,\\f,\\n,\\r,\\t,\\u";
	var expected = "\"\\\",\\\\,\\\\/,\\\\b,\\\\f,\\\\n,\\\\r,\\\\t,\\\\u\"";

	debug("ImJson.toJSONString(input)", ImJson.toJSONString(input));
	debug("expected", expected);

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString002_JSON文字列のエスケープ文字対応() {
	var input = '["im-jssp-rpc\\\"a\\\""]';
	var expected = "\"[\\\"im-jssp-rpc\\\\\\\"a\\\\\\\"\\\"]\"";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString003_JSON文字列のエスケープシーケンスされていない文字対応() {
	var input = "\,\/,\b,\f,\n,\r,\t,\u";
	var expected = "\",/,\\b,\\f,\\n,\\r,\\t,u\"";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString004_JSON文字列の16進対応() {
	var input = "\u005C\u002C\u005C\u002F\u002C\u005C\u0062\u002C\u005C\u0066\u002C\u005C\u006E\u002C\u005C\u0072\u002C\u005C\u0074\u002C\u005C\u0075";
	var expected = "\"\\\\,\\\\/,\\\\b,\\\\f,\\\\n,\\\\r,\\\\t,\\\\u\"";

	assertEquals("NG", expected, ImJson.toJSONString(input) );
}

/**
 *
 */
function testToJSONString005_JSON文字列のダブルクォート() {
	var input = '["im-jssp-rpc\"a\""]';
	var expected = "\"[\\\"im-jssp-rpc\\\"a\\\"\\\"]\"";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString006_JSON文字列のダブルクォート() {
	var input = '{"roles" : [], "departments" : [], "posts" : [], "publicGroups" : []}';
	var expected = "\"{\\\"roles\\\" : [], \\\"departments\\\" : [], \\\"posts\\\" : [], \\\"publicGroups\\\" : []}\"";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString007_JSON文字列変換() {

	var input = new Object();
	    input.ary = new Array();
	    input.ary2= new Array('["im-jssp-rpc\\\"a\\\""]', '["im-jssp-rpc\"a\""]');
	    input.str = "string";
	    input.num = -1;
	    input.bol = true;
	    input.obj = new Object();
	    input.obj.ary = new Array();
	    input.obj.ary2= new Array("\",\\,\\/,\\b,\\f,\\n,\\r,\\t,\\u", "\,\/,\b,\f,\n,\r,\t,\u");
	    input.obj.str = "string1";
	    input.obj.num = 0;
	    input.obj.bol = false;

	var expected = "{\"ary\" : [], \"ary2\" : [\"[\\\"im-jssp-rpc\\\\\\\"a\\\\\\\"\\\"]\", \"[\\\"im-jssp-rpc\\\"a\\\"\\\"]\"], \"str\" : \"string\", \"num\" : -1, \"bol\" : true, \"obj\" : {\"ary\" : [], \"ary2\" : [\"\\\",\\\\,\\\\/,\\\\b,\\\\f,\\\\n,\\\\r,\\\\t,\\\\u\", \",/,\\b,\\f,\\n,\\r,\\t,u\"], \"str\" : \"string1\", \"num\" : 0, \"bol\" : false}}";

	debug("ImJson.toJSONString(input)", ImJson.toJSONString(input));
	debug("expected", expected);

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString008_nullの場合() {
	var input = null;
	var expected = "null";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString009_undefinedの場合() {
	var input = undefined;
	var expected = "undefined";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString010_文字列の場合() {
	var input = "test_文字列㈱①～";
	var expected = "\"test_文字列㈱①～\"";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString011_数値の場合() {
	// 数値

	var input = 0;
	var expected = "0";
	assertEquals("0", expected, ImJson.toJSONString(input));

	input = 1234567890;
	expected = "1234567890";
	assertEquals("正の整数", expected, ImJson.toJSONString(input));

	input = -1234567890;
	expected = "-1234567890";
	assertEquals("負の整数", expected, ImJson.toJSONString(input));

	input = 123.456789;
	expected = "123.456789";
	assertEquals("正の少数", expected, ImJson.toJSONString(input));

	input = -123.456789;
	expected = "-123.456789";
	assertEquals("負の少数", expected, ImJson.toJSONString(input));

	input = 1.23e4;
	expected = "12300";
	assertEquals("正のX乗：1.23 × 10の4乗", expected, ImJson.toJSONString(input));

	input = 1.23E4;
	expected = "12300";
	assertEquals("正のX乗：1.23 × 10の4乗", expected, ImJson.toJSONString(input));

	input = -1.23e3;
	expected = "-1230";
	assertEquals("負のX乗：-1.23 × 10の3乗", expected, ImJson.toJSONString(input));

	input = -1.23E3;
	expected = "-1230";
	assertEquals("負のX乗：-1.23 × 10の3乗", expected, ImJson.toJSONString(input));

	input = 0777;
	expected = "511";
	assertEquals("8進数", expected, ImJson.toJSONString(input));

	input = 0xff88;
	expected = "65416";
	assertEquals("16進数", expected, ImJson.toJSONString(input));

	input = Number.MAX_VALUE;
	expected = "1.7976931348623157e+308";
	assertEquals("定数：Number.MAX_VALUE", expected, ImJson.toJSONString(input));

	input = Number.MIN_VALUE;
	expected = "5e-324";
	assertEquals("定数：Number.MIN_VALUE", expected, ImJson.toJSONString(input));

	input = Number.POSITIVE_INFINITY;
	expected = "Infinity";
	assertEquals("定数：Number.POSITIVE_INFINITY", expected, ImJson.toJSONString(input));

	input = Number.NEGATIVE_INFINITY;
	expected = "-Infinity";
	assertEquals("定数：Number.NEGATIVE_INFINITY", expected, ImJson.toJSONString(input));

	input = Number.NaN;
	expected = "NaN";
	assertEquals("定数：Number.NaN", expected, ImJson.toJSONString(input));

	input = Infinity;
	expected = "Infinity";
	assertEquals("定数：Infinity", expected, ImJson.toJSONString(input));

	input = -Infinity;
	expected = "-Infinity";
	assertEquals("定数：-Infinity", expected, ImJson.toJSONString(input));

	input = Number.NaN;
	expected = "NaN";
	assertEquals("定数：NaN", expected, ImJson.toJSONString(input));

	input = Math.PI;
	expected = "3.141592653589793";
	assertEquals("定数：Math.PI;", expected, ImJson.toJSONString(input));

	input = Math.SQRT2;
	expected = "1.4142135623730951";
	assertEquals("定数：Math.SQRT2;", expected, ImJson.toJSONString(input));

	input = Math.SQRT1_2;
	expected = "0.7071067811865476";
	assertEquals("定数：Math.SQRT1_2;", expected, ImJson.toJSONString(input));

	input = Math.E;
	expected = "2.718281828459045";
	assertEquals("定数：Math.E;", expected, ImJson.toJSONString(input));

	input = Math.LN2;
	expected = "0.6931471805599453";
	assertEquals("定数：Math.LN2;", expected, ImJson.toJSONString(input));

	input = Math.LN10;
	expected = "2.302585092994046";
	assertEquals("定数：Math.LN10;", expected, ImJson.toJSONString(input));

	input = Math.LOG2E;
	if(window.navigator.userAgent.indexOf("MSIE") != -1){
		expected = "1.4426950408889633";
	}
	else{
		expected = "1.4426950408889634";
	}
	assertEquals("定数：Math.LOG2E;", expected, ImJson.toJSONString(input));

	input = Math.LOG10E;
	expected = "0.4342944819032518";
	assertEquals("定数：Math.LOG10E;", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString012_真偽値の場合() {
	var input = true;
	var expected = "true";

	assertEquals("NG", expected, ImJson.toJSONString(input));

	input = false;
	expected = "false";

	assertEquals("NG", expected, ImJson.toJSONString(input));

}

/**
 *
 */
function testToJSONString013_関数の場合() {
	var input = function(){ return "テスト用関数です。"; };
	var expected = "";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}


/**
 *
 */
function testToJSONString014_オブジェクトの場合() {
	var input = {
		prop_null      : null,
		prop_undefined : undefined,
		prop_string    : "文字列",
		prop_number    : 1234567890,
		prop_boolean   : false,
		prop_function  : function(){ return "テスト関数"; },
		prop_object    : {
			inner_obj_string : "テスト",
			inner_obj_number : -12345
		},
		prop_array     : [
			"テスト",
			-12345
		]
	}

	debug("ImJson.toJSONString(input)", ImJson.toJSONString(input));

	var expected = "{\"prop_null\" : null, \"prop_undefined\" : undefined, \"prop_string\" : \"文字列\", \"prop_number\" : 1234567890, \"prop_boolean\" : false, \"prop_object\" : {\"inner_obj_string\" : \"テスト\", \"inner_obj_number\" : -12345}, \"prop_array\" : [\"テスト\", -12345]}";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}


/**
 *
 */
function testToJSONString015_配列の場合() {
	var input = [
		null,
		undefined,
		"文字列",
		1234567890,
		false,
		function(){ return "テスト関数"; },
		{
			inner_obj_string : "テスト",
			inner_obj_number : -12345
		},
		[
			"テスト",
			-12345
		]
	]
	debug("ImJson.toJSONString(input)", ImJson.toJSONString(input));

	var expected = "[null, undefined, \"文字列\", 1234567890, false, {\"inner_obj_string\" : \"テスト\", \"inner_obj_number\" : -12345}, [\"テスト\", -12345]]";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString016_エスケープ文字の場合() {
	var input = new Array();
	input[0]  = "\n";     // \n - ニューライン（改行文字）
	input[1]  = "\f";     // \f - フォームフィード
	input[2]  = "\b";     // \b - バックスペース
	input[3]  = "\r";     // \r - キャリッジリターン（復帰文字）
	input[4]  = "\t";     // \t - タブ文字
	input[5]  = "\'";     // \' - シングルクォート（'）
	input[6]  = "\"";     // \" - ダブルクォート（"）
	input[7]  = "\\";     // \\ - バックスラッシュ（\）
	input[8]  = "\101";   //  - 8進\nnn - 8進数による文字コード指定（例えば "A" は "\101"）
	input[9]  = "\x41";   //  - 16\xnn - 16進数による文字コード指定（例えば "A" は "\x41"）
	input[10] = "\u3042"; //  - \unnnn - Unicode文字（例えば "あ" は "\u3042"）

	debug("ImJson.toJSONString(input)", ImJson.toJSONString(input));

	var expected = "[\"\\n\", \"\\f\", \"\\b\", \"\\r\", \"\\t\", \"'\", \"\\\"\", \"\\\\\", \"A\", \"A\", \"あ\"]";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString017_DebugConsole用_JSON文字列のエスケープ文字対応() {
	var input = "\",\\,\\/,\\b,\\f,\\n,\\r,\\t,\\u";
	var expected = "/* String */\n\"\\\",\\\\,\\\\/,\\\\b,\\\\f,\\\\n,\\\\r,\\\\t,\\\\u\"";

	debug("testToJSONString017_DebugConsole用_JSON文字列のエスケープ文字対応：ImJson.toJSONString(input, true)",
			ImJson.toJSONString(input, true));
	debug("expected", expected);

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString018_DebugConsole用_JSON文字列のエスケープ文字対応() {
	var input = '["im-jssp-rpc\\\"a\\\""]';
	var expected = "/* String */\n\"[\\\"im-jssp-rpc\\\\\\\"a\\\\\\\"\\\"]\"";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString019_DebugConsole用_JSON文字列のエスケープシーケンスされていない文字対応() {
	var input = "\,\/,\b,\f,\n,\r,\t,\u";
	var expected = "/* String */\n\",/,\\b,\\f,\\n,\\r,\\t,u\"";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString020_DebugConsole用_JSON文字列の16進対応() {
	var input = "\u005C\u002C\u005C\u002F\u002C\u005C\u0062\u002C\u005C\u0066\u002C\u005C\u006E\u002C\u005C\u0072\u002C\u005C\u0074\u002C\u005C\u0075";
	var expected = "/* String */\n\"\\\\,\\\\/,\\\\b,\\\\f,\\\\n,\\\\r,\\\\t,\\\\u\"";

	assertEquals("NG", expected, ImJson.toJSONString(input, true) );
}

/**
 *
 */
function testToJSONString021_DebugConsole用_JSON文字列のダブルクォート() {
	var input = '["im-jssp-rpc\"a\""]';
	var expected = "/* String */\n\"[\\\"im-jssp-rpc\\\"a\\\"\\\"]\"";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString022_DebugConsole用_JSON文字列のダブルクォート() {
	var input = '{"roles" : [], "departments" : [], "posts" : [], "publicGroups" : []}';
	var expected = "/* String */\n\"{\\\"roles\\\" : [], \\\"departments\\\" : [], \\\"posts\\\" : [], \\\"publicGroups\\\" : []}\"";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString023_DebugConsole用_JSON文字列変換() {

	var input = new Object();
	    input.ary = new Array();
	    input.ary2= new Array('["im-jssp-rpc\\\"a\\\""]', '["im-jssp-rpc\"a\""]');
	    input.str = "string";
	    input.num = -1;
	    input.bol = true;
	    input.obj = new Object();
	    input.obj.ary = new Array();
	    input.obj.ary2= new Array("\",\\,\\/,\\b,\\f,\\n,\\r,\\t,\\u", "\,\/,\b,\f,\n,\r,\t,\u");
	    input.obj.str = "string1";
	    input.obj.num = 0;
	    input.obj.bol = false;

	var expected =
"/* Object */\n\
{\n\
    /* Array */\n\
    \"ary\" : [\n\
        \n\
    ], \n\
\n\
    /* Array */\n\
    \"ary2\" : [\n\
        /* String */\n\
        \"[\\\"im-jssp-rpc\\\\\\\"a\\\\\\\"\\\"]\", \n\
\n\
        /* String */\n\
        \"[\\\"im-jssp-rpc\\\"a\\\"\\\"]\"\n\
    ], \n\
\n\
    /* String */\n\
    \"str\" : \"string\", \n\
\n\
    /* Number */\n\
    \"num\" : -1, \n\
\n\
    /* Boolean */\n\
    \"bol\" : true, \n\
\n\
    /* Object */\n\
    \"obj\" : {\n\
        /* Array */\n\
        \"ary\" : [\n\
            \n\
        ], \n\
\n\
        /* Array */\n\
        \"ary2\" : [\n\
            /* String */\n\
            \"\\\",\\\\,\\\\/,\\\\b,\\\\f,\\\\n,\\\\r,\\\\t,\\\\u\", \n\
\n\
            /* String */\n\
            \",/,\\b,\\f,\\n,\\r,\\t,u\"\n\
        ], \n\
\n\
        /* String */\n\
        \"str\" : \"string1\", \n\
\n\
        /* Number */\n\
        \"num\" : 0, \n\
\n\
        /* Boolean */\n\
        \"bol\" : false\n\
    }\n\
}";

	debug("ImJson.toJSONString(input, true)", ImJson.toJSONString(input, true));
	debug("expected", expected);

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString024_DebugConsole用_nullの場合() {
	var input = null;
	var expected = "/* Null */\nnull";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString025_DebugConsole用_undefinedの場合() {
	var input = undefined;
	var expected = "/* Undefined */\nundefined";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString026_DebugConsole用_文字列の場合() {
	var input = "test_文字列㈱①～";
	var expected = "/* String */\n\"test_文字列㈱①～\"";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString027_DebugConsole用_数値の場合() {
	// 数値

	var input = 0;
	var expected = "/* Number */\n0";
	assertEquals("0", expected, ImJson.toJSONString(input, true));

	input = 1234567890;
	expected = "/* Number */\n1234567890";
	assertEquals("正の整数", expected, ImJson.toJSONString(input, true));

	input = -1234567890;
	expected = "/* Number */\n-1234567890";
	assertEquals("負の整数", expected, ImJson.toJSONString(input, true));

	input = 123.456789;
	expected = "/* Number */\n123.456789";
	assertEquals("正の少数", expected, ImJson.toJSONString(input, true));

	input = -123.456789;
	expected = "/* Number */\n-123.456789";
	assertEquals("負の少数", expected, ImJson.toJSONString(input, true));

	input = 1.23e4;
	expected = "/* Number */\n12300";
	assertEquals("正のX乗：1.23 × 10の4乗", expected, ImJson.toJSONString(input, true));

	input = 1.23E4;
	expected = "/* Number */\n12300";
	assertEquals("正のX乗：1.23 × 10の4乗", expected, ImJson.toJSONString(input, true));

	input = -1.23e3;
	expected = "/* Number */\n-1230";
	assertEquals("負のX乗：-1.23 × 10の3乗", expected, ImJson.toJSONString(input, true));

	input = -1.23E3;
	expected = "/* Number */\n-1230";
	assertEquals("負のX乗：-1.23 × 10の3乗", expected, ImJson.toJSONString(input, true));

	input = 0777;
	expected = "/* Number */\n511";
	assertEquals("8進数", expected, ImJson.toJSONString(input, true));

	input = 0xff88;
	expected = "/* Number */\n65416";
	assertEquals("16進数", expected, ImJson.toJSONString(input, true));

	input = Number.MAX_VALUE;
	expected = "/* Number */\n1.7976931348623157e+308";
	assertEquals("定数：Number.MAX_VALUE", expected, ImJson.toJSONString(input, true));

	input = Number.MIN_VALUE;
	expected = "/* Number */\n5e-324";
	assertEquals("定数：Number.MIN_VALUE", expected, ImJson.toJSONString(input, true));

	input = Number.POSITIVE_INFINITY;
	expected = "/* Number */\nInfinity";
	assertEquals("定数：Number.POSITIVE_INFINITY", expected, ImJson.toJSONString(input, true));

	input = Number.NEGATIVE_INFINITY;
	expected = "/* Number */\n-Infinity";
	assertEquals("定数：Number.NEGATIVE_INFINITY", expected, ImJson.toJSONString(input, true));

	input = Number.NaN;
	expected = "/* Number */\nNaN";
	assertEquals("定数：Number.NaN", expected, ImJson.toJSONString(input, true));

	input = Infinity;
	expected = "/* Number */\nInfinity";
	assertEquals("定数：Infinity", expected, ImJson.toJSONString(input, true));

	input = -Infinity;
	expected = "/* Number */\n-Infinity";
	assertEquals("定数：-Infinity", expected, ImJson.toJSONString(input, true));

	input = Number.NaN;
	expected = "/* Number */\nNaN";
	assertEquals("定数：NaN", expected, ImJson.toJSONString(input, true));

	input = Math.PI;
	expected = "/* Number */\n3.141592653589793";
	assertEquals("定数：Math.PI;", expected, ImJson.toJSONString(input, true));

	input = Math.SQRT2;
	expected = "/* Number */\n1.4142135623730951";
	assertEquals("定数：Math.SQRT2;", expected, ImJson.toJSONString(input, true));

	input = Math.SQRT1_2;
	expected = "/* Number */\n0.7071067811865476";
	assertEquals("定数：Math.SQRT1_2;", expected, ImJson.toJSONString(input, true));

	input = Math.E;
	expected = "/* Number */\n2.718281828459045";
	assertEquals("定数：Math.E;", expected, ImJson.toJSONString(input, true));

	input = Math.LN2;
	expected = "/* Number */\n0.6931471805599453";
	assertEquals("定数：Math.LN2;", expected, ImJson.toJSONString(input, true));

	input = Math.LN10;
	expected = "/* Number */\n2.302585092994046";
	assertEquals("定数：Math.LN10;", expected, ImJson.toJSONString(input, true));

	input = Math.LOG2E;
	if(window.navigator.userAgent.indexOf("MSIE") != -1){
		expected = "/* Number */\n1.4426950408889633";
	}
	else{
		expected = "/* Number */\n1.4426950408889634";
	}
	assertEquals("定数：Math.LOG2E;", expected, ImJson.toJSONString(input, true));

	input = Math.LOG10E;
	expected = "/* Number */\n0.4342944819032518";
	assertEquals("定数：Math.LOG10E;", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString028_DebugConsole用_真偽値の場合() {
	var input = true;
	var expected = "/* Boolean */\ntrue";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));

	input = false;
	expected = "/* Boolean */\nfalse";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));

}

/**
 *
 */
function testToJSONString029_DebugConsole用_関数の場合() {
	var input = function(){ return "テスト用関数です。"; };
	var expected = "/* Function */\n\"THIS_IS_FUNCTION\"";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString030_DebugConsole用_オブジェクトの場合() {
	var input = {
		prop_null      : null,
		prop_undefined : undefined,
		prop_string    : "文字列",
		prop_number    : 1234567890,
		prop_boolean   : false,
		prop_function  : function(){ return "テスト関数"; },
		prop_object    : {
			inner_obj_string : "テスト",
			inner_obj_number : -12345
		},
		prop_array     : [
			"テスト",
			-12345
		]
	}

	var expected =
"/* Object */\n\
{\n\
    /* Null */\n\
    \"prop_null\" : null, \n\
\n\
    /* Undefined */\n\
    \"prop_undefined\" : undefined, \n\
\n\
    /* String */\n\
    \"prop_string\" : \"文字列\", \n\
\n\
    /* Number */\n\
    \"prop_number\" : 1234567890, \n\
\n\
    /* Boolean */\n\
    \"prop_boolean\" : false, \n\
\n\
    /* Function */\n\
    \"prop_function\" : \"THIS_IS_FUNCTION\", \n\
\n\
    /* Object */\n\
    \"prop_object\" : {\n\
        /* String */\n\
        \"inner_obj_string\" : \"テスト\", \n\
\n\
        /* Number */\n\
        \"inner_obj_number\" : -12345\n\
    }, \n\
\n\
    /* Array */\n\
    \"prop_array\" : [\n\
        /* String */\n\
        \"テスト\", \n\
\n\
        /* Number */\n\
        -12345\n\
    ]\n\
}";

	debug("testToJSONString030 expected\n" + expected);
	debug("testToJSONString030 result\n" + ImJson.toJSONString(input, true));

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}


/**
 *
 */
function testToJSONString031_DebugConsole用_配列の場合() {
	var input = [
		null,
		undefined,
		"文字列",
		1234567890,
		false,
		function(){ return "テスト関数"; },
		{
			inner_obj_string : "テスト",
			inner_obj_number : -12345
		},
		[
			"テスト",
			-12345
		]
	]

	var expected =
"/* Array */\n\
[\n\
    /* Null */\n\
    null, \n\
\n\
    /* Undefined */\n\
    undefined, \n\
\n\
    /* String */\n\
    \"文字列\", \n\
\n\
    /* Number */\n\
    1234567890, \n\
\n\
    /* Boolean */\n\
    false, \n\
\n\
    /* Function */\n\
    \"THIS_IS_FUNCTION\", \n\
\n\
    /* Object */\n\
    {\n\
        /* String */\n\
        \"inner_obj_string\" : \"テスト\", \n\
\n\
        /* Number */\n\
        \"inner_obj_number\" : -12345\n\
    }, \n\
\n\
    /* Array */\n\
    [\n\
        /* String */\n\
        \"テスト\", \n\
\n\
        /* Number */\n\
        -12345\n\
    ]\n\
]";

	debug("testToJSONString031 expected\n" + expected);
	debug("testToJSONString031 result\n" + ImJson.toJSONString(input, true));

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString032_DebugConsole用_エスケープ文字の場合() {
	var input = new Array();
	input[0]  = "\n";     // \n - ニューライン（改行文字）
	input[1]  = "\f";     // \f - フォームフィード
	input[2]  = "\b";     // \b - バックスペース
	input[3]  = "\r";     // \r - キャリッジリターン（復帰文字）
	input[4]  = "\t";     // \t - タブ文字
	input[5]  = "\'";     // \' - シングルクォート（'）
	input[6]  = "\"";     // \" - ダブルクォート（"）
	input[7]  = "\\";     // \\ - バックスラッシュ（\）
	input[8]  = "\101";   //  - 8進\nnn - 8進数による文字コード指定（例えば "A" は "\101"）
	input[9]  = "\x41";   //  - 16\xnn - 16進数による文字コード指定（例えば "A" は "\x41"）
	input[10] = "\u3042"; //  - \unnnn - Unicode文字（例えば "あ" は "\u3042"）

	var expected =
"/* Array */\n\
[\n\
    /* String */\n\
    \"\\n\", \n\
\n\
    /* String */\n\
    \"\\f\", \n\
\n\
    /* String */\n\
    \"\\b\", \n\
\n\
    /* String */\n\
    \"\\r\", \n\
\n\
    /* String */\n\
    \"\\t\", \n\
\n\
    /* String */\n\
    \"'\", \n\
\n\
    /* String */\n\
    \"\\\"\", \n\
\n\
    /* String */\n\
    \"\\\\\", \n\
\n\
    /* String */\n\
    \"A\", \n\
\n\
    /* String */\n\
    \"A\", \n\
\n\
    /* String */\n\
    \"あ\"\n\
]";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}


/**
 *
 */
function testToJSONString033_XMLDocumentの場合() {
	var input = document.getElementById("xmlTest");
	var expected = "\"<DIV id='xmlTest'>\"";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}


/**
 *
 */
function testToJSONString034_XMLDocument_オブジェクト内の場合() {
	var input = new Object();
		input.prop_xml = document.getElementById("xmlTest");

	var expected = "{\"prop_xml\" : \"<DIV id='xmlTest'>\"}";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}

/**
 *
 */
function testToJSONString035_XMLDocument_配列内の場合() {
	var input = new Array();
		input[0] = document.getElementById("xmlTest");

	var expected = "[\"<DIV id='xmlTest'>\"]";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}


/**
 *
 */
function testToJSONString036_DebugConsole用_XMLDocumentの場合() {
	var input = document.getElementById("xmlTest");
	var expected = "/* XML */\n\"<DIV id='xmlTest'>\"";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}


/**
 *
 */
function testToJSONString037_DebugConsole用_XMLDocument_オブジェクト内の場合() {
	var input = new Object();
		input.prop_xml = document.getElementById("xmlTest");

	var expected =
"/* Object */\n\
{\n\
    /* XML */\n\
    \"prop_xml\" : \"<DIV id='xmlTest'>\"\n\
}";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}


/**
 *
 */
function testToJSONString038_DebugConsole用_XMLDocument_配列内の場合() {
	var input = new Array();
		input[0] = document.getElementById("xmlTest");

	var expected =
"/* Array */\n\
[\n\
    /* XML */\n\
    \"<DIV id='xmlTest'>\"\n\
]";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}

/**
 *
 */
function testToJSONString039_XMLDocument_getElementsByTagNameの場合() {
	var input = document.getElementsByTagName("a");

	var expected = "{\"0\" : \"<A id=''>\", \"1\" : \"<A id=''>\"}";
	var evaluate = ImJson.toJSONString(input)

	assertEquals("NG", expected, evaluate);
}

/**
 *
 */
function testToJSONString040_DebugConsole用_XMLDocument_getElementsByTagNameの場合() {
	var input = document.getElementsByTagName("a");

	var expected =
"/* Object */\n\
{\n\
    /* XML */\n\
    \"0\" : \"<A id=''>\", \n\
\n\
    /* XML */\n\
    \"1\" : \"<A id=''>\"\n\
}";

	var evaluate = ImJson.toJSONString(input, true)

	assertEquals("NG", expected, evaluate);
}


/**
 *
 */
function testToJSONString041_プロパティがエスケープ文字の場合() {
	var input = new Object();
	input["\n"]     = "0";  // \n - ニューライン（改行文字）
	input["\f"]     = "1";  // \f - フォームフィード
	input["\b"]     = "2";  // \b - バックスペース
	input["\r"]     = "3";  // \r - キャリッジリターン（復帰文字）
	input["\t"]     = "4";  // \t - タブ文字
	input["\'"]     = "5";  // \' - シングルクォート（'）
	input["\""]     = "6";  // \" - ダブルクォート（"）
	input["\\"]     = "7";  // \\ - バックスラッシュ（\）
	input["\101"]   = "8";  //  - 8進\nnn - 8進数による文字コード指定（例えば "A" は "\101"）
	input["\x42"]   = "9";  //  - 16\xnn - 16進数による文字コード指定（例えば "B" は "\x42"）
	input["\u3042"] = "10"; //  - \unnnn - Unicode文字（例えば "あ" は "\u3042"）

	debug("ImJson.toJSONString(input)", ImJson.toJSONString(input));

	var expected = "{\"\\n\" : \"0\", \"\\f\" : \"1\", \"\\b\" : \"2\", \"\\r\" : \"3\", \"\\t\" : \"4\", \"'\" : \"5\", \"\\\"\" : \"6\", \"\\\\\" : \"7\", \"A\" : \"8\", \"B\" : \"9\", \"あ\" : \"10\"}";

	assertEquals("NG", expected, ImJson.toJSONString(input));
}


/**
 *
 */
function testToJSONString042_DebugConsole用_プロパティがエスケープ文字の場合() {
	var input = new Object();
	input["\n"]     = "0";  // \n - ニューライン（改行文字）
	input["\f"]     = "1";  // \f - フォームフィード
	input["\b"]     = "2";  // \b - バックスペース
	input["\r"]     = "3";  // \r - キャリッジリターン（復帰文字）
	input["\t"]     = "4";  // \t - タブ文字
	input["\'"]     = "5";  // \' - シングルクォート（'）
	input["\""]     = "6";  // \" - ダブルクォート（"）
	input["\\"]     = "7";  // \\ - バックスラッシュ（\）
	input["\101"]   = "8";  //  - 8進\nnn - 8進数による文字コード指定（例えば "A" は "\101"）
	input["\x42"]   = "9";  //  - 16\xnn - 16進数による文字コード指定（例えば "B" は "\x42"）
	input["\u3042"] = "10"; //  - \unnnn - Unicode文字（例えば "あ" は "\u3042"）

	debug("ImJson.toJSONString(input, true)", ImJson.toJSONString(input, true));

	var expected =
"/* Object */\n\
{\n\
    /* String */\n\
    \"\\n\" : \"0\", \n\
\n\
    /* String */\n\
    \"\\f\" : \"1\", \n\
\n\
    /* String */\n\
    \"\\b\" : \"2\", \n\
\n\
    /* String */\n\
    \"\\r\" : \"3\", \n\
\n\
    /* String */\n\
    \"\\t\" : \"4\", \n\
\n\
    /* String */\n\
    \"'\" : \"5\", \n\
\n\
    /* String */\n\
    \"\\\"\" : \"6\", \n\
\n\
    /* String */\n\
    \"\\\\\" : \"7\", \n\
\n\
    /* String */\n\
    \"A\" : \"8\", \n\
\n\
    /* String */\n\
    \"B\" : \"9\", \n\
\n\
    /* String */\n\
    \"あ\" : \"10\"\n\
}";

	assertEquals("NG", expected, ImJson.toJSONString(input, true));
}
