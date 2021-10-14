package sample.query;

public class UseStringForm {

	public static void main(String[] args) {
		String queryName = "SimpleEventQuery";
		String paramName = "EQ_bizLocation";
		String paramValueType = "query:ArrayOfString";
		String paramValue1 = "urn:epc:id:sgln:0614141.00888.1";
		String paramValue2 = "urn:epc:id:sgln:0614141.00888.2";

		String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\r\n"
				+ "                  xmlns:query=\"urn:epcglobal:epcis-query:xsd:1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
				+ "    <soapenv:Header/>\r\n" + "    <soapenv:Body>\r\n" + "        <query:Poll>\r\n"
				+ "            <queryName>" + queryName + "</queryName>\r\n" + "            <params>\r\n"
				+ "                <param>\r\n" + "                    <name>" + paramName + "</name>\r\n"
				+ "                    <value xsi:type=\"" + paramValueType + "\">\r\n"
				+ "                        <string>" + paramValue1 + "</string>\r\n"
				+ "                        <string>" + paramValue2 + "</string>\r\n"
				+ "                    </value>\r\n" + "                </param>\r\n" + "            </params>\r\n"
				+ "        </query:Poll>\r\n" + "    </soapenv:Body>\r\n" + "</soapenv:Envelope>";

		System.out.println(body);
	}
}
