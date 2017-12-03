package com.jks.startup.jms.classes;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Created by jithin on 6/19/2017.
 */
public class Receiver {
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;

    public Receiver() {

    }

    public void receiveMessage(String correlationId) throws Exception {
        try {
            factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("SAMPLEQUEUE");

            String filter = "JMSCorrelationID = '" + correlationId  + "'";
            consumer = session.createConsumer(destination, filter);
            Message message = consumer.receive(10000);

            if (message instanceof TextMessage) {
                TextMessage text = (TextMessage) message;
                System.out.println("Message is: " + text.getText());
            }

            System.out.println("correlationId: " + message.getJMSCorrelationID());
            System.out.println("message Id: " + message.getJMSMessageID());
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            consumer.close();
            connection.close();
        }
    }

    public static void main(String[] args) throws  Exception{
        Receiver receiver = new Receiver();
        receiver.receiveMessage("ce90d88e-9987-40cf-bbd7-0ba82d021014");
    }
}