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

Class("im.lang.ArrayWrapper").extendNative("Array").define(
	/**
	 * This class wraps native Array class.
	 * @constructor
	 * @class
	 *  
	 * @extends Array
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.ArrayWrapper = function () {
		/**
		 * 
		 */
		this.equals = function (anObject) {
			if (this === anObject) {
				return true;
			} else if (anObject == undefined || anObject == null) {
				return false;
			} else if (anObject.constructor === Array.prototype.constructor) {
				if (this.length == anObject.length) {
					for (var i = 0; i < this.length; i++) {
						if (!this[i].equals) {
							if (this[i] !== anObject[i])  {
								return false;
							}
						} else if (!this[i].equals(anObject[i])) {
							return false;
						}
					}
					return true;
				}
				return false;
			} else {
				return false;
			}
		}
		this.toJSON = function () {
			var innerJson = "";
			var separator = "";
			for (var i = 0; i < this.length; i++) {
				var aValue = this[i];
				var aType  = typeof(aValue);
				if (aType == "function") {
					continue;
				} else if (aValue == null || aType == "undefined") {
					innerJson += (separator + aValue);
				} else {
					innerJson += (separator + aValue.toJSON());
				}
				separator = ",";
			}
			return "[" + innerJson + "]";
		}
	}
).initialize();
