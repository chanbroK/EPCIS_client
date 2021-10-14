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
import org.epcis.model.AggregationEventType;
import org.epcis.model.exception.ValidationException;

public class AggregationEventWriter {
	public static Document write(AggregationEventType obj) throws ValidationException {

		Document dbo = EPCISEventWriter.write(obj);

		// Event Type
		dbo.append("isA", "AggregationEvent");
		// Parent ID
		putEPC(dbo, "parentID", obj.getParentID());
		// Child EPCs - using EPCList for query efficiency
		putEPCList(dbo, "epcList", obj.getChildEPCs());
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

		// ChildQuantityList - using QuantityList for query efficiency
		putQuantityList(dbo, "quantityList", obj.getChildQuantityList());

		// SourceList
		putSourceList(dbo, obj.getSourceList());

		// DestinationList
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
}
