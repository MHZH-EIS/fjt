package com.ai.eis.common;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

import javax.xml.bind.JAXBElement;
import java.io.*;
import java.util.*;

public class WordCommon {

    public static void replace(String src, String dest, Map <String, String> map) throws IOException {
        InputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest);
        XWPFDocument document = new XWPFDocument(in);
        Iterator <XWPFParagraph> itPara = document.getParagraphsIterator();
        String text;
        Set <String> set;
        XWPFParagraph paragraph;
        List <XWPFRun> run;
        String key;

        while (itPara.hasNext()) {
            paragraph = itPara.next();
            set = map.keySet();
            Iterator <String> iterator = set.iterator();
            while (iterator.hasNext()) {
                key = iterator.next();
                run = paragraph.getRuns();
                for (int i = 0, runSie = run.size(); i < runSie; i++) {
                    text = run.get(i).getText(run.get(i).getTextPosition());
                    if (text != null && text.equals(key)) {
                        run.get(i).setText(map.get(key), 0);
                    }

                }
            }
        }

        //2. 替换表格中的指定文字
        Iterator <XWPFTable> itTable = document.getTablesIterator();
        XWPFTable table;
        int rowsCount;
        while (itTable.hasNext()) {
            table = itTable.next();
            rowsCount = table.getNumberOfRows();
            for (int i = 0; i < rowsCount; i++) {
                XWPFTableRow row = table.getRow(i);
                List <XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    for (Map.Entry <String, String> e : map.entrySet()) {
                        if (cell.getText().equals(e.getKey())) {
                            cell.removeParagraph(0);
                            cell.setText(e.getValue());
                        }
                    }
                }
            }
        }

        document.write(out);
        in.close();
        out.close();

    }

    public static void searchAndReplace(String srcPath, String destPath, Map <String, String> map) throws IOException {
        InputStream inputStream = new FileInputStream(srcPath);
        HWPFDocument document = new HWPFDocument(inputStream);
        Range range = document.getRange();
        for (Map.Entry <String, String> entry : map.entrySet()) {
            range.replaceText(entry.getKey(), entry.getValue());

        }
        OutputStream outputStream = new FileOutputStream(destPath);
        document.write(outputStream);
    }

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


    public static void main(String[] args) throws IOException, Docx4JException {
        Map <String, String> map = new HashMap <>();
        map.put("projectNo", "SASD323");
        map.put("name", "cyony");
        replacePlaceholder("D:\\temp\\test1.docx", "D:\\temp\\total.docx", map);
    }


}
