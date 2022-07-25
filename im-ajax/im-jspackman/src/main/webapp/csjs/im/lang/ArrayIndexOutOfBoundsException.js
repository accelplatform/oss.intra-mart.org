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
 * Thrown to indicate that an array has been accessed with an illegal index. 
 */

Class("im.lang.ArrayIndexOutOfBoundsException").extend("im.lang.IndexOutOfBoundsException").define(
	/**
	 * Constructs a new ArrayIndexOutOfBoundsException with the specified detail message.
	 * @constructor
	 * @param {String} message the detail message.
	 * @param {im.lang.Throwable} cause? the cause.
	 * @class 
	 * Thrown to indicate that an array has been accessed with an illegal index. 
	 * @extends im.lang.IndexOutOfBoundsException
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.ArrayIndexOutOfBoundsException = function (message, cause) {
		/* constructor */
		{
			this.superclass(message, cause);
			this.name = im.lang.ArrayIndexOutOfBoundsException.clazz.getName();
		}
	}
);
