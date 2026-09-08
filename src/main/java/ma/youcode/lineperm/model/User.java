package ma.youcode.lineperm.model;
public  class User{
// variables
	private String login;
	private String passwordHash ;

// constructure
    public User(String login, String passwordHash){
        this.login = login;
        this.passwordHash = passwordHash;
    }

//getters
    public String getLogin(){
        return login;
    }
    public String getPasswordHash(){
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
    public String toString(){
        return this.login + ":" + this.passwordHash;
    }

}