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
 * Thrown to indicate that an assertion has failed.
 */

Class("im.lang.AssertionError").extend("im.lang.Error").define(
	/**
	 * Constructs an AssertionError with its detail message derived from the specified.
	 * @constructor
	 * @param {Object} detailMessage value to be used in constructing detail message.
	 * @class 
	 * Thrown to indicate that an assertion has failed.
	 * @extends im.lang.Error
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.AssertionError = function (detailMessage) {
		/* constructor */
		{
			this.superclass(detailMessage);
			this.name = im.lang.AssertionError.clazz.getName();
		}
	}
);
