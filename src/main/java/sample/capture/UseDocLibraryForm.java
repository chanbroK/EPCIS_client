package sample.capture;

import org.epcis.client.EPCISCaptureClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseDocLibraryForm {

    public static void main(String[] args) {
        String prefix = "epcis";
        String eventTime = "2020-10-15T20:33:31.116-06:00";
        String eventTimeZoneOffset = "-09:00";
        String eventId = "84b772d1-a687-4e8d-a6ba-0a3d58f6a18d";
        List<String> epcs = new ArrayList<>();
        epcs.add("urn:epc:id:giai:450868.2020.12345");
        epcs.add("urn:epc:id:giai:450868.2020.12346");
        String actionType = "OBSERVE";
        String bizStep = "urn:epcglobal:cbv:bizstep:departing";
        String disposition = "urn:epcglobal:cbv:disp:in_transit";
        String readPoint = "urn:epc:id:sgln:8808244.11111.400";
        String bizLocation = "urn:epc:id:sgln:8808244.11111.0";
        Map<String, String> bizTransactionMap = new HashMap<String, String>();
        List<String> bizTransactionKey = new ArrayList<>();
        bizTransactionKey.add("urn:epcglobal:cbv:btt:po");
        bizTransactionKey.add("urn:epcglobal:cbv:btt:pedigree");
        bizTransactionMap.put("urn:epcglobal:cbv:btt:po", "urn:epc:id:gdti:8808244.2020.2040");
        bizTransactionMap.put("urn:epcglobal:cbv:btt:pedigree",
                "urn:epc:id:gsrn:0614141.0000010253");
        Map<String, String> sourceMap = new HashMap<String, String>();
        List<String> sourceMapKey = new ArrayList<>();
        sourceMapKey.add("urn:epcglobal:cbv:sdt:location");
        sourceMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:450868.2020.12345");
        Map<String, String> destinationMap = new HashMap<String, String>();
        List<String> destinationMapKey = new ArrayList<>();
        destinationMapKey.add("urn:epcglobal:cbv:sdt:location");
        destinationMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:888244.2020.54321");
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            doc.setXmlStandalone(true);
            Element root = doc.createElementNS("urn:epcglobal:epcis:xsd:2", "EPCISDocument");
            root.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("creationDate", "2020-10-11T11:30:47.0Z");
            root.setAttribute("schemaVersion", "1.2");
            root.setPrefix(prefix);
            Element epcisBodyElement = doc.createElement("EPCISBody");
            root.appendChild(epcisBodyElement);
            Element eventListElement = doc.createElement("EventList");
            epcisBodyElement.appendChild(eventListElement);
            Element objectEventElement = doc.createElement("ObjectEvent");
            eventListElement.appendChild(objectEventElement);
            Element eventTimeElement = doc.createElement("eventTime");
            eventTimeElement.setTextContent(eventTime);
            objectEventElement.appendChild(eventTimeElement);
            Element eventTimeZoneOffsetElemenet = doc.createElement("eventTimeZoneOffset");
            eventTimeZoneOffsetElemenet.setTextContent(eventTimeZoneOffset);
            objectEventElement.appendChild(eventTimeZoneOffsetElemenet);
            Element eventIDElement = doc.createElement("eventID");
            eventIDElement.setTextContent(eventId);
            objectEventElement.appendChild(eventIDElement);
            Element epcListElement = doc.createElement("epcList");
            for (String value : epcs) {
                Element epcElement = doc.createElement("epc");
                epcElement.setTextContent(value);
                epcListElement.appendChild(epcElement);
            }
            objectEventElement.appendChild(epcListElement);
            Element actionElement = doc.createElement("action");
            actionElement.setTextContent(actionType);
            objectEventElement.appendChild(actionElement);
            Element bizStepElement = doc.createElement("bizStep");
            bizStepElement.setTextContent(bizStep);
            objectEventElement.appendChild(bizStepElement);
            Element dispositionElement = doc.createElement("disposition");
            dispositionElement.setTextContent(disposition);
            objectEventElement.appendChild(dispositionElement);
            Element readPointElement = doc.createElement("readPoint");
            Element readPointid = doc.createElement("id");
            readPointid.setTextContent(readPoint);
            readPointElement.appendChild(readPointid);
            objectEventElement.appendChild(readPointElement);
            Element bizLocationElement = doc.createElement("bizLocation");
            Element bizLocationid = doc.createElement("id");
            bizLocationid.setTextContent(bizLocation);
            bizLocationElement.appendChild(bizLocationid);
            objectEventElement.appendChild(bizLocationElement);
            Element bizTransactionListElement = doc.createElement("bizTransactionList");
            for (String key : bizTransactionMap.keySet()) {
                System.out.println("key = " + key);
                System.out.println("bizTransactionMap.get(key) = " + bizTransactionMap.get(key));
                Element bizTranscationElement = doc.createElement("bizTransaction");
                bizTranscationElement.setAttribute("type", bizTransactionMap.get(key));
                bizTransactionListElement.appendChild(bizTranscationElement);
            }
            objectEventElement.appendChild(bizTransactionListElement);
            Element sourceListElement = doc.createElement("sourceList");
            for (String key : sourceMap.keySet()) {
                Element sourceElement = doc.createElement("source");
                sourceElement.setAttribute("type", sourceMap.get(key));
                sourceListElement.appendChild(sourceElement);
            }
            objectEventElement.appendChild(sourceListElement);
            Element destinationListElement = doc.createElement("destinationList");
            for (String key : destinationMap.keySet()) {
                Element destinationElement = doc.createElement("destination");
                destinationElement.setAttribute("type", destinationMap.get(key));
                destinationListElement.appendChild(destinationElement);
            }
            objectEventElement.appendChild(destinationListElement);
            eventListElement.appendChild(objectEventElement);
            epcisBodyElement.appendChild(eventListElement);
            root.appendChild(epcisBodyElement);
            doc.appendChild(root);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(out);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            EPCISCaptureClient client = new EPCISCaptureClient(
                    "http://dfpl.sejong.ac.kr:8080/epcis/capture");
            client.sendByString(
                    out.toString(StandardCharsets.UTF_8), false); // HTTP Request Method
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
