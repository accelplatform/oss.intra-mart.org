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

Package("im.lang.reflect");

Import("im.lang.reflect.InvocationTargetException");

/**
 * @fileoverview
 * 
 */

Class("im.lang.reflect.Method").define(
	/**
	 * The constructor is nothing.
	 * @constructor
	 * @param {String} name
	 * @param {Function} func
	 * @param {im.lang.Class} clazz
	 * @class
	 * A Method provides information about, and access to, a single method on a class.
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.reflect.Method = function (name, func, clazz) {
		/* constructor */
		{
			this.superclass();
		}
		/**
		 * Invokes the underlying method represented by this Method object.
		 * @param {Object} obj the object the underlying method is invoked from
		 * @param {Array} args the arguments used for the method call
		 * @return the result of dispatching the method represented by this object 
		 * @type Object
		 * @throws im.lang.reflect.InvocationTargetException if the underlying method throws an exception.
		 */
		this.invoke = function (obj, args) {
			try {
				if (obj[name] instanceof Function) {
					return obj[name].invoke(obj, args);
				} else {
					return func.invoke(obj, args);
				}
			} catch (e) {
				throw new im.lang.reflect.InvocationTargetException(e.message, e);
			}
		}
		/**
		 * Compares this Method against the specified object.<br/>
		 * <br/>
		 * Compares this Method against the specified object. 
		 * Returns true if the objects are the same. 
		 * Two Methods are the same if they were declared by the same class and have the same name.
		 * @param {Object} aMethod
		 * @return true if this object is the same as the obj argument; false otherwise.
		 * @type Boolean
		 */
		this.equals = function (aMethod) {
			try {
				return (name.equals(aMethod.getName())
						&& clazz.equals(aMethod.getClass()));
			} catch (e) {
				return false;
			}
		}
		/**
		 * Returns the method name.
		 * @return the method name
		 * @type String
		 */
		this.getName = function () {
			return name;
		}
		/** @private */
		this.getClass = function () {
			return clazz;
		}
	}
);