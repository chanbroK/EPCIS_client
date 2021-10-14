package org.epcis.document.builder;

import org.epcis.model.VocabularyElementListType;
import org.epcis.model.VocabularyElementType;
import org.epcis.model.VocabularyType;

public class VocabularyBuilder {
	private VocabularyType vocabulary;

	public VocabularyBuilder(String type) {
		this.vocabulary = new VocabularyType();
		this.setType(type);
	}

	public VocabularyBuilder setType(String type) {
		this.vocabulary.setType(type);
		return this;
	}

	public VocabularyBuilder addVocaElement(VocabularyElementType elem) {
		if (this.vocabulary.getVocabularyElementList() == null) {
			this.vocabulary.setVocabularyElementList(new VocabularyElementListType());
		}
		this.vocabulary.getVocabularyElementList().getVocabularyElement().add(elem);
		return this;
	}

	public VocabularyBuilder setVocaElementList(VocabularyElementListType elemList) {
		this.vocabulary.setVocabularyElementList(elemList);
		return this;
	}

	public VocabularyType build() {
		return this.vocabulary;
	}

}
