package sample.query;

import org.epcis.client.EPCISQueryClient;
import org.epcis.client.MethodName;
import org.epcis.client.QueryName;

public class GetQueryNameAndStandardVersionAndVendorVersion {

	public static void main(String[] args) {
		String url = "http://dfpl.sejong.ac.kr:8081/epcis/query";
		EPCISQueryClient client = new EPCISQueryClient(url, MethodName.GetQueryNames);
		System.out.println(client.send());
		client.setQueryName(QueryName.SimpleEventQuery);
		client.send();
		client.setMethodName(MethodName.GetStandardVersion);
		client.send();
		client.setMethodName(MethodName.GetVendorVersion);
		client.send();

	}

}
