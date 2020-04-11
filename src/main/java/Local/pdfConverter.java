package Local;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.PDFText2HTML;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


public class pdfConverter {
    private String path;

    public pdfConverter(String path) {
        this.path = path;
    }

    public pdfConverter() {
    }

    public void readPDF(String TO) throws Exception {
        switch (TO) {
            case "ToImage":
                PDFtoJPG(this.path);
                break;
            case "ToHTML":
                generateHTMLFromPDF(this.path);
                break;
            case "ToText":
                PDFtoText(this.path);
                break;
        }
    }

    private void generateHTMLFromPDF(String pathString) throws IOException {
        File file = new File(pathString);
        PDDocument document = PDDocument.load(file);
        PDFText2HTML stripper = new PDFText2HTML();
        stripper.setStartPage(1);
        stripper.setEndPage(1);
        StringWriter writer = new StringWriter();
        stripper.writeText(document, writer);
        PrintWriter pw = new PrintWriter(pathString.substring(0, pathString.length() - 4) + ".html");
        pw.print(writer.toString());
        pw.close();
        document.close();

    }

    private void PDFtoJPG(String pathString) throws Exception {
        String out = pathString.substring(0, pathString.length() - 4) + ".png";
        PDDocument pd = PDDocument.load(new File(pathString));
        PDFRenderer pr = new PDFRenderer(pd);
        BufferedImage bi = pr.renderImageWithDPI(0, 300);
        ImageIO.write(bi, "png", new File(out));
    }

    private void PDFtoText(String pathString) throws IOException {
        //Loading an existing document
        File file = new File(pathString);
        PDDocument document = PDDocument.load(file);
        //Instantiate PDFTextStripper class
        PDFTextStripper pdfStripper = new PDFTextStripper();
        //Retrieving text from PDF document
        pdfStripper.setStartPage(1);
        pdfStripper.setEndPage(1);
        String text = pdfStripper.getText(document);
        PrintWriter pw = new PrintWriter(pathString.substring(0, pathString.length() - 4) + ".txt");
        pw.print(text);
        pw.close();
        //Closing the document
        document.close();
    }

    public void convert2HTML(String pathString) throws IOException {
        File file = new File(pathString);
        PDDocument document = PDDocument.load(file);
        PDFText2HTML stripper = new PDFText2HTML();
        stripper.setStartPage(1);
        stripper.setEndPage(1);
        StringWriter writer = new StringWriter();
        stripper.writeText(document, writer);
        PrintWriter pw = new PrintWriter(pathString + ".html");
        pw.print(writer.toString());
        pw.close();
        document.close();
    }
}