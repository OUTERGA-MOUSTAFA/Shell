package ma.youcode.lineperm.model;
class User{
// variables
	private String login;
	private String passwordHash ;

// constructure
    public __construct(String login, String passwordHash){
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