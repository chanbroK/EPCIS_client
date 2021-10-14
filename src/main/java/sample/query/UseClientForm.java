package sample.query;

import java.util.ArrayList;
import java.util.List;

import org.epcis.client.EPCISQueryClient;
import org.epcis.client.MethodName;
import org.epcis.client.QueryName;

public class UseClientForm {

	public static void main(String[] args) {
		String url = "http://dfpl.sejong.ac.kr:8081/epcis/query";
		String paramName = "EQ_bizLocation";
		EPCISQueryClient client = new EPCISQueryClient(url, MethodName.Poll);
		List<String> valueList = new ArrayList<>();
		valueList.add("urn:epc:id:sgln:0614141.00888.1");
		valueList.add("urn:epc:id:sgln:0614141.00888.2");
		client.setMethodName(MethodName.Poll).setQueryName(QueryName.SimpleEventQuery).addParam(paramName, valueList);
		System.out.println(client.toXMLString());

	}

}
