package org.epcis.util.unit;

/**
 * Copyright (C) 2020 Jaewook Byun
 *
 * This project is part of Oliot open source (http://oliot.org). Oliot EPCIS
 * v2.x is Java Web Service complying with Electronic Product Code Information
 * Service (EPCIS) v2.0.x
 *
 * This class is a Java implementation of UnitConverterUNECERec20 written in
 * Javascript (https://github.com/mgh128/UnitConverterUNECERec20)
 *
 * @author Jaewook Byun, Ph.D., Assistant Professor, Sejong University,
 *         jwbyun@sejong.ac.kr
 * 
 *         Associate Director, Auto-ID Labs, KAIST, bjw0829@kaist.ac.kr
 */
public class MolarMass {

	public static String rType = "D74";
	public static double rm = 1.0;
	public static double ro = 0.0;
	
	/**
	 * 'Rec20 representation'('name', 'symbol')
	 */
	public enum Type {

		D74("kilogram per mole", "kg/mol"), A94("gram per mole", "g/mol");

		private final String name;
		private final String symbol;

		Type(String name, String symbol) {
			this.name = name;
			this.symbol = symbol;
		}

		public String getName() {
			return name;
		}

		public String getSymbol() {
			return symbol;
		}
	}

	public static Double[] multipliers = { 1.0, 0.001 };
	public static Double[] offsets = { 0.0, 0.0 };

	public static Type[] getRec20Types() {
		return Type.values();
	}

	private Type type;
	private Double value;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public MolarMass(Type type, Double value) {
		this.type = type;
		this.value = value;
	}

	public MolarMass(String uom, Double value) {
		this.type = Type.valueOf(uom);
		this.value = value;
	}
}
