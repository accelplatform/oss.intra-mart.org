package org.intra_mart.common.platform.log.appender;

import java.net.URL;

import junit.framework.TestCase;

import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.common.platform.log.util.LogbackUtil;

public class ContentTypeCharsetSMTPAppenderTest extends TestCase{

	public void testGetCharset() throws Exception {
		String resource = "/org/intra_mart/common/platform/log/appender/ContentTypeCharsetSMTPAppenderTest_logback.xml";
		URL config = this.getClass().getResource(resource);
		LogbackUtil.doLogbackConfigure(config);
		
		Logger logger = Logger.getLogger();
		
		logger.info("デフォルトでは、infoレベルの場合、メール送信されません");
		
		// 「ContentTypeCharsetSMTPAppenderTest_logback.xml」内で無効なSMTPホスト名を3つ指定しています。
		// 3回例外が標準エラーに出力されます。
		logger.error("デフォルトでは、errorレベル以上の場合メール送信されます");
	}

}
