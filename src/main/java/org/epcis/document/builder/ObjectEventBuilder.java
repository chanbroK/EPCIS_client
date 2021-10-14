package org.epcis.document.builder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.epcis.model.ActionType;
import org.epcis.model.BusinessLocationType;
import org.epcis.model.BusinessTransactionListType;
import org.epcis.model.BusinessTransactionType;
import org.epcis.model.DestinationListType;
import org.epcis.model.EPC;
import org.epcis.model.EPCListType;
import org.epcis.model.ILMDType;
import org.epcis.model.ObjectEventType;
import org.epcis.model.ReadPointType;
import org.epcis.model.SourceDestType;
import org.epcis.model.SourceListType;
import org.w3c.dom.Element;

public class ObjectEventBuilder {

	private ObjectEventType objectEvent;

	public ObjectEventBuilder(String eventTime, String eventTimeZoneOffset, List<String> epcs,
			ActionType action, String bizStep, String disposition, String readPoint,
			String bizLocation, Map<String, String> bizTransactionMap) {

		this.objectEvent = new ObjectEventType();
		this.setEventTime(eventTime);
		this.setEventTimeZoneOffset(eventTimeZoneOffset);
		this.setEPCList(epcs);
		this.setAction(action);
		this.setBizStep(bizStep);
		this.setDisposition(disposition);
		this.setReadPoint(readPoint);
		this.setBizLocation(bizLocation);
		this.setBizTransaction(bizTransactionMap);
	}

	public ObjectEventType build() {
		return this.objectEvent;
	}

	// 기본
	public ObjectEventBuilder setEventTime(String dateValue) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		Date date = null;
		try {
			date = format.parse(dateValue);
		} catch (ParseException e) {
			System.out.println("dateValue is not matched in 'yyyy-MM-dd'T'HH:mm:ss.SSSXXX' format");
			e.printStackTrace();
		}
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar xmlGreCal = null;
		try {
			xmlGreCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		objectEvent.setEventTime(xmlGreCal);
		return this;
	}

	public ObjectEventBuilder setEventTimeZoneOffset(String timeZone) {
		this.objectEvent.setEventTimeZoneOffset(timeZone);
		return this;
	}

	public ObjectEventBuilder setEPCList(List<String> epcs) {
		EPCListType epcList = new EPCListType();
		epcs.forEach(epc -> {
			epcList.getEpc().add(new EPC(epc));
		});
		this.objectEvent.setEpcList(epcList);
		return this;
	}

	public ObjectEventBuilder setAction(ActionType action) {
		this.objectEvent.setAction(action);

		return this;
	}

	public ObjectEventBuilder setBizStep(String bizStep) {
		this.objectEvent.setBizStep(bizStep);
		return this;
	}

	public ObjectEventBuilder setDisposition(String disposition) {
		this.objectEvent.setDisposition(disposition);
		return this;
	}

	public ObjectEventBuilder setReadPoint(String readPoint) {
		this.objectEvent.setReadPoint(new ReadPointType(readPoint));
		return this;
	}

	public ObjectEventBuilder setBizLocation(String bizLocation) {
		this.objectEvent.setBizLocation(new BusinessLocationType(bizLocation));
		return this;
	}

	public ObjectEventBuilder setBizTransaction(Map<String, String> bizTransactionMap) {
		BusinessTransactionListType bizTransactionList = new BusinessTransactionListType();
		for (String key : bizTransactionMap.keySet()) {
			BusinessTransactionType bizTransaction = new BusinessTransactionType(key,
					bizTransactionMap.get(key));
			bizTransactionList.getBizTransaction().add(bizTransaction);
		}
		this.objectEvent.setBizTransactionList(bizTransactionList);
		return this;
	}

	// 추가
	public ObjectEventBuilder setEventId(String EventId) {
		this.objectEvent.setEventID(EventId);
		return this;
	}

	public ObjectEventBuilder setSourceList(Map<String, String> sourceMap) {
		SourceListType sourceList = new SourceListType();
		for (String key : sourceMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, sourceMap.get(key));
			sourceList.getSource().add(sourceDest);
		}
		this.objectEvent.setSourceList(sourceList);
		return this;
	}

	public ObjectEventBuilder setDestinationList(Map<String, String> destinationeMap) {
		DestinationListType destinationList = new DestinationListType();
		for (String key : destinationeMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, destinationeMap.get(key));
			destinationList.getDestination().add(sourceDest);
		}
		this.objectEvent.setDestinationList(destinationList);
		return this;
	}

	public ObjectEventBuilder addILMD(Element ilmdChild) {
		ILMDType ilmd = this.objectEvent.getIlmd();
		if (ilmd == null) {
			ilmd = new ILMDType();
		}
		ilmd.getAny().add(ilmdChild);
		this.objectEvent.setIlmd(ilmd);
		return this;
	}

	public ObjectEventBuilder addExtension(Element extension) {
		this.objectEvent.getAny().add(extension);
		return this;

	}

}
