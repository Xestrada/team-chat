import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    public TextField addrField;     //where server address should be
    public TextField inputField;    //where user message is entered
    public TextArea chatField;      //where chat messages are stored
    

    public Controller(){
    }

    /*  send message typed in field
        2 functionality:
            add message to chatField
            send message to server
    */
    public void sendMessage(ActionEvent actionEvent) {
        
        //msg added to chatField
        displayMessage(inputField.getText());
        inputField.setText("");
        
        //Send to server
    }

    //called when "connect" button clicked
    public void connect(ActionEvent actionEvent) {
    }

    //called when "disconnect" button clicked
    public void disconnect(ActionEvent actionEvent) {
    }

    //helper function for formatting message if needed
    public void displayMessage(String msg){
        chatField.appendText(msg + "\n");
    }
}
