import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    public TextField addrField; // where server address should be
    public TextField inputField; // where user message is entered
    public TextArea chatField; // where chat messages are stored
    public TextField nameField; // stores users name

    public Button conButton; // connection button
    public Button disconButton; // disconnection button
    public Button sendButton;

    // client stuff
    public Stack<Message> serverMessages;
    public Client client;
    public Thread thread;

    // Timer stuff for clearing inbox
    Timer timer;
    TimerTask task = new TimerTask() {
        public void run() {
            while (!serverMessages.isEmpty()) {
                Message msg = serverMessages.pop();
                if (!(msg.isLoggingIn() || msg.isLoggingOut())) {
                    displayMessage(msg.getUsername(), msg.getMessage());
                } else if (msg.isLoggingIn()) {
                    displayMessage("SERVER", msg.getUsername() + " connected");
                } else {
                    displayMessage("SERVER", msg.getUsername() + " disconnected");
                }
            }
        }
    };

    public Controller() {
        serverMessages = new Stack();
        client = new Client(serverMessages);
        thread = new Thread(client);
    }

    @FXML
    public void initialize() {
        timer = new Timer();

        addrField.setText("localhost");
        nameField.setText("username");
        disconButton.setDisable(true);
        chatField.setDisable(true);
        inputField.setDisable(true);
        sendButton.setDisable(true);
    }

    /*
     * send message typed in field 2 functionality: add message to chatField send
     * message to server
     */
    public void sendMessage(ActionEvent actionEvent) {
        // Send to server
        client.sendMessage(inputField.getText());
        inputField.setText("");
    }

    // called when "connect" button clicked
    public void connect(ActionEvent actionEvent) {
        if (!nameField.getText().isEmpty() && !addrField.getText().isEmpty() &&
                client.connect(addrField.getText(), nameField.getText())) {
            thread.start();
            timer.scheduleAtFixedRate(task, 2000, 1000);
            conButton.setDisable(true);
            addrField.setDisable(true);
            nameField.setDisable(true);
            chatField.setDisable(false);
            inputField.setDisable(false);
            disconButton.setDisable(false);
            sendButton.setDisable(false);
            // displayMessage("SERVER", "Connected");
        }
    }

    // called when "disconnect" button clicked
    public void disconnect(ActionEvent actionEvent) {
        if (client.disconnect()) {
            // displayMessage("SERVER", "Disconnected");
            System.exit(0);
        }
    }

    // helper function for formatting message if needed
    private void displayMessage(String name, String msg) {
        chatField.appendText(name + ": " + msg + "\n");
    }

}