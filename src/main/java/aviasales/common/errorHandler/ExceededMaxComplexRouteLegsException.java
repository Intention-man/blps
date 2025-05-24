package aviasales.common.errorHandler;

public class ExceededMaxComplexRouteLegsException extends RuntimeException {
    public ExceededMaxComplexRouteLegsException() {
        super("Превышено максимальное количество этапов пути в сложном маршруте");
    }

    public ExceededMaxComplexRouteLegsException(String message) {
        super(message);
    }
}