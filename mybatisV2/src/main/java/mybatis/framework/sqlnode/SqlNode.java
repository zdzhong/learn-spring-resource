package mybatis.framework.sqlnode;

public interface SqlNode {
    void apply(DynamicContext context);
}
