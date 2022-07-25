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
 * The class Exception and its subclasses are a form of Throwable that indicates conditions that a reasonable application might want to catch.
 */

Class("im.lang.Exception").extend("im.lang.Throwable").define(
	/**
	 * Constructs a new exception with the specified detail message.
	 * @constructor
	 * @param {String} message the detail message.
	 * @param {im.lang.Throwable} cause? the cause.
	 * @class 
	 * The class Exception and its subclasses are a form of Throwable that indicates conditions that a reasonable application might want to catch.
	 * @extends im.lang.Throwable
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.Exception = function (message, cause) {
		/* constructor */
		{
			this.superclass(message, cause);
			this.name = im.lang.Exception.clazz.getName();
		}
	}
);
