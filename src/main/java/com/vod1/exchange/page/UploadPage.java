package com.vod1.exchange.page;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.vod1.exchange.service.VideoService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ivan
 */
@Singleton
public class UploadPage extends AbstractPage {

  private static Logger log = LoggerFactory.getLogger(UploadPage.class); //NOPMD
  // location to store file uploaded
  private static final String UPLOAD_DIRECTORY = "upload";
  // upload settings
  private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
  private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
  private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

  @Inject
  private VideoService videoService;

  @Override
  protected String getTemplateFile(HttpServletRequest req) {
    return "velocity/pages/upload.vm";
  }

  //handle file upload here
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // checks if the request actually contains upload file
    if (!ServletFileUpload.isMultipartContent(req)) {
      // if not, we stop here
      PrintWriter writer = resp.getWriter();
      writer.println("Error: Form must has enctype=multipart/form-data.");
      writer.flush();
      return;
    }
    doFileUpload(req);

    // redirects client to list page
    resp.sendRedirect(req.getContextPath());
  }

  private void doFileUpload(HttpServletRequest req) {
    ServletFileUpload upload = prepareFileUpload();
    String uploadPath = prepareUploadPath();

    try {
      // parses the request's content to extract file data
      @SuppressWarnings("unchecked")
      List<FileItem> formItems = upload.parseRequest(req);

      if (formItems != null && formItems.size() > 0) {
        // iterates over form's fields
        for (FileItem item : formItems) {
          // processes only fields that are not form fields
          if (!item.isFormField()) {
            saveUploadFile(uploadPath, item);
          }
        }
      }
    } catch (Exception ex) {
      req.setAttribute("message",
                       "There was an error: " + ex.getMessage());
    }
  }

  private void saveUploadFile(String uploadPath, FileItem item) throws Exception {
    String fileName = new File(item.getName()).getName();
    String filePath = uploadPath + File.separator + fileName;
    File storeFile = new File(filePath);

    // saves the file on disk
    item.write(storeFile);
    log.info("save to upload file {}", storeFile.getAbsolutePath());
    videoService.saveVideo(storeFile);
    //clean up app server's upload dir after saved
    storeFile.delete();
  }

  private String prepareUploadPath() {
    // constructs the directory path to store upload file
    // this path is relative to application's directory
    String uploadPath = getServletContext().getRealPath("")
                        + File.separator + UPLOAD_DIRECTORY;

    // creates the directory if it does not exist
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
      uploadDir.mkdir();
    }
    return uploadPath;
  }

  private ServletFileUpload prepareFileUpload() {
    // configures upload settings
    DiskFileItemFactory factory = new DiskFileItemFactory();
    // sets memory threshold - beyond which files are stored in disk
    factory.setSizeThreshold(MEMORY_THRESHOLD);
    // sets temporary location to store files
    factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

    ServletFileUpload upload = new ServletFileUpload(factory);

    // sets maximum size of upload file
    upload.setFileSizeMax(MAX_FILE_SIZE);

    // sets maximum size of request (include file + form data)
    upload.setSizeMax(MAX_REQUEST_SIZE);
    return upload;
  }
}
