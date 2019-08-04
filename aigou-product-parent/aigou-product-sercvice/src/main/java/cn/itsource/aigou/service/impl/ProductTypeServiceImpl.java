package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.ProductType;
import cn.itsource.aigou.mapper.ProductTypeMapper;
import cn.itsource.aigou.service.IProductTypeService;
import cn.itsource.basic.util.AjaxResult;
import cn.itsource.common.client.RedisClient;
import cn.itsource.common.client.StaticPageClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品目录 服务实现类
 * </p>
 *
 * @author solargen
 * @since 2019-07-30
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private StaticPageClient staticPageClient;

    @Override
    public void getHomePage() {
        //第一步先生成 product.type.vm.html
        Map<String ,Object> map = new HashMap<>();
        String templatePath = "E:\\IdeaWorkSpace\\aigou-parent\\aigou-product-parent\\aigou-product-sercvice\\src\\main\\resources\\template\\product.type.vm";
        String targetPath = "E:\\IdeaWorkSpace\\aigou-parent\\aigou-product-parent\\aigou-product-sercvice\\src\\main\\resources\\template\\product.type.vm.html";
        List<ProductType> productTypes = loop();
        map.put("model",productTypes);
        map.put("templatePath",templatePath);
        map.put("targetPath",targetPath);
        staticPageClient.genStaticPage(map);

        //第二步 ： 生成home.html
        templatePath = "E:\\IdeaWorkSpace\\aigou-parent\\aigou-product-parent\\aigou-product-sercvice\\src\\main\\resources\\template\\home.vm";
        targetPath = "E:\\IdeaWorkSpace\\aigou-web-parent\\aigou-web-home\\home.html";

        Map<String,String> model = new HashMap<>();
        model.put("staticRoot","E:\\IdeaWorkSpace\\aigou-parent\\aigou-product-parent\\aigou-product-sercvice\\src\\main\\resources\\");
        map.put("model",model);
        map.put("templatePath",templatePath);
        map.put("targetPath",targetPath);
        staticPageClient.genStaticPage(map);
    }

    @Override
    public List<ProductType> loadTypeTree() {
        AjaxResult result = redisClient.get("productTypes");
        String restObj = (String) result.getRestObj();
        List<ProductType> productTypes = JSON.parseArray(restObj, ProductType.class);
        if (productTypes==null || productTypes.size()<= 0){
            productTypes = loop();
            redisClient.set("productTypes",JSON.toJSONString(productTypes));
        }
        return productTypes;
    }



    private List<ProductType> loop() {
        List<ProductType> productTypes = baseMapper.selectList(null);
        //定义一个List存放一级菜单
        List<ProductType> list = new ArrayList<>();
        //定义一个Map存放所有的ProductType，key是id，value是类型对象
        Map<Long,ProductType> map = new HashMap<>();
        for (ProductType pt : productTypes) {
            map.put(pt.getId(),pt);
        }
        //循环
        for (ProductType productType : productTypes) {
            if(productType.getPid()==0){
                list.add(productType);
            }else{
                ProductType parent = map.get(productType.getPid());
                List<ProductType> children = parent.getChildren();
                if(children==null){
                    children = new ArrayList<>();
                }
                children.add(productType);
                parent.setChildren(children);
            }
        }
        return list;
    }




    //递归查询多级类别
    public List<ProductType> recursive(long id){
        /*List<ProductType> pid = baseMapper.selectList(new QueryWrapper<ProductType>().eq("pid", id));
        for (ProductType productType : pid) {
            List<ProductType> recursive = recursive(productType.getId());
            if (!recursive.isEmpty()){
                productType.setChildren(recursive);
            }
        }
        return pid;*/
        //查询所有一级菜单
        List<ProductType> parents = baseMapper.selectList(new QueryWrapper<ProductType>().eq("pid", id));
        for (ProductType parent : parents) {
            //取到所有的子
            List<ProductType> children = recursive(parent.getId());
            if(!children.isEmpty()){
                parent.setChildren(children);
            }
        }
        return parents;
    }






}
