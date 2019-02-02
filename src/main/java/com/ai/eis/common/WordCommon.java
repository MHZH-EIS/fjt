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
import java.text.SimpleDateFormat;
import java.util.*;

public class WordCommon {

    public static void replacePlaceholder(File src, File dest, Map <String, String> map) throws FileNotFoundException, Docx4JException {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(src));
        List <Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (map.containsKey(textElement.getValue())) {
                textElement.setValue(map.get(textElement.getValue()));
            }
        }
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

    public static void tach() throws FileNotFoundException, Docx4JException {
        Map <String, String> map = new HashMap <>();
        map.put("projectNo", "A2018CCC0301-3043365");
        map.put("projectName", "配电箱");
        map.put("version", "PZ30");
        map.put("sampleNum", "50");
        map.put("signDate", "2019-1-20");
        map.put("clientCompany", "蚌埠市禹哲电气设备有限公司");
        map.put("clientAddress", "安徽省蚌埠市禹会区禹和路15号禹会工业园1号车间");
        map.put("mfName", "蚌埠市禹哲电气设备有限公司");
        map.put("mfAddress", "安徽省蚌埠市禹会区禹和路15号禹会工业园1号车间");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat format4 = new SimpleDateFormat("yyyyMM");
        map.put("sampleNo", format4.format(new Date()));
        map.put("completeDate", format1.format(new Date()));
        map.put("sDate", format2.format(new Date()));
        map.put("sealDate", format3.format(new Date()));

        map.put("manager", "朱世阳");
        map.put("charger", "苗海泉");
        map.put("signer", "郭总");
        String standard = "GB/T 7251.3-2017《低压成套开关设备和控制设备 第3部分: 由一般人员操作的配电板（DBO）》" + "\r\n" +
                "GB/T 7251.3-2018《低压成套开关设备和控制设备 第3部分: 由一般人员操作的配电板（DBO）》";

        map.put("standard", standard);

        // replacePlaceholder("D:\\temp\\A2018CCC0301-3043365N.docx", "D:\\temp\\total.docx", map);

    }


    public static void main(String[] args) throws JAXBException, Docx4JException, FileNotFoundException {
        Map <String, String> map = new HashMap <>();
        map.put("projectNo", "ASEDD");
        replaceText(new File("D:\\temp\\report.docx"),
                new File("D:\\temp\\total.docx"),
                map);
    }


}
