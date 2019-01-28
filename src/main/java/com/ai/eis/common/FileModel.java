package com.ai.eis.common;

import org.springframework.util.ClassUtils;

import java.io.File;

public class FileModel {

    public static final String BASE_PATH = new File(ClassUtils.getDefaultClassLoader().getResource("").getPath()).getAbsolutePath()
            + File.separator + "upload" + File.separator;

    /**
     * 生成标准文件路径
     *
     * @param stNo     标准号
     * @param fileName 文件名
     * @return
     */
    public static File generateStandard(String stNo, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("standards");
        sb.append(File.separator);
        sb.append(stNo);
        sb.append(File.separator);
        sb.append(fileName);
        return FileUtil.createSpoolAndFile(sb.toString());
    }

    /**
     * 生成标准测试项模板文件路径
     *
     * @param stNo     标准号
     * @param fileName 文件名
     * @return
     */
    public static File generateItem(String stNo, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("standards");
        sb.append(File.separator);
        sb.append(stNo);
        sb.append(File.separator);
        sb.append("items");
        sb.append(File.separator);
        sb.append(fileName);
        return FileUtil.createSpoolAndFile(sb.toString());
    }

    /**
     * 生成合并测试项报告路径
     *
     * @param pId      项目ID
     * @param fileName 文件名
     * @return
     */
    public static File generateMergeExperiment(String pId, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("project");
        sb.append(File.separator);
        sb.append(pId);
        sb.append(File.separator);
        sb.append("experiment");
        sb.append(File.separator);
        sb.append("merge");
        sb.append(File.separator);
        sb.append(fileName);
        return FileUtil.createSpoolAndFile(sb.toString());
    }

    /**
     * 生成测试项报告路径
     *
     * @param pId      项目ID
     * @param fileName 文件名
     * @return
     */
    public static File generateExperiment(String pId, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("project");
        sb.append(File.separator);
        sb.append(pId);
        sb.append(File.separator);
        sb.append("experiment");
        sb.append(File.separator);
        sb.append(fileName);
        return FileUtil.createSpoolAndFile(sb.toString());
    }

}
