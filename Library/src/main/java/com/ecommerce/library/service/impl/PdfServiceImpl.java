package com.ecommerce.library.service.impl;

import com.ecommerce.library.service.PdfService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfServiceImpl implements PdfService {
    private org.slf4j.Logger logger= LoggerFactory.getLogger(PdfServiceImpl.class);


    @Override
    public ByteArrayInputStream createPdf() {
        logger.info("Crate Pdf Started");
        String tittle="PDF heading";
        String content="pdf content";

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();
        PdfWriter.getInstance(document,out);

        document.open();

        Font tittleFont= FontFactory.getFont(FontFactory.HELVETICA_BOLD,25);
        Paragraph tittlePara=new Paragraph(tittle,tittleFont);
        tittlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(tittlePara);

        Font paraFont=FontFactory.getFont(FontFactory.HELVETICA,18);
        Paragraph paragraph=new Paragraph(content,paraFont);
        document.add(paragraph);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
