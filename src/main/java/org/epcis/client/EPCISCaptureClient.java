package org.epcis.client;

import org.epcis.document.builder.Validator;
import org.epcis.model.*;
import org.epcis.util.UnitConverter;
import org.epcis.writer.json.JSONDocumentWriter;
import org.epcis.writer.xml.XMLDocumentWriter;
import org.w3c.dom.Document;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class EPCISCaptureClient {
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
            isValid = Validator.validateAggregationEvent((AggregationEventType) event);
        } else if (event instanceof ObjectEventType) {
            isValid = Validator.validateObjectEvent((ObjectEventType) event);
        } else if (event instanceof TransactionEventType) {
            isValid = Validator.validateTransactionEvent((TransactionEventType) event);
        } else if (event instanceof TransformationEventType) {
            isValid = Validator.validateTransformationEvent((TransformationEventType) event);
        } else if (event instanceof AssociationEventType) {
            isValid = Validator.validateAssociationEvent((AssociationEventType) event);
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

    public Document getEPCISDocument() {
        return this.toXML();
    }

    public Integer sendByDocument(Document document) {
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            StringWriter sw = new StringWriter();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(document), new StreamResult(sw));
            String body = sw.toString();
            return sendByString(body, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer sendByString(String body, boolean isJson) {
        try {
            HttpURLConnection connection = (HttpURLConnection) this.captureEndpoint.openConnection();
            connection.setRequestMethod("POST");
            if (isJson) {
                // using json
                connection.setRequestProperty("Content-Type", "application/json");
            } else {
                // using xml
                connection.setRequestProperty("Content-Type", "application/xml");
            }
            connection.setDoOutput(true);
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
            String returnMsg = in.readLine();
            System.out.println("RESPONSE" + returnMsg);
            return responseCode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer sendByEPCISDocument(boolean byJson) {
        try {
            HttpURLConnection connection = (HttpURLConnection) this.captureEndpoint.openConnection();
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
            String returnMsg = in.readLine();
            System.out.println("RESPONSE" + returnMsg);
            return responseCode;

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

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            XMLGregorianCalendar xmlGreCal = null;
            xmlGreCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            this.epcisDoc.setCreationDate(xmlGreCal);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
