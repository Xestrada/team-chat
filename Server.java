import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Server implements Runnable {

    private boolean hasMessage;
    private final Socket client;
    private Message message;
    private ObjectInputStream in;
    private static PriorityQueue<Message> messages;
    
    // Read Initial Message
    public Server(Socket s) {
        client = s;
        try {
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void setMessage(Message m) {
        message = m;
        hasMessage = true;
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
                    // Read Message into Queue
                }
                

                if(hasMessage) {
                    // Send message to client
                    hasMessage = false;
                }
                
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            
        }
        
    }

    public static void main(String args[]) throws Exception {
        
        Socket sock;
        Thread thread;
        ArrayList<Thread> threads = new ArrayList();
        ServerSocket mainServer = new ServerSocket(1244);
        
        // Connection Thread
        while(true) {
            
            sock = mainServer.accept();
            thread = new Thread(new Server(sock));
            threads.add(thread);
            thread.start();

        }


    }

}
