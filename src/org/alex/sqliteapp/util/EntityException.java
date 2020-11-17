package org.alex.sqliteapp.util;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class EntityException extends Throwable {
    
    private static final Logger LOG = Logger.getLogger(EntityException.class);
    
    private static final long serialVersionUID = 2365196355156732835L;
    private final String errorMessage;    
    
    public EntityException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
        LOG.log(Priority.ERROR, errorMessage);
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
