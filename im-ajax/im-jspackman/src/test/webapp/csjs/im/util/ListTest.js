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

Package("im.util");

Import("im.util.List");
Import("im.util.Iterator");
Import("im.jsunit.TestCase");

/**
 * @fileoverview <br/>
 * サイズ変更可能な配列の実装です。
 */

Class("im.util.ListTest").extend("im.jsunit.TestCase").define(
	im.util.ListTest = function (name) {
		var list;
		var value0 = "aa";
		var value1 = ["bb"];
		var value2 = value0;
		var value3 = null;
		var value4 = undefined;
		var value5 = 100;
		var value6 = {key: "cc"};
		/* constructor */
		{
			this.superclass(name);
		}
		this.setUp = function () {
			list = new im.util.List();
			list.add(value0);
			list.add(value1);
			list.add(value2);
			list.add(value3);
			list.add(value4);
			list.add(value5);
			list.add(value6);
		}
		this.testAdd = function () {
			var expected = list.size() + 1;
			var value7 = "dd";
			list.add(value7);
			assertEquals(expected, list.size());
			assertEquals(value7, list.get(expected - 1));
		}
		this.testClear = function() {
			list.clear();
			assertEquals(0, list.size());
		}
		this.testContains = function () {
			var value7 = "dd";
			list.add(value7);
			assertTrue(list.contains(value7));
			assertFalse(list.contains("ee"));
		}
		this.testGet = function () {
			var value7 = "dd";
			list.add(value7);
			assertEquals(value7, list.get(list.size() - 1));
		}
		this.testIndexOf = function () {
			var value7 = "aa";
			list.add(value7);
			assertEquals(0, list.indexOf(value0));
			assertEquals(1, list.indexOf(value1));
			assertEquals(0, list.indexOf(value2));
			assertEquals(3, list.indexOf(value3));
			assertEquals(4, list.indexOf(value4));
			assertEquals(0, list.indexOf(value7));
			assertEquals(-1,list.indexOf("ee"));
		}
		this.testIsEmpty = function () {
			assertFalse(list.isEmpty());
			list.clear();
			assertTrue(list.isEmpty());
		}
		this.testLastIndexOf = function () {
			var value7 = "aa";
			list.add(value7);
			assertEquals(7, list.lastIndexOf(value0));
			assertEquals(1, list.lastIndexOf(value1));
			assertEquals(7, list.lastIndexOf(value2));
			assertEquals(3, list.lastIndexOf(value3));
			assertEquals(4, list.lastIndexOf(value4));
			assertEquals(7, list.lastIndexOf(value7));
			assertEquals(-1,list.lastIndexOf("ee"));
		}
		this.testRemove = function () {
			var value7 = "aa";
			var size = list.size();
			list.add(value7);
			var removedValue = list.remove(size);
			assertEquals(size, list.size());
			assertEquals(value7, removedValue);
		}
		this.testSet = function () {
			var idx = 3;
			var value7 = "aa";
			var oldValue = list.set(idx, value7);
			assertEquals(value3, oldValue);
			assertEquals(value7, list.get(idx));
		}
		this.testSize = function () {
			assertEquals(7, list.size());
		}
		this.testToArray = function () {
			var ary = list.toArray();
			assertTrue(ary instanceof Array);
			assertEquals(7, ary.length);
		}
		this.testIterator = function () {
			assertTrue(list.iterator() instanceof im.util.Iterator);
		}
	}
);
