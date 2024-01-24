package util.http;

import util.Assert;
import java.io.Serializable;

public interface HttpStatusCode extends Serializable {
    int value();
    boolean is1xxInformational();
    boolean is2xxSuccessful();
    boolean is3xxRedirection();
    boolean is4xxClientError();
    boolean is5xxServerError();

    boolean isError();
    default boolean isSameCodeAs(HttpStatusCode other) {
        return value() == other.value();
    }

    static HttpStatusCode valueOf(int code) {
        Assert.isTrue(code >= 100 && code <= 999,
                () -> "Status code '" + code + "' should be a three-digit positive integer");
        HttpStatus status = HttpStatus.resolve(code);
        if (status != null) {
            return status;
        }
        else {
            return new DefaultHttpStatusCode(code);
        }
    }
}
