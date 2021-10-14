package org.epcis.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.epcis.document.builder.validator;
import org.epcis.writer.json.JSONDocumentWriter;
import org.epcis.model.AggregationEventType;
import org.epcis.model.AssociationEventType;
import org.epcis.model.EPCISBodyType;
import org.epcis.model.EPCISDocumentType;
import org.epcis.model.EPCISHeaderExtensionType;
import org.epcis.model.EPCISHeaderType;
import org.epcis.model.EPCISMasterDataType;
import org.epcis.model.EventListType;
import org.epcis.model.ObjectEventType;
import org.epcis.model.TransactionEventType;
import org.epcis.model.TransformationEventType;
import org.epcis.model.VocabularyListType;
import org.epcis.model.VocabularyType;
import org.epcis.util.UnitConverter;
import org.epcis.writer.xml.XMLDocumentWriter;
import org.w3c.dom.Document;

public class EPCISCaptureClient {
	// Step 1: just create EPCISCaptureClient with url and document and send
	// Step 2: enable to add an individual event as well as vocabulary. At this
	// moment, very detailed validation will be conducted by validate*
	// I put validateAggregationEvent. Other events and vocabulary are same
	// Step 3: epcisDocument to XML string is straightforward
	// to Json is difficult. Good Luck!
	// Step 4: fluent style event as well as vocabulary builder
	// please focus on convenient aspect and cover whole data building
	// TODO : name space simplify --> Root to Leaf clearNamespace
	// TODO : pojo to json
	// TODO : sensorData -> SensorElementBuilder 만들기
	public static UnitConverter unitConverter;
	public static List<String> namespaceMap;
	public URL captureEndpoint;
	public EPCISDocumentType epcisDoc;

	// event
	public EPCISBodyType epcisBody;
	public EventListType eventList;
	public List<Object> events;

	// vocabulary
	public EPCISHeaderType epcisHeader;
	public EPCISHeaderExtensionType epcisHeaderExtension;
	public EPCISMasterDataType masterData;
	public VocabularyListType vocaList;
	public List<VocabularyType> vocabularies;

	public EPCISCaptureClient(String URL) {
		try {
			captureEndpoint = new URL(URL);
			init();
		} catch (MalformedURLException e) {
			System.out.println("Wrong URL");
			e.printStackTrace();
		}
	}

	private void init() {
		epcisDoc = new EPCISDocumentType();

		// event
		epcisBody = new EPCISBodyType();
		eventList = new EventListType();
		events = new ArrayList<Object>();
		eventList.setObjectEventOrAggregationEventOrTransformationEvent(events);
		epcisBody.setEventList(eventList);
		epcisDoc.setEPCISBody(epcisBody);
		// vocabulary
		epcisHeader = new EPCISHeaderType();
		epcisHeaderExtension = new EPCISHeaderExtensionType();
		masterData = new EPCISMasterDataType();
		vocaList = new VocabularyListType();
		vocabularies = new ArrayList<VocabularyType>();
		vocaList.setVocabulary(vocabularies);
		masterData.setVocabularyList(vocaList);
		epcisHeaderExtension.setEPCISMasterData(masterData);
		epcisHeader.setExtension(epcisHeaderExtension);
		epcisDoc.setEPCISHeader(epcisHeader);
		// update namespaceMap
		Map<QName, String> docAttrMap = epcisDoc.getOtherAttributes();
		System.out.println(docAttrMap.size());
		docAttrMap.forEach((qName, uri) -> {
			System.out.println(uri);
		});
		this.setEPCISDoc();
	}

	public void addEvent(Object event) {
		boolean isValid = false;
		if (event instanceof AggregationEventType) {
			isValid = validator.validateAggregationEvent((AggregationEventType) event);
		} else if (event instanceof ObjectEventType) {
			isValid = validator.validateObjectEvent((ObjectEventType) event);
		} else if (event instanceof TransactionEventType) {
			isValid = validator.validateTransactionEvent((TransactionEventType) event);
		} else if (event instanceof TransformationEventType) {
			isValid = validator.validateTransformationEvent((TransformationEventType) event);
		} else if (event instanceof AssociationEventType) {
			isValid = validator.validateAssociationEvent((AssociationEventType) event);
		}
		if (isValid) {
			events.add(event);
		} else {
			System.out.println("Event is not valid");
		}
	}

	public void addVoca(VocabularyType voca) {
		this.vocabularies.add(voca);
	}

	public void clearEvent() {
		events = new ArrayList<Object>();
	}

	public void clearVoca() {
		vocabularies = new ArrayList<VocabularyType>();
	}

	public void clearAll() {
		init();
	}

	public Integer sendEPCISDocument(boolean byJson) {
		try {
			HttpURLConnection connection = (HttpURLConnection) this.captureEndpoint
					.openConnection();
			connection.setRequestMethod("POST");
			String body;
			if (byJson) {
				// using json
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);
				body = this.toJsonLDString();
			} else {
				// using xml
				connection.setRequestProperty("Content-Type", "application/xml");
				connection.setDoOutput(true);
				body = this.toXMLString();
			}
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream()));
			bw.write(body);
			bw.flush();
			bw.close();

			// response handling
			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			// response http code
			int responseCode = connection.getResponseCode();
			if (responseCode == 400) {
				System.out.println("400 ERROR");
			} else if (responseCode == 500) {
				System.out.println("500 ERROR");
			} else {
				System.out.println("200 SUCCESS");
			}
			String returnMsg = in.readLine();
			System.out.println("RESPONSE" + returnMsg);
			return responseCode;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer sendEPCISDocumentByString(boolean byJson, String body) {
		try {
			HttpURLConnection connection = (HttpURLConnection) this.captureEndpoint
					.openConnection();
			connection.setRequestMethod("POST");
			if (byJson) {
				// using json
				connection.setRequestProperty("Content-Type", "application/json");
			} else {
				// using xml
				connection.setRequestProperty("Content-Type", "application/xml");
			}
			connection.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream()));
			bw.write(body);
			bw.flush();
			bw.close();

			// response handling
			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			// response http code
			int responseCode = connection.getResponseCode();
			if (responseCode == 400) {
				System.out.println("400 ERROR");
			} else if (responseCode == 500) {
				System.out.println("500 ERROR");
			} else {
				System.out.println("200 SUCCESS");
			}
			String returnMsg = in.readLine();
			System.out.println("RESPONSE" + returnMsg);
			return responseCode;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setEPCISDoc() {

		// set CreationDate
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String dateValue = format.format(new Date());
		Date date = null;
		try {
			date = format.parse(dateValue);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar xmlGreCal = null;
		try {
			xmlGreCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		this.epcisDoc.setCreationDate(xmlGreCal);

		// setSchemaVersion
		this.epcisDoc.setSchemaVersion(new BigDecimal("2.0"));

		// set
		System.out.println(this.epcisDoc.getOtherAttributes().toString());
	}

	public String toXMLString() {
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			StringWriter sw = new StringWriter();
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.transform(new DOMSource(toXML()), new StreamResult(sw));
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String toJsonLDString() {
		String result = toJsonLD().toJson();
		System.out.println(result);
		return result;
	}

	public Document toXML() {
		try {
			return XMLDocumentWriter.write(this.epcisDoc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public org.bson.Document toJsonLD() {
		return JSONDocumentWriter.write(this.epcisDoc, this.events, this.vocabularies);
	}

//	public void cleanNS(Document doc) {
//		NodeList nodeList = doc.getChildNodes();
//		if (nodeList == null) {
//			return;
//		}
//		for (int i = 0; i < nodeList.getLength(); i++) {
//			Node child = nodeList.item(i);
//
//		}
//	}
}
