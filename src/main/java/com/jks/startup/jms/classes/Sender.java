package com.jks.startup.jms.classes;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.UUID;

/**
 * Created by jithin on 6/19/2017.
 */
public class Sender {

    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageProducer producer = null;

    public Sender() {

    }

    public void sendMessage() throws Exception {

        try {
            factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("SAMPLEQUEUE");
            producer = session.createProducer(destination);
            //producer.setTimeToLive(10 * 1000);//10 seconds

            String correlationId = UUID.randomUUID().toString();
            TextMessage message = session.createTextMessage();
            message.setJMSCorrelationID(correlationId);
            //message.setJMSExpiration(10 * 1000);
            message.setText("Hello new...This is a sample message..sending from FirstClient");
            producer.send(message , DeliveryMode.PERSISTENT, 4, 10*1000);
            System.out.println("message: " + message.getText());
            System.out.println("correlationId: " + correlationId);
            System.out.println("message Id: " + message.getJMSMessageID());

        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            producer.close();
            connection.close();
        }
    }

    public static void main(String[] args) throws Exception{
        Sender sender = new Sender();
        sender.sendMessage();
    }
}