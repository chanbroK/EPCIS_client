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
import org.epcis.model.TransactionEventType;
import org.epcis.model.exception.ValidationException;

/**
 * Copyright (C) 2020 Jaewook Byun
 *
 * This project is part of Oliot open source (http://oliot.org). Oliot EPCIS
 * v2.x is Java Web Service complying with Electronic Product Code Information
 * Service (EPCIS) v2.0.x
 *
 * @author Jaewook Byun, Ph.D., Assistant Professor, Sejong University,
 *         jwbyun@sejong.ac.kr
 * 
 *         Associate Director, Auto-ID Labs, KAIST, bjw0829@kaist.ac.kr
 */
public class TransactionEventWriter {

	public static Document write(TransactionEventType obj) throws ValidationException {

		Document dbo = EPCISEventWriter.write(obj);

		// Event Type
		dbo.put("isA", "TransactionEvent");
		// Parent ID
		putEPC(dbo, "parentID", obj.getParentID());
		// EPC List
		putEPCList(dbo, "epcList", obj.getEpcList());
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
		// SourceList
		putSourceList(dbo, obj.getSourceList());
		// DestinationList
		putDestinationList(dbo, obj.getDestinationList());
		// QuantityList
		putQuantityList(dbo, "quantityList", obj.getQuantityList());

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
}
