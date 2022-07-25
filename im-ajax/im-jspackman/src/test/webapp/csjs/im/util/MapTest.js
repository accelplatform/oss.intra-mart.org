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

Import("im.util.Map");
Import("im.jsunit.TestCase");

/**
 * @fileoverview <br/>
 * サイズ変更可能な配列の実装です。
 */

Class("im.util.MapTest").extend("im.jsunit.TestCase").define(
	im.util.MapTest = function (name) {
		var map;
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
			map = new im.util.Map();
			map.put("value0", value0);
			map.put("value1", value1);
			map.put("value2", value2);
			map.put("value3", value3);
			map.put("value4", value4);
			map.put("value5", value5);
			map.put("value6", value6);
		}
		this.testClear = function () {
			map.clear();
			assertEquals(0, map.size());
		}
		this.testContainsKey = function () {
			assertTrue(map.containsKey("value0"));
			assertFalse(map.containsKey("value10"));
		}
		this.testGet = function () {
			assertEquals(value0, map.get("value0"));
			assertEquals(null, map.get("nothing"));
		}
		this.testIsEmpty = function () {
			assertFalse(map.isEmpty());
			map.clear();
			assertTrue(map.isEmpty());
		}
		this.testKeyList = function () {
			var keyList = map.keyList();
			assertTrue(keyList instanceof Array);
			assertEquals(7, keyList.length);
		}
		this.testPut = function () {
			var value7 = "aa";
			var oldValue = map.put("value7", value7);
			assertEquals(null, oldValue);
			assertEquals(value7, map.put("value7", "bbb"));
		}
		this.testRemove = function () {
			assertEquals(value1, map.remove("value1"));
			assertEquals(null,   map.remove("value1"));
		}
		this.testSize = function () {
			assertEquals(7, map.size());
		}
		this.testValues = function () {
			var values = map.values();
			assertTrue(values instanceof Array);
			assertEquals(7, values.length);
		}
	}
);
