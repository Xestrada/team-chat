import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Client {

    public Client() {

    }

    public static void main(String[] args) throws IOException, InterruptedException {

        //Username and message sent as one string seperated by newLine
        String userName = "username";
        String write  = "";
        String full = userName+"\n"+write;

        //open connection
        InetSocketAddress addr = new InetSocketAddress("localhost", 1244);
        SocketChannel client = SocketChannel.open(addr);

        //send username to server
        while(client.isOpen()) {
                byte[] message = full.getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(message);
                client.write(buffer);
                buffer.clear();

            // Read in new user from server
            ByteBuffer buf = ByteBuffer.allocate(140); // Should be the size of the message Object we willbe
            client.read(buf);
            String line = new String(buf.array()).trim();
            System.out.print(line);
            // wait for 2 seconds before sending next message
            Thread.sleep(2000);
            client.close();
        }
    }
}
