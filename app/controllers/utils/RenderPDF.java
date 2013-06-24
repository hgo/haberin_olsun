package controllers.utils;

import java.io.File;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;

import play.Play;
import play.exceptions.UnexpectedException;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.Result;
import play.templates.JavaExtensions;

public class RenderPDF extends Result {

    private static final String ATTACHMENT_DISPOSITION_TYPE = "attachment";

    PDDocument document;
    String name;

    public RenderPDF(PDDocument document, String name) {
        super();
        this.document = document;
        this.name = name;
    }

    @Override
    public void apply(Request request, Response response) {
        try {
            setContentTypeIfNotSet(response, "application/pdf");
            String dispositionType;
            dispositionType = ATTACHMENT_DISPOSITION_TYPE;
            String contentDisposition = "%s; filename=\"%s\"";
            response.setHeader("Content-Disposition", String.format(contentDisposition, dispositionType, JavaExtensions.slugify(name, Boolean.TRUE)+".pdf"));
            File file = new File(Play.tmpDir + File.separator + System.currentTimeMillis());
            document.save(file);
            response.direct = file;
            document.close();
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }

}