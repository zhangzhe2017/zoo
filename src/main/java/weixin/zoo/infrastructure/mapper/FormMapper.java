package weixin.zoo.infrastructure.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import weixin.zoo.infrastructure.model.Form;
import weixin.zoo.infrastructure.model.FormExample;

public interface FormMapper {
    int countByExample(FormExample example);

    int deleteByExample(FormExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Form record);

    int insertSelective(Form record);

    List<Form> selectByExample(FormExample example);

    Form selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Form record, @Param("example") FormExample example);

    int updateByExample(@Param("record") Form record, @Param("example") FormExample example);

    int updateByPrimaryKeySelective(Form record);

    int updateByPrimaryKey(Form record);
}