package org.epcis.util;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;

import org.bson.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.*;

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
public class CBVAttributeWriteUtil {

	public static HashSet<String> fao3 = new HashSet<>();
	public static HashSet<String> growingMethodCode = new HashSet<>();
	public static HashSet<String> iso3166 = new HashSet<>();
	public static HashSet<String> preservationTechniqueCode = new HashSet<>();
	public static HashSet<String> productionMethodForFishAndSeafoodCode = new HashSet<>();
	public static HashSet<String> ssa = new HashSet<>();
	public static HashSet<String> sst = new HashSet<>();
	public static HashSet<String> storageStateCode = new HashSet<>();
	public static HashSet<String> tradeItemConditionCode = new HashSet<>();
	public static HashSet<String> unlocode = new HashSet<>();
	public static HashSet<String> bizstep = new HashSet<>();
	public static HashSet<String> btt = new HashSet<>();
	public static HashSet<String> disp = new HashSet<>();
	public static HashSet<String> measurement = new HashSet<>();
	public static HashSet<String> sdt = new HashSet<>();
	public static HashSet<String> er = new HashSet<>();

	/**
	 * put means reverse processing required
	 *
	 * @return true if no more processing necessary
	 */
	public static boolean checkOrPutCBVCompliantAttribute(String attrKey, Map<QName, String> otherAttributes,
			List<Object> attrValue, Document attrObj) {
		switch (attrKey) {
		case "urn:epcglobal:cbv:mda#countryOfOrigin":
			// Country from which the goods are supplied.
			// The code list for this attribute is the ISO 3166-1 Alpha-2 list of 2-letter
			// country codes; see http://www.iso.org/iso/country_codes
			// Example: UK
			// Note: When multiple countries of origin are included, the dominant country of
			// origin SHALL be included as the first element.

			// Code
			checkStringOfSet(attrKey, attrValue, attrObj, iso3166);
			return true;
		case "urn:epcglobal:cbv:mda#countryOfExport":
			// Country from which the batch/lot was exported.
			// Note: This is not the same as the country of origin. In the EU this attribute
			// indicates from which third country (outside of European Union) fishery and
			// aquaculture products were exported.
			//
			// The code list for this attribute is the ISO 3166-1 Alpha-2 list of 2-letter
			// country codes; see http://www.iso.org/iso/country_codes
			// Example: UK
			//
			// Note: When multiple countries of export are included, the dominant country of
			// export SHALL be included as the first element.

			// Code
			checkStringOfSet(attrKey, attrValue, attrObj, iso3166);
			return true;
		case "urn:epcglobal:cbv:mda#preservationTechniqueCode":
			// Code value indicating the preservation technique used to preserve the product
			// from deterioration.
			// The code list for this attribute is defined in GDSN; see
			// http://apps.gs1.org/GDD/Pages/clDetails.aspx?semanticURN=urn:gs1:gdd:cl:PreservationTechniqueTypeCode
			// Example: COLD_SMOKE_CURING

			// Code
			checkStringOfSet(attrKey, attrValue, attrObj, preservationTechniqueCode);
			return true;
		case "urn:epcglobal:cbv:mda#tradeItemConditionCode":
			// A code depicting the type of preparation that a trade item will have before
			// being sold to the end consumer (e.g. cut for sale, portioned/pieced). This
			// preparation can be done either by the supplier or the retailer or other
			// parties involved. The style of preparation may be determined by either
			// industry standards, the supplier or the retailer.
			// The code list for this attribute is defined in GDSN; see
			// http://apps.gs1.org/GDD/Pages/clDetails.aspx?semanticURN=urn:gs1:gdd:cl:TradeItemConditionCode&release=2
			// example: GUS

			// Code
			checkStringOfSet(attrKey, attrValue, attrObj, tradeItemConditionCode);
			return true;
		case "urn:epcglobal:cbv:mda#speciesForFisheryStatisticsPurposesCode":
			// The FAO 3 Alpha code of the species of fish for fish and seafood.
			// Example: COD

			// Code
			checkStringOfSet(attrKey, attrValue, attrObj, fao3);
			return true;
		case "urn:epcglobal:cbv:mda#growingMethodCode":
			// The process through which fresh produce is grown and cultivated.
			// The code list for this attribute is defined in GDSN; see
			// http://apps.gs1.org/GDD/Pages/clDetails.aspx?semanticURN=urn:gs1:gdd:cl:GrowingMethodCode&release=1
			// Example: HYDROPONIC

			// Code
			checkStringOfSet(attrKey, attrValue, attrObj, growingMethodCode);
			return true;
		case "urn:epcglobal:cbv:mda#productionMethodForFishAndSeafoodCode":
			// A code specifying how the fish had been grown / cultivated.
			// The code list for this attribute is defined in GS1 EDI; see
			// http://apps.gs1.org/GDD/Pages/clDetails.aspx?semanticURN=urn:gs1:gdd:cl:ProductionMethodForFishAndSeafoodCode&release=1
			// Example: AQUACULTURE

			// Code
			checkStringOfSet(attrKey, attrValue, attrObj, productionMethodForFishAndSeafoodCode);
			return true;
		case "urn:epcglobal:cbv:mda#storageStateCode":
			// A code depicting that the referred product was previously frozen or not.
			// The code list for this attribute is defined in GS1 EDI; see
			// http://apps.gs1.org/GDD/Pages/clDetails.aspx?semanticURN=urn:gs1:gdd:cl:StorageStateCode&release=1
			// Example: Previously Frozen

			// Code
			checkStringOfSet(attrKey, attrValue, attrObj, storageStateCode);
			return true;
		case "urn:epcglobal:cbv:mda#unloadingPort":
			// Port where the goods were unloaded from a seagoing vessel after having been
			// transported by it.
			// The value of this attribute is a user vocabulary maintained by UN/ECE; see
			// http://www.unece.org/cefact/locode/welcome.html
			// Example: DE BRV

			// UN LOCODE
			checkStringOfSet(attrKey, attrValue, attrObj, unlocode);
			return true;
		case "urn:epcglobal:cbv:mda:sst":
			// Sub-Site Type: describes the primary business function of the sub-site
			// location. This master data attribute is only applicable to a sub-site
			// location.
			// This value is expressed as a single numerical code (see code list below); for
			// example, code 201 indicates that the sub-site type is a “back room” as
			// defined below

			// Code List
			checkStringOfSet(attrKey, attrValue, attrObj, sst);
			return true;
		case "urn:epcglobal:cbv:mda:ssa":
			// Sub-Site Attribute: further qualifies the business function of the sub-site
			// location. This master data attribute is only applicable to a sub-site
			// location.
			// Sub-site attributes are expressed as a comma-separated list of zero or more
			// numerical codes (see code list below). For example, if the sub-site type is
			// 203 (sales area), then sub-site attributes of “404,412” further specifies
			// that this location identifier is a sales area for groceries (attribute 412)
			// that are frozen (attribute 404).

			// Code List
			checkStringOfSet(attrKey, attrValue, attrObj, ssa);
			return true;
		case "urn:epcglobal:cbv:mda#countryCode":
			// The ISO 3166-1 alpha-2 code specifying the country for the address.

			// String
			checkStringOfSet(attrKey, attrValue, attrObj, iso3166);
			return true;
		case "urn:epcglobal:cbv:mda#descriptionShort":
			// A free form short length description of the trade item that can be used to
			// identify the trade item at point of sale.
			// Example: Acme Red Widgets

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 35);
			return true;
		case "urn:epcglobal:cbv:mda#dosageFormType":
			// A dosage form is the physical form of a medication that identifies the form
			// of the pharmaceutical item.
			// Example: PILL

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 35);
			return true;
		case "urn:epcglobal:cbv:mda#functionalName":
			// Describes use of the product or service by the consumer. Should help clarify
			// the product classification associated with the GTIN.
			// Example: Widget

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 35);
			return true;
		case "urn:epcglobal:cbv:mda#manufacturerOfTradeItemPartyName":
			// Party name information for the manufacturer of the trade item.
			// Example: Acme Corporation

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 200);
			return true;
		case "urn:epcglobal:cbv:mda#netContentDescription":
			// Free text describing the amount of the trade item contained by a package,
			// usually as claimed on the label.
			// Example: 253 grams

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 500);
			return true;
		case "urn:epcglobal:cbv:mda#labelDescription":
			// A literal reproduction of the text featured on a product's label in the same
			// word-by-word order in which it appears on the front of the product's
			// packaging. This may not necessarily match the GTIN description as loaded by
			// the supplier into the GTIN description field in GDSN.
			// Example: Acme Corporation Tiny Red Widgets

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 500);
			return true;
		case "urn:epcglobal:cbv:mda#regulatedProductName":
			// The prescribed, regulated or generic product name or denomination that
			// describes the true nature of the product and is sufficiently precise to
			// distinguish it from other products according to country specific regulation.
			// Example: Epcistra

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 500);
			return true;
		case "urn:epcglobal:cbv:mda#speciesForFisheryStatisticsPurposesName":
			// The scientific name associated with the
			// speciesforFisheryStatisticsPurposesCode.
			// Example: Gadus morhua

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 500);
			return true;
		case "urn:epcglobal:cbv:mda#strengthDescription":
			// Free text describing the strength of the active ingredient(s) of the product
			// Example: 200mg/100mg

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 500);
			return true;
		case "urn:epcglobal:cbv:mda#tradeItemDescription":
			// An understandable and useable description of a trade item using brand and
			// other descriptors. This attribute is filled with as little abbreviation as
			// possible while keeping to a reasonable length. Free form text field, this
			// data element is repeatable for each language used and must be associated with
			// a valid ISO language code. Field length is 178 characters. This should be a
			// meaningful description of the trade item with full spelling to facilitate
			// message processing. Retailers can use this description as the base to fully
			// understand the brand, flavour, scent etc. of the specific GTIN in order to
			// accurately create a product description as needed for their internal systems.
			// Example: GS1 Brand Base Invisible Solid Deodorant AP Stick Spring Breeze

			// String (1-35)
			checkString(attrKey, attrValue, attrObj, 200);
			return true;
		case "urn:epcglobal:cbv:mda:site":
			// Identifies the site in which this location is contained. For a Sub-site
			// location, this is the identifier of the parent location. For a Site location,
			// this is the identifier of the location itself.
			//
			// When the identifier for the location to which this master data attribute
			// applies is an SGLN EPC, the Site Location master data attribute is always the
			// 13-digit GLN implied by the company prefix and location reference components
			// of that SGLN

			// String (1-128)
			checkString(attrKey, attrValue, attrObj, 128);
			return true;
		case "urn:epcglobal:cbv:mda:ssd":
			// Sub-Site Detail: provides additional proprietary information. This master
			// data attribute is only applicable to a sub-site location.
			// For example, instead of sharing that a product is on some shelf in the back
			// room of store 123, a party may wish to communicate the exact shelf in the
			// backroom of store 123, e.g. shelf #4567. The Sub-Site Detail master data
			// attribute provides the identity of the specific shelf; e.g., 4567

			// String (1-128)
			checkString(attrKey, attrValue, attrObj, 128);
			return true;
		case "urn:epcglobal:cbv:mda#name":
			// Sub-Site Detail: provides additional proprietary information. This master
			// data attribute is only applicable to a sub-site location.
			// For example, instead of sharing that a product is on some shelf in the back
			// room of store 123, a party may wish to communicate the exact shelf in the
			// backroom of store 123, e.g. shelf #4567. The Sub-Site Detail master data
			// attribute provides the identity of the specific shelf; e.g., 4567

			// String
			checkString(attrKey, attrValue, attrObj, Integer.MAX_VALUE);
			return true;
		case "urn:epcglobal:cbv:mda#streetAddressOne":
			// The first free form line of an address. This first part is printed on paper
			// as the first line below the name. For example, the name of the street and the
			// number in the street or the name of a building.

			// String
			checkString(attrKey, attrValue, attrObj, Integer.MAX_VALUE);
			return true;
		case "urn:epcglobal:cbv:mda#streetAddressTwo":
			// The second free form line of an address. This second part is printed on paper
			// as the second line below the name. The second free form line complements the
			// first free form line to locate the party or location.

			// String
			checkString(attrKey, attrValue, attrObj, Integer.MAX_VALUE);
			return true;
		case "urn:epcglobal:cbv:mda#streetAddressThree":
			// The third free form line of an address. This third part is printed on paper
			// as the third line below the name. The third free form line complements the
			// first and second free form lines where necessary.

			// String
			checkString(attrKey, attrValue, attrObj, Integer.MAX_VALUE);
			return true;
		case "urn:epcglobal:cbv:mda#city":
			// Text specifying the name of the city.

			// String
			checkString(attrKey, attrValue, attrObj, Integer.MAX_VALUE);
			return true;
		case "urn:epcglobal:cbv:mda#state":
			// One of the constituent units of a nation having a federal government.

			// String
			checkString(attrKey, attrValue, attrObj, Integer.MAX_VALUE);
			return true;
		case "urn:epcglobal:cbv:mda#postalCode":
			// Text specifying the postal code for an address.

			// String
			checkString(attrKey, attrValue, attrObj, Integer.MAX_VALUE);
			return true;
		case "urn:epcglobal:cbv:mda#drainedWeight":
			// The weight of the trade item when drained of its liquid. For example 225
			// "grm", Jar of pickles in vinegar. Applies to defined bricks of GCI Global
			// trade item Classification - Mainly food trade item. Has to be associated with
			// a valid UoM.
			// Example: [see Section ‎9.2.4

			// Measurement
			putMeasurement(attrKey, otherAttributes, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#grossWeight":
			// Used to identify the gross weight of the trade item. The gross weight
			// includes all packaging materials of the trade item. At pallet level the trade
			// item-GrossWeight includes the weight of the pallet itself. For example, "200
			// grm", value - total pounds, total grams, etc. Has to be associated with a
			// valid UOM.
			// Example: [see Section ‎9.2.4]

			// Measurement
			putMeasurement(attrKey, otherAttributes, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#netWeight":
			// Used to identify the net weight of the trade item. Net weight excludes any
			// packaging materials and applies to all levels but consumer unit level. For
			// consumer unit, Net Content replaces Net Weight (can then be weight, size,
			// volume). Has to be associated with a valid UoM.
			// Example: [see Section ‎9.2.4]

			// Measurement
			putMeasurement(attrKey, otherAttributes, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#bestBeforeDate":
			// The date before which the product is best used or consumed. It is a statement
			// about quality.
			// Example: 2017-03-15

			// Date
			putDate(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#firstFreezeDate":
			// The date of initial freezing, if different from the date of production.
			// Example: 2016-03-15

			// Date
			putDate(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#harvestEndDate":
			// The date when harvesting ended.
			// Example: 2016-03-15

			// Date
			putDate(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#harvestStartDate":
			// The date when harvesting started.
			// Example: 2016-03-15

			// Date
			putDate(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#itemExpirationDate":
			// The date after which the product should not be used or consumed. Its meaning
			// is determined based on the trade item context (e.g., for food, the date will
			// indicate the possibility of a direct health risk resulting from use of the
			// product after the date, for pharmaceutical products, it will indicate the
			// possibility of an indirect health risk resulting from the ineffectiveness of
			// the product after the date). It is often referred to as "use by date" or
			// "maximum durability date.”
			// Example: 2016-03-15

			// Date
			putDate(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#sellByDate":
			// The date before or on which, the product should be sold.
			// Example: 2017-03-15

			// Date
			putDate(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#geoLocation":
			// Geo URI as specified in [RFC5870], consisting of the latitude and longitude
			// of a location, in degrees. Optionally, a Geo URI may also include a
			// location’s altitude.
			// For example, geo:50.942239,6.898350 indicates the geographic position of GS1
			// Germany’s offices.

			checkGeoLocation(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#geoFence":
			// Area polygon (geo-fence) consisting of an array of Geo URIs as specified in
			// [RFC5870], defined according to the following rules:
			// The array SHALL consist of at least 4 individual Geo URIs.
			// The first Geo URI of a given array SHALL be identical to the last one.
			// Each individual Geo URI and the area polygon itself SHALL be embedded in
			// square brackets.
			// The array of Geo URIs SHALL be indicated and processed in sequential order,
			// separated by commas, while following the right-hand rule, i.e.
			// counter-clockwise.
			// If there is the need to define a multi-polygon (e.g. a warehouse location
			// that is split in two parts as it is separated by a street), each partial area
			// polygon SHALL be embedded in separate square brackets.
			// If there is a need to define a hole within an area polygon (e.g. if an area
			// within a property pertains to another organisation), the area polygon and the
			// contained hole SHALL be embedded in separate square brackets and the Geo URIs
			// of the hole SHALL be indicated and processed in sequential order while
			// following the left-hand rule, i.e. clockwise.
			// For example, the geo-fence of GS1 Germany’s offices (which has a rectangular
			// floor plan with an adjacent rectangular side wing) would look like this:
			// [[geo:50.942499,6.898247], [geo:50.942275,6.898292],
			// [geo:50.942263,6.898094], [geo:50.942106,6.898126], [geo:50.942130,6.898526],
			// [geo:50.942512,6.898451], [geo:50.942499,6.898247]]

			checkGeoFence(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#additionalTradeItemID":
			putAdditionalTradeItemID(attrKey, attrValue, attrObj);
			return true;
		case "urn:epcglobal:cbv:mda#certificationList":
			// Information on certification standards to which the trade item, or the
			// process by which it is manufactured, sourced or supplied complies.

			// List of Certification
			return !checkListOfCertification(attrValue);
		case "urn:epcglobal:cbv:mda#farmList":
			// List of structures describing farm information; see below

			// List of Farm
			return !checkListOfFarm(attrValue);
		case "urn:epcglobal:cbv:mda#vesselCatchInformationList":
			// List of structures describing farm information; see below

			// List of vesselCatchInformationList
			return !checkListOfVesselCatchInformation(attrValue);
		case "urn:epcglobal:cbv:mda#additionalPartyIDList":
			// List of structures describing farm information; see below

			// List of additionalPartyIDList
			return !checkListOfAdditionalPartyIDList(attrValue);
		}
		return false;
	}

	/**
	 * <cbvmda:countryOfOrigin>UK</cbvmda:countryOfOrigin>
	 * <cbvmda:countryOfExport>KR</cbvmda:countryOfExport>
	 * <cbvmda:drainedWeight measurementUnitCode="KGM">3.5</cbvmda:drainedWeight>
	 * <cbvmda:grossWeight measurementUnitCode="KGM">3.5</cbvmda:grossWeight>
	 * <cbvmda:lotNumber>ABC123</cbvmda:lotNumber>
	 * <cbvmda:netWeight measurementUnitCode="KGM">3.5</cbvmda:netWeight>
	 *
	 * @return object if cbv-compliant attribute, null if non-cbv-compliant
	 *         attrbitue, false if invalid cbv-compliant attribute
	 */
	public static Object getCBVCompliantAttribute(String attrKey, NamedNodeMap nnm, String attrValue) {
		switch (attrKey) {
		case "urn:epcglobal:cbv:mda#countryOfOrigin":
			// Country from which the goods are supplied.
			// The code list for this attribute is the ISO 3166-1 Alpha-2 list of 2-letter
			// country codes; see http://www.iso.org/iso/country_codes
			// Example: UK
			// Note: When multiple countries of origin are included, the dominant country of
			// origin SHALL be included as the first element.

			// Code
			if (checkStringOfSet(attrValue, iso3166))
				return attrValue;
			break;
		case "urn:epcglobal:cbv:mda#countryOfExport":
			// Country from which the batch/lot was exported.
			// Note: This is not the same as the country of origin. In the EU this attribute
			// indicates from which third country (outside of European Union) fishery and
			// aquaculture products were exported.
			//
			// The code list for this attribute is the ISO 3166-1 Alpha-2 list of 2-letter
			// country codes; see http://www.iso.org/iso/country_codes
			// Example: UK
			//
			// Note: When multiple countries of export are included, the dominant country of
			// export SHALL be included as the first element.

			// Code
			if (checkStringOfSet(attrValue, iso3166))
				return attrValue;
			else
				return false;
		case "urn:epcglobal:cbv:mda#drainedWeight": {
			// The weight of the trade item when drained of its liquid. For example 225
			// "grm", Jar of pickles in vinegar. Applies to defined bricks of GCI Global
			// trade item Classification - Mainly food trade item. Has to be associated with
			// a valid UoM.
			// Example: [see Section ‎9.2.4

			// Measurement
			Object obj = getMeasurement(nnm, attrValue);
			return Objects.requireNonNullElse(obj, false);
		}
		case "urn:epcglobal:cbv:mda#grossWeight": {
			// Used to identify the gross weight of the trade item. The gross weight
			// includes all packaging materials of the trade item. At pallet level the trade
			// item-GrossWeight includes the weight of the pallet itself. For example, "200
			// grm", value - total pounds, total grams, etc. Has to be associated with a
			// valid UOM.
			// Example: [see Section ‎9.2.4]

			// Measurement
			Object obj = getMeasurement(nnm, attrValue);
			return Objects.requireNonNullElse(obj, false);
		}
		case "urn:epcglobal:cbv:mda#netWeight": {
			// Used to identify the net weight of the trade item. Net weight excludes any
			// packaging materials and applies to all levels but consumer unit level. For
			// consumer unit, Net Content replaces Net Weight (can then be weight, size,
			// volume). Has to be associated with a valid UoM.
			// Example: [see Section ‎9.2.4]

			// Measurement
			Object obj = getMeasurement(nnm, attrValue);
			return Objects.requireNonNullElse(obj, false);
		}
		case "urn:epcglobal:cbv:mda#lotNumber":
			// Used to identify the net weight of the trade item. Net weight excludes any
			// packaging materials and applies to all levels but consumer unit level. For
			// consumer unit, Net Content replaces Net Weight (can then be weight, size,
			// volume). Has to be associated with a valid UoM.
			// Example: [see Section ‎9.2.4]

			// String (1-20)
			if (attrValue.length() > 20)
				return false;
			else
				return attrValue;
		}
		return null;
	}

	public static void putDate(String attrKey, List<Object> attrValue, Document attrObj) {
		if (attrValue.size() == 1 && attrValue.get(0) instanceof String) {
			String text = (String) attrValue.get(0);
			try {
				attrObj.put("attributes." + encodeMongoObjectKey(attrKey),
						DatatypeConverter.parseDate(text).getTimeInMillis());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void putMeasurement(String attrKey, Map<QName, String> otherAttributes, List<Object> attrValue,
			Document attrObj) {
		// <attribute id="urn:epcglobal:cbv:mda#drainedWeight"
		// measurementUnitCode="KGM">2.5</attribute>
		// -> { "key" : { "measurementUnitCode": ~ , "value": ~}}

		String measurementUnitCode = otherAttributes.get(new QName("measurementUnitCode"));
		if (measurementUnitCode == null)
			return;
		if (attrValue.size() == 1 && attrValue.get(0) instanceof String) {
			double dVal;
			try {
				dVal = Double.parseDouble((String) attrValue.get(0));
			} catch (Exception e) {
				return;
			}
			Document measure = new Document().append("measurementUnitCode", measurementUnitCode).append("value", dVal);
			attrObj.put("attributes." + encodeMongoObjectKey(attrKey), measure);
		}
	}

	public static Document getMeasurement(NamedNodeMap otherAttributes, String attrValue) {
		// <attribute id="urn:epcglobal:cbv:mda#drainedWeight"
		// measurementUnitCode="KGM">2.5</attribute>
		// -> { "key" : { "measurementUnitCode": ~ , "value": ~}}

		String measurementUnitCode = otherAttributes.getNamedItem("measurementUnitCode").getTextContent();
		if (measurementUnitCode == null)
			return null;

		double dVal;
		try {
			dVal = Double.parseDouble(attrValue);
		} catch (Exception e) {
			return null;
		}
		return new Document().append("measurementUnitCode", measurementUnitCode).append("value", dVal);
	}

	public static void checkGeoLocation(String attrKey, List<Object> attrValue, Document attrObj) {
		if (attrValue.size() == 1 && attrValue.get(0) instanceof String) {
			String text = (String) attrValue.get(0);
			String regex = "^geo:[0-9.]+,[0-9.]+$";
			if (text.matches(regex)) {
				attrObj.put("attributes." + encodeMongoObjectKey(attrKey), text);
			}
		}
	}

	public static void checkGeoFence(String attrKey, List<Object> attrValue, Document attrObj) {
		if (attrValue.size() == 1 && attrValue.get(0) instanceof String) {
			String text = (String) attrValue.get(0);
			String regex = "^\\[(\\[geo:[0-9.]+,[0-9.]+\\],)*(\\[geo:[0-9.]+,[0-9.]+\\])\\]$";
			if (text.matches(regex)) {
				attrObj.put("attributes." + encodeMongoObjectKey(attrKey), text);
			}
		}
	}

	public static void checkString(String attrKey, List<Object> attrValue, Document attrObj, int textSizeLimit) {
		if (attrValue.size() == 1 && attrValue.get(0) instanceof String) {
			String text = (String) attrValue.get(0);
			if (text.length() <= textSizeLimit) {
				attrObj.put("attributes." + encodeMongoObjectKey(attrKey), text);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void checkStringOfSet(String attrKey, List<Object> attrValue, Document attrObj, Set set) {
		if (attrValue.size() == 1 && attrValue.get(0) instanceof String) {
			String text = (String) attrValue.get(0);
			if (set.contains(text)) {
				attrObj.put("attributes." + encodeMongoObjectKey(attrKey), text);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean checkStringOfSet(String attrValue, Set set) {
		return set.contains(attrValue);
	}

	/**
	 * A trade item identifier that is in addition to the GTIN. Example: 12345111111
	 * See section 9.2.2.1
	 *
	 * <attribute id="urn:epcglobal:cbv:mda#additionalTradeItemID">
	 * <additionalTradeItemID tradeItemIDTypeCode=
	 * "BUYER_ASSIGNED">IMP-125</additionalTradeItemID>
	 * <additionalTradeItemID tradeItemIDTypeCode=
	 * "SUPPLIER_ASSIGNED">ASD-987</additionalTradeItemID> </attribute>
	 */
	public static void putAdditionalTradeItemID(String attrKey, List<Object> attrValue, Document attrObj) {
		if (attrValue == null || attrValue.isEmpty())
			return;
		ArrayList<Document> arr = new ArrayList<Document>();
		for (Object obj : attrValue) {
			if (obj instanceof Element) {
				Document item = new Document();
				Element element = (Element) obj;
				String qname = element.getNodeName();
				if (!qname.equals("additionalTradeItemID"))
					return;
				String tradeItemIDTypeCode = element.getAttribute("tradeItemIDTypeCode");
				if (tradeItemIDTypeCode == null)
					return;
				String text = element.getTextContent();
				item.put(tradeItemIDTypeCode, text);
				arr.add(item);
			}
		}
		attrObj.put("attributes." + encodeMongoObjectKey(attrKey), arr);
	}

	/**
	 * <attribute id="urn:epcglobal:cbv:mda#certificationList"> <certification>
	 * <certificationStandard>MSC</certificationStandard>
	 * <certificationAgency>Global Certifation One AISBL</certificationAgency>
	 * <certificationValue>4</certificationValue>
	 * <certificationIdentification>MSC-C-12345</certificationIdentification>
	 * </certification> </attribute>
	 * <p>
	 * { certificationList: [ {certificationStandard: ~, certificationAgency: ~,
	 * certificationValue: ~, certificationIdentification: ~} ] }
	 */
	public static boolean checkListOfCertification(List<Object> attrValue) {
		if (attrValue == null || attrValue.isEmpty())
			return false;
		for (Object obj : attrValue) {
			if (obj instanceof Element) {
				Element element = (Element) obj;
				String qname = element.getNodeName();
				if (!qname.equals("certification"))
					return false;
				Node pointer = element.getFirstChild();
				while (pointer != null) {
					if ((pointer instanceof Element)) {
						Element pointerElem = (Element) pointer;
						String tag = pointerElem.getTagName();
						if (!tag.equals("certificationStandard") && !tag.equals("certificationAgency")
								&& !tag.equals("certificationValue") && !tag.equals("certificationIdentification"))
							return false;
					}
					pointer = pointer.getNextSibling();
				}
			}
		}
		return true;
	}

	/**
	 * <attribute id="urn:epcglobal:cbv:mda#farmList"> <farm>
	 * <farmIdentification>urn:epc:id:sgln:950414.00001.0</farmIdentification>
	 * <farmIdentificationTypeCode>EPC-GLN</farmIdentificationTypeCode> </farm>
	 * </attribute>
	 */
	public static boolean checkListOfFarm(List<Object> attrValue) {
		if (attrValue == null || attrValue.isEmpty())
			return false;
		for (Object obj : attrValue) {
			if (obj instanceof Element) {
				Element element = (Element) obj;
				String qname = element.getNodeName();
				if (!qname.equals("farm"))
					return false;
				Node pointer = element.getFirstChild();
				while (pointer != null) {
					if ((pointer instanceof Element)) {
						Element pointerElem = (Element) pointer;
						String tag = pointerElem.getTagName();
						if (!tag.equals("farmIdentification") && !tag.equals("farmIdentificationTypeCode"))
							return false;
					}
					pointer = pointer.getNextSibling();
				}
			}
		}
		return true;
	}

	/**
	 * <attribute id="urn:epcglobal:cbv:mda#vesselCatchInformationList">
	 * <vesselCatchInformation>
	 * <vesselOperatorG>urn:epc:id:pgln:952414.00001</vesselOperatorG>
	 * <vesselID>DE-X-1234</vesselID> <vesselName>HMS Gena</vesselName>
	 * <vesselFlagState>UK</vesselFlagState> <catchArea>37.2</catchArea>
	 * <fishingGearTypeCode>TM</fishingGearTypeCode>
	 * <economicZone>GRL</economicZone>
	 * <fishConservationReferenceSizeCode>LEGAL</fishConservationReferenceSizeCode>
	 * </vesselCatchInformation> </attribute>
	 */
	public static boolean checkListOfVesselCatchInformation(List<Object> attrValue) {
		if (attrValue == null || attrValue.isEmpty())
			return false;
		for (Object obj : attrValue) {
			if (obj instanceof Element) {
				Element element = (Element) obj;
				String qname = element.getNodeName();
				if (!qname.equals("vesselCatchInformation"))
					return false;
				Node pointer = element.getFirstChild();
				while (pointer != null) {
					if ((pointer instanceof Element)) {
						Element pointerElem = (Element) pointer;
						String tag = pointerElem.getTagName();
						if (!tag.equals("vesselOperatorG") && !tag.equals("vesselID") && !tag.equals("vesselName")
								&& !tag.equals("vesselFlagState") && !tag.equals("catchArea")
								&& !tag.equals("fishingGearTypeCode") && !tag.equals("economicZone")
								&& !tag.equals("fishConservationReferenceSizeCode"))
							return false;
					}
					pointer = pointer.getNextSibling();
				}
			}
		}
		return true;
	}

	/**
	 * <attribute id="urn:epcglobal:cbv:mda#additionalPartyIDList">
	 * <AdditionalPartyID> <additionalPartyID>SE 123456789001</additionalPartyID>
	 * <partyIDTypeCode>EU_VAT_IDENTIFICATION_NUMBER</partyIDTypeCode>
	 * </AdditionalPartyID> </attribute>
	 */
	public static boolean checkListOfAdditionalPartyIDList(List<Object> attrValue) {
		if (attrValue == null || attrValue.isEmpty())
			return false;
		for (Object obj : attrValue) {
			if (obj instanceof Element) {
				Element element = (Element) obj;
				String qname = element.getNodeName();
				if (!qname.equals("AdditionalPartyID"))
					return false;
				Node pointer = element.getFirstChild();
				while (pointer != null) {
					if ((pointer instanceof Element)) {
						Element pointerElem = (Element) pointer;
						String tag = pointerElem.getTagName();
						if (!tag.equals("additionalPartyID") && !tag.equals("partyIDTypeCode"))
							return false;
					}
					pointer = pointer.getNextSibling();
				}
			}
		}
		return true;
	}
	static public String encodeMongoObjectKey(String key) {
		key = key.replace(".", "\uff0e");
		return key;
	}
}
