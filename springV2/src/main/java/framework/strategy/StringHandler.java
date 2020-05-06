package framework.strategy;

public class StringHandler implements TypeHandler {
    @Override
    public Object handler(String value) {
        return value;
    }
}
