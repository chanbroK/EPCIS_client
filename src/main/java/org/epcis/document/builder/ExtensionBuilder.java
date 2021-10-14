package org.epcis.document.builder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.epcis.client.EPCISCaptureClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExtensionBuilder {
	// 2계층 까지 지원
	Element extension;
	Document doc;

	public ExtensionBuilder(String name, String namespace, String prefix) {
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			this.doc = docBuilder.newDocument();
			this.extension = doc.createElementNS(namespace, name);

			if (EPCISCaptureClient.namespaceMap == null) {
				EPCISCaptureClient.namespaceMap = new ArrayList<>();
			}
			if (EPCISCaptureClient.namespaceMap.contains(namespace)) {

			}
			extension.setPrefix(prefix);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Element build() {
		return extension;
	}

	public ExtensionBuilder addChild(Element child) {
		// appendChild
		// Error : org.w3c.dom.DOMException: WRONG_DOCUMENT_ERR: A node is used in a
		// different document than the one that created it.
		extension.appendChild(child);
		return this;
	}

	public ExtensionBuilder addChild(String name, String namespace, String prefix, Object value) {
		Element child = doc.createElementNS(namespace, name);
		child.setPrefix(prefix);
		if (value instanceof Integer) {
			child.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
					"http://www.w3.org/2001/XMLSchema:integer");
		} else if (value instanceof Double) {
//			child.setAttribute("xsi", prefix);
			child.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
					"http://www.w3.org/2001/XMLSchema:double");
		} else if (value instanceof String) {
			child.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
					"http://www.w3.org/2001/XMLSchema:string");
		} else if (value instanceof Boolean) {
			child.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
					"http://www.w3.org/2001/XMLSchema:boolean");
		} else if (value instanceof Date) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			try {
				Date date = format.parse(value.toString());
				child.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
						"http://www.w3.org/2001/XMLSchema:date");
				child.appendChild(doc.createTextNode(date.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("extension value type must be Integer,Double or String");
		}
		child.appendChild(doc.createTextNode(value.toString()));
		extension.appendChild(child);
		return this;
	}

	public ExtensionBuilder setValue(Object value) {
		if (value instanceof Integer) {
			extension.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
					"http://www.w3.org/2001/XMLSchema:integer");
		} else if (value instanceof Double) {
			extension.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
					"xsd:double");
		} else if (value instanceof String) {
			extension.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
					"http://www.w3.org/2001/XMLSchema:string");

		} else if (value instanceof Boolean) {
			extension.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type",
					"http://www.w3.org/2001/XMLSchema:boolean");
		} else if (value instanceof Date) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			try {
				Date date = format.parse(value.toString());
				System.out.println(date.toString());
				extension.appendChild(doc.createTextNode(date.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("extension value type must be Integer,Double or String");
		}
		extension.appendChild(doc.createTextNode(value.toString()));
		return this;
	}
}
