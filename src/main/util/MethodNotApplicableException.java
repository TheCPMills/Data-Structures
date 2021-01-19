package main.util;

public class MethodNotApplicableException extends RuntimeException {
    private static final long serialVersionUID = -9063714253108787401L;

    public MethodNotApplicableException(String methodName, String className) {
        super(methodName + " is not applicable for use in " + className);
    }
}