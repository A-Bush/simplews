package io.ws;

import static io.ws.Config.UPLOAD_HTML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "res" path)
 */
@Path(Config.ROOT)
public class Resources {

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
}
