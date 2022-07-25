package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.intra_mart.common.aid.jdk.util.charset.CharacterMappingBuilder;
import org.intra_mart.common.platform.log.Logger;


/**
 *
 * @see AbstractFilter
 */
public class LuxuryResponseWriterFilter extends AbstractFilter {

	/**
	 * デフォルトコンストラクタ
	 */
	public LuxuryResponseWriterFilter() {
		super();
	}

	/**
	 * @see javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException{
		// レスポンスをラップ
		HttpServletResponse wrapper = new HttpServletResponseWrapper4LuxuryResponseWriterFilter((HttpServletResponse) response);
		chain.doFilter(request, wrapper);					// 実行
	}

}

/**
 * コンテンツの文字エンコーディングを補助するレスポンス
 */
class HttpServletResponseWrapper4LuxuryResponseWriterFilter extends HttpServletResponseWrapper{
	private static Logger _logger = Logger.getLogger();
	
	/**
	 * コンストラクタ
	 */
	protected HttpServletResponseWrapper4LuxuryResponseWriterFilter(HttpServletResponse response){
		super(response);
	}

	/**
	 *
	 * @return バイナリデータ出力に使用する ServletOutputStream
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public ServletOutputStream getOutputStream() throws IOException{
		
		// ServletOutputStreamの取得 
		ServletOutputStream sos = this.getResponse().getOutputStream();
		
		// エンコーディング名の取得
		String enc = this.getResponse().getCharacterEncoding();
		
		try{
			// 文字マッピング配列の取得
			CharacterMappingBuilder builder = CharacterMappingBuilder.instance();
			char[] map = builder.getMapping(enc);
			
			// 出力ストリームのラッパーを作成→返却
			return new ExtendedServletOutputStream(sos, map);
		}
		catch(Exception e){
			_logger.error("WARNING: Unsupported charset error: charset=" + enc + "/exception=" + e.getClass().getName() + "/message=" + e.getMessage());
			return this.getResponse().getOutputStream();
		}
	}

	/**
	 *
	 * @return クライアントに文字データを送り返すことができる PrintWriter オブジェクト
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public PrintWriter getWriter() throws IOException{
		
		// Writerの取得
		Writer writer = this.getResponse().getWriter();
		
		// エンコーディング名の取得
		String enc = this.getResponse().getCharacterEncoding();

		try{
			// 文字マッピング配列の取得
			CharacterMappingBuilder builder = CharacterMappingBuilder.instance();
			char[] map = builder.getMapping(enc);

			// 出力ストリームのラッパーを作成→返却
			return new PrintWriter(new ExtendedServletResponsePrintWriter(writer, map));
		}
		catch(Exception e){
			_logger.error("WARNING: Unsupported charset error: charset=" + enc + "/exception=" + e.getClass().getName() + "/message=" + e.getMessage());
			return this.getResponse().getWriter();
		}
	}


	private static class ExtendedServletOutputStream extends ServletOutputStream{
		private ServletOutputStream out;
		private char[] characterMapping;

		/**
		 * コンストラクタ
		 */
		protected ExtendedServletOutputStream(ServletOutputStream out, char[] map){
			super();
			this.out = out;
			this.characterMapping = map;
		}

		/**
		 * このオブジェクトがラップしている ServletOutputStream を返します。
		 * @return 出力
		 */
		public ServletOutputStream getOutputStream(){
			return this.out;
		}

		/**
		 * @see javax.servlet.ServletOutputStream#print(boolean)
		 */
		public void print(boolean b) throws IOException {
			this.getOutputStream().print(b);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#print(char)
		 */
		public void print(char c) throws IOException {
			try{
				this.getOutputStream().print(this.characterMapping[c]);
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				this.getOutputStream().print(c);	// マッピングの変更はなし
			}
		}

		/**
		 * @see javax.servlet.ServletOutputStream#print(double)
		 */
		public void print(double n) throws IOException {
			this.getOutputStream().print(n);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#print(float)
		 */
		public void print(float n) throws IOException {
			this.getOutputStream().print(n);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#print(int)
		 */
		public void print(int n) throws IOException {
			this.getOutputStream().print(n);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#print(long)
		 */
		public void print(long n) throws IOException {
			this.getOutputStream().print(n);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#print(String)
		 */
		public void print(String s) throws IOException {
			char[] chars = s.toCharArray();

			for(int idx = chars.length - 1; idx >= 0; idx--){
				try{
					chars[idx] = this.characterMapping[chars[idx]];
				}
				catch(ArrayIndexOutOfBoundsException aioobe){
					continue;						// マッピングの変更はなし
				}
			}

			this.getOutputStream().print(new String(chars));
		}

		/**
		 * @see javax.servlet.ServletOutputStream#println()
		 */
		public void println() throws IOException {
			this.getOutputStream().println();
		}

		/**
		 * @see javax.servlet.ServletOutputStream#println(boolean)
		 */
		public void println(boolean b) throws IOException {
			this.getOutputStream().println(b);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#println(char)
		 */
		public void println(char c) throws IOException {
			try{
				this.getOutputStream().println(this.characterMapping[c]);
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				this.getOutputStream().println(c);	// マッピングの変更はなし
			}
		}

		/**
		 * @see javax.servlet.ServletOutputStream#println(double)
		 */
		public void println(double n) throws IOException {
			this.getOutputStream().println(n);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#println(float)
		 */
		public void println(float n) throws IOException {
			this.getOutputStream().println(n);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#println(int)
		 */
		public void println(int n) throws IOException {
			this.getOutputStream().println(n);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#println(long)
		 */
		public void println(long n) throws IOException {
			this.getOutputStream().println(n);
		}

		/**
		 * @see javax.servlet.ServletOutputStream#println(String)
		 */
		public void println(String s) throws IOException {
			char[] chars = s.toCharArray();

			for(int idx = chars.length - 1; idx >= 0; idx--){
				try{
					chars[idx] = this.characterMapping[chars[idx]];
				}
				catch(ArrayIndexOutOfBoundsException aioobe){
					continue;						// マッピングの変更はなし
				}
			}

			this.getOutputStream().println(new String(chars));
		}

		/**
		 * @see java.io.OutputStream#write(byte[], int, int)
		 */
		public void write(byte[] b, int off, int len) throws IOException {
			this.getOutputStream().write(b, off, len);
		}

		/**
		 * @see java.io.OutputStream#write(byte[])
		 */
		public void write(byte[] b) throws IOException {
			this.getOutputStream().write(b);
		}

		/**
		 * @see java.io.OutputStream#write(int)
		 */
		public void write(int b) throws IOException {
			this.getOutputStream().write(b);
		}

		/**
		 * @see java.io.OutputStream#flush()
		 */
		public void flush() throws IOException{
			this.getOutputStream().flush();
		}

		/**
		 * @see java.io.OutputStream#close()
		 */
		public void close() throws IOException{
			ServletOutputStream sos = this.getOutputStream();
			sos.flush();
			sos.close();
		}
	}

	private static class ExtendedServletResponsePrintWriter extends FilterWriter{
		private Writer responseWriter;
		private char[] characterMap;

		/**
		 * コンストラクタ
		 */
		protected ExtendedServletResponsePrintWriter(Writer writer, char[] map){
			super(writer);
			this.responseWriter = writer;
			this.characterMap = map;
		}

		/**
		 * このオブジェクトがラップしている Writer を返します。
		 * @return Writer
		 */
		public Writer getWriter(){
			return this.responseWriter;
		}

		/**
		 * @see java.io.PrintWriter#print(char)
		 */
		public void write(int c) throws IOException{
			try{
				this.getWriter().write(this.characterMap[c]);
			}
			catch(ArrayIndexOutOfBoundsException aioobe){
				this.getWriter().write(c);			// マッピングの変更はなし
			}
		}

		/**
		 * @see java.io.Writer#write(char[], int, int)
		 */
		public void write(char[] cbuf, int off, int len) throws IOException{
			// 終端の決定
			int terminate = off + len;

			// マッピングを修正した新しい文字配列を作成
			char[] chars = new char[terminate];
			for(int idx = off; idx < terminate; idx++){
				try{
					chars[idx] = this.characterMap[cbuf[idx]];
				}
				catch(ArrayIndexOutOfBoundsException aioobe){
					chars[idx] = cbuf[idx];			// マッピングの変更はなし
				}
			}

			this.getWriter().write(chars, off, len);
		}

		/**
		 * @see java.io.Writer#write(String, int, int)
		 */
		public void write(String s, int off, int len) throws IOException{
			// 終端の決定
			int terminate = off + len;

			// マッピングを修正した新しい文字配列を作成
			char[] chars = s.toCharArray();
			for(int idx = off; idx < terminate; idx++){
				try{
					chars[idx] = this.characterMap[chars[idx]];
				}
				catch(ArrayIndexOutOfBoundsException aioobe){
					continue;						// マッピングの変更はなし
				}
			}

			this.getWriter().write(chars, off, len);
		}
	}
}
