package com.ai.eis.common;

import org.docx4j.Docx4J;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCommon {

    public static void replacePlaceholder(String srcPath, String destPath, Map <String, String> map) throws FileNotFoundException, Docx4JException {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(srcPath)));
        List <Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (map.containsKey(textElement.getValue())) {
                textElement.setValue(map.get(textElement.getValue()));
            }
        }
        template.save(new File(destPath));
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


    public static void main(String[] args) throws JAXBException, Docx4JException {
        List <Map <String, Object>> list = new ArrayList <>();
        for (int i = 1; i < 10; i++) {
            Map <String, Object> map = new HashMap <>();
            map.put("index", i);
            map.put("experiment", "光学测试" + i);
            map.put("clause", "3.10." + i);
            map.put("result", "合格");
            list.add(map);
        }
        createWordTable(new File("D:\\temp\\2.docx"), new File("D:\\temp\\total.docx"), list);
    }


}
