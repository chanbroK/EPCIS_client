package sample.capture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epcis.client.EPCISCaptureClient;

public class UseStringForm {

	public static void main(String[] args) {
		//배송로봇 출발 이벤트
		String eventTime = "2020-10-15T20:33:31.116-06:00";
		String eventTimeZoneOffset = "-09:00";
		String eventId = "84b772d1-a687-4e8d-a6ba-0a3d58f6a18d";
		List<String> epcs = new ArrayList<String>();
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

		String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<!DOCTYPE project>\r\n"
				+ "<epcis:EPCISDocument xmlns:epcis=\"urn:epcglobal:epcis:xsd:2\"\r\n"
				+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" creationDate=\"2020-10-11T11:30:47.0Z\" schemaVersion=\"2.0\">\r\n"
				+ "    <EPCISBody>\r\n" 
				+ "        <EventList>\r\n"
				+ "            <ObjectEvent>\r\n" 
				+ "                <eventTime>" + eventTime+ "</eventTime>\r\n" 
				+ "                <eventTimeZoneOffset>" + eventTimeZoneOffset+ "</eventTimeZoneOffset>\r\n" 
				+ "                <eventID>" + eventId+ "</eventID>\r\n" 
				+ "                <epcList>\r\n" 
				+ "                    <epc>"+ epcs.get(0) + "</epc>\r\n" 
				+ "                    <epc>" + epcs.get(1)+ "</epc>\r\n" 
				+ "                </epcList>\r\n" + 
				"                <action>"+ actionType + "</action>\r\n" 
				+ "                <bizStep>" + bizStep+ "</bizStep>\r\n" 
				+ "                <disposition>" + disposition+ "</disposition>\r\n" 
				+ "                <readPoint>\r\n"
				+ "                    <id>" + readPoint + "</id>\r\n"
				+ "                </readPoint>\r\n" 
				+ "                <bizLocation>\r\n"
				+ "                    <id>" + bizLocation + "</id>\r\n"
				+ "                </bizLocation>\r\n" 
				+ "                <bizTransactionList>\r\n"
				+ "						<bizTransaction type=\"" + bizTransactionKey.get(0) + "\">"+bizTransactionMap.get(bizTransactionKey.get(0)) + "</bizTransaction>\r\n"
				+ "						<bizTransaction type=\"" + bizTransactionKey.get(1) + "\">"+ bizTransactionMap.get(bizTransactionKey.get(1)) + "</bizTransaction>\r\n"
				+ "                </bizTransactionList>\r\n" 
				+ "                <sourceList>\r\n"
				+ "                    <source type=\"" + sourceMapKey.get(0) + "\">"+ sourceMap.get(sourceMapKey.get(0)) + "</source>\r\n"
				+ "                </sourceList>\r\n" 
				+ "                <destinationList>\r\n"
				+ "                    <destination type=\"" + destinationMapKey.get(0) + "\">"+ destinationMap.get(destinationMapKey.get(0)) + "</destination>\r\n"
				+ "                </destinationList>\r\n" 
				+ "            </ObjectEvent>\r\n"
				+ "        </EventList>\r\n" 
				+ "    </EPCISBody>\r\n" 
				+ "</epcis:EPCISDocument>";

		Boolean byJson = false; // send xml string
		EPCISCaptureClient client = new EPCISCaptureClient(
				"http://dfpl.sejong.ac.kr:8080/epcis/capture");
		client.sendEPCISDocumentByString(byJson, body); // HTTP Request Method 
	}

}
