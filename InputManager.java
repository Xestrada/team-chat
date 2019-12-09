import java.io.IOException;
import java.util.PriorityQueue;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class InputManager implements Runnable {
    
    private DateTimeFormatter format;
    private volatile PriorityQueue<Message> q;
    private final Server client;
    
    public InputManager(PriorityQueue<Message> q, Server client) {
        this.q = q;
        this.client = client;
        format = DateTimeFormatter.ofPattern("HH:mm");
    }
    
    @Override
    public void run() {
        
        Object temp;
        
        while(!client.isClosed()) {
            try {
                
                temp = client.getInputStream().readObject();
                // Read in Message and add to Queue
                if(temp != null) {
                    Message m = (Message)temp;
                    // Prepare to close Socket if logging out
                    if(m.isLoggingOut()) {
                        client.setToClose();
                    }
                    m.setMessage(m.getMessage() + " " + format.format(LocalTime.now()));
                    q.add(m);
                }
                
            } catch (IOException | ClassNotFoundException e) {
                
                System.out.println(e.getMessage());
                // Close Socket. Connection Reset
                client.setToClose();
                client.closeServer();
                
            }
            
        }
    
    }   
    
}
