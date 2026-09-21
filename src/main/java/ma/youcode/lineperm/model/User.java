package ma.youcode.lineperm.model;
public  class User{
// variables
private int id;
	private final String login;// kayn ghir wahed login l wahed user t instansa
	private  String passwordHash ;

// constructure
    public User(String login, String passwordHash){
        this(0, login, passwordHash);
    }
    public User(int id, String login, String passwordHash) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
    }

//getters

    public int getId() { return id; }
    public String getLogin(){return login;}
    public String getPasswordHash(){return passwordHash;}

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