package org.intra_mart.jssp.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.util.SimpleLog;

/**
 * JavaScript 任意ソースのレスポンス要求の例外
 */
public class JavaScriptTransmissionException extends JSSPTransitionalException{

	private String stream;	// 送信ソース

	/**
	 * 送信ソースは、レスポンスヘッダも含みます。
	 * @param stream 送信ソース
	 */
	public JavaScriptTransmissionException(String stream){
		super();
		if(stream != null){ this.stream = stream; }
		else{ this.stream = ""; }
	}

	/**
	 * クライアントへのレスポンスソースの返却メソッド<br/>
	 * レスポンスには、HTTP ヘッダおよびデータを含む
	 * @return クライアントへのレスポンス・ソース
	 */
	public String getSource(){
		return stream;
	}

	/**
	 * @param resp
	 */
	public void send(HttpServletResponse resp){
		
		boolean committed = resp.isCommitted();
		if(committed == true){
			return;
		}

		StringReader sr = new StringReader(stream);
		BufferedReader bf = new BufferedReader(sr);

		// ヘッダ部の解析
		try{
			while(bf.ready()){
				String line = bf.readLine();
				if(line != null){
					if(line.length() != 0){
						// ヘッダ部
						StringTokenizer st = new StringTokenizer(line, ":", true);
						if(st.countTokens() >= 3){
							try{
								// 名称取得
								String name = st.nextToken().trim();
								st.nextToken();				// デリミタの取得
								// 値取得
								StringBuffer sb = new StringBuffer();
								while(st.hasMoreTokens()){
									sb.append(st.nextToken());
								}
								if(name.equals("Content-Type")){
									resp.setContentType(sb.toString().trim());
								}
								else{
									resp.setHeader(name, sb.toString().trim());
								}
							}
							catch(NoSuchElementException nsee){
								// 数を計算しているので有り得ない
							}
						}
					}
					else{
						// 本文
						OutputStream os = resp.getOutputStream();
						try{
							while(bf.ready()){
								int chr = bf.read();
								if(chr >= 0){ os.write(chr); }
								else{ break; }
							}
							os.flush();
						}
						catch(IOException ioe){
							// TODO [OSS-JSSP] ログ機能実装
							SimpleLog.logp(Level.WARNING, "Error: " + ioe.getMessage(), ioe);
						}
						finally{
							try{
								os.close();
							}
							catch(IOException ioe){
								// なんで？？？
							}
							bf.close();
//							break;
						}
					}
				}
			}
		}
		catch(IOException ioe){
			// よほどの理由がない限り発生しないハズ・・・
		}
	}
}
