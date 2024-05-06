package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.DailyEarning;
import com.ecommerce.library.dto.Monthlyearning;
import com.ecommerce.library.dto.OrderDto;
import com.ecommerce.library.dto.WeeklyEarnings;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.DashBoardService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.PdfService;
import com.ecommerce.library.utils.*;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class PdfController {
    private PdfService pdfService;
    private OrderService orderService;
    private OrderRepository orderRepository;
    private DashBoardService dashBoardService;
    private CsvGeneratorDaily csvGeneratorDaily;
    private CsvGeneratorMonthly csvGeneratorMonthly;
    private CsvGeneratorWeekly csvGeneratorWeekly;

@Autowired
    public PdfController(PdfService pdfService,OrderService orderService,
                         OrderRepository orderRepository,
                         DashBoardService dashBoardService,
                         CsvGeneratorDaily csvGeneratorDaily,CsvGeneratorMonthly csvGeneratorMonthly,
                         CsvGeneratorWeekly csvGeneratorWeekly) {
        this.pdfService = pdfService;
        this.orderRepository = orderRepository;
        this.orderService=orderService;
        this.dashBoardService = dashBoardService;
        this.csvGeneratorDaily=csvGeneratorDaily;
        this.csvGeneratorMonthly=csvGeneratorMonthly;
        this.csvGeneratorWeekly=csvGeneratorWeekly;

    }
    @GetMapping("/report")
    public ResponseEntity<InputStreamResource> pdfCreator(){
        ByteArrayInputStream pdf=pdfService.createPdf();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-dispositionn","inline;file=lcwd.pdf");
        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }


    @GetMapping("/pdfReport")
    public void generatePdf(@ModelAttribute("report") OrderDto orderDto, HttpServletResponse response, Principal principal) throws DocumentException, IOException {


        String value=orderDto.getPdfReport();
        if(value.equals("daily")){
            String email = principal.getName();
            response.setContentType("application/pdf");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
            String currentDateTime = dateFormat.format(new Date());
            System.out.println(currentDateTime);
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);
            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            List<DailyEarning> list=orderService.dailyReport(year,month);


            PdfGenerator pdfGenerator=new PdfGenerator();
            pdfGenerator.setOrders(list);
            pdfGenerator.generate(response);

        }
        if(value.equals("weekly")){
            response.setContentType("application/pdf");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
            String currentDateTime = dateFormat.format(new Date());
            System.out.println(currentDateTime);
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);
            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;


            List<WeeklyEarnings> list=orderService.findWeeklyEarnings(2023);


            WeeklyPdfGenerator pdfGenerator=new WeeklyPdfGenerator();
            pdfGenerator.setOrders(list);
            pdfGenerator.generate(response);
        }
        if(value.equals("monthly")){
            String email = principal.getName();
            response.setContentType("application/pdf");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
            String currentDateTime = dateFormat.format(new Date());
            System.out.println(currentDateTime);
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);

            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);



            List<Monthlyearning> monthlyearnings = orderService.getMonthlyReport(year);

            MonthlyReportPdf monthlyReportPdf = new MonthlyReportPdf();
            monthlyReportPdf.setOrders(monthlyearnings);
            monthlyReportPdf.generate(response);
        }
    }

    @GetMapping("/csvReport")
    public ResponseEntity<byte[]> generateCsvFile(@ModelAttribute("report")OrderDto orderDto) {
        String value=orderDto.getCsvReport();
        if(value.equals("daily")){
            List<DailyEarning> dailyEarnings = orderService.dailyReport(2023,11);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "dailyreport.csv");

            byte[] csvBytes = csvGeneratorDaily.generateCsv(dailyEarnings).getBytes();

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        }
        else if(value.equals(("monthly"))){
            List<Monthlyearning> monthlyearnings=orderService.getMonthlyReport(2023);
            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment","monthlyReport.csv");
            byte[] csvBytes=csvGeneratorMonthly.generateCsv(monthlyearnings).getBytes();
            return new ResponseEntity<>(csvBytes,headers,HttpStatus.OK);
        }
        else{
            List<WeeklyEarnings> weeklyEarnings=orderService.findWeeklyEarnings(2023);
            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment","weeklyReport.csv");
            byte[] csvBytes=csvGeneratorWeekly.generateCsv(weeklyEarnings).getBytes();
            return new ResponseEntity<>(csvBytes,headers,HttpStatus.OK);

        }



    }
}
