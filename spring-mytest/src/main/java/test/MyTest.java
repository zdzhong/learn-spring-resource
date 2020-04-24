package test;

import entry.Blog;
import org.junit.Test;
import service.BlogService;
import service.impl.BlogServiceImpl;

public class MyTest {

    @Test
    public void test() {
        BlogService blogService = new BlogServiceImpl();
        Blog blog = new Blog();
        blog.setId(101);
        Blog result = blogService.queryBlog(blog);
        System.out.println(result);
    }

}
