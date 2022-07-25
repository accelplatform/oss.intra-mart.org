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

/**
 * @fileoverview
 * Thrown when an application tries to load in a class through its string name.
 */

Class("im.lang.reflect.InvocationTargetException").extend("im.lang.Exception").define(
	/**
	 * Constructs a InvocationTargetException with the specified detail message.
	 * @constructor
	 * @param {String} message the detail message.
	 * @param {im.lang.Throwable} cause? the cause.
	 * @class 
	 * InvocationTargetException is a checked exception that wraps an exception thrown by an invoked method or constructor.
	 * @extends im.lang.Exception
	 * @author emooru
	 * @version 0.1
	 */
	im.lang.reflect.InvocationTargetException = function (message, cause) {
		/* constructor */
		{
			this.superclass(message, cause);
			this.name = im.lang.reflect.InvocationTargetException.clazz.getName();
		}
		/**
		 * Get the thrown target exception.
		 *
		 * <p>This method predates the general-purpose exception chaining facility.
		 * The {@link im.lang.Throwable#getCause} method is now the preferred means of
		 * obtaining this information.
		 *
		 * @return the thrown target exception (cause of this exception).
		 * @type im.lang.Throwable
		 */
		this.getTargetException = function () {
			return cause;
		}
	}
);
