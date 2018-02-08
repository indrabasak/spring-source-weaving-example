package com.basaki.error.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@code DatabaseException} exception is thrown when database encounters an
 * exception while performing an operation.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/28/16
 */
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
