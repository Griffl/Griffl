package de.griffl.proofofconcept.communication;

public interface UserMethods {
	public void showNewUser(User user);
	public void updateLoginStatus(User user, boolean loggedIn);
	public void removeUser(User user);
}
