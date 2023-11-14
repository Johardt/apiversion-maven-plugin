package org.jamutils.apiversion;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MavenProjectWriter {

    private final File pomFile;

    public MavenProjectWriter(File pomFile) {
        this.pomFile = pomFile;
    }

    public void writeVersion(String version) throws IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc;
        Element root;
        try {
            // Disable DTDs to prevent XXE attacks
            docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(pomFile);
            root = doc.getDocumentElement();
        } catch (Exception e) {
            throw new IOException(e);
        }

        String escapedVersion = StringEscapeUtils.escapeXml11(version);
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeName().equals("version")) {
                System.out.println(childNodes.item(i).getTextContent());
                childNodes.item(i).setTextContent(escapedVersion);
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(pomFile);

            transformer.transform(source, result);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
