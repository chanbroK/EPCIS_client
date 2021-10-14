package sample.capture;

import org.epcis.client.EPCISCaptureClient;
import org.epcis.document.builder.*;
import org.epcis.model.ActionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseClientForm {
    public static void main(String[] args) {

        EPCISCaptureClient client = new EPCISCaptureClient("https://webhook.site/601ba01d-9107-465c-92fb-616ce38ea05e");
//		EPCISCaptureClient client = new EPCISCaptureClient(
//				"http://dfpl.sejong.ac.kr:8080/epcis/capture");

        // 배송로봇 출발 이벤트
        String eventTime = "2020-10-15T20:33:31.116-06:00";
        String eventTimeZoneOffset = "-09:00";
        List<String> epcs = new ArrayList<String>();
        epcs.add("urn:epc:id:giai:450868.2020.12345");
        epcs.add("urn:epc:id:giai:450868.2020.12346");
        String bizStep = "urn:epcglobal:cbv:bizstep:departing";
        String disposition = "urn:epcglobal:cbv:disp:in_transit";
        String readPoint = "urn:epc:id:sgln:8808244.11111.400";
        String bizLocation = "urn:epc:id:sgln:8808244.11111.0";
        Map<String, String> bizTransactionMap = new HashMap<String, String>();
        bizTransactionMap.put("urn:epcglobal:cbv:btt:po", "urn:epc:id:gdti:8808244.2020.2040");
        bizTransactionMap.put("urn:epcglobal:cbv:btt:pedigree", "urn:epc:id:gsrn:0614141.0000010253");

        String eventId = "84b772d1-a687-4e8d-a6ba-0a3d58f6a18d";
        Map<String, String> sourceMap = new HashMap<String, String>();
        sourceMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:450868.2020.12345");
        Map<String, String> destinationMap = new HashMap<String, String>();
        destinationMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:888244.2020.54321");

        ObjectEventBuilder objectEvent = new ObjectEventBuilder(eventTime, eventTimeZoneOffset, epcs,
                ActionType.OBSERVE, bizStep, disposition, readPoint, bizLocation, bizTransactionMap);
        objectEvent.setEventId(eventId);
        objectEvent.setSourceList(sourceMap);
        objectEvent.setDestinationList(destinationMap);
        client.addEvent(objectEvent.build());
        client.sendByEPCISDocument(false);

        // add Extension
        ExtensionBuilder extensionBuilder = new ExtensionBuilder("volume", "http://droid.delivey.system.com/epcis",
                "dds");
        extensionBuilder.addChild("length", "http://droid.delivey.system.com/epcis", "dds", 12.5);
        extensionBuilder.addChild("width", "http://droid.delivey.system.com/epcis", "dds", 21);
        extensionBuilder.addChild("hight", "http://droid.delivey.system.com/epcis", "dds", 21.5);
        objectEvent.addExtension(extensionBuilder.build());
        client.addEvent(objectEvent.build());

        // 쇼핑이벤트
        eventTime = "2020-10-14T20:33:31.116-06:00";
        eventTimeZoneOffset = "-9:00";
        eventId = "84b772d1-a687-4e8d-a6ba-0a3d58f6a18d";
        bizTransactionMap = new HashMap<String, String>();
        bizTransactionMap.put("urn:epcglobal:cbv:btt:po", "urn:epc:id:gdti:8808244.2020.2040");
        bizTransactionMap.put("urn:epcglobal:cbv:btt:inv", "urn:epcglobal:cbv:bt:8808244073467:1152");
        epcs = new ArrayList<String>();
        epcs.add("urn:epc:id:sgtin:8808244.2020.2020");
        bizStep = "urn:epcglobal:cbv:bizstep:retail_selling";
        disposition = "urn:epcglobal:cbv:disp:retail_sold";
        readPoint = "urn:epc:id:sgln:888244.2020.54321";
        bizLocation = "urn:epc:id:sgln:888244.2020.54321";
        sourceMap = new HashMap<String, String>();
        sourceMap.put("urn:epcglobal:cbv:sdt:owning_party", "urn:epc:id:gln:8808244.2020");
        destinationMap = new HashMap<String, String>();
        destinationMap.put("urn:epcglobal:cbv:sdt:owning_party", "urn:epc:id:sgln:8808244.00001.0");

        TransactionEventBuilder transactionEvent = new TransactionEventBuilder(eventTime, eventTimeZoneOffset, epcs,
                ActionType.ADD, bizStep, disposition, readPoint, bizLocation, bizTransactionMap);
        transactionEvent.setEventId(eventId);
        transactionEvent.setSourceList(sourceMap);
        transactionEvent.setDestinationList(destinationMap);
        transactionEvent.addQuantityElement("urn:epc:class:lgtin:8808244.012345.998877", 200., "NA");

//		client.events.add(transactionEvent.build());

        // 배송로봇 이동 이벤트
        eventTime = "2020-10-16T20:33:31.116-06:00";
        eventTimeZoneOffset = "-09:00";
        eventId = "84b772d1-a687-4e8d-a6ba-0a3d58f6a18d";
        epcs = new ArrayList<String>();
        epcs.add("urn:epc:id:giai:450868.2020.12345");
        bizStep = "urn:epcglobal:cbv:bizstep:transporting";
        disposition = "urn:epcglobal:cbv:disp:in_transit";
        readPoint = "geo:36.357885, 127.381207";
        bizLocation = "geo:36.357885, 127.381207";
        bizTransactionMap = new HashMap<String, String>();
        bizTransactionMap.put("urn:epcglobal:cbv:btt:po", "urn:epc:id:gdti:8808244.2020.2040");
        sourceMap = new HashMap<String, String>();
        sourceMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:8808244.00225.0");
        destinationMap = new HashMap<String, String>();
        destinationMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:888244.2020.54321");
//		<dds:droidInformation>
//        <dds:speed xsi:type="xsd:double">12.5</dds:speed >
//        <dds:acceleration xsi:type="xsd:double">21</dds:acceleration>
//        <dds:battery_capacity xsi:type="xsd:double">4</dds:battery_capacity >
//    	</dds:droidInformation>	
        objectEvent = new ObjectEventBuilder(eventTime, eventTimeZoneOffset, epcs, ActionType.OBSERVE, bizStep,
                disposition, readPoint, bizLocation, bizTransactionMap);
        objectEvent.setEventId(eventId);
        objectEvent.setSourceList(sourceMap);
        objectEvent.setDestinationList(destinationMap);

//		client.addEvent(objectEvent.build());

        VocabularyElementBuilder vocaElemBuilder = new VocabularyElementBuilder(
                "urn:epc:id:giai:880968822.165.1152451");
        vocaElemBuilder.addAttribute("http://droid.delivey.system.com/epcis/Manufacturing_Date", "2020-10-14T12:00:00");
        vocaElemBuilder.addAttribute("http://droid.delivey.system.com/epcis/Owner", "2020-10-14T12:00:00");
        vocaElemBuilder.addAttribute("http://droid.delivey.system.com/epcis/Manufacturing_Date",
                "urn:epc:id:gln:880968822.165");
        vocaElemBuilder.addAttribute("http://droid.delivey.system.com/epcis/Serial_Number", "1152451");
        vocaElemBuilder.addAttribute("http://droid.delivey.system.com/epcis/Max_Speed", "40");
        vocaElemBuilder.addAttribute("http://droid.delivey.system.com/epcis/Min_Speed", "10");
        vocaElemBuilder.addAttribute("http://droid.delivey.system.com/epcis/dateModified", "2020-10-14T12:00:00");

        // List Attribute
        List<Object> attrList = new ArrayList<>();

        attrList.add(new ExtensionBuilder("aa", "http://ext.com/ext1", "ext1").setValue(10).build());
        attrList.add(new ExtensionBuilder("bb", "http://ext.com/ext1", "ext1").setValue(20).build());
        attrList.add(new ExtensionBuilder("cc", "http://ext.com/ext1", "ext1").setValue(30).build());
        attrList.add(new ExtensionBuilder("dd", "http://ext.com/ext1", "ext1").setValue(40).build());
        vocaElemBuilder.addAttributeChild("http://droid.delivey.system.com/epcis/object", attrList);

        List<String> childrenList = new ArrayList<>();
        childrenList.add("urn:epc:id:sgln:0037000.00729.8201");
        childrenList.add("urn:epc:id:sgln:0037000.00729.8202");
        childrenList.add("urn:epc:id:sgln:0037000.00729.8203");
        vocaElemBuilder.setChildren(childrenList);
        VocabularyBuilder vocaBuilder = new VocabularyBuilder("urn:gs1:epcisapp:droid:delivery:system:droid:info");
        vocaBuilder.addVocaElement(vocaElemBuilder.build());

//		client.addVoca(vocaBuilder.build());
        client.sendByEPCISDocument(true);

    }
}
