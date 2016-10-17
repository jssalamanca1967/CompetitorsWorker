package aws;

import akka.stream.javadsl.Zip;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.typesafe.config.ConfigFactory;
import play.Logger;

import scala.collection.JavaConversions.*;
import scala.sys.process.ProcessBuilderImpl;
import services.ZipBuilder;

import java.io.*;

/**
 * Created by John on 05/09/2016.
 */
public class S3Connection  {

    public static final String AWS_S3_BUCKET = "aws.s3.bucket";
    public static final String AWS_ACCESS_KEY = "aws.access.key";
    public static final String AWS_SECRET_KEY = "aws.secret.key";

    public AmazonS3 amazonS3;

    public String s3Bucket;

    public S3Connection() {
        onStart();
    }

    public void onStart() {
        String accessKey = ConfigFactory.load().getString(AWS_ACCESS_KEY);
        String secretKey = ConfigFactory.load().getString(AWS_SECRET_KEY);
        s3Bucket = ConfigFactory.load().getString(AWS_S3_BUCKET);

        if ((accessKey != null) && (secretKey != null)) {
            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            amazonS3 = new AmazonS3Client(awsCredentials);
            if(!amazonS3.doesBucketExist(s3Bucket))
                amazonS3.createBucket(s3Bucket);

        }
    }

    public String uploadFile(File file, String fileName){

        PutObjectRequest putObjectRequest = new PutObjectRequest(s3Bucket, fileName, file);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
        String rta = putObjectRequest.getKey();
        amazonS3.putObject(putObjectRequest); // upload file

        return rta;
    }

    public File downloadFile(String key, String fileName) throws IOException {
        S3Object file = amazonS3.getObject( new GetObjectRequest(s3Bucket, key));
        InputStream is = file.getObjectContent();
        File folder = new File(ZipBuilder.FOLDER + fileName.split("/")[0]);
        folder.mkdir();
        File arch = new File(ZipBuilder.FOLDER + fileName);
        arch.createNewFile();
        try {
            FileOutputStream out = new FileOutputStream(arch);
            IOUtils.copy(is, out);
            out.close();
            is.close();
            file.close();
            return arch;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }


}