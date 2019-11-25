import java.util.PriorityQueue;
import java.util.ArrayList;

public class MessageManager implements Runnable {
    
    private volatile PriorityQueue q;
    private final ArrayList<Server> servers;
    private Message m;
    
    public MessageManager(PriorityQueue q, ArrayList<Server> servers) {
        this.q = q;
        this.servers = servers;
    }
    
    @Override
    public void run() {
        
        while(true) {

            if(!q.isEmpty()) {
                // Pop message and add to Queue in all servers
                m = (Message)q.poll();
                servers.forEach(server -> {
                    server.addMessage(m);
                });
   
            }
        }
        
    }
    
}
