/*
 * Copyright 2007 the OPEN INTRA-MART.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

Package("im.util");

Import("im.util.Iterator", false);

/**
 * @fileoverview <br/>
 * サイズ変更可能な配列の実装です。
 */

Class("im.util.List").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @param {Array} list? リスト
	 * @class
	 * im.util.List クラス。<br/><br/>
	 * サイズ変更可能な配列の実装です。
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.1
	 */
	im.util.List = function (list) {
		/* constructor */
		{
			this.superclass();
			if (!(list instanceof Array)) {
				list = [];
			}
		}
		/**
		 * リストの指定された位置に、指定された要素を挿入します。<br/>
		 * <br/>
		 * 第一引数を省略した場合 add(elem)、リストの最後に指定された要素を追加します。
		 * @throws IndexOutOfBoundsException.
		 * @param {Number} index? 指定の要素が挿入されるインデックス
		 * @param {Object} elem 挿入される要素 
		 */
		this.add = function (index, elem) {
			if (index instanceof Number) {
				if (this.isIndexOutOfBounds(index)) {
					throw new im.lang.ArrayIndexOutOfBoundsException();
				} else {
					var newList = [];
					var idx = 0;
					for (var i = 0; i < list.length + 1; i++) {
						if (i == index) {
							newList.push(elem);
						} else {
							newList.push(list[idx++]);
						}
					}
					/* 置き換える */
					list = newList;
				}
			} else {
				/* index を追加するオブジェクト(elem)と判断 */
				list.push(index);
			}
		}
		/**
		 * リストからすべての要素を削除します。
		 */
		this.clear = function() {
			list = new Array();
		}
		/**
		 * リストに指定の要素がある場合に true を返します。
		 * @param {Object} elem リストにあるかどうかを調べる要素 
		 * @return 指定された要素がある場合は true、そうでない場合は false
		 * @type Boolean
		 */
		this.contains = function (elem) {
			return (this.indexOf(elem) >= 0);
		}
		/**
		 * リスト内の指定された位置にある要素を返します。
		 * @throws IndexOutOfBoundsException.
		 * @param {Number} index 返される要素のインデックス
		 * @return リスト内の指定された位置にある要素
		 * @type Object
		 */
		this.get = function (index) {
			if (this.isIndexOutOfBounds(index)) {
				throw new im.lang.ArrayIndexOutOfBoundsException();
			}
			return list[index];
		}
		/**
		 * equals メソッドを使って等しいかどうかを判定しながら、指定された引数と同じ内容の要素を先頭から検索します。
		 * @param {Object} elem オブジェクト
		 * @return リスト内で引数が最初に現れるインデックス。オブジェクトが見つからない場合は -1
		 * @type Number
		 */
		this.indexOf = function (elem) {
			if (!elem || elem == null) {
				for (var i = 0; i < list.length; i++) {
					if (list[i] === elem) {
						return i;
					}
				}
			} else {
				for (var i = 0; i < list.length; i++) {
					if (elem.equals(list[i])) {
						return i;
					}
				}
				return -1;
			}
		}
		/**
		 * リストに要素がないかどうかを判定します。
		 * @return リストに要素がない場合は true、そうでない場合は false
		 * @type Boolean
		 */
		this.isEmpty = function () {
			return (list.length == 0);
		}
		/**
		 * 指定されたオブジェクトがリスト内で最後に現れるインデックスを返します。
		 * @param {Object} elem オブジェクト
		 * @return リストで指定のオブジェクトと一致する最後のオブジェクトのインデックス。オブジェクトが見つからない場合は -1
		 * @type Number
		 */
		this.lastIndexOf = function (elem) {
			if (!elem || elem == null) {
				for (var i = list.length - 1; i >= 0; i--) {
					if (list[i] === elem) {
						return i;
					}
				}
			} else {
				for (var i = list.length - 1; i >= 0; i--) {
					if (elem.equals(list[i])) {
						return i;
					}
				}
				return -1;
			}
		}
		/**
		 * リスト内の指定された位置から要素を削除します。
		 * @throws IndexOutOfBoundsException.
		 * @param {Number} index 削除される要素のインデックス
		 * @return 削除されたオブジェクト
		 * @type Object
		 */
		this.remove = function (index) {
			if (this.isIndexOutOfBounds(index)) {
				throw new im.lang.ArrayIndexOutOfBoundsException();
			} else {
				var removedValue;
				var newList = [];
				var idx = 0;
				for (var i = 0; i < list.length; i++) {
					if (i == index) {
						removedValue = list[idx];
					} else {
						newList.push(list[idx]);
					}
					idx++;
				}
				/* 置き換える */
				list = newList;
				return removedValue;
			}
		}
		/**
		 * リスト内の指定された位置にある要素を、指定された要素に置き換えます。
		 * @throws IndexOutOfBoundsException.
		 * @param {Number} index 置換される要素のインデックス
		 * @param {Object} elem 指定された位置に格納される要素
		 * @return 指定された位置に以前あった要素
		 * @type Object
		 */
		this.set = function (index, elem) {
			if (this.isIndexOutOfBounds(index)) {
				throw new im.lang.ArrayIndexOutOfBoundsException();
			} else {
				var oldValue = list[index];
				list[index] = elem;
				return oldValue;
			}
		}
		/**
		 * リスト内にある要素の数を返します。
		 * @return リスト内の要素数
		 * @type Number
		 */
		this.size = function () {
			return list.length;
		}
		/**
		 * リスト内のすべての要素が正しい順序で格納されている配列を返します。
		 * @return リスト内のすべての要素が正しい順序で格納されている配列
		 * @type Array
		 */
		this.toArray = function () {
			return list;
		}
		/**
		 * このリスト内の要素を適切な順序で繰り返し処理する反復子を返します。
		 * @return リスト内の要素を適切な順序で繰り返し処理する反復子
		 * @type im.util.Iterator
		 */
		this.iterator = function () {
			return new im.util.Iterator(list);
		}
		/** @private */
		this.isIndexOutOfBounds = function (index) {
			return (index < 0 || list.length < index);
		}
	}
);
