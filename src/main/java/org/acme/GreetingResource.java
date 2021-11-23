package org.acme;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class PDFMergeReq {
    public List<String> urls = new ArrayList();
    public String fileName = UUID.randomUUID().toString();
}


@Path("/hello")
public class GreetingResource {

    @POST
    @Produces("application/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hello(PDFMergeReq pdfMergeReq) throws IOException {
        List<InputStream> inputStreamList = new ArrayList<>();

        for (String url : pdfMergeReq.urls) {
            inputStreamList.add(new URL(url).openStream());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PDFMergerUtility pdfMerge = new PDFMergerUtility();
        pdfMerge.setDestinationStream(output);
        pdfMerge.addSources(inputStreamList);
        pdfMerge.mergeDocuments(null);

        Response.ResponseBuilder responseBuilder = Response.ok(output.toByteArray());
        responseBuilder.header("Content-Disposition", "filename=test.pdf");
        return responseBuilder.build();
    }
}