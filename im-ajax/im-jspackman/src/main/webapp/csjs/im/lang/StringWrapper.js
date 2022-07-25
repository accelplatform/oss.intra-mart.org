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

Package("im.lang");

/**
 * @fileoverview
 * 
 */

Class("im.lang.StringWrapper").extendNative("String").define(
	/**
	 * This class wraps native String class.
	 * @constructor
	 * @param {String} original aa
	 * @class
	 *  
	 * @extends String
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.StringWrapper = function (original) {
		/**
		 * この文字列と指定されたオブジェクトを比較します。
		 * @param {Object} anObject この String と比較されるオブジェクト 
		 * @return String が等しい場合は true、そうでない場合は false
		 * @type Boolean
		 */
		this.equals = function (anObject) {
			if (this === anObject) {
				return true;
			} else if (anObject == undefined || anObject == null) {
				return false;
			} else if (anObject.constructor === String.prototype.constructor) {
				if (this.length == anObject.length) {
					for (var i = 0; i < this.length; i++) {
						if (this.charCodeAt(i) !== anObject.charCodeAt(i)) {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		/**
		 * この文字列と指定されたオブジェクトを大文字小文字を区別せずに比較します。
		 * @param {Object} anObject この String と比較されるオブジェクト 
		 * @return String が等しい場合は true、そうでない場合は false
		 * @type Boolean
		 */
		this.equalsIgnoreCase = function (anObject) {
			if (this === anObject) {
				return true;
			} else if (anObject == undefined || anObject == null) {
				return false;
			} else if (anObject.constructor === String.prototype.constructor) {
				if (this.length == anObject.length) {
					var upperedThis = this.toUpperCase();
					var upperedArgument = anObject.toUpperCase();
					for (var i = 0; i < upperedThis.length; i++) {
						if (upperedThis.charCodeAt(i) !== upperedArgument.charCodeAt(i)) {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		/**
		 * この文字列が、指定された接頭辞で始まるかどうかを判定します。
		 * @param {String} prefix 接頭辞
		 * @return 引数によって表される文字シーケンス、この文字列によって表される文字シーケンスの接頭辞である場合は true、そうでない場合は false。
		 * @type boolean
		 */
		this.startsWith = function (prefix) {
			var regexp = new RegExp("^" + prefix);
			return regexp.test(this);
		}
		/**
		 * この文字列が、指定された接尾辞で終わるかどうかを判定します。
		 * @param {String} suffix 接尾辞
		 * @return 引数によって表される文字シーケンスが、このオブジェクトによって表される文字シーケンスの接尾辞である場合は true、そうでない場合は false。
		 * @type boolean
		 */
		this.endsWith = function (suffix) {
			var regexp = new RegExp(suffix + "$");
			return regexp.test(this);
		}
		this.toJSON = function () {
			return '"' + this.replace(/\"/g, '\\\"') + '"';
		}
	}
).initialize();
