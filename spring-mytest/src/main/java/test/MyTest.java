package test;

import entry.Blog;
import framework.factory.support.DefaultListableBeanFactory;
import framework.reader.XmlBeanDefinitionReader;
import framework.resource.ClasspathResource;
import framework.resource.Resource;
import org.junit.Test;
import service.BlogService;

import java.io.InputStream;

public class MyTest {

    @Test
    public void test() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 读取xml的BeanDefinition阅读器
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = new ClasspathResource("beans.xml");
        InputStream inputStream = resource.getResource();
        reader.loadBeanDefinitions(inputStream);
        BlogService blogService = (BlogService) beanFactory.getBean("blogService");
        Blog blog = new Blog();
        blog.setId(101);
        Blog result = blogService.queryBlog(blog);
        System.out.println(result);
    }

}
