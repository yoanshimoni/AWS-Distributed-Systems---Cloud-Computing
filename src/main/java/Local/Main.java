package Local;

import com.itextpdf.text.DocumentException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, DocumentException {
//        downLoadHtml("output7cdfddcf-776f-49cb-9e60-ef84d5a9f918-summary");
      /*  String filename = "output7cdfddcf-776f-49cb-9e60-ef84d5a9f918-summary";
        com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document(PageSize.A4);
        PdfWriter.getInstance(pdfDoc, new FileOutputStream("txt.pdf"))
                .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        Font myfont = new Font();
        myfont.setStyle(Font.NORMAL);
        myfont.setSize(11);
        pdfDoc.add(new Paragraph("\n"));
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            Paragraph para = new Paragraph(strLine + "\n", myfont);
            para.setAlignment(Element.ALIGN_JUSTIFIED);
            pdfDoc.add(para);
        }
        pdfDoc.close();
        br.close();
        pdfConverter pdfConverter = new pdfConverter("txt.pdf");
        try {
            pdfConverter.readPDF("ToHTML");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    private static void downLoadHtml(String summary) {
        System.out.println("downLoadHtml");

        String result = TextFileToHTML(summary);

        final File file = new File("output_test");

        try {
            FileUtils.writeStringToFile(file, result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String TextFileToHTML(String summary) {
        StringBuilder result = new StringBuilder();

        try {
            File myObj = new File(summary);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                result.append(line).append("\n");
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while creating HTML file");
            e.printStackTrace();
        }
        return result.toString();
    }
}


