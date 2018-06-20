package com.github.paulerpen.datashare.Storageservice;
/**
 * Exception which is to be thrown in the occurrence of an error
 * Also used as parent-class for more specific Exception Use Cases
 * @author paul
 *
 */
public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
