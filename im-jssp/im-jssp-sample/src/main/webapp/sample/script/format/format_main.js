/**
 * Formatオブジェクトを利用した文字列変換のサンプル
 */
function init(request){

	// String型
	var stringDebug = new Array();
 	stringDebug[0] = Format.format("逆順に表示されます。%4$2s %3$2s %2$2s %1$2s", "a", "b", "c", "d");

 	// Number型
	var numberDebug = new Array();
 	numberDebug[0] = Format.format("%sは、10進数で「%<d」, 16進数で「%<x」, 8進数で「%<o」です。", 255);
 	numberDebug[1] = Format.format("123.456を20桁(小数点第5位)で表示します。→[%20.5f]", 123.456);
 	numberDebug[2] = Format.format("通常「%10d」、左揃え「%<-10d」", 512);
 	numberDebug[3] = Format.format("常に符号が含まれます。正数「%+d」、負数「%+d」", 1234, -5678);

 	// Number型（Format.fromNumber関数）
	var fromNumberDebug = new Array();
 	fromNumberDebug[0] = Format.fromNumber("###,###,###.#####", 34567890.1234);//1234567890.1234);

 	// Number型（Format.toMoney関数）
	var toMoneyDebug = new Array();
 	toMoneyDebug[0] = Format.toMoney(1234567890.1234);

	// Boolean型
	var boolDebug = new Object();
	boolDebug[0] = Format.format("Boolean型を表示します。→%B, %b, %S, %s", true, true, true, true);

	// Date型
	var dateDebug = new Array();
	var now = new Date();
	dateDebug[ 0] = Format.format("『%tH』：24 時間制の時（必要に応じて 0 を先頭に追加し、2 桁で表現 (00 - 23)）", now);
	dateDebug[ 1] = Format.format("『%tI』：12 時間制の時（必要に応じて 0 を先頭に追加し、2 桁で表現 (00 - 12)）", now);
	dateDebug[ 2] = Format.format("『%tk』：24 時間制の時", now);
	dateDebug[ 3] = Format.format("『%tl』：12 時間制の時", now);
	dateDebug[ 4] = Format.format("『%tM』：分（必要に応じて 0 を先頭に追加し、2 桁で表現(00 - 59)）", now);
	dateDebug[ 5] = Format.format("『%tS』：秒（必要に応じて 0 を先頭に追加し、2 桁で表現(00 - 60)）", now);
	dateDebug[ 6] = Format.format("『%tp』：午前または午後を表すロケール固有の小文字のマーカ (例、「am」や「pm」)", now);
	dateDebug[ 7] = Format.format("『%tB』：ロケール固有の月の完全な名前", now);
	dateDebug[ 8] = Format.format("『%tb』：ロケール固有の月の省略名", now);
	dateDebug[ 9] = Format.format("『%tA』：ロケール固有の曜日の完全な名前", now);
	dateDebug[10] = Format.format("『%ta』：ロケール固有の曜日の短縮名", now);
	dateDebug[11] = Format.format("『%tY』：年。必要に応じて 0 を先頭に追加し、4 桁以上で表現", now);
	dateDebug[12] = Format.format("『%ty』：年の下 2 桁。（必要に応じて 0 を先頭に追加 (00 - 99)）", now);
	dateDebug[13] = Format.format("『%tm』：月。（必要に応じて 0 を先頭に追加し、2 桁で表現）", now);
	dateDebug[14] = Format.format("『%td』：月の何日目かを表す日。（必要に応じて 0 を先頭に追加し、2 桁で表現(01 -31)）", now);
	dateDebug[15] = Format.format("『%te』：月の何日目かを表す日。（最大 2 桁で表現します (1 - 31)）", now);
	dateDebug[16] = Format.format("『%tT』：「%%tH:%%tM:%%tS」として 24 時間制で書式設定された時刻", now);
	dateDebug[17] = Format.format("『%tr』：「%%tI:%%tM:%%tS %%Tp」として 12 時間制で書式設定された時刻", now);
	dateDebug[18] = Format.format("『%tD』：「%%tm/%%td/%%ty」として書式設定された日付", now);
	dateDebug[19] = Format.format("『%tF』：「%%tY-%%tm-%%td」として書式設定された、ISO 8601 に準拠した日付", now);

	// Date型（ロケール）
	var dateLocaleDebug = new Array();
	dateLocaleDebug[ 0] = Format.formatLocale("ja_JP", "『%tp』：午前または午後を表すロケール固有の小文字のマーカ (例、「am」や「pm」)", now);
	dateLocaleDebug[ 1] = Format.formatLocale("ja_JP", "『%tB』：ロケール固有の月の完全な名前", now);
	dateLocaleDebug[ 2] = Format.formatLocale("ja_JP", "『%tb』：ロケール固有の月の省略名", now);
	dateLocaleDebug[ 3] = Format.formatLocale("ja_JP", "『%tA』：ロケール固有の曜日の完全な名前", now);
	dateLocaleDebug[ 4] = Format.formatLocale("ja_JP", "『%ta』：ロケール固有の曜日の短縮名", now);
	dateLocaleDebug[ 5] = Format.formatLocale("ja_JP", "『%tr』：「%%tI:%%tM:%%tS %%Tp」として 12 時間制で書式設定された時刻", now);

	dateLocaleDebug[ 6] = Format.formatLocale("en_US", "『%tp』：Locale-specific morning or afternoon marker in lower case (e.g.'am' or 'pm')", now);
	dateLocaleDebug[ 7] = Format.formatLocale("en_US", "『%tB』：Locale-specific full month name", now);
	dateLocaleDebug[ 8] = Format.formatLocale("en_US", "『%tb』：Locale-specific abbreviated month name", now);
	dateLocaleDebug[ 9] = Format.formatLocale("en_US", "『%tA』：Locale-specific full name of the day of the week", now);
	dateLocaleDebug[10] = Format.formatLocale("en_US", "『%ta』：Locale-specific short name of the day of the week", now);
	dateLocaleDebug[11] = Format.formatLocale("en_US", "『%tr』：Time formatted for the 12-hour clock as '%%tI:%%tM:%%tS %%Tp'", now);

	// Date型（Format.fromDate()関数）
	var fromDateDebug = new Array();
	fromDateDebug[0] = Format.fromDate("現在、『yyyy-MM-dd(E) a hh:mm:ss.SSSS』です。", new Date());
	fromDateDebug[1] = Format.fromDate("en_US", "現在、『yyyy-MM-dd(E) a hh:mm:ss.SSSS』です。", new Date());

	// String → Dateへの変換（Format.toDate()関数）
	var toDateDebug = new Array();
	toDateDebug[0] = Format.toDate("yyyy-MM-dd", "2008-01-11");
	toDateDebug[1] = Format.toDate("yyyy/MM/dd|HH:mm:ss", "2008/01/11|23:34:45");

	Debug.browse(stringDebug, numberDebug, fromNumberDebug, toMoneyDebug, boolDebug, dateDebug, dateLocaleDebug, fromDateDebug, toDateDebug);

}