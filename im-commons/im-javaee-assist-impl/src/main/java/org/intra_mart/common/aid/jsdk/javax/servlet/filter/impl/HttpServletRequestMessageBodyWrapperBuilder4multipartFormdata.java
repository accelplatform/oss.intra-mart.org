package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletRequestMessageBodyWrapper;


/**
 * HttpServletRequest を拡張したインタフェースです。
 * 
 */
public class HttpServletRequestMessageBodyWrapperBuilder4multipartFormdata extends AbstractHttpServletRequestMessageBodyWrapperBuilder{
	/**
	 * メッセージボディ部をキャッシュするためのファイルパス管理オブジェクト
	 */
	private TemporaryFileManager messageFileManager;
	/**
	 * パラメータ値をキャッシュするためのファイルパス管理オブジェクト
	 */
	private TemporaryFileManager parameterFileManager;

	/**
	 * 一時ファイルを作成する親ディレクトリ
	 */
	private File parentDirectory = new File(System.getProperty("user.dir"));

	/**
	 * インスタンス化のためのゼロパラメータコンストラクタ。
	 */
	public HttpServletRequestMessageBodyWrapperBuilder4multipartFormdata(){
		super();
	}

	/**
	 * このオブジェクトを初期化します。
	 * @throws IllegalStateException 設定または環境に不備があるため正常に動作できる状態ではない
	 */
	public void handleInit(){
		FilterConfig config = this.getFilterConfig();

		// 一時ファイルの保存先ディレクトリの決定
		String path = config.getInitParameter("tmp-directory");
		if(path != null){
			this.parentDirectory = new File(path);
			if(this.parentDirectory.exists()){
				if(this.parentDirectory.isFile()){
					throw new IllegalStateException(path + " is not directory.");
				}
			}
			else{
				if(! this.parentDirectory.mkdirs()){
					throw new IllegalStateException("Create directory error: " + path);
				}
			}
		}

		// メッセージ保存ファイル名フォーマッタの決定
		this.messageFileManager = this.createTemporaryFileManager(config.getInitParameter("tmp-message"), "message_{0}.log");

		// パラメータ保存ファイル名フォーマッタの決定
		this.parameterFileManager = this.createTemporaryFileManager(config.getInitParameter("tmp-parameter"), "parameter_{0}.log");
	}

	/**
	 * 指定のパターンを用いて一時ファイル管理オブジェクトを作成します。
	 * 引数 pattern が null だった場合、defaultPattern を用いて
	 * オブジェクトを作成します。
	 * @param pattern ファイル名のフォーマットパターン文字列
	 * @param defaultPattern ファイル名のフォーマットパターン文字列
	 * @return
	 */
	private TemporaryFileManager createTemporaryFileManager(String pattern, String defaultPattern){
		if(pattern != null){
			try{
				return new TemporaryFileManager(this.parentDirectory, new MessageFormat(pattern));
			}
			catch(IllegalArgumentException  iae){
				throw new RuntimeException("Invalid format:" + pattern, iae);
			}
		}
		else{
			try{
				return new TemporaryFileManager(this.parentDirectory, new MessageFormat(defaultPattern));
			}
			catch(IllegalArgumentException  iae){
				throw new RuntimeException("Invalid format:" + defaultPattern, iae);
			}
		}
	}

	/**
	 * ExtendedHttpServletRequest を作成して返します。<p>
	 * @param request リクエスト
	 * @throws IOException 入出力エラー
	 * @throws ServletException その他の実行時エラー
	 */
	public HttpServletRequestMessageBodyWrapper buildExtendedHttpServletRequest(HttpServletRequest request, HttpServletResponse response) 
		throws IOException, ServletException{

		// メッセージボディ情報の格納先ファイル
		File file = this.messageFileManager.getPath();
		
		// 初期化パラメータ「parent.request.parameter」のboolean値
		boolean parentRequestParameter = 
			Boolean.valueOf(this.getFilterConfig().getInitParameter("parent.request.parameter")).booleanValue();
		
		// 初期化パラメータ「parse.query.string」のboolean値
		boolean parseQueryString = 
			Boolean.valueOf(this.getFilterConfig().getInitParameter("parse.query.string")).booleanValue();
		
		// メッセージボディ、および、エントリの情報をメモリ上からファイルに退避させるファイルサイズの閾値
		int threshold = 0;
		try {
			threshold = Integer.parseInt(this.getFilterConfig().getInitParameter("TemporaryFileThreshold"));
		} catch (NumberFormatException e) {
			// Do Nothing
		}

		// ファイル入出力時のバッファサイズ
		int bufferSize = 8 * 1024;
		try {
			bufferSize = Integer.parseInt(this.getFilterConfig().getInitParameter("TemporaryFileIOBufferSize"));
		} catch (NumberFormatException e) {
			// Do Nothing
		}
		
		
		return new HttpServletRequestMessageBodyWrapper4multipartFormdata(request,
																		   file,
																		   this.parameterFileManager,
																		   parentRequestParameter,
																		   parseQueryString,
																		   threshold,
																		   bufferSize);
	}

	/**
	 * このオブジェクトを初期化します。
	 */
	public void handleDestroy(){
		FilterConfig config = this.getFilterConfig();

		if(Boolean.valueOf(config.getInitParameter("temp-destory")).booleanValue()){
			// ファイルを削除する
			File[] files = this.parentDirectory.listFiles();
			for(int idx = files.length - 1; idx >= 0; idx--){
				files[idx].delete();
			}
		}

		this.parentDirectory = null;
	}
}

