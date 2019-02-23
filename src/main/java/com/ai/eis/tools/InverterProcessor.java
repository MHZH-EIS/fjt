package com.ai.eis.tools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InverterProcessor {

    public static int ratePower = 10000;

    public static void handle(File file) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List <String> lines = FileUtils.readLines(file, "UTF-8");
        List <Map <String, String>> lists = new ArrayList <>();
        List <String> headers = null;
        for (String line : lines) {
            if (line.startsWith("Store No")) {
                headers = Arrays.asList(line.split(","));
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
                        BigDecimal bd = new BigDecimal(value);
                        value = bd.toPlainString();
                    }
                    if (key.equals("Date")) {
                        time += value.trim();
                    } else if (key.equals("Time")) {
                        time += " ";
                        time += value.trim();
                    } else {
                        map.put(headers.get(i), value.trim());
                    }
                }
                map.put("date", String.valueOf(dateFormat.parse(time).getTime()));
                lists.add(map);
            }
        }
        movingSectionComputing(lists, 10000, 0.0f, 0.04f, 5, 3 * 60 * 1000, 30 * 1000);

    }

    public static void movingSectionComputing(List <Map <String, String>> lists,
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
            double value = Double.valueOf(map.get("P-SigmaA-Total"));
            long date = Long.valueOf(map.get("date"));
            if (startDate == 0) {
                startDate = date;
            }
            if (cosMin <= value && value <= cosMax) {
                if (!outRange.isEmpty()) {
                    rsList.addAll(outRange);
                    outRange.clear();
                }
                if (date - startDate < slice) {
                    Map <String, String> rsMap = new HashMap <>();
                    rsMap.put("time", String.valueOf(date));
                    rsMap.put("value", String.valueOf(value));
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
                        rsMap.put("value", String.valueOf(value));
                        outRange.add(rsMap);
                    }
                } else {
                    startDate = 0;
                    rsList.clear();
                }
            }

        }
        movingAvg(rsList);
    }

    public static void movingAvg(List <Map <String, String>> lists) {
        long startDate = 0;
        long lastDate = 0;
        double total = 0;
        int index = 0;
        for (Map <String, String> map : lists) {
            lastDate = Long.valueOf(map.get("time"));
            double value = Double.valueOf(map.get("value"));
            if (startDate == 0) {
                startDate = lastDate;
            }
            if (lastDate - startDate < 60 * 1000) {
                total += value;
                index++;
            } else {
                System.out.println(total / index);
                startDate = lastDate;
                total = value;
                index = 1;
            }
        }
        if (lastDate - startDate == 59 * 1000) {
            System.out.println(total / index);
        } else {
            System.out.println(total / index + "数据不足");
        }
    }


    public static void main(String[] args) throws IOException, ParseException {
        handle(new File("C:\\Users\\86183\\Desktop\\wz\\0=0.csv"));
    }


}
