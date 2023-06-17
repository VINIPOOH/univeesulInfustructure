package infrastructure.dal.conection.dao;

public class DBRuntimeException extends RuntimeException {
    public DBRuntimeException(Throwable cause) {
        super(cause);
    }

    public DBRuntimeException() {
    }
}
