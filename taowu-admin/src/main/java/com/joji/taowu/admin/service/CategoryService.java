package com.joji.taowu.admin.service;

import com.joji.taowu.common.entity.Category;
import com.joji.taowu.common.param.PageParam;
import com.joji.taowu.common.param.PictureParam;

/**
 * 后台管理商品分类业务层
 * */
public interface CategoryService {

    /**
     * 分页数据查询
     * @param pictureParam
     * @return
     */
    Object listPage(PictureParam pictureParam);

    /**
     * 类别数据修改
     * @param category
     * @return
     */
    Object update(Category category);

    /**
     * 移除类别数据
     * @param category
     * @return
     */
    Object remove(Category category);

    /**
     * 类别数据保存
     * @param category
     * @return
     */
    Object save(Category category);
}
