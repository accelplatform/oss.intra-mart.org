package org.intra_mart.common.platform.log;

import junit.framework.TestCase;

/**
 * ログを出力するだけのテストケースです。
 * assertXXX()はありません。
 */
public class LoggerTest extends TestCase {

	private Logger logger = Logger.getLogger();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		logger.info("===================================================================");
	}
	
	public void testAPIリストに記載されているサンプルコード(){
		Logger logger = Logger.getLogger();
		
		logger.error("メッセージです");
		logger.warn("パラメータは「{}」です。", "その１");
		logger.info("パラメータは「{}」 と 「{}」です。", "その１", "その２");
		
		Object[] argArray = new Object[4];
		argArray[0] = "第1パラメータ";
		argArray[1] = 5.678;
		argArray[2] = new java.util.Date();
		argArray[3] = true;
		logger.debug("パラメータは「{}」 と 「{}」 と 「{}」 と 「{}」です。", argArray);
		
		logger.log(Logger.Level.TRACE, "引数で指定されたレベルのログを出力します");

		Throwable t = new Exception("例外");
		logger.error("例外をログ出力します", t);
		logger.log(Logger.Level.TRACE, "引数で指定されたレベルの例外をログ出力します。", t);
	}
	
	public void testログ出力_String(){
		logger.error("<error>");
		logger.warn("<warn>");
		logger.info("<info>");
		logger.debug("<debug>");
		logger.trace("<trace>");
	}

	public void testログ出力_レベル指定_String(){
		logger.log(Logger.Level.ERROR, "<ERROR>");
		logger.log(Logger.Level.WARN, "<WARN>");
		logger.log(Logger.Level.INFO, "<INFO>");
		logger.log(Logger.Level.DEBUG, "<DEBUG>");
		logger.log(Logger.Level.TRACE, "<TRACE>");
	}

	public void testログ出力_String_Object(){
		logger.error("<error> 1:{}", "AAAA");
		logger.warn("<warn> 1:{}", "AAAA");
		logger.info("<info> 1:{}", "AAAA");
		logger.debug("<debug> 1:{}", "AAAA");
		logger.trace("<trace> 1:{}", "AAAA");
	}

	public void testログ出力_レベル指定_String_Object(){
		logger.log(Logger.Level.ERROR, "<ERROR> 1:{}", "AAAA");
		logger.log(Logger.Level.WARN, "<WARN> 1:{}", "AAAA");
		logger.log(Logger.Level.INFO, "<INFO> 1:{}", "AAAA");
		logger.log(Logger.Level.DEBUG, "<DEBUG> 1:{}", "AAAA");
		logger.log(Logger.Level.TRACE, "<TRACE> 1:{}", "AAAA");
	}

	public void testログ出力_String_Object_Object(){
		logger.error("<error> 1:{}, 2:{}", "AAAA", "BBBB");
		logger.warn("<warn> 1:{}, 2:{}", "AAAA", "BBBB");
		logger.info("<info> 1:{}, 2:{}", "AAAA", "BBBB");
		logger.debug("<debug> 1:{}, 2:{}", "AAAA", "BBBB");
		logger.trace("<trace> 1:{}, 2:{}", "AAAA", "BBBB");
	}

	public void testログ出力_レベル指定_String_Object_Object(){
		logger.log(Logger.Level.ERROR, "<ERROR> 1:{}, 2:{}", "AAAA", "BBBB");
		logger.log(Logger.Level.WARN, "<WARN> 1:{}, 2:{}", "AAAA", "BBBB");
		logger.log(Logger.Level.INFO, "<INFO> 1:{}, 2:{}", "AAAA", "BBBB");
		logger.log(Logger.Level.DEBUG, "<DEBUG> 1:{}, 2:{}", "AAAA", "BBBB");
		logger.log(Logger.Level.TRACE, "<TRACE> 1:{}, 2:{}", "AAAA", "BBBB");
	}

	public void testログ出力_String_Object_Object_Object(){
		logger.error("<error> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
		logger.warn("<warn> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
		logger.info("<info> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
		logger.debug("<debug> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
		logger.trace("<trace> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
	}

	public void testログ出力_レベル指定_String_Object_Object_Object(){
		logger.log(Logger.Level.ERROR, "<ERROR> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
		logger.log(Logger.Level.WARN, "<WARN> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
		logger.log(Logger.Level.INFO, "<INFO> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
		logger.log(Logger.Level.DEBUG, "<DEBUG> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
		logger.log(Logger.Level.TRACE, "<TRACE> 1:{}, 2:{}, 3:{}", "AAAA", "BBBB", "CCCC");
	}
	
	public void testログ出力_String_Object4times_可変長引数(){
		logger.error("可変長引数 <error> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
		logger.warn("可変長引数 <warn> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
		logger.info("可変長引数 <info> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
		logger.debug("可変長引数 <debug> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
		logger.trace("可変長引数 <trace> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
	}

	public void testログ出力_レベル指定_String_Object4times_可変長引数(){
		logger.log(Logger.Level.ERROR, "可変長引数 <ERROR> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
		logger.log(Logger.Level.WARN, "可変長引数 <WARN> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
		logger.log(Logger.Level.INFO, "可変長引数 <INFO> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
		logger.log(Logger.Level.DEBUG, "可変長引数 <DEBUG> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
		logger.log(Logger.Level.TRACE, "可変長引数 <TRACE> 1:{}, 2:{}, 3:{}, 4{}", "AAAA", "BBBB", "CCCC", "DDDD");
	}

	public void testログ出力_String_Object4times_配列(){
		Object[] argArys = new String[]{"AAAA", "BBBB", "CCCC", "DDDD"};
		logger.error("配列 <error> 1:{}, 2:{}, 3:{}, 4{}", argArys);
		logger.warn("配列 <warn> 1:{}, 2:{}, 3:{}, 4{}", argArys);
		logger.info("配列 <info> 1:{}, 2:{}, 3:{}, 4{}", argArys);
		logger.debug("配列 <debug> 1:{}, 2:{}, 3:{}, 4{}", argArys);
		logger.trace("配列 <trace> 1:{}, 2:{}, 3:{}, 4{}", argArys);
	}

	public void testログ出力_レベル指定_String_Object4times_配列(){
		Object[] argArys = new String[]{"AAAA", "BBBB", "CCCC", "DDDD"};
		logger.log(Logger.Level.ERROR, "配列 <ERROR> 1:{}, 2:{}, 3:{}, 4{}", argArys);
		logger.log(Logger.Level.WARN, "配列 <WARN> 1:{}, 2:{}, 3:{}, 4{}", argArys);
		logger.log(Logger.Level.INFO, "配列 <INFO> 1:{}, 2:{}, 3:{}, 4{}", argArys);
		logger.log(Logger.Level.DEBUG, "配列 <DEBUG> 1:{}, 2:{}, 3:{}, 4{}", argArys);
		logger.log(Logger.Level.TRACE, "配列 <TRACE> 1:{}, 2:{}, 3:{}, 4{}", argArys);
	}

	public void testログ出力_String_Throwable(){
		logger.error("<error>", new Exception("<error>"));
		logger.warn("<warn>", new Exception("<warn>"));
		logger.info("<info>", new Exception("<info>"));
		logger.debug("<debug>", new Exception("<debug>"));
		logger.trace("<trace>", new Exception("<trace>"));
	}

	public void testログ出力_レベル指定_String_Throwable(){
		logger.log(Logger.Level.ERROR, "<ERROR>", new Exception("<ERROR>"));
		logger.log(Logger.Level.WARN, "<WARN>", new Exception("<WARN>"));
		logger.log(Logger.Level.INFO, "<INFO>", new Exception("<INFO>"));
		logger.log(Logger.Level.DEBUG, "<DEBUG>", new Exception("<DEBUG>"));
		logger.log(Logger.Level.TRACE, "<TRACE>", new Exception("<TRACE>"));		
	}
}
