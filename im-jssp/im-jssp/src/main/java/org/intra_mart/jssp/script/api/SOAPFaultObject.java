package org.intra_mart.jssp.script.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.AxisFault;
import org.intra_mart.jssp.util.ValueObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;

/**
 * SOAPFault オブジェクト。<br/>
 * <br/>
 * SOAPFaultオブジェクト　は、XML形式の
 * <a href="http://www.w3.org/TR/2007/REC-soap12-part1-20070427/#soapfault">SOAPフォルト</a>を
 * JavaScriptのオブジェクト形式に変換したものです。<br/>
 * （SOAPFaultオブジェクトのプロパティは、<a href="#SOAPFaultString">コンストラクタ</a>の説明を参照してください）<br/>
 * <br/>
 * 
 * 以下に、「<a href="#Receive_SOAPFault">SOAPフォルトの受信方法</a>」と
 * 「<a href="#Send_SOAPFault">SOAPフォルトの送信方法</a>」をサンプルコードを用いて説明します。
 * 
 * <a name="Receive_SOAPFault"/>
 * <h2>SOAPフォルトの受信方法</h2>
 * SOAPフォルトを受信するサンプルコードです。<br/>
 * <br/>
 * 	<table border="1">
 * 		<tr><th>SOAPフォルトを受信するサンプルコード</th></tr>
 * 		<tr><td><font size="-1.5">
<pre>
 1: //-------------------------------
 2: // Webサービスの呼び出し
 3: //-------------------------------
 4: try{
 5:     var result = soapClient.add(wsUserInfo, member);
 6: }
 7: catch(soapFault){
 8:     Debug.browse("エラーが発生しました。", soapFault);
 9: }
</pre>
 * 		</font></td></tr>
 * </table>
 * 
 * {@link SOAPClient}オブジェクト を利用してWebサービスを呼び出した際に、
 * Webサービスの結果がSOAPフォルトとして返却されると、このAPIリストで説明している <b>SOAPFaultオブジェクト</b> が例外としてスローされます。<br/>
 * <br/>
 * SOAPFaultオブジェクトを捕捉する（=Webサービス・オペレーションの実行ロジック部分をtry/catchで囲む)ことで、
 * SOAPFaultオブジェクトを利用したエラー処理を行う事が可能となります。<br/>
 * <br/>
 * SOAPFaultオブジェクトのプロパティは、<a href="#SOAPFaultString">コンストラクタ</a>の説明を参照してください。<br/>
 * 
 * <br/>
 * <br/>
 * 
 * <a name="Send_SOAPFault"/>
 * <h2>SOAPフォルトの送信方法</h2>
 * SOAPフォルトを送信するサンプルコードです。<br/>
 * <br/>
 * 	<table border="1">
 * 		<tr><th>SOAPフォルトを送信するサンプルコード</th></tr>
 * 		<tr><td><font size="-1.5">
<pre>
 1: // SOAPFaultオブジェクトの生成（引数には「Fault Reason」を指定します）
 2: var soapFault = new SOAPFault("エラーが発生しました。");
 3: 
 4: // SOAPフォルトの内容を設定します。
 5: soapFault.faultCode             = "SampleFaultCode";
 6: soapFault.faultCodeNameSpaceURI = "http://sample.fault/xsd"; 
 7: 
 8: soapFault.detail         = "エラーの詳細情報です";
 9: soapFault.detailNodeName = "soapFault_detailNodeName";
10: 
11: // SOAPFaultをスロー (ここで処理が終了します)
12: soapFault.throwFault();
</pre>
 * 		</font></td></tr>
 * </table>
 * 
 * <br/>
 * <br/>
 * まず、2行目で、SOAPFaultオブジェクトのインスタンスを生成しています。引数にはSOAPフォルトのFault Reasonを指定します。<br/>
 * <br/>
 * 4～9行目にかけて、SOAPフォルトの具体的な内容を設定しています。<br/>
 * SOAPFaultオブジェクトのプロパティは、<a href="#SOAPFaultString">コンストラクタ</a>の説明を参照してください。<br/>
 * <br/>
 * SOAPFaultオブジェクトをスローするには、12行目のように「<a href="#throwFault">throwFault()</a>」関数を実行してください。<br/>
 * <i>（「throw soapFault;」といった、JavaScriptのスロー構文を利用しないでください）</i><br/>
 * <br/>
 * 「<a href="#throwFault">throwFault()</a>」関数内部で変換処理が行われ、
 * WebサービスクライントにSOAPフォルトメッセージが返却されます。<br/>
 * 
 * <br/>
 * <br/>
 * 
 * 上記サンプルコードで返却されるSOAPメッセージの例です。
 * <br/>
 * 	<table border="1">
 * 		<tr>
 * 			<th>
 * 				SOAPフォルトを含むメッセージの例
 * 			</th>
 * 		</tr>
 * 		<tr>
 * 			<td>
 * 				<font size="-1.5">
<pre>
&lt;?xml version='1.0' encoding='UTF-8'?&gt;
&lt;soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"&gt;
   &lt;soapenv:Header xmlns:wsa="http://www.w3.org/2005/08/addressing"&gt;
      &lt;wsa:Action&gt;http://www.w3.org/2005/08/addressing/soap/fault&lt;/wsa:Action&gt;
      &lt;wsa:RelatesTo&gt;urn:uuid:BBE36157BF34C817391214111551449&lt;/wsa:RelatesTo&gt;
   &lt;/soapenv:Header&gt;
   &lt;soapenv:Body&gt;
      &lt;soapenv:Fault xmlns:axis2ns4="http://sample.fault/xsd"&gt;
         &lt;soapenv:Code&gt;
            &lt;soapenv:Value&gt;axis2ns4:SampleFaultCode&lt;/soapenv:Value&gt;
         &lt;/soapenv:Code&gt;
         &lt;soapenv:Reason&gt;
            &lt;soapenv:Text xml:lang="en-US"&gt;エラーが発生しました。&lt;/soapenv:Text&gt;
         &lt;/soapenv:Reason&gt;
         &lt;soapenv:Detail&gt;
            &lt;soapFault_detailNodeName&gt;エラーの詳細情報です&lt;/soapFault_detailNodeName&gt;
         &lt;/soapenv:Detail&gt;
      &lt;/soapenv:Fault&gt;
   &lt;/soapenv:Body&gt;
&lt;/soapenv:Envelope&gt;
&lt;/pre&gt;
</pre>
 * 				</font>
 * 			</td>
 * 		</tr>
 * 	</table>
 * 
 * <a name="APIListStart"/>
 * 
 * @scope public
 * @name SOAPFault
 * @since 7.0 
 */
public class SOAPFaultObject extends ScriptableObject implements Cloneable, java.io.Serializable {

	public static final String DEFAULT_SOAP_FAULT_DETAIL_NODE_NAME = "Exception";

	private static String _prop4reason = "reason";

	private static String _prop4faultCode             = "faultCode";
	private static String _prop4faultCodeNameSpaceURI = "faultCodeNameSpaceURI";
	
	private static String _prop4faultSubCode             = "faultSubCode";
	private static String _prop4faultSubCodeNameSpaceURI = "faultSubCodeNameSpaceURI";
	
	private static String _prop4detail         = "detail";
	private static String _prop4detailNodeName = "detailNodeName";
	private static String _prop4faultNode      = "faultNode"; // フォルト発生の原因となったノード
	private static String _prop4faultRole      = "faultRole";
	private static String _prop4faultAction    = "faultAction";
	private static String _prop4nodeURI        = "nodeURI";
	
	public SOAPFaultObject(){
	}

	@Override
	public String getClassName() {
		return "SOAPFault";
	}

	/**
	 * SOAPFaultオブジェクトを生成します。<br/>
	 * 引数には、SOAPフォルトの Reason要素 に格納するメッセージを指定します。<br/>
	 * <br/>
	 * インスタンス生成後、SOAPフォルトの要素をプロパティに設定します。
	 * その後、「<a href="#throwFault">throwFault()</a>」関数を実行し、このオブジェクトをスローしてください。<br/>
	 * <br/>
	 * このオブジェクトのプロパティとSOAPフォルトの要素の関係は以下の通りです。
	 * <table border="1">
	 * 	<tr>
	 * 		<th>プロパティ</th>
	 * 		<th>SOAPフォルトの要素</th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>faultCode</td>
	 * 		<td>フォルト・コード</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>faultCodeNameSpaceURI</td>
	 * 		<td>フォルト・コードの名前空間</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>faultSubCode</td>
	 * 		<td>フォルト・サブ・コード</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>faultSubCodeNameSpaceURI</td>
	 * 		<td>フォルト・サブ・コードの名前空間</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>detail</td>
	 * 		<td>詳細要素</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>detailNodeName</td>
	 * 		<td>詳細要素のノード名（省略時は「Exception」が設定されます）</td>
	 * 	</tr>
	 * 
	 *  <!-- 
	 *  	faultNode, nodeURI, faultRoleは、設定してもWebサービス・クライアントには返信されません。
	 *  	（AxisFaultにfaultNodeなどの値を設定しても、その内容は省かれます）
	 *  	これは、Axis2の制限です。
	 * 	<tr>
	 * 		<td>faultNode</td>
	 * 		<td>フォルト・ノード</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>nodeURI</td>
	 * 		<td>フォルト・ノードを示すURI</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>faultRole</td>
	 * 		<td>フォルト・ロール</td>
	 * 	</tr>
	 * 	-->
	 * 
	 * </table>
	 * 
	 * @param reason String SOAPフォルトの Reason要素 に格納するメッセージ
	 * @scope public
	 */
	public static Object jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr) {

		if (inNewExpr == false) {
			throw new IllegalStateException("This constructor must be invoked via \"new\" operator.");
		}

		if (args.length == 0 || args[0] == null) {
			throw new IllegalArgumentException("Please set one or more arguments.");
		}
		
		// JSプログラムがWebサービス・クライアントの場合（＝JSプログラムにAxisFaultを伝える場合を想定）
		if(args[0] instanceof NativeJavaObject){
			
			NativeJavaObject njo = (NativeJavaObject) args[0];
			Object unwrapped = njo.unwrap();
			
			if(unwrapped instanceof AxisFault){
				ValueObject vo = convertFromAxisFaultToValueObject(cx, (AxisFault) unwrapped);
				return vo;
			}
			else{
				String msg = "SOAPFault is '" + AxisFault.class.getName() + "' wrapper. (Cannot wrap " + unwrapped + ".)";
				throw new IllegalStateException(msg);
			}
		}

		// JSプログラムがWebサービス・プロバイダの場合（＝JSプログラムからSOAPFaultを送信する場合を想定）
		else{
			SOAPFaultObject thisObj = new SOAPFaultObject();
			thisObj.reason = args[0].toString();
			
			return thisObj;
		}
	}
	
	private String reason;

	/**
	 * SOAPFaultオブジェクトをスローします。<br/>
	 * SOAPFaultオブジェクトのプロパティに設定された値をもとに変換処理が行われ、
	 * WebサービスクライントにSOAPフォルトメッセージを送信します。<br/>
	 * <br/>
	 * SOAPFaultオブジェクトのプロパティは、<a href="#SOAPFaultString">コンストラクタ</a>の説明を参照してください。<br/>
	 * <br/>
	 * SOAPFaultオブジェクトをスローする場合は、この関数を使用してください。<br/>
	 * <i>（「throw soapFault;」といった、JavaScriptのスロー構文を利用しないでください）</i><br/>
	 * <br/>
	 * 
	 * @scope public
	 */
	public void jsFunction_throwFault() throws AxisFault {

		Object faultCode             = this.get(_prop4faultCode, this);
		Object faultCodeNameSpaceURI = this.get(_prop4faultCodeNameSpaceURI, this);

		Object faultSubCode             = this.get(_prop4faultSubCode, this);
		Object faultSubCodeNameSpaceURI = this.get(_prop4faultSubCodeNameSpaceURI, this);

		Object detail         = this.get(_prop4detail, this);
		Object detailNodeName = this.get(_prop4detailNodeName, this);
		Object faultNode      = this.get(_prop4faultNode, this);
		Object faultRole      = this.get(_prop4faultRole, this);
		Object faultAction    = this.get(_prop4faultAction, this);
		Object nodeURI        = this.get(_prop4nodeURI, this);
		
		// faultCodeのデフォルト値
		if(faultCode == NOT_FOUND){
			faultCode = "Receiver";
			faultCodeNameSpaceURI = NOT_FOUND;
		}
		
		QName faultCodeQName = null;
		if(faultCodeNameSpaceURI == NOT_FOUND){
			faultCodeQName = new QName(faultCode.toString());
		}
		else{
			faultCodeQName = new QName(faultCodeNameSpaceURI.toString(), faultCode.toString());
		}
		
		OMElement faultDetail = null;
		if(detail != NOT_FOUND){
			QName qName4Detail = null;
			if(detailNodeName != NOT_FOUND){
				qName4Detail = new QName(detailNodeName.toString());
			}
			else{
				qName4Detail = new QName(DEFAULT_SOAP_FAULT_DETAIL_NODE_NAME);
			}

			OMFactory factory = OMAbstractFactory.getOMFactory();
			faultDetail = factory.createOMElement(qName4Detail);
			faultDetail.setText(detail.toString());
		}

		AxisFault af = new AxisFault(faultCodeQName,
									 this.reason,
									 (faultNode != NOT_FOUND) ? faultNode.toString() : null,
									 (faultRole != NOT_FOUND) ? faultRole.toString() : null,
									 faultDetail);
		

		if(faultSubCode != NOT_FOUND){
			QName faultSubCodeQName = null;
			if(faultSubCodeNameSpaceURI == NOT_FOUND){
				faultSubCodeQName = new QName(faultSubCode.toString());
			}
			else{
				faultSubCodeQName = new QName(faultSubCodeNameSpaceURI.toString(), faultSubCode.toString());
			}
			
			// Listの要素がQNameである理由：右記参照 -> org.apache.axis2.util.MessageContextBuilder.createFaultEnvelope(MessageContextBuilder.java:518)
			// Listの要素が一つである理由：Axis2-1.4の実装では、複数のFaultSubCodeを設定してもListの最後しか反映されないため。
			List<QName> list = new ArrayList<QName>();
			list.add(faultSubCodeQName);
			af.setFaultSubCodes(list);
		}

		if(faultAction != NOT_FOUND){
			af.setFaultAction(faultAction.toString());
		}
		
		if(nodeURI != NOT_FOUND){
			af.setNodeURI(nodeURI.toString());
		}

		throw af;
	}

	/**
	 * @param af
	 * @return
	 */
	private static ValueObject convertFromAxisFaultToValueObject(Context cx, AxisFault af) {

		ValueObject vo = new ValueObject();
		vo.setClassName("SOAPFault");
		
		vo.put("name", vo, vo.getClassName());
		vo.put("message", vo, af.getMessage()); // JSの擬似Error用のメッセージ

		// AxisFaultをラップ（∵JSのtypeof()でエラーとならないように）
        Object wrappedAxisFault = cx.getWrapFactory().wrap(cx, vo, af, af.getClass());
		vo.put("javaException", vo, wrappedAxisFault);

		// ============= 以下、SOAPFault用のプロパティ =============
		vo.put(_prop4reason, vo, af.getReason());
		
		QName faultCode = af.getFaultCode();
		if(faultCode != null){
			vo.put(_prop4faultCode,             vo, faultCode.getLocalPart());
			vo.put(_prop4faultCodeNameSpaceURI, vo, faultCode.getNamespaceURI());
		}
		
		List<QName> faultSubCodes = af.getFaultSubCodes();
		if(faultSubCodes != null && !faultSubCodes.isEmpty()){
			QName subCode = faultSubCodes.get(0);
			vo.put(_prop4faultSubCode,             vo, subCode.getLocalPart());
			vo.put(_prop4faultSubCodeNameSpaceURI, vo, subCode.getNamespaceURI());
		}

		OMElement detail = af.getDetail();
		if(detail != null){
			vo.put(_prop4detail, vo, detail.toString());
		}
		
		String faultNode = af.getFaultNode();
		if(faultNode != null){
			vo.put(_prop4faultNode, vo, faultNode);
		}
		
		String faultRole = af.getFaultRole();
		if(faultNode != null){
			vo.put(_prop4faultRole, vo, faultRole);
		}
		
		String faultAction = af.getFaultAction();
		if(faultAction != null){
			vo.put(_prop4faultAction, vo, faultAction);
		}

		String nodeURI = af.getNodeURI();
		if(nodeURI != null){
			vo.put(_prop4nodeURI, vo, nodeURI);
		}
		
		return vo;
	}
}
