package org.epcis.writer.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.epcis.model.EPCISDocumentType;
import org.epcis.model.EPCISQueryDocumentType;
import org.w3c.dom.Document;

public class XMLDocumentWriter {
	public static Document write(org.epcis.model.Document doc)
			throws ParserConfigurationException, JAXBException {
		Document result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		JAXBContext jc = null;
		if (doc instanceof EPCISDocumentType) {
			jc = JAXBContext.newInstance(EPCISDocumentType.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(doc, result);

			result.getDocumentElement().setPrefix("epcis");
			result.setXmlStandalone(true);
			// Document localName 을 수정하기 위해 EPCISDocumentType class에
			// @XmlRootElement(name = "EPCISDocument") 추가
			// RootElement를 추가해야 Marshalling 가능하다.
			result.normalize();

//		System.out.println(result.getDocumentElement().getAttributes().getLength());
		} else if (doc instanceof EPCISQueryDocumentType) {
			jc = JAXBContext.newInstance(EPCISQueryDocumentType.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(doc, result);
		}
		return result;
	}
}
