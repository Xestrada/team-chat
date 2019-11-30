import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Server implements Runnable {

    private final Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static PriorityQueue<Message> clientMessages;

    public Server(Socket s) {
        client = s;
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void push(Message m){
        System.out.println("Sending message from: " + m.getUsername());
        try {
            out.writeObject(m);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   @Override
    public void run() {
        Thread t = new Thread(new InputManager(Server.clientMessages, in, client));
        t.start();
        while(!client.isClosed()){

        }
        try{
            client.close();
            t.join();
        }
        catch (IOException | InterruptedException e){
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
