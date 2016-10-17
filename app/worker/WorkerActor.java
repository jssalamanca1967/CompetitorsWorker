package worker;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.stream.scaladsl.Zip;
import aws.S3Connection;
import aws.SQSConnection;
import services.ZipBuilder;

import java.io.File;

/**
 * Created by John on 27/09/2016.
 */
public class WorkerActor extends UntypedActor {

    public static Props props = Props.create(WorkerActor.class);

    @Override
    public void onReceive(Object message) throws Exception {



        SQSConnection sqs = new SQSConnection();
        S3Connection s3 = new S3Connection();

        // Estructura del mensaje: <nombreArchivo>;<keyArchivo>

        String mensaje = sqs.getMessage();

        if(!mensaje.equals(SQSConnection.VACIO)){

            String[] split = mensaje.split(";");
            String nombreArch = split[0];
            String key = split[1];

            File file = s3.downloadFile(key, nombreArch);
            File archivo = new File(file.getAbsolutePath());
            File folder = archivo.getParentFile();

            if(file != null){
                ZipBuilder zip = new ZipBuilder();
                File archZip = zip.sendToZip(file);

                s3.uploadFile(archZip, archZip.getName());

                archZip.delete();
                boolean asd = archivo.delete();
                if(asd)
                    System.out.println("Borró");
                folder.delete();
            }
            else{
                System.out.println("El archivo es null");
            }

        }

    }
}
