package com.wisdom.common.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.JSONObject;

public class BundleCMPFiles {

	public static final String STR_UNDERSCORE = "_";
	public static final String STRING_DOT = ".";
	public static final String FILE_EXTENSION_ZIP = "zip";
	public static final String PATH_SEPERATOR = "/";
	public static final String FILE_EXTENSION_CMP = "cmp";
	public static final String OUTPUT_ZIP_FILE = "RapidContentCMP";

	protected String targetCmpBundleLoc = null;
	protected String sourceCmpLoc = null;

	protected Set<String> fileList;
	protected JSONObject config;

	//protected static Logger logger = Logger.getLogger(BundleCMPFiles.class);

	public BundleCMPFiles(JSONObject config) {
		this.sourceCmpLoc = config.getString("m3h.cmp.file.location");
		this.targetCmpBundleLoc = config.getString("m3h.cmptarget.file.location");
		this.fileList = new HashSet<String>();
		this.config = config;
	}

	/*
	 * public static void main(String[] args) { JSONObject config = new
	 * JSONObject(); config.put("m3h.db.url",
	 * "jdbc:mysql://zchen-server.usac.mmm.com:3306/RapidContent");
	 * config.put("m3h.db.user", "developer"); config.put("m3h.db.password",
	 * "emids3mhis"); config.put("m3h.cmp.file.location",
	 * "/Users/newhirelogin/Documents/sourceCmp");
	 * config.put("m3h.cmptarget.file.location",
	 * "/Users/newhirelogin/Documents/targetCmp"); BundleCMPFiles bundleCMPFiles
	 * = new BundleCMPFiles(config); bundleCMPFiles.generateBundle();
	 * System.exit(1); }
	 */

	public void generateBundle() {
		generateCmpFileList(new File(sourceCmpLoc));
		CMPVersionGenerater versionGenerator = constructCMPGenerator(config);
		String latestVersion = versionGenerator.generateLatestVersion();
		String zipFileName = constructNewZipFileName(latestVersion);
		bundleFiles(zipFileName);
		cleanCmpFileList(sourceCmpLoc);
	}

	public CMPVersionGenerater constructCMPGenerator(JSONObject config) {
		return new CMPVersionGenerater();
	}

	protected String constructNewZipFileName(String version) {
		StringBuffer nameBuffer = new StringBuffer();

		nameBuffer.append(targetCmpBundleLoc);
		nameBuffer.append(PATH_SEPERATOR);
		nameBuffer.append(OUTPUT_ZIP_FILE);
		nameBuffer.append(STR_UNDERSCORE);
		nameBuffer.append(version);
		nameBuffer.append(STRING_DOT);
		nameBuffer.append(FILE_EXTENSION_ZIP);

		return nameBuffer.toString();
	}

	/**
	 * Zip it
	 * 
	 * @param zipFilePath
	 *            output ZIP file location
	 */
	public void bundleFiles(String zipFilePath) {

		try {

			ZipOutputStream zipStream = constructZipStream(zipFilePath);

			//logger.info("Output to Zip : " + zipFilePath);

			for (String file : this.fileList) {

				//logger.info("File Added : " + file);
				writeToZipStream(zipStream, file);
			}

			zipStream.closeEntry();
			zipStream.close();

			//logger.info("CMP Bundle creation completed : " + zipFilePath);
		} catch (IOException ex) {
			//logger.error(ex.getMessage());
		}
	}

	void writeToZipStream(ZipOutputStream zipStream, String file) throws FileNotFoundException, IOException {
		
		
		ZipEntry zipEntry = new ZipEntry(file);
		zipStream.putNextEntry(zipEntry);
		InputStream in = new FileInputStream(sourceCmpLoc + File.separator + file);

		int len;
		byte[] buffer = new byte[1024];
		while ((len = in.read(buffer)) > 0) {
			zipStream.write(buffer, 0, len);
		}

		in.close();
	}

	public ZipOutputStream constructZipStream(String zipFilePath) throws FileNotFoundException {
		OutputStream fos = new FileOutputStream(zipFilePath);
		ZipOutputStream zipStream = new ZipOutputStream(fos);
		return zipStream;
	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param file
	 *            file or directory
	 */
	public void generateCmpFileList(File file) {

		// add file only
		if (file.isFile()) {
			File absoluteFile = file.getAbsoluteFile();
			String fileExtension = getFileExtension(absoluteFile.getName());
			//logger.info("fileExtension : " + fileExtension);
			if (FILE_EXTENSION_CMP.equalsIgnoreCase(fileExtension)) {
				fileList.add(absoluteFile.getName());// generateBundleEntry(absoluteFile.toString()));
			}
		}

		if (file.isDirectory()) {
			String[] subNode = file.list();
			for (String filename : subNode) {
				generateCmpFileList(new File(file, filename));
			}
		}

	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param cmpFilesPath
	 *            file or directory
	 */
	public void cleanCmpFileList(String cmpFilesPath) {

		File cmpFileDirectory = new File(cmpFilesPath);
		if (cmpFileDirectory.isDirectory()) {
			String[] fileList2 = cmpFileDirectory.list();
			for (String fileName : fileList2) {
				String fileExtension = getFileExtension(fileName);
				if (FILE_EXTENSION_CMP.equalsIgnoreCase(fileExtension)) {
					File file = new File(cmpFilesPath + PATH_SEPERATOR + fileName);
					//logger.info("deleting file" + file.getName());
					if (file.delete()) {
						//logger.info(file.getName() + " is deleted!");
					} else {
						//logger.info("Delete operation is failed.");
					}
				}
			}
		}
	}

	/**
	 * Format the file path for zip
	 * 
	 * @param filePath
	 *            file path
	 * @return Formatted file path
	 */
	protected String generateBundleEntry(String filePath) {
		return filePath.substring(sourceCmpLoc.length() + 1, filePath.length());
	}

	/**
	 * returns file extension
	 * 
	 * @param file
	 * @return
	 */
	public String getFileExtension(String fileName) {
		try {
			return fileName.substring(fileName.lastIndexOf(STRING_DOT) + 1);
		} catch (Exception e) {
			return "";
		}
	}
}