package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.pdfbox.TextToPDF;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

import play.mvc.Controller;
import controllers.utils.RenderPDF;

public class Application extends Controller {

    public static void index() throws COSVisitorException, IOException {
        // // Create a new empty document
        // PDDocument document = new PDDocument();
        //
        // // Create a new blank page and add it to the document
        // PDPage blankPage = new PDPage();
        // document.addPage( blankPage );
        // throw new RenderPDF(document, "asli gibidir");
        render();
    }

    public static void test() throws IOException {

        // PDDocument document = new PDDocument();
        // PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
        // document.addPage( page );
        // /* ... */
        // /* code to add some text to the page */
        // /* ... */
        //
        // InputStream in = new FileInputStream(new
        // File("c:/Users/hgo/Desktop/a.gif"));
        // PDJpeg img = new PDJpeg(document, in);
        // PDPageContentStream contentStream= new PDPageContentStream( document,
        // page );
        // contentStream.drawImage(img, 658, 804);
        // PDFont font = PDType1Font.HELVETICA_BOLD;
        // contentStream.beginText();
        // contentStream.setFont( font, 12 );
        // contentStream.moveTextPositionByAmount( 100, 700 );
        // contentStream.drawString( "Hello World" );
        // contentStream.endText();
        // contentStream.close();
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
        document.addPage(page);
        InputStream in = new FileInputStream(new File("c:/Users/hgo/Desktop/gus.jpg"));
        PDJpeg img = new PDJpeg(document, in);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        /* ... */
        /* code to add some text to the page */
        /* ... */

        String title = "cok adl; ,l;dsao ewqkopdas ,ewq,opeop dsamldsamke ewql;e wq";
        contentStream.drawImage(img, (PDPage.PAGE_SIZE_A4.getWidth() / 2) - 61, (PDPage.PAGE_SIZE_A4.getHeight()) - 122 - 30);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int fontSize = 30; // Or whatever font size you want.
        float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.moveTextPositionByAmount((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - titleHeight);
        // contentStream.moveTextPositionByAmount(100, 700);
        contentStream.drawString(title);
        contentStream.endText();
        contentStream.close();
        throw new RenderPDF(document, "guss");
    }
    
    public static void r() throws IOException{
        String dile = "D:\\Development\\play-1.2.5\\framework\\src\\play\\libs\\mime-types.properties";
        PDDocument text = createPDFFromText(new FileReader(new File(dile)));
        
        throw new RenderPDF(text, "text");
    }

    private static int fontSize = 50;
    private static PDSimpleFont font = PDType1Font.HELVETICA;

    private static PDDocument createPDFFromText(Reader text) throws IOException {
        PDDocument doc = null;
        try {

            int margin = 40;
            float height = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000;

            // calculate font height and increase by 5 percent.
            height = height * fontSize * 1.05f;
            doc = new PDDocument();
            BufferedReader data = new BufferedReader(text);
            String nextLine = null;
            PDPage page = new PDPage();
            PDPageContentStream contentStream = null;
            float y = -1;
            float maxStringLength = page.getMediaBox().getWidth() - 2 * margin;
            while ((nextLine = data.readLine()) != null) {

                String[] lineWords = nextLine.trim().split(" ");
                int lineIndex = 0;
                while (lineIndex < lineWords.length) {
                    StringBuffer nextLineToDraw = new StringBuffer();
                    float lengthIfUsingNextWord = 0;
                    do {
                        nextLineToDraw.append(lineWords[lineIndex]);
                        nextLineToDraw.append(" ");
                        lineIndex++;
                        if (lineIndex < lineWords.length) {
                            String lineWithNextWord = nextLineToDraw.toString() + lineWords[lineIndex];
                            lengthIfUsingNextWord = (font.getStringWidth(lineWithNextWord) / 1000) * fontSize;
                        }
                    } while (lineIndex < lineWords.length && lengthIfUsingNextWord < maxStringLength);
                    if (y < margin) {
                        page = new PDPage();
                        doc.addPage(page);
                        if (contentStream != null) {
                            contentStream.endText();
                            contentStream.close();
                        }
                        contentStream = new PDPageContentStream(doc, page);
                        contentStream.setFont(font, fontSize);
                        contentStream.beginText();
                        y = page.getMediaBox().getHeight() - margin + height;
                        contentStream.moveTextPositionByAmount(margin, y);

                    }
                    // System.out.println( "Drawing string at " + x + "," + y );

                    if (contentStream == null) {
                        throw new IOException("Error:Expected non-null content stream.");
                    }
                    contentStream.moveTextPositionByAmount(0, -height);
                    y -= height;
                    contentStream.drawString(nextLineToDraw.toString());
                }

            }
            if (contentStream != null) {
                contentStream.endText();
                contentStream.close();
            }
        } catch (IOException io) {
            if (doc != null) {
                doc.close();
            }
            throw io;
        }
        return doc;
    }
}