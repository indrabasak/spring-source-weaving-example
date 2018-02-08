package com.basaki.error.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@code DataNotFoundException} exception is thrown when no item is found
 * during databsase look up.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/28/16
 */
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }
}
