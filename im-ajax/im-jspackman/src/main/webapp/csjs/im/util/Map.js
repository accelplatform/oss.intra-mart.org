Package("im.util");

/**
 * @fileoverview <br/>
 * 文字列キーを値にマッピングするオブジェクトです。
 */

Class("im.util.Map").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class im.util.Map クラス。<br/>
	 * <br/>
	 * 文字列キーを値にマッピングするオブジェクトです。<br/>
	 * マップには、同一のキーを複数登録することはできません。<br/>
	 * 各キーは1つの値にしかマッピングできません。
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.1
	 */
	im.util.Map = function () {
		/** @private */
		var entry;
		/* constructor */
		{
			this.superclass();
			entry = {}
		}
		/**
		 * マップからマッピングをすべて削除します。
		 */
		this.clear = function () {
			entry = {}
		}
		/**
		 * 指定されたキーのマッピングがマップに含まれている場合に true を返します。
		 * @param {String} key キー
		 * @return マップが指定のキーのマッピングを保持する場合は true
		 * @type Boolean
		 */
		this.containsKey = function (key) {
			return (typeof(entry[key]) != "undefined");
		}
		/**
		 * マップが指定のキーをマップする値を返します。
		 * @param {String} key キー
		 * @return マップが、指定されたキーにマッピングしている値。このキーに対するマッピングがマップにない場合は null
		 * @type Object
		 */
		this.get = function (key) {
			return (this.containsKey(key) ? entry[key] : null);
		}
		/**
		 * マップがキーと値のマッピングを保持しない場合に true を返します。
		 * @return マップがキーと値のマッピングを保持しない場合は true
		 * @type Boolean
		 */
		this.isEmpty = function () {
			return (this.size() == 0);
		}
		/**
		 * マップに含まれているキーリストを返します。<br/>
		 * @return マップに含まれているキーリスト
		 * @type Array
		 */
		this.keyList = function () {
			var keyList = [];
			for (var key in entry) {
				keyList.push(key);
			}
			return keyList;
		}
		/**
		 * 指定された値と指定されたキーをこのマップに関連付けます。
		 * @param {String} key キー
		 * @param {Object} value 指定されるキーに関連付けられる値
		 * @return 指定されたキーに関連した以前の値。key にマッピングがなかった場合は null。
		 * @type Object
		 */
		this.put = function (key, value) {
			var oldValue = this.get(key);
			entry[key] = value;
			return oldValue;
		}
		/**
		 * このキーにマッピングがある場合に、そのマッピングをマップから削除します。
		 * @param {String} key キー
		 * @return 指定されたキーと関連付けられていた以前の値。キーのマッピングがなかった場合は null。
		 * @type Object
		 */
		this.remove = function (key) {
			var oldValue = this.get(key);
			delete entry[key];
			return oldValue;
		}
		/**
		 * マップ内のキーと値のマッピングの数を返します。
		 * @return マップ内のキー値マッピングの数
		 * @type Number
		 */
		this.size = function () {
			var count = 0;
			for (var key in entry)  {
				count++;
			}
			return count;
		}
		/**
		 * マップに含まれている値のリストビューを返します。
		 * @return マップ内に保持されている値のリストビュー
		 * @type Array
		 */
		this.values = function () {
			var valueList = [];
			for (var key in entry) {
				valueList.push(entry[key]);
			}
			return valueList;
		}
	}
);