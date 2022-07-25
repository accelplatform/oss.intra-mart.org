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

Import("im.jsunit.Assert");
Import("im.jsunit.AssertionFailedError");

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.TestFailure").define(
	/**
	 * Constructs a TestFailure with the given test and exception.
	 * @constructor
	 * @class A <code>TestFailure</code> collects a failed test together with the caught exception.
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.TestFailure = function (failedTest, thrownException) {
		/* コンストラクタ */
		{
			this.superclass();
		}
		/**
		 * Gets the failed test.
		 * @return the failed test
		 * @type im.jsunit.Test
		 */
		this.failedTest = function () {
			return fFailedTest;
		}
		/**
		 * Gets the thrown exception.
		 * @return the thrown exception
		 * @type im.lang.Throwable
		 */
		this.thrownException = function () {
			return fThrownException;
		}
		/**
		 * Returns a short description of the failure.
		 * @return a short description of the failure
		 * @type String
		 */
		this.toString = function () {
			return (fFailedTest + ": " + fThrownException.getMessage());
		}
		/**
		 * Returns a exception message.
		 * @return a exception message.
		 * @type String
		 */
		this.exceptionMessage = function () {
			return this.thrownException().getMessage();
		}
		/**
		 * Returns true if the thrown Exception is instance of {@link im.jsunit.AssertionFailedError}.
		 * @return Returns true if the thrown Exception is instance of {@link im.jsunit.AssertionFailedError}.
		 * @type Boolean
		 */
		this.isFailure = function () {
			return (thrownException() instanceof im.jsunit.AssertionFailedError);
		}
	}
);
