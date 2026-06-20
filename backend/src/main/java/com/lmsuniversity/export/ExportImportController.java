package com.lmsuniversity.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path = "/api/export")
@CrossOrigin(origins = "http://localhost:4200")
public class ExportImportController {
	
//    @Autowired
//    ExportImportService exportImportService;
//    
//
//	@RequestMapping(path = "", method = RequestMethod.GET)
//    public void exportToPdf(HttpServletResponse response) throws IOException {
//        this.exportImportService.exportToPdf(response);
//    }
}
