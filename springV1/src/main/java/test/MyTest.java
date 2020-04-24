package test;

import entry.Blog;
import ioc.BeanDefinition;
import org.junit.Before;
import org.junit.Test;
import service.BlogService;

import java.util.HashMap;
import java.util.Map;

public class MyTest {

    private Map<String, Object> singletonObjects = new HashMap<String, Object>();

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<String, BeanDefinition>();

    @Before
    public void init() {
        // 解析xml
    }

    @Test
    public void test() {
        BlogService blogService = (BlogService) getBean("BlogService");
        Blog blog = new Blog();
        blog.setId(101);
        System.out.println(blogService.queryBlog(blog));
    }

    private Object getBean(String beanName) {
        // 先从一级缓存中获取
        Object singletonObject = singletonObjects.get(beanName);
        if (null != singletonObject) {
            return singletonObject;
        }
        // 如果一级缓存中不存在，则进行创建bean实例
        BeanDefinition beanDefinition = beanDefinitions.get(beanName);
        if (null == beanDefinition || null == beanDefinition.getClazzName()) {
            return null;
        }
        // 根据bean信息，判断该bean的类型（单例 or 原型）
        if (beanDefinition.isSingleton()) {
            // 根据beanDefinition创建bean
            singletonObject = creatBean(beanDefinition);
            // 如果是单例bean再创建成功之后需要将其存入一级缓存中

        } else if (beanDefinition.isProtoType()) {
            // 根据beanDefinition创建bean

        } else {

        }
        return singletonObject;
    }

    private Object creatBean(BeanDefinition beanDefinition) {
        return null;
    }


}
