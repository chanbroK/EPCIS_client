package org.epcis.document.builder;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.epcis.model.ActionType;
import org.epcis.model.AssociationEventType;
import org.epcis.model.BusinessLocationType;
import org.epcis.model.BusinessTransactionListType;
import org.epcis.model.BusinessTransactionType;
import org.epcis.model.DestinationListType;
import org.epcis.model.EPC;
import org.epcis.model.EPCListType;
import org.epcis.model.PersistentDispositionType;
import org.epcis.model.QuantityElementType;
import org.epcis.model.QuantityListType;
import org.epcis.model.ReadPointType;
import org.epcis.model.SourceDestType;
import org.epcis.model.SourceListType;
import org.w3c.dom.Element;

public class AssociationEventBuilder {
	private AssociationEventType associationEvent;

	public AssociationEventBuilder(String eventTime, String eventTimeZoneOffset, List<String> epcs,
			ActionType action, String bizStep, String disposition, String readPoint,
			String bizLocation, Map<String, String> bizTransactionMap) {

		this.associationEvent = new AssociationEventType();
		this.setEventTime(eventTime);
		this.setEventTimeZoneOffset(eventTimeZoneOffset);
		this.setChildEPCList(epcs);
		this.setAction(action);
		this.setBizStep(bizStep);
		this.setDisposition(disposition);
		this.setReadPoint(readPoint);
		this.setBizLocation(bizLocation);
		this.setBizTransaction(bizTransactionMap);
	}

	public AssociationEventType build() {
		return this.associationEvent;
	}

	// 기본
	public AssociationEventBuilder setEventTime(String dateValue) {
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
		associationEvent.setEventTime(xmlGreCal);
		return this;
	}

	public AssociationEventBuilder setEventTimeZoneOffset(String timeZone) {
		this.associationEvent.setEventTimeZoneOffset(timeZone);
		return this;
	}

	public AssociationEventBuilder setChildEPCList(List<String> epcs) {
		EPCListType epcList = new EPCListType();
		epcs.forEach(epc -> {
			epcList.getEpc().add(new EPC(epc));
		});
		this.associationEvent.setChildEPCs(epcList);
		return this;
	}

	public AssociationEventBuilder setAction(ActionType action) {
		this.associationEvent.setAction(action);

		return this;
	}

	public AssociationEventBuilder setBizStep(String bizStep) {
		this.associationEvent.setBizStep(bizStep);
		return this;
	}

	public AssociationEventBuilder setDisposition(String disposition) {
		this.associationEvent.setDisposition(disposition);
		return this;
	}

	public AssociationEventBuilder setReadPoint(String readPoint) {
		this.associationEvent.setReadPoint(new ReadPointType(readPoint));
		return this;
	}

	public AssociationEventBuilder setBizLocation(String bizLocation) {
		this.associationEvent.setBizLocation(new BusinessLocationType(bizLocation));
		return this;
	}

	public AssociationEventBuilder setBizTransaction(Map<String, String> bizTransactionMap) {
		BusinessTransactionListType bizTransactionList = new BusinessTransactionListType();
		for (String key : bizTransactionMap.keySet()) {
			BusinessTransactionType bizTransaction = new BusinessTransactionType(key,
					bizTransactionMap.get(key));
			bizTransactionList.getBizTransaction().add(bizTransaction);
		}
		this.associationEvent.setBizTransactionList(bizTransactionList);
		return this;
	}

	// 추가
	public AssociationEventBuilder setEventId(String EventId) {
		this.associationEvent.setEventID(EventId);
		return this;
	}

	public AssociationEventBuilder setSourceList(Map<String, String> sourceMap) {
		SourceListType sourceList = new SourceListType();
		for (String key : sourceMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, sourceMap.get(key));
			sourceList.getSource().add(sourceDest);
		}
		this.associationEvent.setSourceList(sourceList);
		return this;
	}

	public AssociationEventBuilder setDestinationList(Map<String, String> destinationeMap) {
		DestinationListType destinationList = new DestinationListType();
		for (String key : destinationeMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, destinationeMap.get(key));
			destinationList.getDestination().add(sourceDest);
		}
		this.associationEvent.setDestinationList(destinationList);
		return this;
	}

	public AssociationEventBuilder addChildQuantityElement(String epcClass, Double quantity,
			String uom) {
		QuantityElementType qElement = new QuantityElementType(epcClass, new BigDecimal(quantity),
				uom);
		JAXBElement<BigDecimal> bd = new JAXBElement<BigDecimal>(new QName("quantity"),
				BigDecimal.class, new BigDecimal(quantity));
		qElement.setQuantity(bd);
		QuantityListType qList;
		if (this.associationEvent.getChildQuantityList() == null) {
			qList = new QuantityListType();
			qList.getQuantityElement().add(qElement);
			this.associationEvent.setChildQuantityList(qList);
		} else {
			this.associationEvent.getChildQuantityList().getQuantityElement().add(qElement);
		}
		return this;
	}

	public AssociationEventBuilder setPersistentDisposition(List<String> unsets,
			List<String> sets) {
		PersistentDispositionType pDisposition = new PersistentDispositionType();
		pDisposition.setSet(unsets);
		pDisposition.setUnset(unsets);
		this.associationEvent.setPersistentDisposition(pDisposition);
		return this;
	}

	public AssociationEventBuilder setParentId(String id) {
		this.associationEvent.setParentID(id);
		return this;
	}

	public AssociationEventBuilder addExtension(Element extension) {
		this.associationEvent.getAny().add(extension);
		return this;
	}
}
