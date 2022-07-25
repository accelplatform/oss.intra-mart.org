Package("im.util");

/**
 * @fileoverview <br/>
 * キュー。
 */

Class("im.util.Queue").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class im.util.Queue クラス。<br/>
	 * キューとは、最初に入ったものを最初に取り出すデータ構造のこと(FIFO)。
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.1
	 */
	im.util.Queue = function () {
		var ary;
		/* constructor */
		{
			this.superclass();
			ary = [];
		}
		/**
		 * キューが空かどうかを判定します。
		 * @return キューに項目が入っていない場合は true、そうでない場合は false
		 * @type Boolean
		 */
		this.empty = function () {
			return (ary.length == 0);
		}
		/**
		 * キューに入っているオブジェクトの数を返します。
		 * @return キューに入っているオブジェクトの数
		 * @type Number
		 */
		this.size = function () {
			return ary.length;
		}
		/**
		 * キューからオブジェクトを取り出します。
		 * <br/><br/>
		 * 先頭のオブジェクトを削除し、そのオブジェクトを関数の値として返します
		 * @return キューの先頭にあるオブジェクト。キューが空の場合は null
		 * @type Object
		 */
		this.dequeue = function () {
			if (this.empty()) {
				return null;
			} else {
				return ary.shift();
			}
		}
		/**
		 * キューにオブジェクトを入れます。	
		 * @param {Object} item オブジェクト
		 */
		this.enqueue = function (item) {
			ary.push(item);
		}
	}
);
