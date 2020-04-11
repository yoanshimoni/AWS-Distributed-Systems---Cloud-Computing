package Local;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.*;

public class ManagerListner implements MessageListener {

    private ObjectMapper mapper;
    private String outputFileName;

    public ManagerListner(String outputFileName) {
        this.mapper = new ObjectMapper();
        this.outputFileName = outputFileName;
    }

    @Override
    public void onMessage(Message message) {
        DoneTask doneTask = parseMsg(message);
        S3BucketOps s3BucketOps = new S3BucketOps(doneTask.getBucket(), doneTask.getKey());
        s3BucketOps.downloadSummaryFile();
        /*pdfConverter pdfConverter = new pdfConverter(doneTask.getKey());
        try {
            pdfConverter.SummaryFileToHTML(doneTask.getKey(),this.outputFileName);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }*/
        try {
            this.html(doneTask.getKey(), this.outputFileName);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        try {
            message.acknowledge();
        } catch (JMSException ex) {
            System.err.println("Caught an exception while acknowledging message");
        }
    }

    private DoneTask parseMsg(Message msg) {
        DoneTask doneTask = null;
        try {
            String message = ((TextMessage) msg).getText();
            doneTask = mapper.readValue(message, DoneTask.class);
        } catch (IOException | JMSException ex) {
            System.err.println("Caught an exception while parsing message");
            ex.printStackTrace();
            System.exit(1);
        }
        return doneTask;
    }

    private void html(String SummryFileName, String outputName) throws IOException, DocumentException {
        com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document(PageSize.A4);
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(outputName))
                .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        Font myfont = new Font();
        myfont.setStyle(Font.NORMAL);
        myfont.setSize(11);
        pdfDoc.add(new Paragraph("\n"));
        BufferedReader br = new BufferedReader(new FileReader(SummryFileName));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            Paragraph para = new Paragraph(strLine + "\n", myfont);
            para.setAlignment(Element.ALIGN_JUSTIFIED);
            pdfDoc.add(para);
        }
        pdfDoc.close();
        br.close();
        pdfConverter pdfConverter = new pdfConverter(outputName);
        try {
            pdfConverter.readPDF("ToHTML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
