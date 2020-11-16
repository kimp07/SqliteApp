package org.alex.sqliteapp.util;

public class EntityException extends Throwable {

	private static final long serialVersionUID = 2365196355156732835L;
	private String errorMessage;
	
	public EntityException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	@Override
	public String getMessage() {
		return errorMessage;
	}
	
	
}
