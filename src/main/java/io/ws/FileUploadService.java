package io.ws;

import static io.ws.Imports.*;

import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path(Imports.UPLOAD)
public class FileUploadService {

    public FileUploadService() {}

    @Context
    private UriInfo context;

    /**
     * Returns text response to caller containing current time-stamp
     * @return error response in case of missing parameters an internal exception or
     * success response if file has been stored successfully
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam(FILE) InputStream uploadedInputStream,
            @FormDataParam(FILE) FormDataContentDisposition fileDetail) {

        // check if all form parameters are provided
        if (uploadedInputStream == null || fileDetail == null)
            return Response.status(400).entity(INVALID_FORM_DATA).build();

        // create our destination folder, if it not exists
        try {
            createFolderIfNotExists(UPLOAD_FOLDER);
        } catch (SecurityException se) {
            return Response.status(500).entity(CAN_NOT_CREATE_DESTINATION_FOLDER_ON_SERVER).build();
        }

        String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
        try {
            saveToFile(uploadedInputStream, uploadedFileLocation);
        } catch (IOException e) {
            return Response.status(500).entity(CAN_NOT_SAVE_FILE).build();
        }

        return Response.status(200).entity(FILE_SAVED_TO + uploadedFileLocation).build();
    }

    /**
     * Utility method to save InputStream data to target location/file
     * @param inStream - InputStream to be saved
     * @param target - full path to destination file
     */
    private void saveToFile(InputStream inStream, String target) throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];

        out = new FileOutputStream(new File(target));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }

    /**
     * Creates a folder to desired location if it not already exists
     * @param dirName - full path to the folder
     * @throws SecurityException - in case you don't have permission to create the folder
     */
    private void createFolderIfNotExists(String dirName) throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }
}