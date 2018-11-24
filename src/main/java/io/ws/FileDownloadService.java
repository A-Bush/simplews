package io.ws;

import com.google.gson.Gson;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path(Config.IMAGES)
public class FileDownloadService {
    // array of supported extensions
    static final String[] EXTENSIONS = new String[]{"jpg", "jpeg", "gif", "png", "bmp"};

    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    @GET
    @Produces("text/json")
    public Response getFolderImages(@QueryParam("lastknown") String lastknown) {
        //Gets the contents of the folder (reverse order : more recent first)
        //see http://stackoverflow.com/questions/11300847/load-and-display-all-the-images-from-a-folder
        File dir = new File(Config.UPLOAD_FOLDER);
        File[] files = dir.listFiles(IMAGE_FILTER);
        assert files != null;
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.compare(f2.lastModified(), f1.lastModified());
            }
        });
        //Fills a list (from the more recent one, until the last known file)
        ArrayList<String> newfiles = new ArrayList<String>();
        for (File f : files) {
            if (lastknown != null && f.getName().equals(lastknown))
                break;
            newfiles.add(f.getName());
        }
        //Answers the list as a JSON array (using google-gson, but could be done manually here)
        return Response.status(Status.OK).entity(new Gson().toJson(newfiles)).type("text/json").build();
    }

    @GET
    @Path("/{image}")
    @Produces("image/*")
    public Response getImage(@PathParam("image") String image) {
        File f = new File(Config.UPLOAD_FOLDER + image);
        if (!f.exists()) {
            throw new WebApplicationException(404);
        }
        String mt = new MimetypesFileTypeMap().getContentType(f);
        return Response.ok(f, mt).build();
    }

}
