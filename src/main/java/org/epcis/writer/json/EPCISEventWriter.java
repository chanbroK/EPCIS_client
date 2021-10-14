package org.epcis.writer.json;

import static org.epcis.util.BSONWriteUtil.putErrorDeclaration;
import static org.epcis.util.BSONWriteUtil.putEventID;
import static org.epcis.util.BSONWriteUtil.putFlatten;
import static org.epcis.util.BSONWriteUtil.putOtherAttributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.bson.Document;
import org.epcis.model.EPCISEventType;
import org.epcis.model.exception.ValidationException;

public class EPCISEventWriter {
	public static Document write(EPCISEventType obj) throws ValidationException {
		Document dbo = new Document();

		// Event Time
		dbo.put("eventTime", obj.getEventTime().toString());
		// Event Time Zone
		dbo.put("eventTimeZoneOffset", obj.getEventTimeZoneOffset());
		// Record Time : according to M5
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
		dbo.put("recordTime", xmlGreCal.toString());

		// OtherAttributes
		putOtherAttributes(dbo, obj.getOtherAttributes());

		// Event ID
		putEventID(dbo, obj.getEventID());

		// Error Declaration
		Document errExtension = putErrorDeclaration(dbo, obj.getErrorDeclaration());
		if (errExtension != null)
			putFlatten(dbo, "errf", errExtension.get("extension", Document.class));

		return dbo;
	}
}
