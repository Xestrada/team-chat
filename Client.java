import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Stack;
import java.util.Scanner;

public class Client {
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

    public boolean connect(String username) {
        try {
            s = new Socket("localhost", 1124);
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
            t = new Thread(new ClientInputManager(s, serverMessages, in));
            t.start();
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

    public static void main(String[] args) {
        Stack<Message> serverMessages = new Stack();
        Client client = new Client(serverMessages);

        Scanner kb = new Scanner(System.in);

        try {
            System.out.print("Enter username: ");
            String username = kb.nextLine();
            client.connect(username);
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
