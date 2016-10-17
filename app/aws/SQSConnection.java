package aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.buffered.ReceiveQueueBuffer;
import com.amazonaws.services.sqs.model.*;
import com.typesafe.config.ConfigFactory;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 30/09/2016.
 */
public class SQSConnection {

    public static final String AWS_ACCESS_KEY = "aws.access.key";
    public static final String AWS_SECRET_KEY = "aws.secret.key";
    public static final String AWS_SQS_URL = "aws.sqs.url";
    public static final String VACIO = "Vacio";

    public AmazonSQS amazonSQS;

    public SQSConnection() {
        onStart();
    }

    public void onStart() {
        String accessKey = ConfigFactory.load().getString(AWS_ACCESS_KEY);
        String secretKey = ConfigFactory.load().getString(AWS_SECRET_KEY);

        if ((accessKey != null) && (secretKey != null)) {
            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            amazonSQS = new AmazonSQSClient(awsCredentials);

        }
    }

    public void sendMessage(String message){
        String sqsUrl = ConfigFactory.load().getString(AWS_SQS_URL);
        amazonSQS.sendMessage(sqsUrl, message);
    }

    public String getMessage(){
        String sqsUrl = ConfigFactory.load().getString(AWS_SQS_URL);
        ReceiveMessageResult result = amazonSQS.receiveMessage(sqsUrl);
        List<Message> mensajes = result.getMessages();
        String mensaje = VACIO;
        if(result.getMessages().size() > 0){
            mensaje = result.getMessages().get(0).getBody();
            String receip = mensajes.get(0).getReceiptHandle();
            DeleteMessageRequest request = new DeleteMessageRequest(sqsUrl, receip);
            amazonSQS.deleteMessage(request);
        }
        return mensaje;
    }

    public int queueSize(){
        String sqsUrl = ConfigFactory.load().getString(AWS_SQS_URL);
        List<String> atributos = new ArrayList<>();
        atributos.add("ApproximateNumberOfMessages");
        GetQueueAttributesResult result = amazonSQS.getQueueAttributes(new GetQueueAttributesRequest(sqsUrl, atributos));

        String mensajes = result.getAttributes().get("ApproximateNumberOfMessages");
        return Integer.parseInt(mensajes);
    }

    public void envviar100Mensajes(){
        for(int i = 0; i < 100; i++){
            sendMessage("Hola" + i);
        }
    }
}
