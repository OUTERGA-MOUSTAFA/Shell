package ma.youcode.lineperm.service;
package ma.youcode.lineperm.model.User;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
class UserService{
	public static void main(string [] args){

		private final Map<String, User> users = new HashMap<>();
		private final Path storagePath = Paths.get("reasources/users.txt");
    	//private User currentUser = null;

		public void saveUser(){
			StringBuilder data = new StringBuilder();
			data.append(users.toString()).append(System.lineSeparator()/**or jut ":" */);

			Files.writeString(storagePath, data.toString(), null);
		}

	}
}