import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Server implements Runnable {

    private final Socket client;
    private ObjectInputStream in;
    private Stack<Message> messages;
    private static PriorityQueue<Message> clientMessages;
    
    
    public Server(Socket s) {
        client = s;
        try {
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void addMessage(Message m) {
        messages.add(m);
    }
    
    @Override
    public void run() {
        
        while(true) {
            
            try {
                
                /* 
                * Not sure if this is a good way to check if there is
                * something in the ObjectInputStream
                */
                if(in.available() != 0) {
                    // Add Message to Queue
                    Server.clientMessages.add((Message)in.readObject());
                }
                
                // Send Message to client if available in Stack
                if(!messages.isEmpty()) {
                    
                }
                
            } catch (IOException | ClassNotFoundException e ) {
                System.out.println(e.getMessage());
            }
            
        }
        
    }

    public static void main(String args[]) throws Exception {
        
        // Server vars
        Socket sock;
        Thread thread;
        Server tempServer;
        ArrayList<Thread> threads = new ArrayList();
        ArrayList<Server> servers = new ArrayList();
        ServerSocket mainServer = new ServerSocket(1244);

        // Manage Messages from all Servers
        clientMessages = new PriorityQueue();
        MessageManager manager = new MessageManager(clientMessages, servers);
        thread = new Thread(manager);
        thread.start();
        
        // Connection Thread
        while(true) {
            
            // Add new Server to List
            sock = mainServer.accept();
            tempServer = new Server(sock);
            servers.add(tempServer);
            
            // Create Thread and ass to List
            thread = new Thread(tempServer);
            threads.add(thread);
            thread.start();

        }


    }

}
