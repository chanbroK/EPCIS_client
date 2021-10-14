package sample.capture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epcis.document.builder.AggregationEventBuilder;
import org.epcis.document.builder.ExtensionBuilder;
import org.epcis.document.builder.ObjectEventBuilder;
import org.epcis.document.builder.TransactionEventBuilder;
import org.epcis.document.builder.TransformationEventBuilder;
import org.epcis.document.builder.VocabularyBuilder;
import org.epcis.document.builder.VocabularyElementBuilder;
import org.epcis.client.EPCISCaptureClient;
import org.epcis.model.ActionType;

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
		client.sendEPCISDocument(false);

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
		// AggregationEvent(EPCIS X)
		eventTime = "2013-06-08T14:58:56.591Z";
		eventTimeZoneOffset = "+02:00";
		eventId = "84b772d1-a687-4e8d-a6ba-0a3d58f6a18d";
		String parentId = "urn:epc:id:sscc:0614141.1234567890";
		epcs = new ArrayList<String>();
		epcs.add("urn:epc:id:sgtin:0614141.107346.2017");
		epcs.add("urn:epc:id:sgtin:0614141.107346.2018");
		bizStep = "urn:epcglobal:cbv:bizstep:receiving";
		disposition = "urn:epcglobal:cbv:disp:in_progress";
		readPoint = "urn:epc:id:sgln:0037000.00729.8202";
		bizLocation = "urn:epc:id:sgln:0037000.00729.8202";
		bizTransactionMap = new HashMap<String, String>();
		bizTransactionMap.put("urn:epcglobal:cbv:btt:po", "urn:epc:id:gdti:0614141.00001.1618034");
		bizTransactionMap.put("urn:epcglobal:cbv:btt:pedigree", "urn:epc:id:gsrn:0614141.0000010253");
		sourceMap = new HashMap<String, String>();
		sourceMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:4012345.00225.0");
		sourceMap.put("urn:epcglobal:cbv:sdt:possessing_party", "urn:epc:id:pgln:4012345.00225");
		sourceMap.put("urn:epcglobal:cbv:sdt:owning_party", "urn:epc:id:pgln:4012345.00225");
		destinationMap = new HashMap<String, String>();
		destinationMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:0614141.00777.0");
		destinationMap.put("urn:epcglobal:cbv:sdt:possessing_party", "urn:epc:id:pgln:0614141.00777");
		destinationMap.put("urn:epcglobal:cbv:sdt:owning_party", "urn:epc:id:pgln:0614141.00777");
		List<String> unsets = new ArrayList<String>();
		unsets.add("urn:epcglobal:cbv:disp:completeness_inferred");
		unsets.add("urn:epcglobal:cbv:disp:completeness_inferred");
		List<String> sets = new ArrayList<String>();
		sets.add("urn:epcglobal:cbv:disp:completeness_verified");
		sets.add("urn:epcglobal:cbv:disp:completeness_verified");
		AggregationEventBuilder aggregationEvent = new AggregationEventBuilder(eventTime, eventTimeZoneOffset, epcs,
				ActionType.OBSERVE, bizStep, disposition, readPoint, bizLocation, bizTransactionMap);
		aggregationEvent.setParentId(parentId);
		aggregationEvent.setEventId(eventId);
		aggregationEvent.setSourceList(sourceMap);
		aggregationEvent.setDestinationList(destinationMap);
		aggregationEvent.setPersistentDisposition(unsets, sets);
		aggregationEvent.addChildQuantityElement("urn:epc:idpat:sgtin:4012345.098765.*", 10., null);
		aggregationEvent.addChildQuantityElement("urn:epc:class:lgtin:4012345.012345.998877", 200.5, "KGM");
//		client.addEvent(aggregationEvent.build());

		// AssociationEvent는 Aggregation과 동일

		// TransformationEvent
		eventTime = "2013-10-31T14:58:56.591Z";
		eventTimeZoneOffset = "+02:00";
		List<String> inputEpcs = new ArrayList<String>();
		inputEpcs.add("urn:epc:id:sgtin:4012345.011122.25");
		inputEpcs.add("urn:epc:id:sgtin:4000001.065432.99886655");
		List<String> outputEpcs = new ArrayList<String>();
		outputEpcs.add("urn:epc:id:sgtin:4012345.077889.25");
		outputEpcs.add("urn:epc:id:sgtin:4012345.077889.26");
		outputEpcs.add("urn:epc:id:sgtin:4012345.077889.27");
		outputEpcs.add("urn:epc:id:sgtin:4012345.077889.28");
		String transformationId = "urn:epc:id:gdti:0614141.12345.400";
		bizStep = "urn:epcglobal:cbv:bizstep:commissioning";
		disposition = "urn:epcglobal:cbv:disp:in_progress";
		readPoint = "urn:epc:id:sgln:4012345.00001.0";
		bizLocation = "urn:epc:id:sgln:0614141.00888.0";
		bizTransactionMap = new HashMap<String, String>();
		bizTransactionMap.put("urn:epcglobal:cbv:btt:po", "urn:epc:id:gdti:0614141.00001.1618034");
		bizTransactionMap.put("urn:epcglobal:cbv:btt:pedigree", "urn:epc:id:gsrn:0614141.0000010253");
		sourceMap = new HashMap<String, String>();
		sourceMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:4012345.00225.0");
		sourceMap.put("urn:epcglobal:cbv:sdt:possessing_party", "urn:epc:id:pgln:4012345.00225");
		sourceMap.put("urn:epcglobal:cbv:sdt:owning_party", "urn:epc:id:pgln:4012345.00225");
		destinationMap = new HashMap<String, String>();
		destinationMap.put("urn:epcglobal:cbv:sdt:location", "urn:epc:id:sgln:0614141.00777.0");
		destinationMap.put("urn:epcglobal:cbv:sdt:possessing_party", "urn:epc:id:pgln:0614141.00777");
		destinationMap.put("urn:epcglobal:cbv:sdt:owning_party", "urn:epc:id:pgln:0614141.00777");
		unsets = new ArrayList<String>();
		unsets.add("urn:epcglobal:cbv:disp:completeness_inferred");
		sets = new ArrayList<String>();
		sets.add("urn:epcglobal:cbv:disp:completeness_verified");

		TransformationEventBuilder transformationEvent = new TransformationEventBuilder(eventTime, eventTimeZoneOffset,
				inputEpcs, outputEpcs, bizStep, disposition, readPoint, bizLocation, bizTransactionMap);
		transformationEvent.setTransformationId(transformationId);
		transformationEvent.setSourceList(sourceMap);
		transformationEvent.setDestinationList(destinationMap);
		// inputQuantity
		transformationEvent.addInputQuantityElement("urn:epc:class:lgtin:4012345.011111.4444", 10., "KGM");
		transformationEvent.addInputQuantityElement("urn:epc:class:lgtin:0614141.077777.987", 30., null);
		// outputQuantity
		transformationEvent.addOutputQuantityElement("urn:epc:class:lgtin:4012345.011111.4444", 10., "KGM");
		transformationEvent.addOutputQuantityElement("urn:epc:class:lgtin:0614141.077777.987", 30., null);
		transformationEvent.addOutputQuantityElement("urn:epc:idpat:sgtin:4012345.066666.*", 220., "KGM");

//		client.addEvent(transformationEvent.build());

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
		client.sendEPCISDocument(true);

	}
}
