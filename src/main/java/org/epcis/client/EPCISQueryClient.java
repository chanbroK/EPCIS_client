package org.epcis.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EPCISQueryClient {
	// TODO: addParam 완성
	// TODO: REST
	// TODO: Subscribe, GetSubscriptionNames
	// TODO: response to EPCISEvent, EPCISVoca
	private URL queryEndpoint;
	private MethodName methodName; // GetQueryNames, GetStandardVersion, GetVendorVersion, Poll, Unsubscribe,
									// Subscribe, GetSubscriptionNames
	private Document queryDoc;
	private Element envelope;
	private Element header;
	private Element body;
	private Element query;
	private Element params;

	// Duplicate variable
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	public EPCISQueryClient(String URL, MethodName methodName) {
		try {
			this.queryEndpoint = new URL(URL);
			this.methodName = methodName;
			init();
		} catch (MalformedURLException e) {
			System.out.println("[Error] >> Wrong URL");
			e.printStackTrace();
		}
	}

	public EPCISQueryClient setMethodName(MethodName methodName) {
		this.body.removeChild(this.query);
		this.methodName = methodName;
		this.setQuery();
		this.body.appendChild(this.query);
		return this;
	}

	public String send() {
		try {
			HttpURLConnection connection = (HttpURLConnection) this.queryEndpoint.openConnection();
			connection.setRequestMethod("POST");
			String body;
			connection.setRequestProperty("Content-Type", "application/xml");
			connection.setDoOutput(true);
			body = this.toXMLString();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			bw.write(body);
			bw.flush();
			bw.close();

			// response handling
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			// response http code
			int responseCode = connection.getResponseCode();
			if (responseCode == 400) {
				System.out.println("400 ERROR");
			} else if (responseCode == 500) {
				System.out.println("500 ERROR");
			} else {
				System.out.println("200 SUCCESS");
			}
			String result = "";
			while (true) {
				String returnMsg = in.readLine();
				if (returnMsg == null) {
					break;
				}
				result += returnMsg + "\n";
			}
			return result;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//////////////////////////////////////////////////////////////////////////////////
	private void init() {
		try {
			this.queryDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			this.envelope = this.queryDoc.createElementNS("http://schemas.xmlsoap.org/soap/envelope/",
					"soapenv:Envelope");
			this.header = this.queryDoc.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "soapenv:Header");
			this.body = this.queryDoc.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "soapenv:Body");

			this.setQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.body.appendChild(this.query);
		this.header.appendChild(this.body);
		this.envelope.appendChild(this.header);
		this.queryDoc.appendChild(this.envelope);
	}

	//////////////////////////////////////////////////////////////////////////////////
	private void setQuery() {
		if (this.methodName == MethodName.Poll) {
			this.query = this.queryDoc.createElementNS("urn:epcglobal:epcis-query:xsd:1", "query:Poll");
		} else if (this.methodName == MethodName.GetQueryNames) {
			this.query = this.queryDoc.createElementNS("urn:epcglobal:epcis-query:xsd:1", "query:GetQueryNames");
		} else if (this.methodName == MethodName.GetStandardVersion) {
			this.query = this.queryDoc.createElementNS("urn:epcglobal:epcis-query:xsd:1", "query:GetStandardVersion");
		} else {
			// GetVendorVersion
			this.query = this.queryDoc.createElementNS("urn:epcglobal:epcis-query:xsd:1", "query:GetVendorVersion");
		}
	}

	public String toXMLString() {
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			StringWriter sw = new StringWriter();
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.transform(new DOMSource(this.queryDoc), new StreamResult(sw));
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public EPCISQueryClient setQueryName(QueryName queryName) {
		try {
			if (this.methodName != MethodName.Poll) {
				throw new Exception();
			}
			Element queryNameElement = this.queryDoc.createElement("queryName");
			queryNameElement.setTextContent(queryName.toString());
			this.query.appendChild(queryNameElement);
		} catch (Exception e) {
			System.out.println("[Error] >>" + this.methodName + "has no queryName");
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public EPCISQueryClient addParam(String name, Object value) {
		if (this.methodName != MethodName.Poll) {
			System.out.println("[Error] >>" + this.methodName + "has no queryName");
			return null;
		}
		if (this.params == null) {
			this.params = this.queryDoc.createElement("params");
			this.query.appendChild(this.params);
		}
		Element paramElem = this.queryDoc.createElement("param");
		Element nameElem = this.queryDoc.createElement("name");
		Element valueElem = this.queryDoc.createElement("value");
		if (value instanceof List) {
			// query:ArrayOfString
			System.out.println("[ArrayOfString]");
			valueElem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "query:ArrayOfString");
			for (String obj : (List<String>) value) {
				Element objElem = this.queryDoc.createElement("string");
				objElem.setTextContent(obj);
				valueElem.appendChild(objElem);
			}
		} else if (value instanceof String) {
			// xsd:string
			System.out.println("[String]");
			valueElem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xsd:string");
			valueElem.setTextContent(value.toString());
		} else if (value instanceof Date) {
			// xsd:dateTime
			// TODO : date format convert!!
			System.out.println("[DateTime]");
			// convert date format
			String dateValue = this.dateFormat.format(value);
			valueElem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xsd:dateTime");
			valueElem.setTextContent(dateValue.toString());
		} else if (value instanceof Double || value instanceof Float) {
			// xsd:double
			System.out.println("[Double]");
			valueElem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xsd:double");
			valueElem.setTextContent(value.toString());
		} else if (value instanceof Integer) {
			// xsd:integer
			System.out.println("[Integer]");
			valueElem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xsd:integer");
			valueElem.setTextContent(value.toString());
		} else if (value instanceof Boolean) {
			// xsd:boolean
			// true / false
			System.out.println("[Boolean]");
			valueElem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xsd:boolean");
			valueElem.setTextContent(value.toString());
		} else if (value == null) {
			// Void Parameter value type
			// query:VoidHolder
			System.out.println("[VoidHolder]");
			valueElem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "VoidHolder");
		} else {
			System.out.println("[Error] >>" + value
					+ "allow value data type is List, String, Date, Double, Float, Integer, Boolean and null");
			return null;
		}
		paramElem.appendChild(nameElem);
		paramElem.appendChild(valueElem);
		this.params.appendChild(paramElem);
		return this;
	}

}
