package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
* ${table.comment!} 前端控制器
* @author ${author}
* @date ${date}
*/

@Controller
@Api(value = "${table.comment!}")
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

}
