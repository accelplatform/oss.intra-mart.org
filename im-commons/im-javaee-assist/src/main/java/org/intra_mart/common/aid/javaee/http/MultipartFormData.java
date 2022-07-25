package org.intra_mart.common.aid.javaee.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletRequest;

/**
 * フォームから enctype が multipart/form-data で送信されてきた情報を
 * 解析するオブジェクトです。<P>
 *
 * このオブジェクトは、ブラウザから multipart/form-data 形式でリクエストされた
 * データを解析し、boundary で区切られた各情報(エンティティ)をとしてリストに
 * 保管します。<P>
 *
 * このクラスを利用する事により、ブラウザからサーバに対するファイルの
 * アップロードなどが簡単に実装する事ができます。<BR>
 *
 */
public class MultipartFormData extends ArrayList{
	// インスタンス変数

	/**
	 * リクエストされた情報を解析して、新しいオブジェクトを構築します。<BR>
	 * multipart/form-data 形式のリクエストを解析します。<P>
	 *
	 * req が multipart/form-data で受信したリクエストではない場合、
	 * IllegalArgumentException をスローします。<P>
	 *
	 * @param req サーブレットエンジンから渡されるリクエスト情報オブジェクト
	 * @exception IOException リクエストデータ解析時の入出力エラー
	 * @exception IllegalArgumentException 引数が不正の場合
	 */
	public MultipartFormData(ServletRequest req) throws IOException, IllegalArgumentException{
		try{
			// バウンダリの摘出
			String content_type = req.getContentType();
			String boundary;
			if(content_type.endsWith(";")){
				// 終端を削除しつつ前後の空白を除去して抽出
				boundary = "--" + content_type.substring(content_type.lastIndexOf("boundary=") + 9, content_type.length() - 1).trim();
			}
			else{
				// 前後の空白を除去して抽出
				boundary = "--" + content_type.substring(content_type.lastIndexOf("boundary=") + 9).trim();
			}
			InputStream in = req.getInputStream();
			int max = req.getContentLength();
			byte[] strm = new byte[max];
			if(in.markSupported()){ in.mark(max); }
			int po = 0;
			while(po < max){
				int bytes = in.read(strm, po, max - po);
				if(bytes > 0){ po += bytes; }
				else{
					if(in.markSupported()){ in.reset(); }
					throw new IllegalArgumentException("Data not found");
				}
			}
			if(in.markSupported()){ in.reset(); }
			try{
				parseMIME(boundary, new String(strm, "8859_1"));
			}
			catch(UnsupportedEncodingException uee){
				throw new IOException(uee.getMessage());
			}
		}
		catch(IOException ioe){
			throw ioe;
		}
		catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * 指定のインデックスに該当するエンティティを取得します。<P>
	 *
	 * インデックスが範囲外の場合(インデックスが負または size() よりも大きい)
	 * IndexOutOfBoundsException をスローします。<BR>
	 *
	 * @param index 返すエンティティのインデックス
	 * @return リスト内の指定された位置にあるエンティティ
	 */
	public MultipartFormData.Entity getEntity(int index){
		return (MultipartFormData.Entity) this.get(index);
	}

	/**
	 * 指定の名前を持つエンティティを取得します。<P>
	 *
	 * エンティティの名前とは、フォームのコントロール名称のことです。
	 * 指定の名前を持つエンティティが複数存在する場合、その中のいずれか一つが
	 * 返されます。
	 * 該当するエンティティが存在しない場合 null を返します。<BR>
	 *
	 * @return 指定の名前を持つエンティティ
	 * @see MultipartFormData.Entity#getName
	 */
	public MultipartFormData.Entity getEntity(String name){
		Iterator view = this.iterator();

		while(view.hasNext()){
			MultipartFormData.Entity item = (MultipartFormData.Entity) view.next();
			if(name.equals(item.getName())){ return item; }
		}

		return null;
	}

	/**
	 * 指定の名前を持つエンティティをすべて取得します。<P>
	 *
	 * エンティティの名前とは、フォームのコントロール名称のことです。
	 * 該当するエンティティが存在しない場合、空の配列を返します。<BR>
	 *
	 * @return 指定の名前を持つエンティティ
	 * @see MultipartFormData.Entity#getName
	 */
	public MultipartFormData.Entity[] getEntities(String name){
		ArrayList list = new ArrayList();
		Iterator view = this.iterator();

		while(view.hasNext()){
			MultipartFormData.Entity item = (MultipartFormData.Entity) view.next();
			if(name.equals(item.getName())){ list.add(item); }
		}

		return (MultipartFormData.Entity[]) list.toArray(new MultipartFormData.Entity[list.size()]);
	}

	// データ解析
	private void parseMIME(String boundary, String src) throws IOException{
		String terminate = boundary.concat("--");		// 終端コード

		// 各データの摘出
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
			if((viewPt = src.indexOf(boundaryCRLF, edPt)) != -1){
				edPt = viewPt + boundaryCRLF.length();
			}
			else if((viewPt = src.indexOf(boundaryCR, edPt)) != -1){
				edPt = viewPt + boundaryCR.length();
			}
			else if((viewPt = src.indexOf(boundaryLF, edPt)) != -1){
				edPt = viewPt + boundaryLF.length();
			}
			else if((viewPt = src.indexOf(terminateCRLF, edPt)) != -1){
				edPt = viewPt + terminateCRLF.length();
			}
			else if((viewPt = src.indexOf(terminateCR, edPt)) != -1){
				edPt = viewPt + terminateCR.length();
			}
			else if((viewPt = src.indexOf(terminateLF, edPt)) != -1){
				edPt = viewPt + terminateLF.length();
			}
			else if((viewPt = src.indexOf(terminate, edPt)) != -1){
				edPt = viewPt + terminate.length();
			}
			else{
				edPt = src.length();
			}

			// 部分情報の取得
			if(stPt < viewPt){
				if(src.charAt(viewPt - 1) == '\n'){ viewPt--; }
				if(src.charAt(viewPt - 1) == '\r'){ viewPt--; }
				this.add(new EntityObject(src.substring(stPt, viewPt)));
			}

			stPt = edPt;	// 次の検索開始位置を設定
		}while(edPt != src.length());
	}

	private static class EntityObject extends HashMap implements MultipartFormData.Entity{
		private byte[] body;
		private String ctrlName = null;

		public EntityObject(String src) throws IOException{
			int beginData = 0;
			String head;

			// ヘッダの摘出
			if(src.startsWith("\n")){
				head = "";
				beginData = 1;
			}
			else if(src.startsWith("\r\n")){
				head = "";
				beginData = 2;
			}
			else{
				int CRLFCRLF = src.indexOf("\r\n\r\n");
				if(CRLFCRLF != -1){
					head = src.substring(0, CRLFCRLF + 2);
					beginData = CRLFCRLF + 4;
				}
				else{
					CRLFCRLF = src.indexOf("\n\n");
					if(CRLFCRLF != -1){
						head = src.substring(0, CRLFCRLF + 1);
						beginData = CRLFCRLF + 2;
					}
					else{
						CRLFCRLF = src.indexOf("\r\r");
						if(CRLFCRLF != -1){
							head = src.substring(0, CRLFCRLF + 1);
							beginData = CRLFCRLF + 2;
						}
						else{
							head = "";
						}
					}
				}
			}
//System.out.println("head: " + head);

			// Header の解析
			BufferedReader br = new BufferedReader(new StringReader(head));
			while(br.ready()){
				String line = br.readLine();						// １行取得
				if(line != null){
					int colon = line.indexOf(":");
					if(colon != -1){
						this.put(line.substring(0, colon).trim(), line.substring(colon + 1).trim());
						if(line.startsWith("Content-Disposition:")){
							// コントロール名称の取得
							int name = line.indexOf("name=");
							if(name != -1){
								int endName = head.indexOf(";", name);
								if(endName == -1){ endName = line.length(); }
								if(line.charAt(name + 5) == '"'){
									// " で囲まれている
									this.ctrlName = line.substring(name + 6, endName - 1);
								}
								else{
									this.ctrlName = line.substring(name + 5, endName).trim();
								}
							}
						}
					}
					else{
						this.put(line.trim(), "");
					}
				}
				else{
					break;
				}
			}

			// Body Part の取得
			this.body = src.substring(beginData).getBytes("8859_1");
		}

		public String getName(){
			return this.ctrlName;
		}

		public String[] getHeaderNames(){
			return (String[]) (new ArrayList(this.keySet())).toArray(new String[this.size()]);
		}

		public String getHeader(String key){
			return (String) this.get(key);
		}

		public boolean containsHeader(String key){
			return this.containsKey(key);
		}

		public String getContent(){
			return new String(body);
		}

		public String getContent(String enc) throws UnsupportedEncodingException{
			return new String(body, enc);
		}

		public int getContentLength(){
			if(body != null){
				return body.length;
			}
			else{
				return 0;
			}
		}

		public byte[] getBytes(){
			return body;
		}

		public Reader getReader(){
			return new StringReader(getContent());
		}

		public InputStream getInputStream(){
			return new ByteArrayInputStream(body);
		}
	}	// End of EntityObject

	/**
	 * フォームデータのエンティティです。<P>
	 *
	 * フォームから送信された情報を解析した結果の各エンティティは、
	 * ヘッダ情報とデータで構成されます。<BR>
	 *
	 */
	public static interface Entity{
		/**
		 * フォームのコントロール名称の取得。<P>
		 *
		 * フォームから送信された情報のうち、このエンティティが表す情報の
		 * もととなったフォーム中に定義されたコントロールの名称を返します。<BR>
		 *
		 * @return コントロール名称
		 */
		public String getName();

		/**
		 * ヘッダ名称一覧の取得。<P>
		 *
		 * このエンティティのもつヘッダ情報の名称一覧を返します。<BR>
		 *
		 * @return ヘッダ名称の取得
		 */
		public String[] getHeaderNames();

		/**
		 * ヘッダの取得。<P>
		 *
		 * @param name ヘッダ名称
		 * @return ヘッダ名称にマップされている値
		 */
		public String getHeader(String name);

		/**
		 * 指定された文字列がヘッダの名称かどうかを判定します。<P>
		 *
		 * @param name ヘッダ名称
		 * @return ヘッダ名称が値にマップされている場合 true
		 */
		public boolean containsHeader(String name);

		/**
		 * データ部をバイト長を取得します。<P>
		 *
		 * @return データのサイズ
		 */
		public int getContentLength();

		/**
		 * データ部を文字列表現として取得します。<P>
		 * 入力されたバイトデータから文字列への変換には、プラットフォームの
		 * デフォルトエンコーディングを使います。<BR>
		 *
		 * @return データ
		 */
		public String getContent();

		/**
		 * データ部を指定された文字コードで変換した結果を文字列表現として
		 * 取得します。<P>
		 * 指定のエンコード名がサポートされていない場合
		 * UnsupportedEncodingException がスローされます。<BR>
		 *
		 * @param enc エンコード名
		 * @return データ
		 * @see java.io.UnsupportedEncodingException
		 */
		public String getContent(String enc) throws UnsupportedEncodingException;

		/**
		 * データ部をバイト配列として取得します。<P>
		 *
		 * @return データ
		 */
		public byte[] getBytes();

		/**
		 * データ部を Reader として取得します。<P>
		 *
		 * @return データ
		 */
		public Reader getReader();

		/**
		 * データ部を入力ストリームとして取得します。<P>
		 *
		 * @return データ
		 */
		public InputStream getInputStream();
	}	// End of FormDataEntity
}		// End of MultipurposeInternetMailExtensions

/* End of File */