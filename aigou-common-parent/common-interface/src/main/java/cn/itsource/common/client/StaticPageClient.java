package cn.itsource.common.client;

import cn.itsource.basic.util.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "common-server",fallbackFactory = StaticPageFallBackFactory.class)
public interface StaticPageClient {

    @PostMapping("/genStaticPage")
    public AjaxResult genStaticPage(@RequestBody Map<String,Object> map);

}
