package com.games.job.server.entity.restful;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author:liujh
 * @create_time:2017/2/17 16:53
 * @project:job-center
 * @full_name:com.games.job.server.entity.restful.Base
 * @ide:IntelliJ IDEA
 */
public class Base {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
