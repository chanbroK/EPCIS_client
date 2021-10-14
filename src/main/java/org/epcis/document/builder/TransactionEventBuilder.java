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
import org.epcis.model.BusinessLocationType;
import org.epcis.model.BusinessTransactionListType;
import org.epcis.model.BusinessTransactionType;
import org.epcis.model.DestinationListType;
import org.epcis.model.EPC;
import org.epcis.model.EPCListType;
import org.epcis.model.QuantityElementType;
import org.epcis.model.QuantityListType;
import org.epcis.model.ReadPointType;
import org.epcis.model.SourceDestType;
import org.epcis.model.SourceListType;
import org.epcis.model.TransactionEventType;
import org.w3c.dom.Element;

public class TransactionEventBuilder {
	private TransactionEventType transactionEvent;

	public TransactionEventBuilder(String eventTime, String eventTimeZoneOffset, List<String> epcs,
			ActionType action, String bizStep, String disposition, String readPoint,
			String bizLocation, Map<String, String> bizTransactionMap) {

		this.transactionEvent = new TransactionEventType();
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

	public TransactionEventType build() {
		return this.transactionEvent;
	}

	// 기본
	public TransactionEventBuilder setEventTime(String dateValue) {
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
		transactionEvent.setEventTime(xmlGreCal);
		return this;
	}

	public TransactionEventBuilder setEventTimeZoneOffset(String timeZone) {
		this.transactionEvent.setEventTimeZoneOffset(timeZone);
		return this;
	}

	public TransactionEventBuilder setEPCList(List<String> epcs) {
		EPCListType epcList = new EPCListType();
		epcs.forEach(epc -> {
			epcList.getEpc().add(new EPC(epc));
		});
		this.transactionEvent.setEpcList(epcList);
		return this;
	}

	public TransactionEventBuilder setAction(ActionType action) {
		this.transactionEvent.setAction(action);

		return this;
	}

	public TransactionEventBuilder setBizStep(String bizStep) {
		this.transactionEvent.setBizStep(bizStep);
		return this;
	}

	public TransactionEventBuilder setDisposition(String disposition) {
		this.transactionEvent.setDisposition(disposition);
		return this;
	}

	public TransactionEventBuilder setReadPoint(String readPoint) {
		this.transactionEvent.setReadPoint(new ReadPointType(readPoint));
		return this;
	}

	public TransactionEventBuilder setBizLocation(String bizLocation) {
		this.transactionEvent.setBizLocation(new BusinessLocationType(bizLocation));
		return this;
	}

	public TransactionEventBuilder setBizTransaction(Map<String, String> bizTransactionMap) {
		BusinessTransactionListType bizTransactionList = new BusinessTransactionListType();
		for (String key : bizTransactionMap.keySet()) {
			BusinessTransactionType bizTransaction = new BusinessTransactionType(key,
					bizTransactionMap.get(key));
			bizTransactionList.getBizTransaction().add(bizTransaction);
		}
		this.transactionEvent.setBizTransactionList(bizTransactionList);
		return this;
	}

	// 추가
	public TransactionEventBuilder setEventId(String EventId) {
		this.transactionEvent.setEventID(EventId);
		return this;
	}

	public TransactionEventBuilder addQuantityElement(String epcClass, Double quantity,
			String uom) {
		QuantityElementType qElement = new QuantityElementType(epcClass, new BigDecimal(quantity),
				uom);
		JAXBElement<BigDecimal> bd = new JAXBElement<BigDecimal>(new QName("quantity"),
				BigDecimal.class, new BigDecimal(quantity));
		qElement.setQuantity(bd);
		QuantityListType qList;
		if (this.transactionEvent.getQuantityList() == null) {
			qList = new QuantityListType();
			qList.getQuantityElement().add(qElement);
			this.transactionEvent.setQuantityList(qList);
		} else {
			this.transactionEvent.getQuantityList().getQuantityElement().add(qElement);
		}
		return this;
	}

	public TransactionEventBuilder setSourceList(Map<String, String> sourceMap) {
		SourceListType sourceList = new SourceListType();
		for (String key : sourceMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, sourceMap.get(key));
			sourceList.getSource().add(sourceDest);
		}
		this.transactionEvent.setSourceList(sourceList);
		return this;
	}

	public TransactionEventBuilder setDestinationList(Map<String, String> destinationeMap) {
		DestinationListType destinationList = new DestinationListType();
		for (String key : destinationeMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, destinationeMap.get(key));
			destinationList.getDestination().add(sourceDest);
		}
		this.transactionEvent.setDestinationList(destinationList);
		return this;
	}

	public TransactionEventBuilder addExtension(Element extension) {
		this.transactionEvent.getAny().add(extension);
		return this;
	}
}
