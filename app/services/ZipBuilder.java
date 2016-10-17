package services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by John on 30/09/2016.
 */
public class ZipBuilder {

    public final static String FOLDER = "./public/data/";

    public File sendToZip(File file) throws IOException {

        System.out.println(file.getName());

        File f = new File(FOLDER + file.getName() + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
        ZipEntry e = new ZipEntry(file.getName());
        out.putNextEntry(e);


        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buf)) > 0) {
            out.write(buf, 0, bytesRead);
        }

        out.closeEntry();
        out.close();

        return f;
    }
}
