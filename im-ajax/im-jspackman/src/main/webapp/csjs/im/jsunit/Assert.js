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

Import("im.jsunit.AssertionFailedError");

/**
 * @fileoverview
 * このファイルまたはクラスの概要を記述♪
 */

Class("im.jsunit.Assert").define(
	/**
	 * コンストラクタの説明。
	 * @constructor
	 * @class クラスの説明
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.1
	 */
	im.jsunit.Assert = function () {
		/* コンストラクタ */
		{
			this.superclass();
		}
	}
);
/* static field and static method of im.jsunit.Assert class */
{
	/**
	 * Asserts that a condition is true. If it isn't it throws
	 * an {@link im.jsunit.AssertionFailedError}.
	 * @param {boolean} condition 
	 */
	im.jsunit.Assert.assertTrue = function (condition) {
		if (!condition) {
			im.jsunit.Assert.fail(null);
		}
	}
	/**
	 * Asserts that a condition is false. If it isn't it throws
	 * an {@link im.jsunit.AssertionFailedError}.
	 * @param {boolean} condition 
	 */
	im.jsunit.Assert.assertFalse = function (condition) {
		if (condition) {
			im.jsunit.Assert.fail(null);
		}
	}
	/**
	 * Asserts that two objects are equal. If they are not
	 * an {@link im.jsunit.AssertionFailedError} is thrown.
	 * @param {Object} expected 
	 * @param {Object} actual 
	 */
	im.jsunit.Assert.assertEquals = function (expected, actual) {
		if (expected == null && actual == null) {
			return;
		}
		if (expected != null) {
			if (expected.equals) {
				if (expected.equals(actual)) {
					return;
				}
			} else if (Object.equals(expected, actual)) {
				return;
			}
		}
		im.jsunit.Assert.failNotEquals(null, expected, actual);
	}
	/**
	 * Asserts that an object isn't null.
	 * @param {Object} object 
	 */
	im.jsunit.Assert.assertNotNull = function (object) {
		im.jsunit.Assert.assertTrue(object != null);
	}
	/**
	 * Asserts that an object is null.
	 * @param {Object} object 
	 */
	im.jsunit.Assert.assertNull = function (object) {
		im.jsunit.Assert.assertTrue(object == null);
	}
	/**
	 * Asserts that two objects refer to the same object. If they are not
	 * an {@link im.jsunit.AssertionFailedError} is thrown with the given message.
	 * @param {Object} expected 
	 * @param {Object} actual 
	 */
	im.jsunit.Assert.assertSame = function (expected, actual) {
		if (expected == actual) {
			return;
		}
		im.jsunit.Assert.failNotSame(expected, actual);
	}
	/**
	 * Asserts that two objects do not refer to the same object. If they do
	 * refer to the same object an {@link im.jsunit.AssertionFailedError} is thrown with the
	 * given message.
	 * @param {Object} expected 
	 * @param {Object} actual 
	 */
	im.jsunit.Assert.assertNotSame = function (expected, actual) {
		if (expected == actual) {
			im.jsunit.Assert.failSame(null);
		}
	}
	/** @private */
	im.jsunit.Assert.failSame = function (message) {
		var formatted = "";
 		if (message != null) {
			formatted = message + " ";
		}
 		im.jsunit.Assert.fail(formatted + "expected not same");
	}
	/** @private */
	im.jsunit.Assert.failNotSame = function (message, expected, actual) {
		var formatted = "";
		if (message != null) {
			formatted = message + " ";
		}
		im.jsunit.Assert.fail(formatted + "expected same:<" + expected + "> was not:<" + actual + ">");
	}
	/** @private */
	im.jsunit.Assert.failNotEquals = function (message, expected, actual) {
		var formatted = "";
		if (message != null) {
			formatted = message + " ";
		}
		im.jsunit.Assert.fail(formatted + "expected:<" + expected + "> but was:<" + actual + ">");
	}
	/**
	 * Fails a test with the given message.
	 * @param {String} message error message;
	 */
	im.jsunit.Assert.fail = function (message) {
		throw new im.jsunit.AssertionFailedError(message);
	}
	/**
	 * Globalize public assert functions for short cut call.
	 * @private
	 */
	im.jsunit.Assert.initialize = function () {
		assertTrue    = im.jsunit.Assert.assertTrue;
		assertFalse   = im.jsunit.Assert.assertFalse;
		assertEquals  = im.jsunit.Assert.assertEquals;
		assertNotNull = im.jsunit.Assert.assertNotNull;
		assertNull    = im.jsunit.Assert.assertNull;
		assertSame    = im.jsunit.Assert.assertSame;
		assertNotSame = im.jsunit.Assert.assertNotSame;
		fail          = im.jsunit.Assert.fail;
	}
}
