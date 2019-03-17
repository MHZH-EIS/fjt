package com.ai.eis.modeler;

import com.ai.eis.common.FileModel;
import com.ai.eis.common.FileUtil;
import com.ai.eis.common.WordCommon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class InverterModeler implements AbstractModeler {

    private Logger logger = LoggerFactory.getLogger(InverterModeler.class);

    public static void main(String[] args) throws Exception {
        FileModel.setBasePath("D:\\lic");
        Map <String, String> map = new HashMap <>();
        map.put("zero", "C:\\Users\\86183\\Desktop\\New folder\\Delta-reactive power\\PF=1.0.csv");
        map.put("max", "C:\\Users\\86183\\Desktop\\New folder\\Delta-reactive power\\Q=0.49Pn.csv");
        map.put("min", "C:\\Users\\86183\\Desktop\\New folder\\Delta-reactive power\\Q=-0.49Pn.csv");
        //map.put("zero", "C:\\Users\\86183\\Desktop\\New folder\\Goodwe\\B1.2.2.2-Q=0.csv");
        //map.put("max", "C:\\Users\\86183\\Desktop\\New folder\\Goodwe\\B1.2.2.2-Q=MAX.csv");
        //map.put("min", "C:\\Users\\86183\\Desktop\\New folder\\Goodwe\\B1.2.2.2-Q=-MAX.csv");


        map.put("ratePower", "60000");
        map.put("projectNo", "CYONY20190301");
        map.put("trfNo", "ZHUSY20190301");
        InverterModeler inverterModeler = new InverterModeler();
        inverterModeler.process(map);
    }

    @Override
    public File process(Map <String, String> param) throws Exception {
        File zeroFile = new File(param.get("zero"));
        File maxFile = new File(param.get("max"));
        File minFile = new File(param.get("min"));
        int ratePower = Integer.valueOf(param.get("ratePower"));
        String projectNo = param.get("projectNo");

        File tableTemplate = new File(FileModel.getBasePath() + File.separator + "modeler" + File.separator
                + "inverter" + File.separator + "inverter.docx");
        File chartTemplate = new File(FileModel.getBasePath() + File.separator + "modeler" + File.separator
                + "inverter" + File.separator + "chart.xml");
        if (!tableTemplate.exists() || !chartTemplate.exists()) {
            throw new IOException("找不到模板文件");
        }

        List <File> allFiles = new ArrayList <>();

        File zeroWord = FileModel.generateReport(projectNo, "zero.docx");
        File maxWord = FileModel.generateReport(projectNo, "max.docx");
        File minWord = FileModel.generateReport(projectNo, "min.docx");
        File chartWord = FileModel.generateReport(projectNo, "chart.docx");
        File reportFile = FileModel.generateReport(projectNo, projectNo + ".docx");

        allFiles.add(zeroWord);
        allFiles.add(maxWord);
        allFiles.add(minWord);
        allFiles.add(chartWord);

        LinkedHashMap <String, String> QzeroMap = handle(zeroFile, ratePower);
        LinkedHashMap <String, String> QmaxMap = handle(maxFile, ratePower);
        LinkedHashMap <String, String> QminMap = handle(minFile, ratePower);

        QzeroMap.put("projectNo", projectNo);
        QzeroMap.put("trfNo", param.get("trfNo"));
        QzeroMap.put("dataSource", "Q=0");
        QmaxMap.put("dataSource", "Q=+Max");
        QminMap.put("dataSource", "Q=-Max");

        try {
            String chart = FileUtils.readFileToString(chartTemplate, "UTF-8");
            WordCommon.replacePlaceholder(tableTemplate, zeroWord, QzeroMap);
            WordCommon.replacePlaceholder(tableTemplate, maxWord, QmaxMap);
            WordCommon.replacePlaceholder(tableTemplate, minWord, QminMap);

            Map <String, String> chartMap = new HashMap <>();
            chartMap.putAll(drawChart(QzeroMap, "zero"));
            chartMap.putAll(drawChart(QmaxMap, "max"));
            chartMap.putAll(drawChart(QminMap, "min"));
            for (Map.Entry <String, String> entry : chartMap.entrySet()) {
                chart = chart.replace(entry.getKey(), entry.getValue());
            }
            WordprocessingMLPackage.load(new ByteArrayInputStream(chart.getBytes())).save(chartWord);
            FileUtil.mergeWord(allFiles, reportFile);
            if (reportFile.exists()) {
                logger.info("报告生成成功{}", projectNo);
            }
            return reportFile;
        } finally {
            if (zeroWord.exists()) {
                zeroWord.delete();
            }
            if (maxWord.exists()) {
                maxWord.delete();
            }
            if (minWord.exists()) {
                minWord.delete();
            }
            if (chartWord.exists()) {
                chartWord.delete();
            }
        }
    }

    public static LinkedHashMap <String, String> handle(File file, int ratePower) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List <String> lines = FileUtils.readLines(file, "UTF-8");
        List <Map <String, String>> lists = new ArrayList <>();
        List <String> headers = null;
        for (String line : lines) {
            if (line.startsWith("\"Store No") || line.startsWith("Store No")) {
                headers = Arrays.stream(line.split(","))
                                .map(x -> x.replace("\"", "").trim())
                                .collect(Collectors.toList());
            } else if (headers != null) {
                String[] values = line.split(",");
                if (values.length != headers.size()) {
                    continue;
                }
                Map <String, String> map = new HashMap <>();
                String time = "";
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    String key = headers.get(i);
                    if (value.contains("E+")) {
                        BigDecimal bd = new BigDecimal(value.trim());
                        value = bd.toPlainString();
                    }
                    switch (key) {
                        case "Date":
                            time += value.trim();
                            break;
                        case "Time":
                            time += " ";
                            time += value.trim();
                            break;
                        default:
                            map.put(headers.get(i), value.trim());
                            break;
                    }
                }
                map.put("date", String.valueOf(dateFormat.parse(time).getTime()));
                lists.add(map);
            }
        }
        BigDecimal step = new BigDecimal("0.1");

        LinkedHashMap <String, String> rsMap = new LinkedHashMap <>();
        for (float i = 0.0f; i <= 1; ) {
            rsMap.putAll(movingSectionComputing(lists, ratePower, i, 0.05f, 5, 3 * 60 * 1000, 30 * 1000));
            BigDecimal b = new BigDecimal(String.valueOf(i));
            i = b.add(step).floatValue();
        }
        rsMap.forEach((k, v) -> System.out.println(k + "=" + v));
        return rsMap;


    }

    public static String parser(String value) {
        if (value.endsWith(")")) {
            value = StringUtils.substringBefore(value, "(");
        }
        return value;
    }


    public static Map <String, String> movingSectionComputing(List <Map <String, String>> lists,
                                                              int ratePower,
                                                              float powerBin,
                                                              float costFactor,
                                                              int outliers,
                                                              int slice,
                                                              int stableTime) {
        double cosMin = ratePower * (powerBin - costFactor);
        double cosMax = ratePower * (powerBin + costFactor);
        List <Map <String, String>> rsList = new ArrayList <>();
        List <Map <String, String>> outRange = new ArrayList <>();

        long startDate = 0;
        for (Map <String, String> map : lists) {
            String pst = map.get("P-SigmaA-Total");
            String qst = map.get("Q-SigmaA-Total");
            String pfst = map.getOrDefault("PF-SigmaA-Total", "0");
            String p4t = map.get("P-4-Total");
            String all = pst + qst + pfst + p4t;

            if (all.contains("NAN") || all.contains("Error")) {
                continue;
            }

            double apw = Double.valueOf(pst);
            double rpv = Double.valueOf(qst);
            double dpw = Double.valueOf(p4t);
            double cos = Double.valueOf(pfst);
            long date = Long.valueOf(map.get("date"));
            if (startDate == 0) {
                startDate = date;
            }
            if (cosMin <= apw && apw <= cosMax) {
                if (!outRange.isEmpty()) {
                    rsList.addAll(outRange);
                    outRange.clear();
                }
                if (date - startDate < slice) {
                    Map <String, String> rsMap = new HashMap <>();
                    rsMap.put("time", String.valueOf(date));
                    rsMap.put("apw", String.valueOf(apw));
                    rsMap.put("rpv", String.valueOf(rpv));
                    rsMap.put("dpw", String.valueOf(dpw));
                    rsMap.put("cos", String.valueOf(cos));
                    rsList.add(rsMap);
                } else {
                    break;
                }
            } else {
                if (date - startDate > stableTime) {
                    if (outRange.size() > outliers) {
                        break;
                    }
                    if (date - startDate < slice) {
                        Map <String, String> rsMap = new HashMap <>();
                        rsMap.put("time", String.valueOf(date));
                        rsMap.put("apw", String.valueOf(apw));
                        rsMap.put("rpv", String.valueOf(rpv));
                        rsMap.put("dpw", String.valueOf(dpw));
                        rsMap.put("cos", String.valueOf(cos));
                        outRange.add(rsMap);
                    }
                } else {
                    startDate = 0;
                    rsList.clear();
                }
            }

        }
        return movingAvg(rsList, String.valueOf((int) (powerBin * 10)), ratePower);
    }

    public static Map <String, String> movingAvg(List <Map <String, String>> lists,
                                                 String powerBin,
                                                 int ratePower) {
        Map <String, String> rsMap = new LinkedHashMap <>();
        long startDate = 0;
        long lastDate = 0;
        double apwTotal = 0;
        double rpvTotal = 0;
        double dpwTotal = 0;
        double cosTotal = 0;
        int index = 0;
        int no = 0;
        for (Map <String, String> map : lists) {
            lastDate = Long.valueOf(map.get("time"));
            double apw = Double.valueOf(map.get("apw"));
            double rpv = Double.valueOf(map.get("rpv"));
            double dpw = Double.valueOf(map.get("dpw"));
            double cos = Double.valueOf(map.get("cos"));
            if (startDate == 0) {
                startDate = lastDate;
            }
            if (lastDate - startDate < 60 * 1000) {
                apwTotal += apw;
                rpvTotal += rpv;
                dpwTotal += dpw;
                cosTotal += cos;
                index++;
            } else {
                no++;
                rsMap.put("apw" + powerBin + no, String.format("%.1f", apwTotal / index));
                rsMap.put("rpv" + powerBin + no, String.format("%.1f", rpvTotal / index));
                rsMap.put("dpw" + powerBin + no, String.format("%.1f", dpwTotal / index));
                rsMap.put("apu" + powerBin + no, String.format("%.2f", apwTotal / index / ratePower));
                rsMap.put("rpu" + powerBin + no, String.format("%.2f", rpvTotal / index / ratePower));
                rsMap.put("dpu" + powerBin + no, String.format("%.2f", dpwTotal / index / ratePower));
                rsMap.put("cos" + powerBin + no, String.format("%.2f", cosTotal / index));
                startDate = lastDate;
                apwTotal = apw;
                rpvTotal = rpv;
                dpwTotal = dpw;
                cosTotal = cos;
                index = 1;
            }
        }

        if (lastDate - startDate == 59 * 1000) {
            no++;
            rsMap.put("apw" + powerBin + no, String.format("%.1f", apwTotal / index));
            rsMap.put("rpv" + powerBin + no, String.format("%.1f", rpvTotal / index));
            rsMap.put("dpw" + powerBin + no, String.format("%.1f", dpwTotal / index));
            rsMap.put("apu" + powerBin + no, String.format("%.2f", apwTotal / index / ratePower));
            rsMap.put("rpu" + powerBin + no, String.format("%.2f", rpvTotal / index / ratePower));
            rsMap.put("dpu" + powerBin + no, String.format("%.2f", dpwTotal / index / ratePower));
            rsMap.put("cos" + powerBin + no, String.format("%.2f", cosTotal / index));
        } else if (apwTotal > 0) {
            no++;
            rsMap.put("apw" + powerBin + no, String.format("%.1f", apwTotal / index) + "(lack)");
            rsMap.put("rpv" + powerBin + no, String.format("%.1f", rpvTotal / index) + "(lack)");
            rsMap.put("dpw" + powerBin + no, String.format("%.1f", dpwTotal / index) + "(lack)");
            rsMap.put("apu" + powerBin + no, String.format("%.2f", apwTotal / index / ratePower) + "(lack)");
            rsMap.put("rpu" + powerBin + no, String.format("%.2f", rpvTotal / index / ratePower) + "(lack)");
            rsMap.put("dpu" + powerBin + no, String.format("%.2f", dpwTotal / index / ratePower) + "(lack)");
            rsMap.put("cos" + powerBin + no, String.format("%.2f", cosTotal / index) + "(lack)");
        }
        while (no < 3) {
            no++;
            rsMap.put("apw" + powerBin + no, "0(lack)");
            rsMap.put("rpv" + powerBin + no, "0(lack)");
            rsMap.put("dpw" + powerBin + no, "0(lack)");
            rsMap.put("apu" + powerBin + no, "0(lack)");
            rsMap.put("rpu" + powerBin + no, "0(lack)");
            rsMap.put("dpu" + powerBin + no, "0(lack)");
            rsMap.put("cos" + powerBin + no, "0(lack)");
        }
        return rsMap;
    }

    public static Map <String, String> drawChart(LinkedHashMap <String, String> dataMap, String tag) {
        Map <String, String> chartMap = new HashMap <>();
        StringBuilder rpuSb = new StringBuilder();
        StringBuilder apuSb = new StringBuilder();
        rpuSb.append("<c:ptCount val=\"%s\"/>");
        apuSb.append("<c:ptCount val=\"%s\"/>");
        int ptCount = 0;
        for (int i = 0; i < 11; i++) {
            double rpuSlot = 0;
            double apuSlot = 0;
            for (int j = 1; j < 4; j++) {
                rpuSlot += Double.valueOf(parser(dataMap.get("rpu" + i + j)));
                apuSlot += Double.valueOf(parser(dataMap.get("apu" + i + j)));
            }
            if (apuSlot != 0 && rpuSlot != 0) {
                ptCount++;
                rpuSb.append("<c:pt idx=\"" + ptCount + "\">");
                rpuSb.append("<c:v>" + String.format("%.2f", rpuSlot / 3) + "</c:v>");
                rpuSb.append("</c:pt>");
                apuSb.append("<c:pt idx=\"" + ptCount + "\">");
                apuSb.append("<c:v>" + String.format("%.2f", apuSlot / 3) + "</c:v>");
                apuSb.append("</c:pt>");
            }
        }
        chartMap.put(tag + "_rpuData", String.format(rpuSb.toString(), ptCount));
        chartMap.put(tag + "_apuData", String.format(apuSb.toString(), ptCount));
        return chartMap;
    }
}
