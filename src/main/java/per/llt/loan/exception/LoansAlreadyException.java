package per.llt.loan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoansAlreadyException extends RuntimeException {

    public LoansAlreadyException(String message) {
        super(message);
    }
}
