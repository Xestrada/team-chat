
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Stack;
import java.util.Scanner;

public class Client implements Runnable {
    private volatile Stack<Message> serverMessages;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket s;
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
            e.printStackTrace();
        }
        return false;
    }

    public boolean disconnect() {
        try {
            sendLogoutMessage();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean close() {
        try {
            s.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
                    e.printStackTrace();
                    try {
                        s.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}