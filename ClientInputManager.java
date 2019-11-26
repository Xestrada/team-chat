import java.io.IOException;
import java.util.Stack;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientInputManager implements Runnable {

    private final Socket s;
    private volatile Stack<Message> q;
    private final ObjectInputStream in;

    public ClientInputManager(Socket s, Stack<Message> q, ObjectInputStream in) {
        this.s = s;
        this.q = q;
        this.in = in;
    }

    @Override
    public void run() {

        Object temp = null;

        while (!s.isClosed()) {

            try {
                temp = in.readObject();
                // Read in Message and add to Queue
                if (temp != null) {
                    q.push((Message) temp);
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

    }

}
