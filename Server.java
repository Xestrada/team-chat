import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Server implements Runnable {

    private final Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Stack<Message> messages;
    private static PriorityQueue<Message> clientMessages; 
    
    public Server(Socket s) {
        client = s;
        messages = new Stack();
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void addMessage(Message m) {
        messages.add(m);
    }
    
    @Override
    public void run() {
     
        // Manage this Sockets Inputs
        Thread t = new Thread(new InputManager(Server.clientMessages, in, client));
        t.start();
        
        // Send Messages out to Client
        while(!client.isClosed()) {
            
            try {

                // Send Message to client if available in Stack
                if(!messages.isEmpty()) {
                    Message m = messages.pop();
                    System.out.println("Sending message from: " + m.getUsername());
                    out.writeObject(m);
                    out.flush();
                    out.reset();
                    
                }
                
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            
        }
        
        try {
            // Close Connection and Join InputManager
            client.close();
            t.join();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }   
    }

    public static void main(String args[]) throws Exception {
        
        // Server vars
        Socket sock;
        Thread thread;
        Server tempServer;
        ArrayList<Thread> threads = new ArrayList();
        ArrayList<Server> servers = new ArrayList();
        ServerSocket mainServer = new ServerSocket(1124);

        // Manage Messages from all Servers
        Server.clientMessages = new PriorityQueue();
        MessageManager manager = new MessageManager(Server.clientMessages, servers);
        thread = new Thread(manager);
        thread.start();
        
        // Connection Thread
        while(true) {
            
            // Add new Server to List
            sock = mainServer.accept();
            tempServer = new Server(sock);
            servers.add(tempServer);
            
            // Create Thread and add to List
            thread = new Thread(tempServer);
            threads.add(thread);
            thread.start();

        }


    }

}
