package com.bluestone.app.payment.gateways.atom;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Component
public class AtomXMLParserService {
	
	private static final Logger log = LoggerFactory.getLogger(AtomXMLParserService.class);
	
	public Map<String, String> parseResponse(String vXMLStr) throws ParserConfigurationException, SAXException, IOException{
		log.debug("AtomXMLParserService.parseResponse(): xml String=[{}]", vXMLStr);
		Map<String,String> xmlResponseMap = new HashMap<String, String>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	InputSource isBuf = new InputSource();
    	isBuf.setCharacterStream(new StringReader(vXMLStr));
    	Document doc = dBuilder.parse(isBuf);
    	doc.getDocumentElement().normalize();
    	
    	NodeList nList = doc.getElementsByTagName("RESPONSE");

    	for (int tempN = 0; tempN < nList.getLength(); tempN++) 
    	{
    		Node nNode = nList.item(tempN);
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) 
    		{
    		  Element eElement = (Element) nNode;            		  
    		  String xmlURL = eElement.getElementsByTagName("url").item(0).getChildNodes().item(0).getNodeValue();
    		  xmlResponseMap.put("xmlURL", xmlURL);
    		  NodeList aList = eElement.getElementsByTagName("param");
    		  String vParamName;
    		  for (int atrCnt = 0; atrCnt< aList.getLength();atrCnt++)
    		  {
    			  vParamName = aList.item(atrCnt).getAttributes().getNamedItem("name").getNodeValue();            			  
    			  
    			  if (vParamName.equals("ttype") )
    			  {
    				  String xmlttype = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
    				  xmlResponseMap.put("xmlttype", xmlttype);
    			  }
    			  else if (vParamName.equals("tempTxnId") )
    			  {
    				  String xmltempTxnId = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
    				  xmlResponseMap.put("xmltempTxnId", xmltempTxnId);
    			  }
    			  else if (vParamName.equals("token") )
    			  {
    				  String xmltoken = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
    				  xmlResponseMap.put("xmltoken", xmltoken);
    			  }
    			  else if (vParamName.equals("txnStage") )
    			  {
    				  String xmltxnStage = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
    				  xmlResponseMap.put("xmltxnStage", xmltxnStage);
    			  }
    		  }
    		}
    	}

    	return xmlResponseMap;    	
	}

}
