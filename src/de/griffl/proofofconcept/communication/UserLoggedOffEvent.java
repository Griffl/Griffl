package de.griffl.proofofconcept.communication;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;
/**
 * 
 * @author sebastianschneider
 *
 */
public class UserLoggedOffEvent implements Event{
	
	public interface UserLoggedOffListener extends Listener{
		@ListenerMethod
		public void userLoggedOff(UserLoggedOffEvent event);
	}
}
