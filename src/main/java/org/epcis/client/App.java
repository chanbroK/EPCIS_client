package org.epcis.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App {
	public static void main(String[] args) {
		String url = "http://dfpl.sejong.ac.kr:8081/epcis/query";
		EPCISQueryClient client = new EPCISQueryClient(url, MethodName.GetQueryNames);
//		client.setQueryName(QueryName.SimpleEventQuery);
//		client.send();
//		client.setMethodName(MethodName.GetStandardVersion);
//		client.send();
//		client.setMethodName(MethodName.GetVendorVersion);
//		client.send();
		List<String> valueList = new ArrayList<>();
		valueList.add("OBSERVE");
		client.setMethodName(MethodName.Poll).setQueryName(QueryName.SimpleEventQuery).addParam("EQ_action", valueList)
				.addParam("EQ_ffff", new Date());

		System.out.println(client.toXMLString());
	}
}
