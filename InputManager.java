import java.io.IOException;
import java.util.PriorityQueue;
import java.io.ObjectInputStream;
import java.net.Socket;

public class InputManager implements Runnable {
    
    private volatile PriorityQueue<Message> q;
    private final ObjectInputStream in;
    private final Socket client;
    
    public InputManager(PriorityQueue<Message> q, ObjectInputStream in, Socket s) {
        this.q = q;
        this.in = in;
        client = s;
    }
    
    @Override
    public void run() {
        
        Object temp;
        
        while(!client.isClosed()) {
            
            try {
                
                temp = in.readObject();
                // Read in Message and add to Queue
                if(temp != null && !client.isClosed()) {
                    q.add((Message)temp);
                }
                
            } catch (IOException | ClassNotFoundException e) {
                
                System.out.println(e.getMessage());
                // Close Socket. Connection Reset
                try {
                    client.close();
                } catch (IOException m) {
                    System.out.println(m.getMessage());
                }
                
            }
            
        }
    
    }   
    
}
