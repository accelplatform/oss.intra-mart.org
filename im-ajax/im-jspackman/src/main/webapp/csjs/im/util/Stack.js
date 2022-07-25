Package("im.util");

/**
 * @fileoverview <br/>
 * スタック。
 */
 
Class("im.util.Stack").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class im.util.Stack クラス。<br/>
	 * スタックとは、最後に入ったものを最初に取り出すデータ構造のこと(LIFO)。
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.1
	 */
	im.util.Stack = function () {
		var ary;
		/* constructor */
		{
			this.superclass();
			ary = [];
		}
		/**
		 * スタックが空かどうかを判定します。
		 * @return スタックに項目が入っていない場合は true、そうでない場合は false
		 * @type Boolean
		 */
		this.empty = function () {
			return (ary.length == 0);
		}
		/**
		 * スタックに入っているオブジェクトの数を返します。
		 * @return スタックに入っているオブジェクトの数
		 * @type Number
		 */
		this.size = function () {
			return ary.length;
		}
		/**
		 * スタックからオブジェクトを取り出します。<br/>
		 * <br/>
		 * 最後に入れられたオブジェクトを削除し、そのオブジェクトを関数の値として返します。
		 * @return スタックの先頭にあるオブジェクト 
		 * @type Object
		 * @throws im.util.EmptyStackException
		 */
		this.pop = function () {
			if (this.empty()) {
				throw new im.util.EmptyStackException();
			}
			return ary.pop();
		}
		/**
		 * スタックにオブジェクトを入れます。
		 * @param {Object} item オブジェクト
		 */
		this.push = function (item) {
			ary.push(item);
		}
	}
);
