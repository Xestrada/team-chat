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
    public  void run() {
        while(true) {
            if(!q.isEmpty()) {
                
                m = (Message) q.poll();
                System.out.println("Sending message from: " + m.getUsername());
                servers.forEach((Server server) -> {
     
                    server.push(m);
                    
                    // Close Server if server needs to close
                    if(server.needToClose()) {
                        if(!server.isClosed())
                            server.closeServer();
                    }
                });
                
                // Remove all closed Servers
                synchronized (servers) {
                    servers.removeIf(server -> (server.isClosed()));
                }
                
            }
        }

    }

}
