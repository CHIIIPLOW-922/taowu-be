package com.joji.taowu.category.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joji.taowu.category.mapper.CategoryMapper;
import com.joji.taowu.category.service.CategoryService;
import com.joji.taowu.common.client.ProductClient;
import com.joji.taowu.common.entity.Category;
import com.joji.taowu.common.param.PageParam;
import com.joji.taowu.common.param.PictureParam;
import com.joji.taowu.common.param.ProductCategoryParam;
import com.joji.taowu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 淘物商城商品分类业务实现层
 * */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    ProductClient productClient;



    @Override
    public List<Category> list() {
        List<Category> categories = categoryMapper.selectList(null);

        //最多返回12条数据
        List<Category> list = categories.stream().limit(Math.min(categories.size(), 12)).collect(Collectors.toList());

        log.info("CategoryServiceImpl.list业务结束，结果:{}",list);
        return list;
    }

    @Override
    public Category detail(String categoryName) {
        //参数判断
        if (StringUtils.isEmpty(categoryName)){
            //如果没有默认类型,给一个手机类型
            categoryName = "手机";
        }
        //数据库查询
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name",categoryName);
        Category category = categoryMapper.selectOne(queryWrapper);
        //返回对象
        log.info("CategoryServiceImpl.detail业务结束，结果:{}",category);
        return category;
    }

    @Override
    public List<Integer> names(ProductCategoryParam productCategoryParam) {
        List<Integer> ids = new ArrayList<>();
        //获取类别名称
        List<String> categoryName = productCategoryParam.getCategoryName();
        //判断返回
        if (categoryName == null || categoryName.size() == 0){
            log.info("CategoryServiceImpl.names业务结束，没有类别名称!结果:{}",ids);
            return ids;
        }


        for (String s : categoryName) {
            System.err.println(s);
        }

        //查询数据库
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_name",categoryName);
        queryWrapper.select("category_id");
        List<Object> list = categoryMapper.selectObjs(queryWrapper);

        //结果封装
        Integer[] idsArray = list.toArray(new Integer[]{});
        ids = Arrays.asList(idsArray);

        log.info("CategoryServiceImpl.names业务结束，结果:{}",ids);
        return ids;
    }

    @Override
    public R page(PictureParam pictureParam) {

        List<Category> records = categoryMapper.list(((pictureParam.getCurrentPage()-1)* pictureParam.getPageSize()), pictureParam.getPageSize());
        long total = categoryMapper.selectCount(null);

        R r = R.ok("查询类别数据成功!", records, total);

        log.info("CategoryServiceImpl.page业务结束，结果:{}",r);

        return r;
    }

    @Override
    public R update(Category category) {
        int rows = categoryMapper.updateById(category);

        if (rows > 0){
            return R.ok("类别修改成功!");
        }

        return R.fail("类别修改失败!");
    }

    @Override
    public R remove(Category category) {
        Integer categoryId = category.getCategoryId();
        //调用商品服务,查询类别对应的商品数量
        long count = productClient.count(categoryId);
        //判断数量,如果有引用,不能删除,反之可以删除
        if (count > 0){

            return R.fail("无法删除类别,有:"+count+"件商品引用!");
        }

        int rows = categoryMapper.deleteById(categoryId);

        if (rows == 0){

            return R.fail("删除类别失败!");
        }
        return R.ok("类别删除成功!");
    }

    @Override
    public R save(Category category) {
        int rows = categoryMapper.insert(category);

        if (rows > 0){
            return R.ok("类别保存成功!");
        }

        return R.fail("类别保存失败!");
    }
}
