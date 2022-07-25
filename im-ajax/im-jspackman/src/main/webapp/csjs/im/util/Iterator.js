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

/**
 * @fileoverview
 * イテレータ。
 */

Class("im.util.Iterator").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @param {Array} list リスト
	 * @class
	 * im.util.Iterator クラス。<br/>
	 * <br/>
	 * コレクションの反復子です。
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.1
	 */
	im.util.Iterator = function (collection) {
		/** @private */
		var _currentIdx;
		var list;
		var removedState;
		/* constructor */
		{
			this.superclass();
			_currentIdx = -1;
			list = collection;
			removedState = true;
		}
		/**
		 * 繰り返し処理でさらに要素がある場合に true を返します。
		 * @return 反復子がさらに要素を持つ場合は true
		 * @type Boolean
		 */
		this.hasNext = function () {
			return (_currentIdx < list.length - 1);
		}
		/**
		 * 繰り返し処理で次の要素を返します。
		 * @return 繰り返し処理で次の要素
		 * @type Object
		 */
		this.next = function () {
			removedState = false;
			return list[++_currentIdx];
		}
		/**
		 * 基になるコレクションから、反復子によって最後に返された要素を削除します。
		 * throws im.lang.IllegalStateException next メソッドがまだ呼び出されていないか、next メソッドの最後の呼び出しのあとに remove メソッドがすでに呼び出されている場合
		 */
		this.remove = function () {
			if (removedState) {
				throw new im.lang.IllegalStateException();
			} else {
				removedState = true;
			}
			var tmpList = new Array();
			var listLength = list.length;
			for (var i = 0; i < listLength; i++) {
				var obj = list.shift();
				if (i != _currentIdx) {
					tmpList.push(obj);
				}
			}
			for (var i = 0; i < listLength - 1; i++) {
				list.push(tmpList.shift());
			}
		}
	}
);
