package com.ai.eis.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.*;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    /**
     * 文件复制
     *
     * @param source 原文件
     * @param target 目标文件
     * @throws IOException
     */
    public static void nioTransferCopy(File source, File target) throws IOException {
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            FileChannel in = inStream.getChannel();
            FileChannel out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } finally {
            IOUtils.closeQuietly(inStream);
            IOUtils.closeQuietly(outStream);
        }

    }

    public static File createSpoolAndFile(String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static void mergeWord(List <File> srcDocxs, File destDocx, boolean breakPage) throws IOException, Docx4JException {
        WordprocessingMLPackage target = null;
        final File generated = File.createTempFile("generated", ".docx");
        int index = 0;
        for (File srcDocx : srcDocxs) {
            if (target == null) {
                OutputStream os = new FileOutputStream(generated);
                os.write(IOUtils.toByteArray(new FileInputStream(srcDocx)));
                os.close();
                target = WordprocessingMLPackage.load(generated);
            } else {
                MainDocumentPart mainDocumentPart = target.getMainDocumentPart();
                if (breakPage) {
                    addPageBreak(mainDocumentPart);
                }
                AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/part" + index++ + ".docx"));
                afiPart.setBinaryData(IOUtils.toByteArray(new FileInputStream(srcDocx)));
                Relationship relationship = mainDocumentPart.addTargetPart(afiPart);
                CTAltChunk chunk = Context.getWmlObjectFactory().createCTAltChunk();
                chunk.setId(relationship.getId());
                mainDocumentPart.addObject(chunk);
            }
        }
        if (target != null) {
            target.save(generated);
            FileUtils.copyInputStreamToFile(new FileInputStream(generated), destDocx);
        }
    }

    public static void mergeWord(List <File> srcDocxs, File destDocx) throws IOException, Docx4JException {
        mergeWord(srcDocxs, destDocx, true);
    }

    private static void addPageBreak(MainDocumentPart documentPart) {
        ObjectFactory objectFactory = new ObjectFactory();
        P p = objectFactory.createP();
        R r = objectFactory.createR();
        p.getContent().add(r);
        Br br = objectFactory.createBr();
        r.getContent().add(br);
        br.setType(org.docx4j.wml.STBrType.PAGE);
        documentPart.addObject(p);
    }

    public static void main(String[] args) throws IOException, Docx4JException {
        mergeWord(Arrays.asList(new File("D:\\temp\\cover.docx"),new File("D:\\temp\\q1.docx")),new File("D:\\temp\\total.docx"));
    }

}
