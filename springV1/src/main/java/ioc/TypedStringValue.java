package ioc;

public class TypedStringValue {
    // value属性值
    private String value;
    // value属性值对应的真正类型
    private Class<?> targetType;

    public TypedStringValue(String value, Class<?> targetType) {
        this.value = value;
        this.targetType = targetType;
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
}
