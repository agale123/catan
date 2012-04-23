import java.util.*;

/** 
 * Stores a request to send to the C back-end, as well
 * as the response from the C
 */
public class Request {
	private String _command;
	private boolean _completed;
	private ArrayList<String> _result; 
	
	/** Initialize a command with the string that should be written to the C */
	public Request(String cmd) {
		_command = cmd;
		_completed = false;
	}
	
	/** Set the request as completed */
	public void setCompleted() {
		synchronized(this) {
			_completed = true;
		}
	}
	
	/** Check whether the request has been completed */
	public boolean isCompleted() {
		synchronized(this) {
			return _completed;
		}
	}
	
	/** Get the request command that will be written to the C */
	public String getRequest() {
		return _command;
	}
	
	/** Update the result which is an arraylist of lines printed by the C */
	public void setResult(ArrayList<String> o) {
		_result = o;
	}
	
	/** Get the result */
	public ArrayList<String> getResult() {
		return _result;
	}
	
	/** Checks whether the command is the shutdown command */
	public boolean close() {
		return _command.equals("shutdown");
	}
}

