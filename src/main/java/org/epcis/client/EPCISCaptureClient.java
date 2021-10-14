package org.epcis.client;

import org.epcis.model.EPCISDocumentType;
import org.epcis.writer.json.JSONDocumentWriter;
import org.epcis.writer.xml.XMLDocumentWriter;
import org.json.JSONObject;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


// TODO: Make Sample
// TODO: solve related problem <- downgrade
public class EPCISCaptureClient {
    public URL captureEndpoint;

    public EPCISCaptureClient(String URL) {
        try {
            captureEndpoint = new URL(URL);
        } catch (MalformedURLException e) {
            System.out.println("Wrong URL");
            e.printStackTrace();
        }
    }

    public Integer sendByDocument(Document document) {
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            StringWriter sw = new StringWriter();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(document), new StreamResult(sw));
            String body = sw.toString();
            return sendByString(body, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer sendByJson(JSONObject json) {
        String body = json.toString();
        this.sendByString(body, true);
        return null;
    }

    public Integer sendByEPCISDocument(EPCISDocumentType epcisDoc, boolean isJson) {
        try {
            String body;
            if (isJson) {
                // using json
                body = this.toJsonLDString(epcisDoc);
            } else {
                // using xml
                body = this.toXMLString(epcisDoc);
            }
            return sendByString(body, isJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toXMLString(EPCISDocumentType epcisDoc) {
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            StringWriter sw = new StringWriter();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            Document xml = XMLDocumentWriter.write(epcisDoc);
            tf.transform(new DOMSource(xml), new StreamResult(sw));
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJsonLDString(EPCISDocumentType epcisDoc) {
        org.bson.Document json = JSONDocumentWriter.write(epcisDoc);
        return json.toJson();
    }

    public Integer sendByString(String body, boolean isJson) {
        try {
            HttpURLConnection connection = (HttpURLConnection) this.captureEndpoint
                    .openConnection();
            connection.setRequestMethod("POST");
            if (isJson) {
                // using json
                connection.setRequestProperty("Content-Type", "application/json");
            } else {
                // using xml
                connection.setRequestProperty("Content-Type", "application/xml");
            }
            connection.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream()));
            bw.write(body);
            bw.flush();
            bw.close();

            // response handling
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            // response http code
            int responseCode = connection.getResponseCode();
            if (responseCode == 400) {
                System.out.println("400 ERROR");
            } else if (responseCode == 500) {
                System.out.println("500 ERROR");
            } else {
                System.out.println("200 SUCCESS");
            }
            String returnMsg = in.readLine();
            System.out.println("RESPONSE" + returnMsg);
            return responseCode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
