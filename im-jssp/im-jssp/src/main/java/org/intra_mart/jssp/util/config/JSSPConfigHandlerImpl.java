package org.intra_mart.jssp.util.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.intra_mart.common.aid.jdk.javax.xml.XMLProperties;
import org.mozilla.javascript.Context;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * コンフィグファイルの設定値を取得するための標準実装です。
 */
public class JSSPConfigHandlerImpl implements JSSPConfigHandler {

    private static final String ROOT_ELEMENT ="/intra-mart/jssp";
    
    private XMLProperties jsspConfigProperties;
    
    private Document jsspConfigDocument;
    private XPath xpath;

	private String _initialFunctionName;
	private String _finallyFunctionName;

	private Boolean _debugBrowseEnable;
	private Boolean _debugPrintEnable;
	private Boolean _debugWriteEnable;
	private String _debugWriteFilePath;
	private Boolean _debugConsoleEnable;

	private String _uriPrefix;
	private String _uriSuffix;
	private String _jsspKey4FromPagePath;
	private String _jsspKey4NextEventPagePath;
	private String _jsspKey4NextEventName;
	private String _jsspKey4ActionEventPagePath;
	private String _jsspKey4ActionEventName;
	private String _jsspKey4Mark;
	private String _signatureKey;
    
	private int _languageVersion = Integer.MIN_VALUE;
	/**
	 * このコンストラクタは、
	 * リソース「/org/intra_mart/jssp/util/config/jssp-config.xml」をコンフィグファイルとします。
	 * 
	 * @deprecated JSSP実行環境の起動時にのみ利用します。
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public JSSPConfigHandlerImpl() throws ParserConfigurationException, SAXException, IOException {
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
	public JSSPConfigHandlerImpl(String configFilePath) throws ParserConfigurationException, SAXException, IOException {
		super();
		
        InputStream is = JSSPConfigHandlerImpl.class.getResourceAsStream(configFilePath);

        try{
	        if(is != null) {
	        	jsspConfigProperties = new XMLProperties(is);
	        }
	        else {
	        	throw new FileNotFoundException(configFilePath + " is not found.");
	        }
        }
        finally{
        	is.close();
        }

	
        try {
			is = JSSPConfigHandlerImpl.class.getResourceAsStream(configFilePath);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			jsspConfigDocument = builder.parse(is);

			XPathFactory factory = XPathFactory.newInstance();
			xpath = factory.newXPath();
		}
        finally{
        	is.close();
        }
	}
	
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getServerCharacterEncoding()
	 */
	public String getServerCharacterEncoding() {
		String serverCharacterEncoding = 
			jsspConfigProperties.getProperty("/intra-mart/server-character-encoding");

		// 未設定の場合
		if(serverCharacterEncoding == null){		
			serverCharacterEncoding = System.getProperty("file.encoding");
		}
		
		return 	serverCharacterEncoding;		
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getOutputDirectory4ComiledScript()
	 */
	public String getOutputDirectory4ComiledScript() {
		String outputDirectory4ComiledScript = 
					jsspConfigProperties.getProperty(ROOT_ELEMENT + "/compile/output/script");
		
		// 未設定の場合
		if(outputDirectory4ComiledScript == null){		
			outputDirectory4ComiledScript = "WEB-INF/work/jssp/_functioncontainer";
		}
		
		return 	outputDirectory4ComiledScript;		
	}


	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getOutputDirectory4ComiledView()
	 */
	public String getOutputDirectory4ComiledView() {
		
		String outputDirectory4ComiledView = 
					jsspConfigProperties.getProperty(ROOT_ELEMENT + "/compile/output/view");	
		
		// 未設定の場合
		if(outputDirectory4ComiledView == null){		
			outputDirectory4ComiledView = "WEB-INF/work/jssp/_presentationpage";
		}
		
		return 	outputDirectory4ComiledView;		
	}
	
	
	private static final String ID_GENERAL_SOURCE_PATH = ROOT_ELEMENT + "/source-path/general/directory";
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getGeneralSourceDirectories()
	 */
	public String[] getGeneralSourceDirectories() {
		String[] generalSourceDirs = jsspConfigProperties.getProperties(ID_GENERAL_SOURCE_PATH);
		
		// 未設定の場合は、ホームディレクトリを標準のソースパスとする
		if(generalSourceDirs == null || generalSourceDirs.length == 0){		
			generalSourceDirs = new String[1];
			generalSourceDirs[0] = HomeDirectory.instance().getAbsolutePath();
		}
		
		return 	generalSourceDirs;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getSourceDirectories(java.util.Locale)
	 */
	public String[] getSourceDirectories(Locale locale) {
		
		List<String> sourceDirectoryLists = new ArrayList<String>();

		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		if (variant.length() > 0) {
			String localeName = language + "_" + country + "_" + variant;
			sourceDirectoryLists.addAll(getSourceDirectoryFromI18NSourcePathPrefixAndSufix(localeName));
			sourceDirectoryLists.addAll(getSourceDirectoryFromI18NSourcePath(localeName));
		}
		if (country.length() > 0) {
			String localeName = language + "_" + country;
			sourceDirectoryLists.addAll(getSourceDirectoryFromI18NSourcePathPrefixAndSufix(localeName));
			sourceDirectoryLists.addAll(getSourceDirectoryFromI18NSourcePath(localeName));
		}
		if (language.length() > 0) {
			String localeName = language;
			sourceDirectoryLists.addAll(getSourceDirectoryFromI18NSourcePathPrefixAndSufix(localeName));
			sourceDirectoryLists.addAll(getSourceDirectoryFromI18NSourcePath(localeName));
		}

		return sourceDirectoryLists.toArray(new String[sourceDirectoryLists.size()]);
		
	}
	
	private static final String ID_I18N_SOURCE_PATH_PREFIX = ROOT_ELEMENT + "/source-path/international/local/";
	private static final String ID_I18N_SOURCE_PATH_SUFFIX = "/directory";

	/**
	 * @param localeName
	 * @return
	 */
	private List<String> getSourceDirectoryFromI18NSourcePathPrefixAndSufix(String localeName){
				
		String key = ID_I18N_SOURCE_PATH_PREFIX
						.concat(localeName)
							.concat(ID_I18N_SOURCE_PATH_SUFFIX);

		String[] sourceDirs = jsspConfigProperties.getProperties(key);
		return Arrays.asList(sourceDirs);	
	
	}
	
	
	private static final String ID_I18N_SOURCE_PATH = ROOT_ELEMENT + "/source-path/international/directory";

	/**
	 * @param localeName
	 * @return
	 */
	private List<String> getSourceDirectoryFromI18NSourcePath(String localeName) {

		List<String> list = new ArrayList<String> ();
		
		String[] sourceDirs = jsspConfigProperties.getProperties(ID_I18N_SOURCE_PATH);

		for(String path : sourceDirs){
			if(path.endsWith("/") || path.endsWith(File.separator)){
				list.add(path + localeName);
			}
			else{
				list.add(path + "/" + localeName);
			}				
		}
		
		return list;
	}
	
	private static final String ID_GENERAL_CLASS_PATH = ROOT_ELEMENT + "/class-path/general";
	private static final String ID_I18N_CLASS_PATH = ROOT_ELEMENT + "/class-path/international";

	private static final String ID_CLASS_PATH_DIRECTORIES = "/classes";
	private static final String ID_CLASS_ARCHIVES = "/archive";
	private static final String ID_CLASS_ARCHIVE_DIRECTORIES = "/libraries";
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getGeneralClassPathDirectories()
	 */
	public String[] getGeneralClassPathDirectories() {
		return jsspConfigProperties.getProperties(ID_GENERAL_CLASS_PATH + ID_CLASS_PATH_DIRECTORIES);
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getClassPathDirectories(java.util.Locale)
	 */
	public String[] getClassPathDirectories(Locale locale) {
		return jsspConfigProperties.getProperties(ID_I18N_CLASS_PATH + "/" + locale.toString() + ID_CLASS_PATH_DIRECTORIES);
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getGeneralClassArchives()
	 */
	public String[] getGeneralClassArchives() {
		return jsspConfigProperties.getProperties(ID_GENERAL_CLASS_PATH + ID_CLASS_ARCHIVES);
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getClassArchives(java.util.Locale)
	 */
	public String[] getClassArchives(Locale locale) {
		return jsspConfigProperties.getProperties(ID_I18N_CLASS_PATH + "/" + locale.toString() + ID_CLASS_ARCHIVES);
	}


	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getGeneralClassArchiveDirectories()
	 */
	public String[] getGeneralClassArchiveDirectories() {
		return jsspConfigProperties.getProperties(ID_GENERAL_CLASS_PATH + ID_CLASS_ARCHIVE_DIRECTORIES);
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getClassArchiveDirectories(java.util.Locale)
	 */
	public String[] getClassArchiveDirectories(Locale locale) {
		return jsspConfigProperties.getProperties(ID_I18N_CLASS_PATH + "/" + locale.toString() + ID_CLASS_ARCHIVE_DIRECTORIES);
	}


	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJavaScriptAPI4Class()
	 */
	public String[] getJavaScriptAPI4Class() {
		try {
			if(exprJavaScriptAPI4Class == null){
				exprJavaScriptAPI4Class = xpath.compile(ROOT_ELEMENT + "/java-script-api/api-class/@name");
			}

			return getAttrValues(exprJavaScriptAPI4Class);
		}
		catch (XPathExpressionException e) {
			throw new IllegalStateException(e);
		}
	}
	private XPathExpression exprJavaScriptAPI4Class = null;
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJavaScriptAPI4Script()
	 */
	public String[] getJavaScriptAPI4Script() {
		try {
			if(exprJavaScriptAPI4Script == null){
				exprJavaScriptAPI4Script = xpath.compile(ROOT_ELEMENT + "/java-script-api/api-script/@name");
			}

			return getAttrValues(exprJavaScriptAPI4Script);
		}
		catch (XPathExpressionException e) {
			throw new IllegalStateException(e);
		}
	}
	private XPathExpression exprJavaScriptAPI4Script = null;


	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJavaScriptAPI4ClassConfig(java.lang.String)
	 */
	public NodeList getJavaScriptAPI4ClassConfig(String name) {
		return getNodeList(ROOT_ELEMENT + "/java-script-api/api-class[@name=\"" + name + "\"]/*");
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJavaScriptAPI4ScriptConfig(java.lang.String)
	 */
	public NodeList getJavaScriptAPI4ScriptConfig(String name) {
		return getNodeList(ROOT_ELEMENT + "/java-script-api/api-script[@name=\"" + name + "\"]/*");
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJSSPTags4ClassConfig(java.lang.String)
	 */
	public NodeList getJSSPTags4ClassConfig(String name) {
		return getNodeList(ROOT_ELEMENT + "/jssp-tag/tag-class[@name=\"" + name + "\"]/*");
	}

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJSSPTags4ScriptConfig(java.lang.String)
	 */
	public NodeList getJSSPTags4ScriptConfig(String name) {
		return getNodeList(ROOT_ELEMENT + "/jssp-tag/tag-script[@name=\"" + name + "\"]/*");
	}

	private NodeList getNodeList(String expression){
		try {
			XPathExpression expr = xpath.compile(expression);
			Object result = expr.evaluate(jsspConfigDocument, XPathConstants.NODESET);
			NodeList nodeList = (NodeList) result;
			return nodeList;
		}
		catch (XPathExpressionException e) {
			throw new IllegalStateException(e);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJSSPTags4Class()
	 */
	public String[] getJSSPTags4Class() {
		try {
			if(exprJSSPTags4Class == null){
				exprJSSPTags4Class = xpath.compile(ROOT_ELEMENT + "/jssp-tag/tag-class/@name");
			}

			return getAttrValues(exprJSSPTags4Class);
		}
		catch (XPathExpressionException e) {
			throw new IllegalStateException(e);
		}
	}
	private XPathExpression exprJSSPTags4Class = null;
	

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJSSPTags4Script()
	 */
	public String[] getJSSPTags4Script() {
		try {
			if(exprJSSPTags4Script == null){
				exprJSSPTags4Script = xpath.compile(ROOT_ELEMENT + "/jssp-tag/tag-script/@name");
			}

			return getAttrValues(exprJSSPTags4Script);
		}
		catch (XPathExpressionException e) {
			throw new IllegalStateException(e);
		}
	}
	private XPathExpression exprJSSPTags4Script = null;

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getInitializer4Class()
	 */
	public String[] getInitializer4Class() {
		return jsspConfigProperties.getProperties(ROOT_ELEMENT + "/initializer/application/initializer-class");
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getInitializer4Script()
	 */
	public String[] getInitializer4Script() {
		return jsspConfigProperties.getProperties(ROOT_ELEMENT + "/initializer/application/initializer-script");
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getContextFactoryListeners()
	 */
	public String[] getContextFactoryListeners() {
		return jsspConfigProperties.getProperties(ROOT_ELEMENT + "/listener/context-factory-listener/listener-class");		
	}


	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getRequestProcessPagePath()
	 */
	public String getRequestProcessScript() {
		return jsspConfigProperties.getProperty(ROOT_ELEMENT + "/script/request-process-script");		
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getInitialFunctionName()
	 */
	public String getInitialFunctionName() {
		if(_initialFunctionName == null){
			_initialFunctionName = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/script/initial-function-name");		
			
			// 未設定の場合
			if(_initialFunctionName == null){
				_initialFunctionName = "init";
			}			
		}
		
		return _initialFunctionName;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getFinallyFunctionName()
	 */
	public String getFinallyFunctionName() {
		if(_finallyFunctionName == null){
			_finallyFunctionName = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/script/finally-function-name");		
			
			// 未設定の場合
			if(_finallyFunctionName == null){
				_finallyFunctionName = "close";
			}			
		}
		return _finallyFunctionName;
	}
	
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#isDebugBrowseEnable()
	 */
	public boolean isDebugBrowseEnable() {
		if(_debugBrowseEnable == null){
			String debugBrowseString = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/debug/browse/@enable");
			
			if(debugBrowseString != null){
				_debugBrowseEnable = Boolean.valueOf(debugBrowseString);
			}
			// 未設定の場合
			else{
				_debugBrowseEnable = true;
			}
		}
		return _debugBrowseEnable;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#isDebugPrintEnable()
	 */
	public boolean isDebugPrintEnable() {
		if(_debugPrintEnable == null){
			String debugPrintString = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/debug/print/@enable");
			
			if(debugPrintString != null){
				_debugPrintEnable = Boolean.valueOf(debugPrintString);
			}
			// 未設定の場合
			else{
				_debugPrintEnable = true;
			}
		}
		return _debugPrintEnable;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#isDebugWriteEnable()
	 */
	public boolean isDebugWriteEnable() {
		if(_debugWriteEnable == null){
			String debugWriteString = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/debug/write/@enable");
			
			if(debugWriteString != null){
				_debugWriteEnable = Boolean.valueOf(debugWriteString);
			}
			// 未設定の場合
			else{
				_debugWriteEnable = true;
			}
		}
		return _debugWriteEnable;
	}
	
	

	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getDebugWriteFilePath()
	 */
	public String getDebugWriteFilePath() {
		if(_debugWriteFilePath == null){
			_debugWriteFilePath = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/debug/write/@path");

			// 未設定の場合
			if(_debugWriteFilePath == null || _debugWriteFilePath.length() == 0){
				_debugWriteFilePath = "debug.log";
			}			

		}
		return _debugWriteFilePath;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#isDebugConsoleEnable()
	 */
	public boolean isDebugConsoleEnable() {
		if(_debugConsoleEnable == null){
			String debugConsoleString = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/debug/console/@enable");
			
			if(debugConsoleString != null){
				_debugConsoleEnable = Boolean.valueOf(debugConsoleString);
			}
			// 未設定の場合
			else{
				_debugConsoleEnable = true;
			}
		}
		return _debugConsoleEnable;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJsspKey4ActionEventName()
	 */
	public String getJsspKey4ActionEventName() {
		if(_jsspKey4ActionEventName == null){
			_jsspKey4ActionEventName = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/action-event-name");

			// 未設定の場合
			if(_jsspKey4ActionEventName == null){
				_jsspKey4ActionEventName = "im_action";
			}			
		}
		return _jsspKey4ActionEventName;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJsspKey4ActionEventPagePath()
	 */
	public String getJsspKey4ActionEventPagePath() {
		if(_jsspKey4ActionEventPagePath == null){
			_jsspKey4ActionEventPagePath = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/action-event-page-path");

			// 未設定の場合
			if(_jsspKey4ActionEventPagePath == null){
				_jsspKey4ActionEventPagePath = "im_event";
			}
		}
		return _jsspKey4ActionEventPagePath;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJsspKey4FromPagePath()
	 */
	public String getJsspKey4FromPagePath() {
		if(_jsspKey4FromPagePath == null){
			_jsspKey4FromPagePath = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/from-page-path");

			// 未設定の場合
			if(_jsspKey4FromPagePath == null){
				_jsspKey4FromPagePath = "im_from";
			}	
		}
		return _jsspKey4FromPagePath;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJsspKey4Mark()
	 */
	public String getJsspKey4Mark() {
		if(_jsspKey4Mark == null){
			_jsspKey4Mark = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/mark");

			// 未設定の場合
			if(_jsspKey4Mark == null){
				_jsspKey4Mark = "im_mark";
			}	
		}
		return _jsspKey4Mark;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJsspKey4NextEventName()
	 */
	public String getJsspKey4NextEventName() {
		if(_jsspKey4NextEventName == null){
			_jsspKey4NextEventName = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/next-event-name");

			// 未設定の場合
			if(_jsspKey4NextEventName == null){
				_jsspKey4NextEventName = "im_func";
			}	
		}
		return _jsspKey4NextEventName;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getJsspKey4NextEventPagePath()
	 */
	public String getJsspKey4NextEventPagePath() {
		if(_jsspKey4NextEventPagePath == null){
			_jsspKey4NextEventPagePath = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/next-event-page-path");

			// 未設定の場合
			if(_jsspKey4NextEventPagePath == null){
				_jsspKey4NextEventPagePath = "";
			}
		}
		return _jsspKey4NextEventPagePath;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getSignatureKey()
	 */
	public String getSignatureKey() {
		if(_signatureKey == null){
			_signatureKey = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/signature");

			// 未設定の場合
			if(_signatureKey == null){
				_signatureKey = "org.intra_mart.jssp.signature.id";
			}
		}
		return _signatureKey;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getURIPrefix()
	 */
	public String getURIPrefix() {
		if(_uriPrefix == null){
			_uriPrefix = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/uri-prefix");

			// 未設定の場合
			if(_uriPrefix == null){
				_uriPrefix = "";
			}
		}
		return _uriPrefix;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getURISuffix()
	 */
	public String getURISuffix() {
		if(_uriSuffix == null){
			_uriSuffix = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/key/uri-suffix");

			// 未設定の場合
			if(_uriSuffix == null){
				_uriSuffix = ".jssps";
			}
		}
		return _uriSuffix;
	}

	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.util.config.JSSPConfigHandler#getLanguageVersion()
	 */
	public int getLanguageVersion() {
		
		if(_languageVersion == Integer.MIN_VALUE){
			String versionString = 
				jsspConfigProperties.getProperty(ROOT_ELEMENT + "/language-version");
			
            if(versionString != null){
        		double d;
				try {
					d = Double.parseDouble(versionString);
				}
				catch (NumberFormatException e) {
					throw new IllegalArgumentException("Bad language version: " + versionString);
				}
            		
                if(Context.isValidLanguageVersion((int) d)){
    				_languageVersion = (int) d;
                }
                else{
                	Context.checkLanguageVersion((int) (d * 100));
    				_languageVersion = (int) (d * 100);
                }
			}
			// 未設定の場合
			else{
				_languageVersion = Context.VERSION_DEFAULT;
			}
		}
		
		return _languageVersion;
	}


	private String[] getAttrValues(XPathExpression expression) throws XPathExpressionException {
		Object result = expression.evaluate(jsspConfigDocument, XPathConstants.NODESET);
		NodeList nodeList = (NodeList) result;
		
		List<String> list = new ArrayList<String>();
		for(int idx = 0; idx < nodeList.getLength(); idx++){
			Node node = nodeList.item(idx);
			switch (node.getNodeType()) {
				case Node.ATTRIBUTE_NODE:
					Attr attr = (Attr) node;
					list.add(attr.getValue());
					break;
	
				default:
					list.add(node.getTextContent());
					break;
			}
		}
		
		return list.toArray(new String[list.size()]);
	}

}
