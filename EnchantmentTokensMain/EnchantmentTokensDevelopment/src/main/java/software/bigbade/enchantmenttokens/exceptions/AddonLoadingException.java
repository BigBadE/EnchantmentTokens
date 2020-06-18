package software.bigbade.enchantmenttokens.exceptions;

public class AddonLoadingException extends RuntimeException {
    public AddonLoadingException(String error, Exception parent) {
        super(error, parent);
    }
}
