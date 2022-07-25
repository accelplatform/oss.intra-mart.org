package org.intra_mart.jssp.util.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.common.aid.jdk.javax.xml.XMLProperties;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerImpl;
import org.xml.sax.SAXException;


/**
 * JSSP-RPC環境での、コンフィグファイルの設定値を取得するための標準実装です。
 */
public class JsspRpcConfigHandlerImpl extends JSSPConfigHandlerImpl implements JsspRpcConfigHandler {

    private static final String ROOT_ELEMENT ="/intra-mart/jssp-rpc";

    private XMLProperties jsspConfigProperties;

    private String _jsspRpcURISuffix;
    private String _marshallerPagePath;
    private String _unmarshallFunctionName;
    private String _marshallFunctionName;
    private String _marshall4ArgumentsFunctionName;
    private Boolean _isThrowUnmarshallException;
    
	/**
	 * このコンストラクタは、
	 * リソース「/org/intra_mart/jssp/util/config/jssp-config.xml」をコンフィグファイルとします。
	 * 
	 * @deprecated JSSP実行環境の起動時にのみ利用します。
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public JsspRpcConfigHandlerImpl() throws ParserConfigurationException, SAXException, IOException {
		this("/org/intra_mart/jssp/util/config/jssp-config.xml");
	}

	/**
	 * このコンストラクタは、引数で指定されたファイルをコンフィグファイルとします。
	 * 
	 * @deprecated JSSP実行環境の起動時にのみ利用します。
	 * @param configFilePath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public JsspRpcConfigHandlerImpl(String configFilePath) throws ParserConfigurationException, SAXException, IOException {
		super(configFilePath);
		
        InputStream is = JsspRpcConfigHandlerImpl.class.getResourceAsStream(configFilePath);

        if(is != null) {
        	jsspConfigProperties = new XMLProperties(is);
        }
        else {
        	throw new FileNotFoundException(configFilePath + " is not found.");
        }
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JsspRpcConfigHandler#getJsspRpcURISuffix()
	 */
	public String getJsspRpcURISuffix() {
		if(_jsspRpcURISuffix == null){
			_jsspRpcURISuffix = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/uri-suffix");

			// 未設定の場合
			if(_jsspRpcURISuffix == null){
				_jsspRpcURISuffix = ".jssprpc";
			}
		}
		return _jsspRpcURISuffix;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JsspRpcConfigHandler#getMarshallerPagePath()
	 */
	public String getMarshallerPagePath() {
		if(_marshallerPagePath == null){
			_marshallerPagePath = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/marshller/page-path");

			// 未設定の場合
			if(_marshallerPagePath == null){
				_marshallerPagePath = "jssp_rpc/common/json";
			}
		}
		return _marshallerPagePath;
	}


	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JsspRpcConfigHandler#getUnmarshallFunctionName()
	 */
	public String getUnmarshallFunctionName() {
		if(_unmarshallFunctionName == null){
			_unmarshallFunctionName = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/marshller/function-name/unmarshall");

			// 未設定の場合
			if(_unmarshallFunctionName == null){
				_unmarshallFunctionName = "unmarshall";
			}
		}
		return _unmarshallFunctionName;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JsspRpcConfigHandler#getMarshallFunctionName()
	 */
	public String getMarshallFunctionName() {
		if(_marshallFunctionName == null){
			_marshallFunctionName = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/marshller/function-name/marshall");

			// 未設定の場合
			if(_marshallFunctionName == null){
				_marshallFunctionName = "marshall";
			}
		}
		return _marshallFunctionName;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JsspRpcConfigHandler#getMarshall4ArgumentsFunctionName()
	 */
	public String getMarshall4ArgumentsFunctionName() {
		if(_marshall4ArgumentsFunctionName == null){
			_marshall4ArgumentsFunctionName = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/marshller/function-name/marshall-arguments");

			// 未設定の場合
			if(_marshall4ArgumentsFunctionName == null){
				_marshall4ArgumentsFunctionName = "marshall4Arguments";
			}
		}
		return _marshall4ArgumentsFunctionName;
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JsspRpcConfigHandler#isThrowUnmarshallException()
	 */
	public boolean isThrowUnmarshallException() {
		if(_isThrowUnmarshallException == null){
			String isThrowUnmarshallExceptionString = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/marshller/throw-unmarshall-exception");
			
			if(isThrowUnmarshallExceptionString != null){
				_isThrowUnmarshallException = Boolean.valueOf(isThrowUnmarshallExceptionString);
			}
			// 未設定の場合
			else{
				_isThrowUnmarshallException = true;
			}
		}
		return _isThrowUnmarshallException;
	}

}
