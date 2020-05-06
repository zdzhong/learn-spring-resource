package framework.ioc;


import framework.strategy.TypeRegister;

public class TypedStringValue {
    // value属性值
    private String value;
    // value属性值对应的真正类型
    private Class<?> targetType;

    private TypeRegister typeRegister;

    public TypedStringValue(String value) {
        this.value = value;
        this.typeRegister = new TypeRegister();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }

    public TypeRegister getTypeRegister() {
        return typeRegister;
    }
}
