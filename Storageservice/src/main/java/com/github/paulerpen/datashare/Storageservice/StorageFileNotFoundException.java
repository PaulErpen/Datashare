package com.github.paulerpen.datashare.Storageservice;

/**
 * Exception to be thrown in the case of a file not being found by the
 * StorageService
 * @author paul
 *
 */
public class StorageFileNotFoundException extends StorageException {

	private static final long serialVersionUID = 1L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
