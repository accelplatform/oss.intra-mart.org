package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.exception.EmptyQueryException;
import org.intra_mart.common.aid.jsdk.javax.servlet.exception.ExcessiveQueryException;
import org.intra_mart.common.aid.jsdk.javax.servlet.exception.LengthRequiredException;
import org.intra_mart.common.aid.jsdk.javax.servlet.exception.UnripeQueryException;
import org.intra_mart.common.aid.jsdk.javax.servlet.filter.RequestParameter;


/**
 * HttpServletRequest を拡張したインタフェースです。
 */
public class HttpServletRequestMessageBodyWrapper4multipartFormdata 
			extends AbstractHttpServletRequestMessageBodyWrapper{
	
	/**
	 * 入力されたメッセージボディを保存するファイルパス
	 */
	private File msgBodyTempFile;
	
	/**
	 * エントリを保存する一時ファイル名を管理します。
	 */
	private TemporaryFileManager fileManager;

	/**
	 * ファイル入出力時のバッファサイズ
	 */
	private int bufferSize = 8 * 1024;
		
	/**
	 * メッセージボディ、および、エントリの情報を、
	 * メモリ上からファイルに退避させるファイルサイズの閾値<BR>
	 * この閾値より大きい場合、一時ファイルが生成されます。
	 */
    private int threshold = 0;
	
	/**
     * 指定された閾値に達するまではデータをメモリ上に保持し、 
     * 閾値に達した場合にはディスク上に出力する出力ストリームです。 
	 */
	private DefferdFileOutputStream defferdFileOutputStream4MsgBody;
	
	/** バイト配列を文字コード変換せずに取得する為の静的変数 */
	private static String NOT_CONVERT_CHARSET = "ISO-8859-1";
	
    /**
	 * @param request リクエスト
	 * @param file 入力されたメッセージボディを保存するファイルパス
	 * @param fileManager エントリを保存する一時ファイル名を管理するマネージャ
	 * @param parentRequestParameter 初期化パラメータ「parent.request.parameter」のboolean値
	 * @param parseQueryString 初期化パラメータ「parse.query.string」のboolean値
	 * @param threshold メッセージボディ、および、エントリの情報をメモリ上からファイルに退避させるファイルサイズの閾値
	 * @param bufferSize ファイル入出力時のバッファサイズ
	 * @throws ServletException
	 * @throws IOException
	 */
	public HttpServletRequestMessageBodyWrapper4multipartFormdata(HttpServletRequest request, 
																	File file, 
																	TemporaryFileManager fileManager, 
																	boolean parentRequestParameter,
																	boolean parseQueryString,
																	int threshold,
																	int bufferSize) 
				throws ServletException, IOException {
		
		super(request, parentRequestParameter, parseQueryString);
		
		this.msgBodyTempFile = file;
		this.fileManager = fileManager;
	    this.threshold = threshold;
		this.bufferSize = bufferSize;
	    this.defferdFileOutputStream4MsgBody = new DefferdFileOutputStream(this.msgBodyTempFile, 
	    																 	this.threshold, 
	    																 	this.bufferSize);
  
        int contentLength = request.getContentLength();
        if (contentLength >= 0) {
			
			ServletInputStream sis = request.getInputStream();			

			try{
				byte[] bytes = new byte[this.bufferSize]; // バッファ確保
				
				while(true){
					int len = sis.read(bytes);
					if(len != -1) {
						// 閾値チェック
						defferdFileOutputStream4MsgBody.checkThreshold(len); 

						// データ出力
						defferdFileOutputStream4MsgBody.getCurrentStream().write(bytes, 0, len);
						
						// データ出力済みサイズ設定
						defferdFileOutputStream4MsgBody.addWrittenSize(len);
					}
					else {
						break;
					}
				}
			}
			finally {
				// ストリームをクローズ
				defferdFileOutputStream4MsgBody.getCurrentStream().close();
				sis.close();
			}

			// 受信データチェック
			long messageSize = defferdFileOutputStream4MsgBody.getLength();

			// 入力データが予定よりも少ない
			if(messageSize < contentLength) {
				if(messageSize == 0) {
					throw new EmptyQueryException("Request query was unreceivable: 0/" + contentLength);
				}
				else {
					throw new UnripeQueryException("The input was completed while acquiring the request query: " + messageSize + "/" + contentLength);
				}
			}
			// データ受信完了→超過データのチェック
			if(messageSize > contentLength) {
				throw new ExcessiveQueryException("The input of a request query is over CONTENT_LENGTH: " + messageSize + "/" + contentLength);
			}
		} 
        
        // CONTENT_LENGTHが不明
		else {
			throw new LengthRequiredException("Size specification of a query is unknown: " + contentLength);
		}
		
	}
	
	/* (非 Javadoc)
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.HttpServletRequestMessageBodyWrapper#getMessageBody()
	 */
	public InputStream getMessageBody() throws IOException{

		InputStream is = null;

		// メッセージボディの情報がメモリ上に保持されている場合
		if (defferdFileOutputStream4MsgBody.isInMemory()) {
			is = new ByteArrayInputStream(this.defferdFileOutputStream4MsgBody.getData());
	    } 		
		// メッセージボディの情報が一時ファイルに出力されている場合
	    else {
    		is = new FileInputStream(this.defferdFileOutputStream4MsgBody.getFile());
	    }

		// メッセージボディを含む入力ストリームを返却
		return new BufferedInputStream(is, this.bufferSize);
	}

	
	/* (非 Javadoc)
	 * @see org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl.AbstractHttpServletRequestMessageBodyWrapper#getRequestParameters(java.io.InputStream)
	 */
	protected RequestParameter[] getRequestParameters(InputStream in) throws IOException{
		
		String boundary = getBoundary();

		// 解析(InputStreamを利用)
		return analyzeMessage(boundary, in);
	}

	/**
	 * バウンダリを取得します。
	 * @return バウンダリ
	 */
	private String getBoundary() {
		
		// CONTENT-TYPE の確認
		String content_type = this.getContentType();
		
		// バウンダリの摘出
		String boundary;
		
		if(content_type.endsWith(";")) {
			// 終端を削除しつつ前後の空白を除去して抽出
			boundary = "--" + content_type.substring(content_type.lastIndexOf("boundary=") + 9, content_type.length() - 1).trim();
		}
		else {
			// 前後の空白を除去して抽出
			boundary = "--" + content_type.substring(content_type.lastIndexOf("boundary=") + 9).trim();
		}
		return boundary;
	}


	/**
	 * multipart/form-data を解析します。<BR>
	 * 解析中にファイルアップロードのエントリを発見した場合、
	 * 該当エントリのRequestParameter情報は、エントリのサイズによって格納場所が異なります。
	 * エントリのサイズが閾値より大きい場合は、一時ファイルへ、
	 * エントリのサイズが閾値以下の場合は、　　メモリ上に保持されます。
	 * @param boundary バウンダリ文字列
	 * @param in 入力
	 */
	protected RequestParameter[] analyzeMessage(String boundary, InputStream in) throws IOException {

		Collection collection = new ArrayList();		// 器
		String terminate = boundary.concat("--");		// 終端コード

		CharArrayWriter writer = new CharArrayWriter(this.bufferSize);
		byte[] chrArray = new byte[this.bufferSize];
		while(true){
			int len = in.read(chrArray);
			if(len != -1){
				writer.write(new String(chrArray, NOT_CONVERT_CHARSET), 0, len);
			}
			else{
				break;
			}
		}
		String qString = writer.toString();		// 文字列化
		writer = null;							// 破棄

		// 各データの摘出
		Collection streams = new ArrayList();
		int stPt = 0;
		int edPt = 0;
		int viewPt = 0;
		String boundaryCR = boundary.concat("\r");
		String boundaryLF = boundary.concat("\n");
		String boundaryCRLF = boundary.concat("\r\n");
		String terminateCR = terminate.concat("\r");
		String terminateLF = terminate.concat("\n");
		String terminateCRLF = terminate.concat("\r\n");
		do{
			// 終端の検索
			if((viewPt = qString.indexOf(boundaryCRLF, edPt)) != -1){
				edPt = viewPt + boundaryCRLF.length();
			}
			else if((viewPt = qString.indexOf(boundaryCR, edPt)) != -1){
				edPt = viewPt + boundaryCR.length();
			}
			else if((viewPt = qString.indexOf(boundaryLF, edPt)) != -1){
				edPt = viewPt + boundaryLF.length();
			}
			else if((viewPt = qString.indexOf(terminateCRLF, edPt)) != -1){
				edPt = viewPt + terminateCRLF.length();
			}
			else if((viewPt = qString.indexOf(terminateCR, edPt)) != -1){
				edPt = viewPt + terminateCR.length();
			}
			else if((viewPt = qString.indexOf(terminateLF, edPt)) != -1){
				edPt = viewPt + terminateLF.length();
			}
			else if((viewPt = qString.indexOf(terminate, edPt)) != -1){
				edPt = viewPt + terminate.length();
			}
			else{
				edPt = qString.length();
			}

			// 部分情報の取得
			if(stPt < viewPt){
				if(qString.charAt(viewPt - 1) == '\n'){ viewPt--; }
				if(qString.charAt(viewPt - 1) == '\r'){ viewPt--; }
				streams.add(qString.substring(stPt, viewPt));
			}

			stPt = edPt;	// 次の検索開始位置を設定
		} while(edPt != qString.length());
		
		// 巨大データの破棄
		qString = null;		

		// 各セクション毎にヘッダとデータを分離
		Iterator cursor = streams.iterator();		// セクション群
		while(cursor.hasNext()){
			String data = (String) cursor.next();
			int beginData = 0;
			String head;

			// ヘッダの摘出
			if(data.startsWith("\n")){
				head = "";
				beginData = 1;
			}
			else if(data.startsWith("\r\n")){
				head = "";
				beginData = 2;
			}
			else{
				int CRLFCRLF = data.indexOf("\r\n\r\n");
				if(CRLFCRLF != -1){
					head = data.substring(0, CRLFCRLF + 2);
					beginData = CRLFCRLF + 4;
				}
				else{
					CRLFCRLF = data.indexOf("\n\n");
					if(CRLFCRLF != -1){
						head = data.substring(0, CRLFCRLF + 1);
						beginData = CRLFCRLF + 2;
					}
					else{
						// フォーマットエラー＝破損データ？
						continue;
					}
				}
			}

			// ヘッダー、および、データ部分のバイト配列を文字コード変換せずに取得 ("ISO-8859-1"を利用していることに注意)
			byte[] bHeader = head.getBytes(NOT_CONVERT_CHARSET); 						// ヘッダー部分			
			byte[] bData = data.substring(beginData).getBytes(NOT_CONVERT_CHARSET); 	// データ部分
			
			// フォームデータの場合
			if(head.indexOf("filename=") == -1) {
				
				RequestParameter req4parameter = 
					new RequestParameter4mimeEntity4parameter(new ByteArrayInputStream(bHeader),
															  bData, 
															  this);					
				collection.add(req4parameter);

			} 
			// アップロードされたファイルの場合（ファイルサイズが閾値内）
			else if(bData.length < this.threshold) {

				RequestParameter req4fileInMemory = 
					new RequestParameter4mimeEntity4fileInMemory(new ByteArrayInputStream(bHeader),
																 bData, 
															 	 this);
				
				collection.add(req4fileInMemory);
			
			}
			// アップロードされたファイルの場合（ファイルサイズが閾値より大きい）
			else {
				// アップロードされたファイル→一時ファイルへ出力
				File file = this.fileManager.getPath();
				OutputStream out = new BufferedOutputStream(new FileOutputStream(file), this.bufferSize);
				
				try{
					out.write(bData);
				}
				finally {
					out.close();
				}
				RequestParameter req4file = 
		    				new RequestParameter4mimeEntity4file(new ByteArrayInputStream(bHeader),
		    													 file,
		    													 this,
		    													 this.bufferSize);
				collection.add(req4file);

			}
		}

		return (RequestParameter[]) collection.toArray(new RequestParameter[collection.size()]);
	}

	

    /**
     * 指定された閾値に達するまではデータをメモリ上に保持し、 
     * 閾値に達した場合にはディスク上に出力する出力ストリームです。 
     * もし閾値に達することなくクローズされた場合には、データはディスクに全く書き込まれません。
     */
    private class DefferdFileOutputStream { 
    	
	    /** 現在データの出力ストリーム */
	    private OutputStream currentOutputStream;

	    /** 閾値 */
	    private int threshold = 0;
	
        /** 出力済みデータサイズ */
	    private long writtenSize = 0;

	    /** 閾値に達した場合 true、閾値に達していない場合 false が格納されます */
	    private boolean thresholdExceeded = false;

	    /** 閾値に達する前の出力ストリーム（メモリ上） */
        private ByteArrayOutputStream memoryOutputStream;
	    

	    /** 閾値に達した後の出力先ファイル */
        private File outputFile;
	    
		/** ファイル入出力時のバッファサイズ */
		private int bufferSize = 8 * 1024;
	    
	    /**
	     * 閾値と、閾値に達した後にデータをセーブするファイル、
	     * および、ファイル入出力時のバッファサイズを指定してクラスのインスタンスを生成します。
	     * 
	     * @param outputFile 閾値に達した後の出力先ファイル
	     * @param threshold 閾値
	     * @param bufferSize ファイル入出力時のバッファサイズ
	     */
	    public DefferdFileOutputStream(File outputFile, int threshold, int bufferSize) {
	    	
	    	this.outputFile = outputFile;
	    	this.threshold = threshold;
			this.bufferSize = bufferSize;
	    	
			// 最初
	        this.memoryOutputStream = new ByteArrayOutputStream(threshold);
	        this.currentOutputStream = memoryOutputStream;
	        
		}

		/**
		 * 現在使われている出力ストリームを返します。
		 * @return 現在使われている出力ストリーム。
		 * @throws IOException エラーが発生した場合。
		 */
		public OutputStream getCurrentStream() throws IOException {
	        return currentOutputStream;
	    }
	    
		
	    /**
	     * 引数で与えられたデータサイズを出力した場合、閾値を超えるか否かを判定し、
	     * 閾値を超える場合は、出力ストリームを切り替えます。
	     * @param count 書き込み予定のデータサイズ 
	     * @throws IOException エラーが発生した場合。
	     */
	    public void checkThreshold(int count) throws IOException {
	        if (!thresholdExceeded && (writtenSize + count > threshold)) {
	            thresholdReached();
	            thresholdExceeded = true;
	        }
	    }
	
	    /**
	     * 現在使用している出力ストリームをメモリを対象にしているものからディスクを対象にするものに切り替えます。
	     * @throws IOException エラーが発生した場合。
	     */
	    public void thresholdReached() throws IOException {
	    	
	        FileOutputStream fos = new FileOutputStream(this.outputFile);
			OutputStream os = new BufferedOutputStream(fos, this.bufferSize);

	        byte[] data = memoryOutputStream.toByteArray();
			os.write(data);

			currentOutputStream = os;
			
			memoryOutputStream.close();
	        memoryOutputStream = null;
	    }
	    
	    /**
	     * 閾値に達したか否かを判定します。
	     * @return 閾値に達した場合 true、閾値に達していない場合 false が返却されます。
	     */
	    public boolean isThresholdExceeded() {
	        return (writtenSize > threshold);
	    }
	
	    /**
	     * データがメモリ上にあるか否かを判定します。
	     * @return データがメモリ上にある場合 true、データがディスク上にある場合は false が返却されます。
	     */
	    public boolean isInMemory() {
	        return (!isThresholdExceeded());
	    }
	
	    /**
	     * データがメモリ上にある場合、出力ストリームをバイトの配列にして返します。
	     * @return 出力ストリームのデータ。 データが取得できなかった場合には null 。
	     */
	    public byte[] getData() {
	        if (memoryOutputStream != null) {
	            return memoryOutputStream.toByteArray();
	        }
	        return null;
	    }
	    
	    /**
	     * データがディスク上にある場合、出力ストリームを File にして返します。
	     * データがメモリ上にある場合には、このメソッドは null を返します。
	     * @return 出力ストリームのファイル。 データがメモリ上にある場合には null 。
	     */
	    public File getFile() {
	    	if(!isInMemory()){
	    		return outputFile;
	    	}
	    	return null;
	    }    
		
		/**
		 * 出力済みのデータサイズを返却します。
		 * @return 出力済みのデータサイズ
		 */
		public long getLength(){
			return writtenSize;
			
			/* 正しくは、以下のコードです。
				if(isInMemory()){
					return memoryOutputStream.toByteArray().length;
				}
				else {
					return outputFile.length();
				}
			*/
		}

		public long addWrittenSize(int len) {
			writtenSize += len;
			return writtenSize;
		}

    }
    

}


