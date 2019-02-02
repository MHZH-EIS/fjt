package com.ai.eis.common;

import org.docx4j.Docx4J;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.JaxbXmlPart;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WordCommon {

    public static void replacePlaceholder(File src, File dest, Map <String, String> map) throws Exception {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(src));
        List <Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (map.containsKey(textElement.getValue())) {
                textElement.setValue(map.get(textElement.getValue()));
            }
        }
        replaceFooter(template, map);
        template.save(dest);
    }

    private static List <Object> getAllElementFromObject(Object obj, Class <?> toSearch) {
        List <Object> result = new ArrayList <>();
        if (obj instanceof JAXBElement) {
            obj = ((JAXBElement <?>) obj).getValue();
        }
        if (obj.getClass().equals(toSearch)) {
            result.add(obj);
        } else if (obj instanceof ContentAccessor) {
            List <?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }
        }
        return result;
    }

    public static void replaceText(File src, File dest, Map <String, String> map) throws Docx4JException, JAXBException {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(src);
        wordMLPackage.getMainDocumentPart().variableReplace(map);
        wordMLPackage.save(dest);
    }

    /**
     * @param template
     * @param dest
     * @param dataList
     * @throws Docx4JException
     * @throws JAXBException
     */
    public static void createWordTable(File template, File dest, List <Map <String, Object>> dataList) throws Docx4JException, JAXBException {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(template);
        ClassFinder find = new ClassFinder(Tbl.class);
        new TraversalUtil(wordMLPackage.getMainDocumentPart().getContent(), find);
        Tbl table = (Tbl) find.results.get(0);
        Tr dynamicTr = (Tr) table.getContent().get(1);
        String dynamicTrXml = XmlUtils.marshaltoString(dynamicTr);
        for (Map <String, Object> dataMap : dataList) {
            Tr newTr = (Tr) XmlUtils.unmarshallFromTemplate(dynamicTrXml, dataMap);
            table.getContent().add(newTr);
        }
        table.getContent().remove(1);
        Docx4J.save(wordMLPackage, dest);

    }

    public static void replaceFooter(WordprocessingMLPackage wordMLPackage, Map <String, String> map) throws Exception {
        RelationshipsPart relationshipPart = wordMLPackage.getMainDocumentPart().getRelationshipsPart();
        List <Relationship> relationships = relationshipPart.getRelationships().getRelationship();
        for (Relationship r : relationships) {
            if (r.getId().equals("rId13") || r.getId().equals("rId14")) {
                JaxbXmlPart part = (JaxbXmlPart) relationshipPart.getPart(r);
                List <Object> texts = getAllElementFromObject(part.getContents(), Text.class);
                for (Object text : texts) {
                    Text textElement = (Text) text;
                    if (map.containsKey(textElement.getValue())) {
                        textElement.setValue(map.get(textElement.getValue()));
                    }
                }
            }
        }
    }


}
