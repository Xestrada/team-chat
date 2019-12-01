import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Server {

    private boolean willClose;
    private final Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static PriorityQueue<Message> clientMessages;

    public Server(Socket s) {
        willClose = false;
        client = s;
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean closeServer() {
        try {
            client.close();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public boolean needToClose() {
        return willClose;
    }
    
    public boolean isClosed() {
        return client.isClosed();
    }

    public void push(Message m){
        if(!isClosed()) {
            try {
                out.writeObject(m);
                out.flush();
                out.reset();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void setToClose() {
        willClose = true;
    }
    
    public ObjectInputStream getInputStream() {
        return in;
    }
    
    public Socket getSocket() {
        return client;
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
            
            synchronized (servers) {
                servers.add(tempServer);
            }

            // Create Thread for InputManager and add to List
            thread = new Thread(new InputManager(Server.clientMessages, tempServer));
            threads.add(thread);
            thread.start();

        }


    }

}
