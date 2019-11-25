import java.io.IOException;
import java.util.PriorityQueue;
import java.io.ObjectInputStream;

public class InputManager implements Runnable {
    
    private volatile PriorityQueue<Message> q;
    private final ObjectInputStream in;
    
    public InputManager(PriorityQueue<Message> q, ObjectInputStream in) {
        this.q = q;
        this.in = in;
    }
    
    @Override
    public void run() {
        
        Object temp = null;
        
        while(true) {
            
            try {
                temp = in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            
            // Read in Message and add to Queue
            if(temp != null) {
                q.add((Message)temp);
                System.out.println(q.isEmpty());
                
            }
            
        }
    
    }   
    
}
