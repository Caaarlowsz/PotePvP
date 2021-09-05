package br.com.yallandev.potepvp.manager;

import br.com.yallandev.potepvp.evento.RDMAutomatic;

public class EventManager {
	
	private RDMAutomatic rdmAutomatic;
	
	public EventManager() {
		this.rdmAutomatic = null;
	}
	
	public boolean isRunningRDM() {
		return rdmAutomatic != null;
	}
	
	public RDMAutomatic getRdmAutomatic() {
		return rdmAutomatic;
	}
	
	public void setRdmAutomatic(RDMAutomatic rdmAutomatic) {
		this.rdmAutomatic = rdmAutomatic;
	}

}
