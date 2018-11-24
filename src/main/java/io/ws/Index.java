package io.ws;

import static io.ws.Imports.FILES_HTML;
import static io.ws.Imports.UPLOAD_HTML;

import java.io.*;
import java.util.Objects;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "res" path)
 */
@Path(Imports.ROOT)
public class Index {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream getHTML() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(UPLOAD_HTML)).getFile());
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path(Imports.FILES)
    @Produces(MediaType.TEXT_HTML)
    public InputStream getData() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(FILES_HTML)).getFile());
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
