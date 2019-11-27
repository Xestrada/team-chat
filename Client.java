import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Stack;
import java.util.Scanner;

public class Client {

    public Client() {
    }

    public static void main(String[] args) throws Exception {
        Stack<Message> serverMessages = new Stack();

        // Connect to Server
        ObjectOutputStream out;
        ObjectInputStream in;
        Message message = new Message();
        Socket s = new Socket("localhost", 1124);
        Scanner kb = new Scanner(System.in);

        // Send initial login Object
        out = new ObjectOutputStream(s.getOutputStream());
        message.setLogIn(true);

        System.out.print("Enter your username: ");
        String username = kb.nextLine();
        message.setUsername(username);
        out.writeObject(message);
        out.flush();
        out.reset();

        // Setup Input
        in = new ObjectInputStream(s.getInputStream());
        Thread t = new Thread(new ClientInputManager(s, serverMessages, in));
        t.start();

        // FOLLOWING SHOULD BE MOVED TO FRONTEND
        while (!s.isClosed()) {
            if (!serverMessages.isEmpty()) {

                Message m = (Message) serverMessages.pop();
                System.out.println("Received Message From: " + m.getUsername());
            }
        }

    }
}
