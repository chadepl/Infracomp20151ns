package generadorCarga;

import sebastian.Principal;
import uniandes.gload.core.Task;

public class ClientServerTask extends Task{

	@Override
	public void fail() {
		System.out.println(Task.MENSAJE_FAIL);
	}

	@Override
	public void success() {
		System.out.println(Task.OK_MESSAGE);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Principal principal = new Principal();
	}

}
