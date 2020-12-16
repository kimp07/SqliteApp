package org.alex.sqliteapp.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class EntityThrowable extends Throwable {

    private static final Logger LOG = Logger.getLogger(EntityThrowable.class);

    private static final long serialVersionUID = 2365196355156732835L;
    private final String errorMessage;

    public EntityThrowable(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
        LOG.log(Level.ERROR, errorMessage);
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
