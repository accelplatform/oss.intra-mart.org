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
 * We expect that the Throwable class is the superclass of all errors and exceptions.
 */

//Class("im.lang.Throwable").extend("Error").define(
Class("im.lang.Throwable").define(
	/**
	 * Constructs a new throwable with the specified detail message.
	 * @constructor
	 * @param {String} message the detail message.
	 * @param {im.lang.Throwable} cause? the cause.
	 * @class 
	 * The Throwable class is the superclass of all errors and exceptions.
	 * @extends Error
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.Throwable = function (message, cause) {
		/* constructor */
		{
			this.superclass(message);
			this.message = message;
			this.name    = im.lang.Throwable.clazz.getName();
		}
		/**
		 * Returns the detail message string of this throwable.
		 * @return the detail message string of this Throwable instance.
		 * @type String
		 */
		this.getMessage = function () {
			return this.message;
		}
		/**
		 * Returns a short description of this throwable.
		 * @return a string representation of this throwable.
		 * @type String
		 */
		this.toString = function () {
			return "[" + this.name  + "]: " + this.message;
		}
		/**
		 * Returns the cause of this exception (the thrown target exception,
		 * which may be <tt>null</tt>).
		 *
		 * @return  the cause of this exception.
		 * @type im.lang.Throwable
		 */
		this.getCause = function () {
			return cause;
		}
	}
);
