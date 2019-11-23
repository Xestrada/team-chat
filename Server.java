import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

public class Server {

    public Server() {

    }

    public static void main(String args[]) throws Exception {

        // Setup Server Non-Blocking
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(1244));
        server.configureBlocking(false);

        // Create Selector based on Server
        Selector selector = Selector.open();
        server.register(selector, server.validOps());

        // Store SocketChannels
        ArrayList<SocketChannel> clients = new ArrayList<>();

        while (true) {

            // Select keys that are ready
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();

            // Iterate Through keys
            while (it.hasNext()) {
                SelectionKey key = it.next();

                // Accept a new Client and set key to read
                if (key.isAcceptable()) {
                    SocketChannel sock = server.accept();
                    sock.configureBlocking(false);
                    sock.register(selector, SelectionKey.OP_READ);
                    clients.add(sock);

                } else if (key.isReadable()) {

                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buf = ByteBuffer.allocate(140); // Should be the size of the message Object we willbe
                    // Read in userName and message
                    client.read(buf);
                    String line = new String(buf.array()).trim();

                    //Server side display of connected members
                    String[] split = line.split("\n");
                    String user = split[0];
                    System.out.println(user+" has joined the server...");
                    String newUser = split[0]+" has joined the server...";

                    //send new user update to everyone
                    byte[] message = newUser.getBytes();
                    ByteBuffer buffer = ByteBuffer.wrap(message);
                    client.write(buffer);
                    buffer.clear();
                    client.close();


            }
            it.remove();

        }
    }

}}
