package org.epcis.writer.json;

import org.bson.Document;
import org.epcis.model.*;
import org.epcis.model.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class JSONDocumentWriter {
    public static Document write(EPCISDocumentType epcisDoc, List<Object> events,
                                 List<VocabularyType> vocabularies) {
        Document json = new Document();
        json.put("isA", "EPCISDocument");
        json.put("schemaVersion", epcisDoc.getSchemaVersion().toString());
        json.put("creationDate", epcisDoc.getCreationDate().toString());
        Document context = new Document();
        // add namespace

        json.put("@context", context);
        // event
        ArrayList<Document> eventList = new ArrayList<Document>();
        events.forEach(event -> {
            try {
                Document eventJson = null;
                if (event instanceof AggregationEventType) {
                    eventJson = AggregationEventWriter.write((AggregationEventType) event);
                } else if (event instanceof ObjectEventType) {
                    eventJson = ObjectEventWriter.write((ObjectEventType) event);
                } else if (event instanceof TransactionEventType) {
                    eventJson = TransactionEventWriter.write((TransactionEventType) event);
                } else if (event instanceof TransformationEventType) {
                    eventJson = TransformationEventWriter.write((TransformationEventType) event);
                } else if (event instanceof AssociationEventType) {
                    eventJson = AssociationEventWriter.write((AssociationEventType) event);
                }
                if (eventJson != null) {
                    eventList.add(eventJson);
                }
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        });
        Document epcisBodyJson = new Document();
        epcisBodyJson.put("eventList", eventList);
        json.put("epcisBody", epcisBodyJson);
        // voca
        List<Document> vocaList = new ArrayList<>();
        vocabularies.forEach(voca -> {
            vocaList.add(MasterDataWriter.write(voca));
        });
        Document epcisHeaderJson = new Document();
        Document epcisMasterData = new Document();
        epcisMasterData.put("vocabularyList", vocaList);
        epcisHeaderJson.put("epcisMasterData", epcisMasterData);
        json.put("epcisHeader", epcisHeaderJson);

        return json;
    }
}
