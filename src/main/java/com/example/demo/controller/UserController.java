package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.service.UserService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

@Controller
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView model = new ModelAndView();

		model.setViewName("home");
		return model;
	}

	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
		JasperPrint jasperPrint = null;

		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"users.pdf\""));
		// Para que el browser me reconozca el pdf para descargarlo
		OutputStream out = response.getOutputStream();
		jasperPrint = userService.exportFile();
		// Se pasa el jasper llenado y el stream para q sea reconocible por el browser
		JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	}

	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public void exportExcel(ModelAndView model, HttpServletResponse response)
			throws IOException, JRException, SQLException {
		JasperPrint jasperPrint = null;

//		response.setContentType("application/x-download");
//		response.setHeader("Content-Disposition", String.format("attachment; filename=\"users.xlsx\""));
		// Para que el browser me reconozca el pdf para descargarlo
		OutputStream out = response.getOutputStream();
		jasperPrint = userService.exportFile();
		// Se pasa el jasper llenado y el stream para q sea reconocible por el browser
		// JasperExportManager.exportReport(jasperPrint, out);

		JRXlsxExporter xlsxExporter = new JRXlsxExporter();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		xlsxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		xlsxExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "users.xlsx");

		// uncomment this codes if u are want to use servlet output stream
		// xlsxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
		// servletOutputStream);

		xlsxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);

		xlsxExporter.exportReport();

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

		// uncomment this codes if u are want to use servlet output stream
		// servletOutputStream.write(os.toByteArray());

		response.getOutputStream().write(os.toByteArray());
		response.getOutputStream().flush();
		response.getOutputStream().close();
		response.flushBuffer();

	}
//
//		JRXlsxExporter exporter = new JRXlsxExporter();
//		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("users.xlsx"));
//
//		
//		try {
//            exporter.exportReport();
//            out.flush();
//        } catch (JRException ex) {
//            ex.printStackTrace();
//        }		
//        
//		
//		response.setContentType("application/vnd.ms-excel");
//	    response.setHeader("Content-disposition", "attachment; filename=users.xls");
//	    ServletOutputStream out = response.getOutputStream();
//	    JRXlsExporter exporter = new JRXlsExporter();
//	    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//	    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
//	    exporter.exportReport();
//	    out.flush();

}