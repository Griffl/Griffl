package de.griffl.proofofconcept.communication;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;
/**
 * 
 * @author sebastianschneider
 *
 */
public class UserLoggedInEvent implements Event{
	public interface UserLoggedInListener extends Listener{
		@ListenerMethod
		public void userLoggedIn(UserLoggedInEvent event);
	}
}
