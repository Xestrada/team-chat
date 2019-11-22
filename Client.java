import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Client {

    public Client() {

    }

    public static void main(String[] args) throws IOException, InterruptedException {

        InetSocketAddress addr = new InetSocketAddress("localhost", 1234);
        SocketChannel client = SocketChannel.open(addr);

        ArrayList<String> messages = new ArrayList<String>();

        // create a ArrayList with companyName list
        messages.add("Hello!");
        messages.add("This is a test.");
        messages.add("Good bye!");

        for (String m : messages) {

            byte[] message = new String(m).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            client.write(buffer);

            buffer.clear();

            // wait for 2 seconds before sending next message
            Thread.sleep(2000);
        }
        client.close();
    }
}