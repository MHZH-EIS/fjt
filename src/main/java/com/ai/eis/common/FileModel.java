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
     * 生成样品描述文件路径
     *
     * @param pId      项目id
     * @param fileName 文件名
     * @return
     */
    public static File generateSample(Integer pId, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("project");
        sb.append(File.separator);
        sb.append(pId);
        sb.append(File.separator);
        sb.append("samples");
        sb.append(File.separator);
        sb.append(fileName);
        return FileUtil.createSpoolAndFile(sb.toString());
    }

    /**
     * 获取样品文件
     *
     * @param pId 项目id
     * @return
     */
    public static File getSample(String pId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("project");
        sb.append(File.separator);
        sb.append(pId);
        sb.append(File.separator);
        sb.append("samples");
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
     * @param pId 项目ID
     * @return
     */
    public static File generateMergeExperiment(String pId) {
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
        sb.append("merge.docx");
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

    /**
     * 获取实验项清单模板文件
     *
     * @return
     */
    public static File getExperimentBriefTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("common");
        sb.append(File.separator);
        sb.append("template");
        sb.append(File.separator);
        sb.append("ex_brief.docx");
        return new File(sb.toString());
    }

    /**
     * 获取实验项目清单存放路径
     *
     * @param projectId 项目ID
     * @return
     */
    public static File generateExperimentBrief(String projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("project");
        sb.append(File.separator);
        sb.append(projectId);
        sb.append(File.separator);
        sb.append("experiment");
        sb.append(File.separator);
        sb.append("list");
        sb.append(File.separator);
        sb.append("ex_brief.docx");
        return FileUtil.createSpoolAndFile(sb.toString());
    }


    /**
     * 获取设备清单模板文件
     *
     * @return
     */
    public static File getDevBriefTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("common");
        sb.append(File.separator);
        sb.append("template");
        sb.append(File.separator);
        sb.append("dev_brief.docx");
        return new File(sb.toString());
    }

    /**
     * 获取设备清单的存放路径
     *
     * @param projectId
     * @return
     */
    public static File generateDevBrief(String projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("project");
        sb.append(File.separator);
        sb.append(projectId);
        sb.append(File.separator);
        sb.append("device");
        sb.append(File.separator);
        sb.append("dev_brief.docx");
        return FileUtil.createSpoolAndFile(sb.toString());
    }

    /**
     * 获取封面模板文件
     *
     * @return
     */
    public static File getCoverTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("common");
        sb.append(File.separator);
        sb.append("cover");
        sb.append(File.separator);
        sb.append("cover.docx");
        return new File(sb.toString());
    }

    /**
     * 生成封面文件临时文件
     *
     * @param projectId
     * @return
     */
    public static File generateCoverFile(String projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("project");
        sb.append(File.separator);
        sb.append(projectId);
        sb.append(File.separator);
        sb.append("cover.docx");
        return FileUtil.createSpoolAndFile(sb.toString());
    }

    /**
     * 获取尾页
     *
     * @return
     */
    public static File getEndPage() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("common");
        sb.append(File.separator);
        sb.append("endpage");
        sb.append(File.separator);
        sb.append("endpage.docx");
        return new File(sb.toString());
    }

    public static String getReportName(String projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_PATH);
        sb.append("project");
        sb.append(File.separator);
        sb.append(projectId);
        sb.append(File.separator);
        sb.append("report");
        sb.append(File.separator);
        sb.append("report.docx");
        return sb.toString();
    }

    public static File generateReport(String projectId) {

        return FileUtil.createSpoolAndFile(getReportName(projectId).toString());
    }


}
