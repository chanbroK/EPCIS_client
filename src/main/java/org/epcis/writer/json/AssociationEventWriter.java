package org.epcis.writer.json;

import static org.epcis.util.BSONWriteUtil.putAction;
import static org.epcis.util.BSONWriteUtil.putAny;
import static org.epcis.util.BSONWriteUtil.putBizLocation;
import static org.epcis.util.BSONWriteUtil.putBizStep;
import static org.epcis.util.BSONWriteUtil.putBizTransactionList;
import static org.epcis.util.BSONWriteUtil.putDestinationList;
import static org.epcis.util.BSONWriteUtil.putDisposition;
import static org.epcis.util.BSONWriteUtil.putEPC;
import static org.epcis.util.BSONWriteUtil.putEPCList;
import static org.epcis.util.BSONWriteUtil.putEventHashID;
import static org.epcis.util.BSONWriteUtil.putFlatten;
import static org.epcis.util.BSONWriteUtil.putPersistentDisposition;
import static org.epcis.util.BSONWriteUtil.putQuantityList;
import static org.epcis.util.BSONWriteUtil.putReadPoint;
import static org.epcis.util.BSONWriteUtil.putSensorElementList;
import static org.epcis.util.BSONWriteUtil.putSourceList;

import org.bson.Document;
import org.epcis.client.EPCISCaptureClient;
import org.epcis.model.AssociationEventType;
import org.epcis.model.exception.ValidationException;

/**
 * Copyright (C) 2020 Jaewook Byun
 * <p>
 * This project is part of Oliot open source (http://oliot.org). Oliot EPCIS
 * v2.x is Java Web Service complying with Electronic Product Code Information
 * Service (EPCIS) v2.0.x
 *
 * @author Jaewook Byun, Ph.D., Assistant Professor, Sejong University,
 *         jwbyun@sejong.ac.kr
 *         <p>
 *         Associate Director, Auto-ID Labs, KAIST, bjw0829@kaist.ac.kr
 */
public class AssociationEventWriter {

	public static Document write(AssociationEventType obj) throws ValidationException {

		// validate(obj);

		Document dbo = EPCISEventWriter.write(obj);

		// Event Type
		dbo.put("isA", "AssociationEvent");
		// parentID
		putEPC(dbo, "parentID", obj.getParentID());
		// Child EPCs - using EPCList for query efficiency
		putEPCList(dbo, "epcList", obj.getChildEPCs());
		// Child QuantityList = using QuantityList for query efficiency
		putQuantityList(dbo, "quantityList", obj.getChildQuantityList());
		// Action
		putAction(dbo, obj.getAction());
		// Biz Step
		putBizStep(dbo, obj.getBizStep());
		// Disposition
		putDisposition(dbo, obj.getDisposition());
		// ReadPoint
		putReadPoint(dbo, obj.getReadPoint());
		// BizLocation
		putBizLocation(dbo, obj.getBizLocation());
		// BizTransactionList
		putBizTransactionList(dbo, obj.getBizTransactionList());
		// Source List
		putSourceList(dbo, obj.getSourceList());
		// Dest List
		putDestinationList(dbo, obj.getDestinationList());

		// SensorElementList
		putSensorElementList(dbo, obj.getSensorElementList(), EPCISCaptureClient.unitConverter);
		// PersistentDisposition
		putPersistentDisposition(dbo, obj.getPersistentDisposition());

		// Vendor Extension
		Document extension = putAny(dbo, "extension", obj.getAny(), false);
		if (extension != null)
			putFlatten(dbo, "extf", extension);

		// put event id
		if (!dbo.containsKey("eventID")) {
			putEventHashID(dbo);
		}

		return dbo;
	}

	public void validate(AssociationEventType obj) throws ValidationException {
		if (obj.getAction().value().equals("ADD") && obj.getParentID() == null
				|| (obj.getChildEPCs() == null && obj.getChildQuantityList() == null)) {
			ValidationException e = new ValidationException();
			e.setReason(
					"ADD AssociationEvent requires parentID and instance or class-level child EPCs");
			e.setStackTrace(new StackTraceElement[0]);
			throw e;
		} else if (obj.getAction().value().equals("DELETE") && obj.getParentID() == null) {
			ValidationException e = new ValidationException();
			e.setReason("DELETE AssociationEvent requires parentID");
			e.setStackTrace(new StackTraceElement[0]);
			throw e;
		} else if (obj.getAction().value().equals("OBSERVE")
				&& (obj.getChildEPCs() == null && obj.getChildQuantityList() == null)) {
			ValidationException e = new ValidationException();
			e.setReason("OBSERVE AssociationEvent requires instance or class-level child EPCs");
			e.setStackTrace(new StackTraceElement[0]);
			throw e;
		}
	}
}
