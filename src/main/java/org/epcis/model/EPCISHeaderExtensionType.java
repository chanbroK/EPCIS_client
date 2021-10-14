//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.11 at 04:47:34 PM KST 
//


package org.epcis.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for EPCISHeaderExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EPCISHeaderExtensionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EPCISMasterData" type="{urn:epcglobal:epcis:xsd:2}EPCISMasterDataType" minOccurs="0"/>
 *         &lt;element name="extension" type="{urn:epcglobal:epcis:xsd:2}EPCISHeaderExtension2Type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EPCISHeaderExtensionType", propOrder = {
    "epcisMasterData",
    "extension"
})
public class EPCISHeaderExtensionType {

    @XmlElement(name = "EPCISMasterData")
    protected EPCISMasterDataType epcisMasterData;
    protected EPCISHeaderExtension2Type extension;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the epcisMasterData property.
     * 
     * @return
     *     possible object is
     *     {@link EPCISMasterDataType }
     *     
     */
    public EPCISMasterDataType getEPCISMasterData() {
        return epcisMasterData;
    }

    /**
     * Sets the value of the epcisMasterData property.
     * 
     * @param value
     *     allowed object is
     *     {@link EPCISMasterDataType }
     *     
     */
    public void setEPCISMasterData(EPCISMasterDataType value) {
        this.epcisMasterData = value;
    }

    /**
     * Gets the value of the extension property.
     * 
     * @return
     *     possible object is
     *     {@link EPCISHeaderExtension2Type }
     *     
     */
    public EPCISHeaderExtension2Type getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     * 
     * @param value
     *     allowed object is
     *     {@link EPCISHeaderExtension2Type }
     *     
     */
    public void setExtension(EPCISHeaderExtension2Type value) {
        this.extension = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
