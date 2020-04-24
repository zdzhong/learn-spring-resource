package strategy;

import java.util.HashMap;
import java.util.Map;

public class TypeRegister {

    private Map<Class<?>, TypeHandler> typeHandlers = new HashMap<>();

    public TypeRegister() {
        init();
    }

    private void init() {
        typeHandlers.put(Integer.class, new IntegerHandler());
        typeHandlers.put(String.class, new StringHandler());
    }

    public TypeHandler getHandler(Class<?> clazz) {
        return typeHandlers.get(clazz);
    }
}
