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
import org.epcis.model.AggregationEventType;
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

public class AggregationEventBuilder {
	private AggregationEventType aggregationEvent;

	public AggregationEventBuilder(String eventTime, String eventTimeZoneOffset, List<String> epcs,
			ActionType action, String bizStep, String disposition, String readPoint,
			String bizLocation, Map<String, String> bizTransactionMap) {

		this.aggregationEvent = new AggregationEventType();
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

	public AggregationEventType build() {
		return this.aggregationEvent;
	}

	// 기본
	public AggregationEventBuilder setEventTime(String dateValue) {
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
		aggregationEvent.setEventTime(xmlGreCal);
		return this;
	}

	public AggregationEventBuilder setEventTimeZoneOffset(String timeZone) {
		this.aggregationEvent.setEventTimeZoneOffset(timeZone);
		return this;
	}

	public AggregationEventBuilder setChildEPCList(List<String> epcs) {
		EPCListType epcList = new EPCListType();
		epcs.forEach(epc -> {
			epcList.getEpc().add(new EPC(epc));
		});
		this.aggregationEvent.setChildEPCs(epcList);
		return this;
	}

	public AggregationEventBuilder setAction(ActionType action) {
		this.aggregationEvent.setAction(action);

		return this;
	}

	public AggregationEventBuilder setBizStep(String bizStep) {
		this.aggregationEvent.setBizStep(bizStep);
		return this;
	}

	public AggregationEventBuilder setDisposition(String disposition) {
		this.aggregationEvent.setDisposition(disposition);
		return this;
	}

	public AggregationEventBuilder setReadPoint(String readPoint) {
		this.aggregationEvent.setReadPoint(new ReadPointType(readPoint));
		return this;
	}

	public AggregationEventBuilder setBizLocation(String bizLocation) {
		this.aggregationEvent.setBizLocation(new BusinessLocationType(bizLocation));
		return this;
	}

	public AggregationEventBuilder setBizTransaction(Map<String, String> bizTransactionMap) {
		BusinessTransactionListType bizTransactionList = new BusinessTransactionListType();
		for (String key : bizTransactionMap.keySet()) {
			BusinessTransactionType bizTransaction = new BusinessTransactionType(key,
					bizTransactionMap.get(key));
			bizTransactionList.getBizTransaction().add(bizTransaction);
		}
		this.aggregationEvent.setBizTransactionList(bizTransactionList);
		return this;
	}

	// 추가
	public AggregationEventBuilder setEventId(String EventId) {
		this.aggregationEvent.setEventID(EventId);
		return this;
	}

	public AggregationEventBuilder setSourceList(Map<String, String> sourceMap) {
		SourceListType sourceList = new SourceListType();
		for (String key : sourceMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, sourceMap.get(key));
			sourceList.getSource().add(sourceDest);
		}
		this.aggregationEvent.setSourceList(sourceList);
		return this;
	}

	public AggregationEventBuilder setDestinationList(Map<String, String> destinationeMap) {
		DestinationListType destinationList = new DestinationListType();
		for (String key : destinationeMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, destinationeMap.get(key));
			destinationList.getDestination().add(sourceDest);
		}
		this.aggregationEvent.setDestinationList(destinationList);
		return this;
	}

	public AggregationEventBuilder addChildQuantityElement(String epcClass, Double quantity,
			String uom) {
		QuantityElementType qElement = new QuantityElementType(epcClass, new BigDecimal(quantity),
				uom);
		JAXBElement<BigDecimal> bd = new JAXBElement<BigDecimal>(new QName("quantity"),
				BigDecimal.class, new BigDecimal(quantity));
		qElement.setQuantity(bd);
		QuantityListType qList;
		if (this.aggregationEvent.getChildQuantityList() == null) {
			qList = new QuantityListType();
			qList.getQuantityElement().add(qElement);
			this.aggregationEvent.setChildQuantityList(qList);
		} else {
			this.aggregationEvent.getChildQuantityList().getQuantityElement().add(qElement);
		}
		return this;
	}

	public AggregationEventBuilder setPersistentDisposition(List<String> unsets,
			List<String> sets) {
		PersistentDispositionType pDisposition = new PersistentDispositionType();
		pDisposition.setSet(unsets);
		pDisposition.setUnset(unsets);
		this.aggregationEvent.setPersistentDisposition(pDisposition);
		return this;
	}

	public AggregationEventBuilder setParentId(String id) {
		this.aggregationEvent.setParentID(id);
		return this;
	}

	public AggregationEventBuilder addExtension(Element extension) {
		this.aggregationEvent.getAny().add(extension);
		return this;
	}
}
