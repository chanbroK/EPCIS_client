package org.epcis.writer.json;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.epcis.model.VocabularyType;
import org.w3c.dom.Element;

public class MasterDataWriter {
	public static Document write(VocabularyType voca) {
		Document result = new Document();
		result.put("type", voca.getType());
		List<Document> elemList = new ArrayList<>();
		voca.getVocabularyElementList().getVocabularyElement().forEach(vElem -> {
			Document elem = new Document();
			elem.put("id", vElem.getId());
			List<Document> attrList = new ArrayList<>();
			vElem.getAttribute().forEach(aElem -> {
				Document attr = new Document();
				attr.put("id", aElem.getId());
				if (aElem.getContent().get(0) instanceof String) {
					attr.put("attribute", aElem.getContent().get(0));
				} else {
					// Object
					// 1단계 까지 지원
					Document extension = new Document();
					aElem.getContent().forEach(e -> {
						Element eElem = (Element) e;
						extension.put(eElem.getTagName(), eElem.getTextContent().toString());
					});
					attr.put("attribute", extension);
				}
				attrList.add(attr);
			});
			elem.put("attributes", attrList);
			List<String> idList = new ArrayList<>();
			vElem.getChildren().getId().forEach(id -> {
				idList.add(id);
			});
			elem.put("children", idList);
			elemList.add(elem);
		});
		result.put("vocabularyElementList", elemList);
		return result;
	}
}
