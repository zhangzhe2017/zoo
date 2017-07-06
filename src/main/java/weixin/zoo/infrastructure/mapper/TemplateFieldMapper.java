package weixin.zoo.infrastructure.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import weixin.zoo.infrastructure.model.TemplateField;
import weixin.zoo.infrastructure.model.TemplateFieldExample;

public interface TemplateFieldMapper {
    long countByExample(TemplateFieldExample example);

    int deleteByExample(TemplateFieldExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TemplateField record);

    int insertSelective(TemplateField record);

    List<TemplateField> selectByExample(TemplateFieldExample example);

    TemplateField selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TemplateField record, @Param("example") TemplateFieldExample example);

    int updateByExample(@Param("record") TemplateField record, @Param("example") TemplateFieldExample example);

    int updateByPrimaryKeySelective(TemplateField record);

    int updateByPrimaryKey(TemplateField record);
}