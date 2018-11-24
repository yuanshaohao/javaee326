package cn.itcast.core.service.brand;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;


@Service
public class BrandServiceImpl implements BrandService {

    /*不用dubbo有关系的resources,可以提高性能*/
    /*
     * 好处:1.jdk提供注解效率高
     *     2.降低耦合度
     * */
    @Resource
    private BrandDao brandDao;


    /**
     * 查询所有品牌,不分页
     *
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }


    /**
     * 品牌的分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //自己实现分页,需要计算出起始行,   项目:通过分页助手去实现
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);
        //查询   强转一下
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);
        //将数据封装到PageResult中
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }


    /**
     * 数据回显
     * @param id
     * @return
     */
    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }


    /**
     * 品牌列表的条件查询
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return
     */

    @Override
    public PageResult search(Integer pageNum, Integer pageSize, Brand brand) {
        // 设置分页条件
        PageHelper.startPage(pageNum, pageSize);
        // 设置查询条件
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        // 封装品牌的名称
        if(brand.getName() != null && !"".equals(brand.getName().trim())){
            // 条件封装：拼接sql语句，没有%
            criteria.andNameLike("%"+brand.getName().trim()+"%");
        }
        if(brand.getFirstChar() != null && !"".equals(brand.getFirstChar().trim())){
            // 条件封装：拼接sql语句
            criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
        }
        // 根据id降序
        brandQuery.setOrderByClause("id desc");
        // 根据条件查询
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 品牌保存
     * @param brand
     */
    @Transactional
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }


    /**
     * 品牌更新
     * @param brand
     */

    @Override
    public void update(Brand brand) {
        //精准修改,能够判断条件是否为空
        brandDao.updateByPrimaryKeySelective(brand);
    }


    /**品牌批量删除
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        //判断ids是否为空
        if (ids!=null&& ids.length>0) {
            //遍历数组
            for (Long id : ids) {
                //逐个删除
                brandDao.deleteByPrimaryKey(id);
            }
        }
    }


    public void del(Long[] ids){
        if (ids!=null&& ids.length>0){
            //批量删除,需要自定义方法,并在dao.xml文件中写sql语句
            brandDao.deleteByPrimaryKeys(ids);
        }

    }


}
