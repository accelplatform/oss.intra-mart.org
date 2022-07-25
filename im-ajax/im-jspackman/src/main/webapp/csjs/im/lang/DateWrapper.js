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

Class("im.lang.DateWrapper").extendNative("Date").define(
	/**
	 * This class wraps native Date class.
	 * @constructor
	 * @class
	 *  
	 * @extends Date
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.DateWrapper = function () {
		/**
		 * 
		 */
		this.equals = function(anObject) {
			if (this === anObject) {
				return true;
			} else if (anObject == undefined || anObject == null) {
				return false;
			} else if (anObject.constructor === Date.prototype.constructor) {
				return (this.getTime() == anObject.getTime());
			} else {
				return false;
			}
		}
		this.toJSON = function () {
			// TODO どういうフォーマットがいいかなぁ...
			return '"' + this.toString() + '"';
		}
	}
).initialize();
