package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.DataGrid;
import com.ai.eis.common.FileModel;
import com.ai.eis.common.Reflection;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisCreateReport;
import com.ai.eis.model.EisDevice;
import com.ai.eis.modeler.AbstractModeler;
import com.ai.eis.service.EisCreateReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

 

@Controller
@RequestMapping(value = "/report-tool/project")
public class ReportToolController {
	private Logger logger = LoggerFactory.getLogger(ReportToolController.class);

	@Autowired
	private EisCreateReportService eisCreateReportService;
	private static final String modeler = "com.ai.eis.modeler.InverterModeler";

	@RequestMapping
	public void index() {

	}
	@RequestMapping("/progform")
	public void progfrom() {
		
	}

	@RequestMapping("/form")
	public void form(Integer id, Model model) {
		if (id != null) {
			ObjectMapper mapper = new ObjectMapper();
			EisCreateReport resource = eisCreateReportService.queryById(id);

			try {
				model.addAttribute("resource", mapper.writeValueAsString(resource));
			} catch (JsonProcessingException e) {
				logger.error("json转换错误", e);
			}
		}
	}

	@RequestMapping(value = "/downloadquery")
    @ResponseBody
    public AjaxResult downloadQuery(@RequestParam(value = "projectNo", defaultValue = "") Integer projectNo) {
		EisCreateReport report =  eisCreateReportService.queryById(projectNo);
		if (StringUtils.isEmpty(report.getReportFilePath())) {
			return new AjaxResult(false).setMessage("项目报告文件还未生成，请检查！");
		}
		File downloadFile = new File(report.getReportFilePath());
		if (!downloadFile.exists()) {
			return new AjaxResult(false).setMessage("项目报告文件还未生成，请检查！");
		}
		
		return new AjaxResult(true);
	}
	
	
	@RequestMapping(value = "/download")
    @ResponseBody
	public AjaxResult downloadFile(HttpServletRequest request, HttpServletResponse response,
		   
			@RequestParam(value = "projectNo", defaultValue = "") Integer projectNo) {

		try {
 
			EisCreateReport report =  eisCreateReportService.queryById(projectNo);
			if (StringUtils.isEmpty(report.getReportFilePath())) {
				return new AjaxResult(false).setMessage("项目报告文件还未生成，请检查！");
			}
			
			File downloadFile = new File(report.getReportFilePath());
		
			if (!downloadFile.exists() ) {
				return new AjaxResult(false).setMessage("项目报告文件还未生成，请检查！");
			}	else if (downloadFile.isDirectory()) {
				return new AjaxResult(false).setMessage("项目报告文件路径为目录，请检查！");
			}
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(downloadFile.getName(), "UTF-8"));
			FileInputStream in = new FileInputStream(report.getReportFilePath());
			OutputStream out = response.getOutputStream();
			byte buffer[] = new byte[1024];
			int len = 0;

			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			in.close();
			out.close();

		} catch (Exception e) {
			logger.error("下载失败:{}", e.getMessage());
			e.printStackTrace();
			return new AjaxResult(false).setMessage(e.getMessage());
		}

		return new AjaxResult(true);

	}
	
	private void deleteFile(String filePath) {
		if (!StringUtils.isEmpty(filePath)) {
			File one = new File(filePath);
			if (one.exists()) {
				one.delete();
			}
		}
	}
	
	@Transactional
	@RequestMapping(value = "/save")
	@ResponseBody
	public AjaxResult insertDevice(@Valid EisCreateReport report, BindingResult br) throws ClassNotFoundException {
		if (br.hasErrors()) {
			logger.error("对象校验失败：" + br.getAllErrors());
			return new AjaxResult(false).setData(br.getAllErrors());
		} else {
			Class<?> clazz = Class.forName(modeler);
			AbstractModeler modelerInstance = (AbstractModeler)Reflection.newInstance(clazz);
			try {

				MultipartFile qzonefile = report.getQzoneFile();
				if (!qzonefile.isEmpty()) {
					File file = FileModel.generateReport(report.getReportNo(), qzonefile.getOriginalFilename());
					qzonefile.transferTo(file);
					report.setQzoneFilePath(file.getAbsolutePath());
					logger.info("Q-0文件上传成功，地址为{}", file.getAbsolutePath());
				}

				MultipartFile qminusfile = report.getQminusMaxFile();
				if (!qzonefile.isEmpty()) {
					File file = FileModel.generateReport(report.getReportNo(), qminusfile.getOriginalFilename());
					qminusfile.transferTo(file);
					report.setQminusMaxFilePath(file.getAbsolutePath());
					logger.info("-QMax文件上传成功，地址为{}", file.getAbsolutePath());
				}

				MultipartFile qmaxfile = report.getQplusMaxFile();
				if (!qmaxfile.isEmpty()) {
					File file = FileModel.generateReport(report.getReportNo(), qmaxfile.getOriginalFilename());
					qmaxfile.transferTo(file);
					report.setQplusMaxFilePath(file.getAbsolutePath());
					
					logger.info("+QMax文件上传成功，地址为{}", file.getAbsolutePath());
				}

				report.setSubmitTime(new Date());
				Map<String,String> param = new HashMap<>();
				param.put("zero", report.getQzoneFilePath());
				param.put("max", report.getQplusMaxFilePath());
				param.put("min", report.getQminusMaxFilePath());
				param.put("ratePower", String.valueOf(report.getRatePower()));
				param.put("projectNo",  report.getReportNo() );
				param.put("trfNo", report.getTrfNo());
				
				File reportFile = modelerInstance.process(param);
				if (reportFile != null) {
					report.setReportFilePath(reportFile.getAbsolutePath());
				}
				
				if (report.getProjectNo() != null) {
					EisCreateReport old = eisCreateReportService.queryById(report.getProjectNo());
					deleteFile(old.getQminusMaxFilePath());
					deleteFile(old.getQplusMaxFilePath());
					deleteFile(old.getQzoneFilePath());
					deleteFile(old.getReportFilePath());
					
					eisCreateReportService.update(report);
					logger.info("更新了一个项目{}", report.getProjectName());
				} else {
					eisCreateReportService.insert(report);
					logger.info("添加了一个新项目{}", report.getProjectName());
				}
				return new AjaxResult(true);
			} catch (Exception e) {
				return new AjaxResult(false).setMessage(e.getMessage());
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public DataGrid<EisCreateReport> list(Integer page, Integer rows,
			@RequestParam(value = "projectNo", defaultValue = "") String projectNo,
			@RequestParam(value = "projectName", defaultValue = "") String projectName,
			@RequestParam(value = "reportNo", defaultValue = "") String reportNo) {
		com.github.pagehelper.Page<Object> pg = PageHelper.startPage(page, rows);

		List<EisCreateReport> lst = listAll(projectNo, projectName, reportNo);
		DataGrid<EisCreateReport> dg = new DataGrid<EisCreateReport>(lst);
		dg.setTotal(pg.getTotal());

		return dg;
	}

	@ResponseBody
	@RequestMapping(value = "/listAll")
	private List<EisCreateReport> listAll(@RequestParam(value = "projectNo", defaultValue = "") String projectNo,
			@RequestParam(value = "projectName", defaultValue = "") String projectName,
			@RequestParam(value = "reportNo", defaultValue = "") String reportNo) {
		Map<String, String> map = new HashMap<>();
		map.put("projectNo", Tools.liker(projectNo));
		map.put("projectName", Tools.liker(projectName));
		map.put("reportNo", Tools.liker(reportNo));
		return eisCreateReportService.queryByCondition(map);
	}

	@ResponseBody
	@RequestMapping(value = "/remove")
	public AjaxResult remove(@RequestParam(value = "projectNo") Integer projectNo) {
		try {
			EisCreateReport report = eisCreateReportService.queryById(projectNo);
			if (report == null) {
				return new AjaxResult(true);
			}
			if (!StringUtils.isEmpty(report.getQzoneFilePath())) {
				File qzonefile = new File(report.getQzoneFilePath());
				qzonefile.delete();
			}

			if (!StringUtils.isEmpty(report.getQminusMaxFilePath())) {
				File qminusFile = new File(report.getQminusMaxFilePath());
				qminusFile.delete();
			}

			if (!StringUtils.isEmpty(report.getQplusMaxFilePath())) {
				File qplusfile = new File(report.getQplusMaxFilePath());
				qplusfile.delete();
			}

			if (!StringUtils.isEmpty(report.getReportFilePath())) {
				File reportFile = new File(report.getReportFilePath());
				reportFile.delete();
			}

			eisCreateReportService.deleteByPrimaryKey(projectNo);
			return new AjaxResult(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new AjaxResult(false).setData(e);
		}
	}

}
