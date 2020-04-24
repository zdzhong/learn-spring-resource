package service.impl;

import dao.BlogDao;
import entry.Blog;
import service.BlogService;

public class BlogServiceImpl implements BlogService {

    private BlogDao blogDao;

    @Override
    public Blog queryBlog(Blog blog) {
        return blogDao.queryBlog(blog);
    }
}
