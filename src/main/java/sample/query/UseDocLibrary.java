package sample.query;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UseDocLibrary {

	public static void main(String[] args) {
		String queryName = "SimpleEventQuery";
		String paramName = "EQ_bizLocation";
		String paramValueType = "query:ArrayOfString";
		String paramValue1 = "urn:epc:id:sgln:0614141.00888.1";
		String paramValue2 = "urn:epc:id:sgln:0614141.00888.2";
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			doc.setXmlStandalone(true);

			Element envelope = doc.createElementNS("http://schemas.xmlsoap.org/soap/envelope", "Envelope");
			envelope.setAttribute("xmlns:query", "urn:epcglobal:epcis-query:xsd:1");
			envelope.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			envelope.setPrefix("soapenv");
			doc.appendChild(envelope);

			Element header = doc.createElementNS("http://schemas.xmlsoap.org/soap/envelope", "Header");
			header.setPrefix("soapenv");
			envelope.appendChild(header);

			Element body = doc.createElementNS("http://schemas.xmlsoap.org/soap/envelope", "Body");
			body.setPrefix("soapenv");
			header.appendChild(body);

			Element poll = doc.createElementNS("urn:epcglobal:epcis-query:xsd:1", "Poll");
			poll.setPrefix("query");
			body.appendChild(poll);

			Element params = doc.createElement("params");
			poll.appendChild(params);

			Element param = doc.createElement("param");
			params.appendChild(param);

			Element name = doc.createElement("name");
			param.appendChild(name);

			Element value = doc.createElement("value");
			value.setAttribute("xsi:type", paramValueType);
			param.appendChild(value);

			Element valueString1 = doc.createElement("string");
			valueString1.setTextContent(paramValue1);
			value.appendChild(valueString1);

			Element valueString2 = doc.createElement("string");
			valueString2.setTextContent(paramValue2);
			value.appendChild(valueString2);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		DOMSource source = new DOMSource(doc);
//		StreamResult result = new StreamResult(out);
//		TransformerFactory transFactory = TransformerFactory.newInstance();
//		Transformer transformer = transFactory.newTransformer();
//		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//		transformer.transform(source, result);
//
//		System.out.println(out.toString());

	}
}
