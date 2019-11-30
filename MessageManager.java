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
    public  void run() {
        while(true) {
            if(!q.isEmpty()) {
                m = (Message) q.poll();
                servers.forEach(server -> {
                    server.push(m);
                });
            }
        }

    }

}
