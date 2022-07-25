package org.intra_mart.common.platform.log.appender;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import ch.qos.logback.classic.net.SMTPAppender;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Layout;

/**
 * ログをメールで送信するAppenderです。</br>
 * 設定は、ログ出力設定ファイル「im_logger_xxx.xml」で行います。</br>
 * Logback標準のSMTPAppenderとの違いは、</br>
 * このクラスを利用した場合は、以下の指定が可能となります。
 * </br></br>
 * 
 * <li>X-Body-Content-Typeヘッダのcharset指定</li>
 * <li>SMTPサーバの複数指定</li>
 * <br>
 * <br>
 * <h3>設定例（im_logger_xxx.xml）</h3>
 * <pre>
 * &lt;appender name="EMAIL" class="org.intra_mart.common.platform.log.appender.ContentTypeCharsetSMTPAppender"&gt;
 *     &lt;SMTPHosts&gt;
 *         &lt;SMTPHost>host1&lt;/SMTPHost&gt;
 *         &lt;SMTPHost>host2&lt;/SMTPHost&gt;
 *         &lt;SMTPHost>host3&lt;/SMTPHost&gt;
 *     &lt;/SMTPHosts&gt;	
 *     &lt;to&gt;user1@xxx.jp&lt;/to&gt;
 *     &lt;charset&gt;ISO-2022-JP&lt;/charset&gt;
 *     &lt;from&gt;user2@xxx.jp&lt;/from&gt;
 *     &lt;layout class="ch.qos.logback.classic.PatternLayout"&gt;
 *         &lt;Pattern&gt;%date %-5level %logger{255} - %message%n&lt;/Pattern&gt;
 *     &lt;/layout&gt;
 * &lt;/appender&gt;
 * </pre>
 * 
 */
public class ContentTypeCharsetSMTPAppender extends SMTPAppender {

	private String charset;
	private List<String> smtpHosts;

	/**
	 * コンストラクタ<br/>
	 * このコンストラクタでは、SMTPサーバ設定用のリストを生成します。
	 */
	public ContentTypeCharsetSMTPAppender() {
		this.smtpHosts = new ArrayList<String>();
	}

	/**
	 * SMTPサーバを設定します。<br/>
	 * 
	 * @param smtpHost SMTPサーバ
	 */
	public void setSMTPHost(String smtpHost) {
		this.smtpHosts.add(smtpHost);
	}

	/**
	 * エンコード方式を返します。
	 * 
	 * @return charset エンコード方式
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * エンコード方式を設定します。
	 * 
	 * @param charset
	 *            エンコード方式
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * ログのメール送信を行います。</br>
	 * メールのヘッダ情報はこのメソッドで生成します。
	 * 
	 * @param lastEventObject Loggingオブジェクト
	 */
	protected void sendBuffer(LoggingEvent lastEventObject) {

		Layout<LoggingEvent> layout = this.getLayout();

		StringBuffer sbuf = new StringBuffer();

		String header = layout.getFileHeader();
		if (header != null) {
			sbuf.append(header);
		}
		String presentationHeader = layout.getPresentationHeader();
		if (presentationHeader != null) {
			sbuf.append(presentationHeader);
		}

		fillBuffer(sbuf);

		String footer = layout.getFileFooter();
		if (footer != null) {
			sbuf.append(footer);
		}
		String presentationFooter = layout.getPresentationFooter();
		if (presentationFooter != null) {
			sbuf.append(presentationFooter);
		}

		Multipart mp = new MimeMultipart();
		MimeBodyPart part = new MimeBodyPart();

		Iterator<String> it = smtpHosts.iterator();
		Exception lastException = null;

		while (it.hasNext()) {
			try {
				String charset = getCharset();
				if (charset != null) {
					part.setContent(sbuf.toString(), layout.getContentType() + "; charset=" + charset);
				}
				else {
					part.setContent(sbuf.toString(), layout.getContentType());
				}

				mp.addBodyPart(part);

				String smtphost = it.next();
				Message msg = makeMimeMessage(smtphost, lastEventObject, mp);

				if (subjectLayout != null) {
					msg.setSubject(subjectLayout.doLayout(lastEventObject));
				}

				msg.setContent(mp);
				msg.setSentDate(new Date());
				Transport.send(msg);

				// ここまでくれば送信成功
				return;
			}
			catch (Exception e) {
				lastException = e;
				e.printStackTrace();

				// 再送信を行う
				continue;
			}
		}

		// 全て失敗
		addError("Error occured while sending e-mail notification.", lastException);

	}

	/**
	 * メール送信用にMimeMessageを生成します。
	 * 
	 * @param smtpHost
	 *            SMTPサーバ
	 * @param lastEventObject
	 *            Loggingオブジェクト
	 * @param mp
	 *            Multipart
	 * @return MimeMessage
	 * @throws MIME形式の電子メールメッセージ
	 */
	private Message makeMimeMessage(String smtpHost, LoggingEvent lastEventObject, Multipart mp) throws MessagingException {
		Properties props = new Properties(System.getProperties());

		props.put("mail.smtp.host", smtpHost);
		Session session = Session.getInstance(props, null);

		Message sendmsg = new MimeMessage(session);

		try {
			List<String> to = getTo();
			sendmsg.addRecipients(Message.RecipientType.TO, parseAddress(to));
			sendmsg.setFrom(getAddress(getFrom()));
			sendmsg.setSubject(subjectLayout.doLayout(lastEventObject));
			sendmsg.setContent(mp);
			sendmsg.setSentDate(new Date());

		}
		catch (MessagingException me) {
			throw me;
		}
		return sendmsg;
	}

	/**
	 * Fromメールアドレス文字列をInternetAddressオブジェクトに変換します。
	 * 
	 * @param addressStr
	 *            メールアドレス文字列
	 * @return InternetAddressオブジェクト
	 */
	private InternetAddress getAddress(String addressStr) {
		try {
			return new InternetAddress(addressStr);
		}
		catch (AddressException e) {
			addError("Could not parse address [" + addressStr + "].", e);
			return null;
		}
	}

	/**
	 * Toメールアドレスの配列をInternetAddressオブジェクトに変換します。
	 * 
	 * @param addressList
	 *            メールアドレスの配列
	 * @return iaArray InternetAddressオブジェクト
	 */
	private InternetAddress[] parseAddress(List<String> addressList) {

		InternetAddress[] iaArray = new InternetAddress[addressList.size()];

		for (int i = 0; i < addressList.size(); i++) {
			try {
				InternetAddress[] tmp = InternetAddress.parse(addressList.get(i), true);
				// one <To> element should contain one email address
				iaArray[i] = tmp[0];
			}
			catch (AddressException e) {
				addError("Could not parse address [" + addressList.get(i) + "].", e);
				return null;
			}
		}

		return iaArray;
	}

}
