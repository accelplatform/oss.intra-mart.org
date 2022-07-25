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
Class("im.lang.ObjectWrapper").extendNative("Object").define(
	/**
	 * This class wraps native Object class.
	 * @constructor
	 * @class
	 * Class Object is the root of the class hierarchy.<br/>
	 * Every class has Object as a superclass.
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.ObjectWrapper = function () {}
).initialize();
/* static field and static method of Object class */
{
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @param {Object} obj the object to check
	 * @return true if this object is the same as the obj argument; false otherwise.
	 * @tyep Boolean
	 */
	Object.equals = function (obj1, obj2) {
		if (obj1 === obj2) {
			return true;
		} else if (obj2 == undefined || obj2 == null) {
			return false;
		} else if (obj2.constructor === Object.prototype.constructor) {
			for (var param in obj1) {
				if (!obj1[i].equals) {
					if (obj1[i] !== obj2[i]) {
						return false;
					}
				} else if (!Object.equals(obj1[i], obj2[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	Object.toJSON = function (obj) {
		var innerJson = "";
		var separator = "";
		for (var param in obj) {
			var aValue = obj[param];
			var aType = typeof(aValue);
			if (aType == "function") {
				continue;
			}
			else 
				if (aValue == null || aType == "undefined") {
					innerJson += (separator + param + ":" + aValue);
				}
				else {
					innerJson += (separator + param + ":" + aValue.toJSON());
				}
			separator = ",";
		}
		return "{" + innerJson + "}";
	}
}
