package strategy;

public class IntegerHandler implements TypeHandler {
    @Override
    public Object handler(String value) {
        return Integer.parseInt(value);
    }
}
