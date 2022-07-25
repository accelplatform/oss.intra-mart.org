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

/**
 * @fileoverview
 * Stack クラスのメソッドによってスローされ、そのスタックが空であることを示します。
 */

Class("im.util.EmptyStackException").extend("im.lang.RuntimeException").define(
	/**
	 * エラーメッセージ文字列に null を使って、新しい EmptyStackException を構築します。 
	 * @constructor
	 * @class 
	 * Stack クラスのメソッドによってスローされ、そのスタックが空であることを示します。 
	 * @extends im.lang.IndexOutOfBoundsException
	 * @author emooru
	 * @version 0.1
	 */
	im.util.EmptyStackException = function () {
		/* constructor */
		{
			this.superclass(null);
			this.name = im.util.EmptyStackException.clazz.getName();
		}
	}
);
