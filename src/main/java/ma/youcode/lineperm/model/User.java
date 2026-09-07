package ma.youcode.lineperm.service.User;
class User{
// variables
	private login String;
	private passwordHash String;

// constructure
    public __constract(String login, String passwordHash){
        this.login = login;
        this.passwordHash = passwordHash;
    }

//getters
    public String getLogin(){
        return login;
    }
    public String getPasswordHaash(){
        return passwordHash;
    }

//setters
    public void setLogin(String login){
        this.login = login;
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }

// override 
    public String toString(String login, String passwordHash){
        return this.login + ":" + this.passwordHash;
    }
    
}