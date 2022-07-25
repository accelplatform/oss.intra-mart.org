package org.intra_mart.common.aid.jdk.javax.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * OPEN INTRA-MART モジュールを intra-mart WebPlatform 上で動作させるためのラッパークラスです。
 * 
 * @see jp.co.intra_mart.common.aid.jdk.javax.xml.XMLProperties
 */
public class XMLProperties extends jp.co.intra_mart.common.aid.jdk.javax.xml.XMLProperties{
	private static final long serialVersionUID = -1639180228521183697L;

	public XMLProperties(File f) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		super(f);
	}

	public XMLProperties(InputStream in) throws ParserConfigurationException, SAXException, IOException{
		super(in);
	}

	public XMLProperties(InputSource in) throws ParserConfigurationException, SAXException, IOException{
		super(in);
	}

	public XMLProperties(InputStream in, Properties parent) throws ParserConfigurationException, SAXException, IOException{
		super(in, parent);
	}

	public XMLProperties(InputSource in, Properties parent) throws ParserConfigurationException, SAXException, IOException{
		super(in, parent);
	}
}

