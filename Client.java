import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client {

    public Client() {}

    public static void main(String[] args) throws Exception {
        
        // Connect to Server
        ObjectOutputStream out;
        ObjectInputStream in;
        Message message = new Message();
        Socket s = new Socket("localhost", 1124);
        
        // Send initial login Object
        out = new ObjectOutputStream(s.getOutputStream());
        message.setLogIn(true);
        message.setUsername("User");
        out.writeObject(message);
        out.flush();
        out.reset();
        
        // Setup Input
        in = new ObjectInputStream(s.getInputStream());
        
        while(true) {
            
            Object temp = in.readObject();
            if(temp != null) {

                Message m = (Message)temp;
                System.out.println(m.getUsername());
                
            }
        }
        
        
    }
}
