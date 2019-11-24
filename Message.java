public class Message implements Comparable<Message> {
    
    private boolean logIn, logOut;
    private String message;
    private String username;
    
    public String getUsername() {
        return username;
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean isLoggingIn() {
        return logIn;
    }
    
    public boolean isLoggingOut() {
        return logOut;
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
