package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    public TextField addrField;     //where server address should be
    public TextField inputField;    //where user message is entered
    public TextArea chatField;      //where chat messages are stored
    public TextField nameField;     //stores users name

    public Button conButton;        //connection button
    public Button disconButton;     //disconnection button

    public Boolean connected;

                                    //client stuff
    public Stack<Message> serverMessages;
    public Client client;
    public Thread thread;

                                    //Timer stuff for clearing inbox
    Timer timer;
    TimerTask task = new TimerTask(){
        public void run(){
            while(!serverMessages.isEmpty()){
                Message msg = serverMessages.pop();
                displayMessage(msg.getUsername(), msg.getMessage());
            }
        }
    };




    public Controller(){
        serverMessages = new Stack();
        client = new Client(serverMessages);
        thread = new Thread(client);
    }

    @FXML
    public void initialize(){
        BooleanBinding booleanBind = addrField.textProperty().isEmpty()
                                        .or(nameField.textProperty().isEmpty());


        conButton.disableProperty().bind(booleanBind);
        timer = new Timer();

        connected = false;

        addrField.setText("localhost");
        nameField.setText("tim");
    }

    /*  send message typed in field
        2 functionality:
            add message to chatField
            send message to server
    */
    public void sendMessage(ActionEvent actionEvent) {
        
        //msg added to chatField
        displayMessage("me", inputField.getText());
        inputField.setText("");
        
        //Send to server
        client.sendMessage(inputField.getText());
    }

    //called when "connect" button clicked
    public void connect(ActionEvent actionEvent) {
            if(client.connect(addrField.getText(), nameField.getText())){
                thread.start();
                timer.scheduleAtFixedRate(task, 2000, 1000);
                displayMessage("SERVER", "Connected");
            }



    }

    //called when "disconnect" button clicked
    public void disconnect(ActionEvent actionEvent) {
        if(client.disconnect()){
            displayMessage("SERVER", "Disconnected");
        }
    }

    //helper function for formatting message if needed
    private void displayMessage(String name, String msg){
        chatField.appendText(name + ": " + msg + "\n");
    }



}


