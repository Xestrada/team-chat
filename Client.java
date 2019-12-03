import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Stack;
import java.util.Scanner;

public class Client implements Runnable {
    private volatile Stack<Message> serverMessages;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket s;
    private Thread t;
    private String username;

    public Client() {
    }

    public Client(Stack<Message> serverMessages) {
        this.serverMessages = serverMessages;
    }

    public boolean connect(String address, String username) {
        try {
            s = new Socket(address, 1124);
            this.username = username;
            init();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean disconnect() {
        try {
            sendLogoutMessage();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean close() {
        try {
            s.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void init() {
        try {
            // Setup Output
            out = new ObjectOutputStream(s.getOutputStream());
            // Setup Input
            in = new ObjectInputStream(s.getInputStream());
            // send login message to server
            sendLoginMessage();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Message buildMessage(String s) {
        Message message = new Message();
        message.setUsername(username);
        message.setMessage(s);
        return message;
    }

    // called to send general messages
    public void sendMessage(String s) {
        try {
            Message message = buildMessage(s);
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // called once in init()
    public void sendLoginMessage() {
        Message message = new Message();
        message.setUsername(username);
        message.setLogIn(true);
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // will be called during logout process
    public void sendLogoutMessage() {
        Message message = new Message();
        message.setUsername(username);
        message.setLogOut(true);
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        // Stack<Message> serverMessages = new Stack();
        // Client client = new Client(serverMessages);

        try {
            Object temp = null;

            while (!s.isClosed()) {

                try {
                    temp = in.readObject();
                    // Read in Message and add to Queue
                    if (temp != null) {
                        serverMessages.push((Message) temp);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                    try {
                        s.close();
                    } catch (IOException exception) {
                        System.out.println(exception.getMessage());
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Stack<Message> serverMessages = new Stack();
        Client client = new Client(serverMessages);
        Scanner kb = new Scanner(System.in);

        Thread thread = new Thread(client);
        try {
            System.out.print("Enter username: ");
            String username = kb.nextLine();
            client.connect("localhost", username);
            thread.start();
            System.out.print("> ");
            String m = kb.nextLine();
            client.sendMessage(m);
            client.sendLogoutMessage();
            // // FOLLOWING SHOULD BE MOVED TO FRONTEND
            // while (!s.isClosed()) {
            // if (!serverMessages.isEmpty()) {

            // Message m = (Message) serverMessages.pop();
            // System.out.println("Received Message From: " + m.getUsername());
            // }
            // }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
