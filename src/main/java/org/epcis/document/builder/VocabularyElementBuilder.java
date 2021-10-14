package org.epcis.document.builder;

import java.util.ArrayList;
import java.util.List;

import org.epcis.model.AttributeType;
import org.epcis.model.IDListType;
import org.epcis.model.VocabularyElementType;
import org.w3c.dom.Element;

public class VocabularyElementBuilder {
	private VocabularyElementType vocaElement;

	public VocabularyElementBuilder(String id) {
		this.vocaElement = new VocabularyElementType();
		this.vocaElement.setId(id);
	}

	public VocabularyElementType build() {
		return this.vocaElement;
	}

	public VocabularyElementBuilder setId(String id) {
		this.vocaElement.setId(id);
		return this;
	}

	public VocabularyElementBuilder addAttribute(String id, String value) {

		if (this.vocaElement.getAttribute() == null) {
			this.vocaElement.setAttribute(new ArrayList<AttributeType>());
		}
		AttributeType attr = new AttributeType();
		List<Object> valueList = new ArrayList<Object>();
		valueList.add(value);
		attr.setContent(valueList);
		attr.setId(id);
		this.vocaElement.getAttribute().add(attr);
		return this;
	}

	public VocabularyElementBuilder setChildren(List<String> childList) {
		if (this.vocaElement.getChildren() == null) {
			this.vocaElement.setChildren(new IDListType());
		}
		childList.forEach(child -> {
			this.vocaElement.getChildren().getId().add(child);
		});
		return this;
	}

	public VocabularyElementBuilder addAttributeChild(String id, List<Object> childAttr) {

		if (this.vocaElement.getAttribute() == null) {
			this.vocaElement.setAttribute(new ArrayList<AttributeType>());
		}
		AttributeType attr = new AttributeType();
		attr.setContent(childAttr);
		attr.setId(id);
		this.vocaElement.getAttribute().add(attr);
		return this;
	}

	public VocabularyElementBuilder addExtension(Element extension) {
		this.vocaElement.getAny().add(extension);
		return this;
	}

}
