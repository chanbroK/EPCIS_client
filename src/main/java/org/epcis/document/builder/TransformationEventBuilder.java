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
import org.epcis.model.TransformationEventType;
import org.w3c.dom.Element;

public class TransformationEventBuilder {
	private TransformationEventType transformationEvent;

	public TransformationEventBuilder(String eventTime, String eventTimeZoneOffset,
			List<String> inputEpcs, List<String> outputEpcs, String bizStep, String disposition,
			String readPoint, String bizLocation, Map<String, String> bizTransactionMap) {

		this.transformationEvent = new TransformationEventType();
		this.setEventTime(eventTime);
		this.setEventTimeZoneOffset(eventTimeZoneOffset);
		this.setInputEPCList(inputEpcs);
		this.setOutputEPCList(outputEpcs);
		this.setBizStep(bizStep);
		this.setDisposition(disposition);
		this.setReadPoint(readPoint);
		this.setBizLocation(bizLocation);
		this.setBizTransaction(bizTransactionMap);
	}

	public TransformationEventType build() {
		return this.transformationEvent;
	}

	// 기본
	public TransformationEventBuilder setEventTime(String dateValue) {
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
		transformationEvent.setEventTime(xmlGreCal);
		return this;
	}

	public TransformationEventBuilder setEventTimeZoneOffset(String timeZone) {
		this.transformationEvent.setEventTimeZoneOffset(timeZone);
		return this;
	}

	public TransformationEventBuilder setInputEPCList(List<String> epcs) {
		EPCListType epcList = new EPCListType();
		epcs.forEach(epc -> {
			epcList.getEpc().add(new EPC(epc));
		});
		this.transformationEvent.setInputEPCList(epcList);
		return this;
	}

	public TransformationEventBuilder setOutputEPCList(List<String> epcs) {
		EPCListType epcList = new EPCListType();
		epcs.forEach(epc -> {
			epcList.getEpc().add(new EPC(epc));
		});
		this.transformationEvent.setOutputEPCList(epcList);
		return this;
	}

	public TransformationEventBuilder setBizStep(String bizStep) {
		this.transformationEvent.setBizStep(bizStep);
		return this;
	}

	public TransformationEventBuilder setDisposition(String disposition) {
		this.transformationEvent.setDisposition(disposition);
		return this;
	}

	public TransformationEventBuilder setReadPoint(String readPoint) {
		this.transformationEvent.setReadPoint(new ReadPointType(readPoint));
		return this;
	}

	public TransformationEventBuilder setBizLocation(String bizLocation) {
		this.transformationEvent.setBizLocation(new BusinessLocationType(bizLocation));
		return this;
	}

	public TransformationEventBuilder setBizTransaction(Map<String, String> bizTransactionMap) {
		BusinessTransactionListType bizTransactionList = new BusinessTransactionListType();
		for (String key : bizTransactionMap.keySet()) {
			BusinessTransactionType bizTransaction = new BusinessTransactionType(key,
					bizTransactionMap.get(key));
			bizTransactionList.getBizTransaction().add(bizTransaction);
		}
		this.transformationEvent.setBizTransactionList(bizTransactionList);
		return this;
	}

	// 추가
	public TransformationEventBuilder setEventId(String EventId) {
		this.transformationEvent.setEventID(EventId);
		return this;
	}

	public TransformationEventBuilder setSourceList(Map<String, String> sourceMap) {
		SourceListType sourceList = new SourceListType();
		for (String key : sourceMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, sourceMap.get(key));
			sourceList.getSource().add(sourceDest);
		}
		this.transformationEvent.setSourceList(sourceList);
		return this;
	}

	public TransformationEventBuilder setDestinationList(Map<String, String> destinationeMap) {
		DestinationListType destinationList = new DestinationListType();
		for (String key : destinationeMap.keySet()) {
			SourceDestType sourceDest = new SourceDestType(key, destinationeMap.get(key));
			destinationList.getDestination().add(sourceDest);
		}
		this.transformationEvent.setDestinationList(destinationList);
		return this;
	}

	public TransformationEventBuilder addInputQuantityElement(String epcClass, Double quantity,
			String uom) {
		QuantityElementType qElement = new QuantityElementType(epcClass, new BigDecimal(quantity),
				uom);
		JAXBElement<BigDecimal> bd = new JAXBElement<BigDecimal>(new QName("quantity"),
				BigDecimal.class, new BigDecimal(quantity));
		qElement.setQuantity(bd);
		QuantityListType qList;
		if (this.transformationEvent.getInputQuantityList() == null) {
			qList = new QuantityListType();
			qList.getQuantityElement().add(qElement);
			this.transformationEvent.setInputQuantityList(qList);
		} else {
			this.transformationEvent.getInputQuantityList().getQuantityElement().add(qElement);
		}
		return this;
	}

	public TransformationEventBuilder addOutputQuantityElement(String epcClass, Double quantity,
			String uom) {
		QuantityElementType qElement = new QuantityElementType(epcClass, new BigDecimal(quantity),
				uom);
		JAXBElement<BigDecimal> bd = new JAXBElement<BigDecimal>(new QName("quantity"),
				BigDecimal.class, new BigDecimal(quantity));
		qElement.setQuantity(bd);
		QuantityListType qList;
		if (this.transformationEvent.getOutputQuantityList() == null) {
			qList = new QuantityListType();
			qList.getQuantityElement().add(qElement);
			this.transformationEvent.setOutputQuantityList(qList);
		} else {
			this.transformationEvent.getOutputQuantityList().getQuantityElement().add(qElement);
		}
		return this;
	}

	public TransformationEventBuilder setPersistentDisposition(List<String> unsets,
			List<String> sets) {
		PersistentDispositionType pDisposition = new PersistentDispositionType();
		pDisposition.setSet(unsets);
		pDisposition.setUnset(unsets);
		this.transformationEvent.setPersistentDisposition(pDisposition);
		return this;
	}

	public TransformationEventBuilder setTransformationId(String id) {
		this.transformationEvent.setTransformationID(id);
		return this;
	}

	public TransformationEventBuilder addExtension(Element extension) {
		this.transformationEvent.getAny().add(extension);
		return this;
	}
}
