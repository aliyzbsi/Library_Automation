package Library.exceptions;

public class GlobalErrorHandler {

    public ErrorResponse handlerException(RuntimeException e){
        return new ErrorResponse(e.getMessage());
    }
    public ErrorResponse handlerException(IllegalArgumentException e){
        return new ErrorResponse(e.getMessage());
    }
    public ErrorResponse handlerException(NumberFormatException e){
        return new ErrorResponse(e.getMessage());
    }
    public ErrorResponse handlerException(Exception e){
        return new ErrorResponse(e.getMessage());
    }

}
