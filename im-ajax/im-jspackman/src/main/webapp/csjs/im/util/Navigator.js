/*
 * Copyright 2009 the OPEN INTRA-MART.
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
 * @fileoverview <br/>
 * navigator 情報へのアクセッサとお便利機能を提供。
 */

Class("im.util.Navigator").define(
	/**
	 * コンストラクタ。
	 * @constructor
	 * @class
	 * im.util.Navigator クラス。<br/><br/>
	 * @extends im.lang.PackmanObject
	 * @author emooru
	 * @version 0.1
	 */
	im.util.Navigator = function () {
		/* constructor */
		{
			this.superclass();
		}
	}
);
/* static field and static method of im.util.Navigator class */
{
	/** 
	 * ブラウザタイプをチェックした値を保持するための変数
	 * @private
	 */
	im.util.Navigator.navigatorType;
	/**
	 * ブラウザタイプ： 未知
	 * @final
	 * @type Number
	 */
	im.util.Navigator.UNKNOWN           = 0;
　	/** 
	 * ブラウザタイプ： Internet Explorer
	 * @final
	 * @type Number
	 */
	im.util.Navigator.INTERNET_EXPLORER = 1;
　	/** 
	 * ブラウザタイプ： Firefox
	 * @final
	 * @type Number
	 */
	im.util.Navigator.FIREFOX           = 2;
　	/** 
	 * ブラウザタイプ： Netscape
	 * @final
	 * @type Number
	 */
	im.util.Navigator.NETSCAPE          = 3;
　	/** 
	 * ブラウザタイプ： Safari
	 * @final
	 * @type Number
	 */
	im.util.Navigator.SAFARI            = 4;
　	/** 
	 * ブラウザタイプ： Opera
	 * @final
	 * @type Number
	 */
	im.util.Navigator.OPERA             = 5;
	/**
	 * ナビゲータタイプを取得します。<br/>
	 * <br/>
	 * navigator.userAgent で判断します。<br/>
	 * [注意] <br/>
	 * userAgentはユーザで変更できるため、必ずしも正しいナビゲータタイプが取得できるとは限りません。
	 * 
	 * @return ナビゲータタイプ
	 * @type Number
	 */
	im.util.Navigator.getNavigatorType = function () {
		return im.util.Navigator.navigatorType;
	}
	
	/**
	 * ナビゲータタイプが Internet Explorer かどうか取得します。
	 * 
	 * @return Internet Explorer かどうか
	 * @type Boolean
	 */
	im.util.Navigator.isIE = function () {
		return (im.util.Navigator.navigatorType == im.util.Navigator.INTERNET_EXPLORER);
	}
	/** @private */
	im.util.Navigator.initialize = function () {
		var userAgent = navigator.userAgent;
		var navigatorType;
		if (userAgent.indexOf("Opera") != -1) {
			navigatorType = im.util.Navigator.OPERA;
		}
		else if (userAgent.indexOf("MSIE") != -1) {
			navigatorType = im.util.Navigator.INTERNET_EXPLORER;
		}
		else if (userAgent.indexOf("Firefox") != -1) {
			navigatorType = im.util.Navigator.FIREFOX;
		}
		else if (userAgent.indexOf("Netscape") != -1) {
			navigatorType = im.util.Navigator.NETSCAPE;
		}
		else if (userAgent.indexOf("Safari") != -1) {
			navigatorType = im.util.Navigator.SAFARI;
		}
		else {
			navigatorType = im.util.Navigator.UNKNOWN;
		}
		im.util.Navigator.navigatorType = navigatorType;
	}
}
