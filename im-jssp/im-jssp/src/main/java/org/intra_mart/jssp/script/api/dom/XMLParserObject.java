package org.intra_mart.jssp.script.api.dom;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.common.aid.jdk.java.lang.StringUtil;
import org.intra_mart.jssp.script.api.FileAccessObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XMLを解析するパーサーオブジェクト。<br/>
 * <br/>
 * XML を解析するパーサーを提供します。<br/>
 * ドキュメントノードが複数存在する場合は、XML 解析エラーとなりますのでご注意下さい。<br/>
 * <br/>
 * 
 * @scope public
 * @name XMLParser
 */
public class XMLParserObject extends ScriptableObject implements Serializable{

	private static final String PROP_NAME_4_JAVA_EXCEPTION = "javaException";
	private boolean error = true;
	private String errorMessage = null;

	/**
     * デフォルトコンストラクタ。 <br/>
     */
	public XMLParserObject(){
		super();

		setSuccessStatus();
	}
	
	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return 実行環境上での名称
	 */
	public String getClassName() {
		return "XMLParser";
	}

	
	/**
	 * コンストラクタ。<br/>
	 * <br/>
	 * XMLを解析するパーサーオブジェクトを構築します。<br/>
	 * 
	 * @scope public
	 */
	public static synchronized Object jsConstructor(Context cx,
														Object[] args, 
														Function ctorObj, 
														boolean inNewExpr){
		if(inNewExpr){
			// new 演算子によりコール
			return new XMLParserObject();
		}
		else{
			// 関数指定
		    return null;
		}
	}
	
    /* 
	 * JSから呼び出される実メソッドです。
	 * (APIリストは下記のダミーメソッドに記述してあります) 
	 */
    public DOMDocumentObject jsFunction_parse(Object args) {
    	
        if(args instanceof String){
			return parse( (String)args );
		} 
        else if(args instanceof FileAccessObject) {
			return parse( (FileAccessObject)args );
		}
        else {
            throw new IllegalArgumentException();
        }
        
    }

    /**
	 * Web Application Server が動作しているマシン上の
     * ファイルコンテンツを XML ドキュメントとして構文解析し、
     * 新しい DOMDocument オブジェクトを返します。<br/>
     * <br/>
     * @scope public
     * @param path String 
     * 					Web Application Server が動作しているマシン上のファイル・パス<br/>
     * 					(Web Application Server が動作しているマシン上での絶対パス、または、<br/>
     * 					システムプロパティ user.dir (ユーザーの現在の作業ディレクトリ)
     * 					で指定されるディレクトリからの相対パス)
     *  
     * @return DOMDocument DOMDocumentオブジェクト
     */
    public Object parse(String path, 
    							Object dummy) {
        // APIリスト用 ダミーメソッドです。
        return null;
    }
    
    private DOMDocumentObject parse(String path){
    	
    	try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new File(path));
			
			setSuccessStatus();
			return new DOMDocumentObject(document);
			
		} 
		catch(SAXException saxe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, saxe);

			this.error = true;
			this.errorMessage = saxe.getMessage();
			return null;
		} 
		catch(IOException ioe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, ioe);
			
			this.error = true;
			this.errorMessage = ioe.getMessage();
			return null;
		} 
		catch (ParserConfigurationException pce) {
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, pce);
			
			this.error = true;
			this.errorMessage = pce.getMessage();
			return null;
		}
		catch(NullPointerException npe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, npe);
			
			this.error = true;
			this.errorMessage = npe.getClass().getName() + " : " + npe.getMessage();
			return null;
		} 
		catch(Exception e){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, e);
			
			this.error = true;
			this.errorMessage = e.getClass().getName() + " : " + e.getMessage();
			return null;
		}
	}
    
    
    /**
     * Web Application Server が動作しているマシン上
     * のファイルコンテンツを XML ドキュメントとして構文解析し、
     * 新しい DOMDocument オブジェクトを返します。<br/>
     * <br/>
     * @scope public
     * @param file 
     *            File ファイル操作オブジェクト
     *  
     * @return DOMDocument DOMDocumentオブジェクト
     */
    public Object parse(FileAccessObject file, 
    							Object dummy) {
        // APIリスト用 ダミーメソッドです。
        return null;
    }
    
	private DOMDocumentObject parse(FileAccessObject file){
		return parse(file.jsFunction_path());
	}	
    		
	
    /**
     * 引数で与えられた文字列を XML ドキュメントとして構文解析し、
     * 新しい DOMDocument オブジェクトを返します。<br/>
     * <br/>
     * 
	 * 例えば、以下のようなＸＭＬ
	 * 
	 * <pre>
	 * &lt;A&gt;
	 *   &lt;B&gt;Ｂタグの中のテキスト&lt;/B&gt;
	 *   &lt;C&gt;Ｃタグの中のテキスト&lt;/C&gt;
	 * &lt;/A&gt;
	 * </pre>
	 * 
	 * から、&lt;C&gt; タグに囲まれたテキスト値を取得する場合は以下のようにプログラムします。
	 * 
	 * <pre>
	 * 
	 * var xmlString = &quot;&lt;A&gt;&lt;B&gt;Ｂタグの中のテキスト&lt;/B&gt;&lt;C&gt;Ｃタグの中のテキスト&lt;/C&gt;&lt;/A&gt;&quot;;
	 * 
	 * var parser = new XMLParser();
	 * var dom = parser.parseString(xmlString);
	 * 
	 * if(!parser.isError()){
	 *     var elms = dom.getElementsByTagName(&quot;C&quot;);
	 *     var elm = elms[0];
	 *     var childs = elm.getChildNodes();
	 *     var data = childs[0].getValue();
	 * 
	 *     Debug.print(&quot;C's text: &quot; + data);
	 * }
	 * else {
	 *     Debug.print("Error occured ! : " + parser.getErrorMessage() );
	 * }
	 * </pre>
     * 
     * @scope public
     * @param src 
     *            String XML形式の文字列
     * 
     * @return DOMDocument DOMDocumentオブジェクト
     */
    public DOMDocumentObject jsFunction_parseString(Object src) {
    	
        if( !(src instanceof String) ){
            throw new IllegalArgumentException();
        }
        
    	try{
			
			org.xml.sax.InputSource is = new org.xml.sax.InputSource(new StringReader((String)src));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			
			setSuccessStatus();
			return new DOMDocumentObject(document);
		
		} 
		catch(SAXException saxe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, saxe);
			
			this.error = true;
			this.errorMessage = saxe.getMessage();
			return null;
		} 
		catch(IOException ioe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, ioe);
			
			this.error = true;
			this.errorMessage = ioe.getMessage();
			return null;
		} 
		catch (ParserConfigurationException pce) {
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, pce);
			
			this.error = true;
			this.errorMessage = pce.getMessage();
			return null;
		}
		catch(NullPointerException npe){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, npe);
			
			this.error = true;
			this.errorMessage = npe.getClass().getName() + " : " + npe.getMessage();
			return null;
		} 
		catch(Exception e){
			this.put(PROP_NAME_4_JAVA_EXCEPTION, this, e);
			
			this.error = true;
			this.errorMessage = e.getClass().getName() + " : " + e.getMessage();
			return null;
		}
    }

    
	/**
	 * エラー発生のチェックを行います。<br/>
	 * <br/>
	 * XML ソースを解析中にエラーが発生していた場合 true を返します。<br/>
	 * 
	 * @scope public
	 * @return Boolean true : エラーあり / false : エラーなし
	 */
	public boolean jsFunction_isError(){
		return this.error;
	}

	
	/**
	 * エラーメッセージの取得を行います。<br/>
	 * <br/>
	 * XML ソースを解析中にエラーが発生していた場合、そのエラーメッセージを返します。<br/>
	 * 
	 * @scope public
	 * @return String エラーメッセージ
	 */
	public String jsFunction_getErrorMessage(){
		return this.errorMessage;
	}
    
	/**
	 * 成功状態を設定します。
	 */
	private void setSuccessStatus(){
		this.error = false;
		this.errorMessage = StringUtil.EMPTY_STRING;
	}
		
}