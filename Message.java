public class Message implements Comparable<Message> {
    
    private boolean logIn, logOut;
    private String message;
    private String username;
    
    public Message() {
        username = message = "";
        logOut = logIn = false;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String u) {
        username = u;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String m) {
        message = m;
    }
    
    public boolean isLoggingIn() {
        return logIn;
    }
    
    public void setLogIn(boolean li) {
        logIn = li;
    }
    
    public boolean isLoggingOut() {
        return logOut;
    }
    
    public void setLogOut(boolean lo) {
        logOut = lo;
    }
    
    @Override
    public int compareTo(Message m) {
        
        if(logOut && m.isLoggingOut()) {
            return 0;
        }
        
        if(!logOut && m.isLoggingOut()) {
            return 1;
        } else {
            return -1;
        }
    }
    
}
