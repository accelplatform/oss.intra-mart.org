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

Package("im.jsunit");

/**
 * @fileoverview
 * Thrown when an assertion failed.
 */

Class("im.jsunit.AssertionFailedError").extend("im.lang.AssertionError").define(
	/**
	 * Constructs an AssertionFailedError with its detail message.
	 * @constructor
	 * @param {String} message the detail message.
	 * @class 
	 * Thrown when an assertion failed.
	 * @extends im.lang.AssertionError
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.AssertionFailedError = function (detailMessage) {
		/* constructor */
		{
			this.superclass(detailMessage);
			this.name = im.jsunit.AssertionFailedError.clazz.getName();
		}
	}
);
