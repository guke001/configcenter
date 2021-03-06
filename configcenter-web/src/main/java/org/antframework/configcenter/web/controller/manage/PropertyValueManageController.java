/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 11:38 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.order.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.FindAppProfilePropertyValueOrder;
import org.antframework.configcenter.facade.order.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.order.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.FindAppProfilePropertyValueResult;
import org.antframework.configcenter.facade.result.QueryPropertyValueResult;
import org.antframework.manager.web.common.ManagerAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性value管理controller
 */
@RestController
@RequestMapping("/manage/propertyValue")
public class PropertyValueManageController {
    @Autowired
    private PropertyValueService propertyValueService;

    /**
     * 设置多个属性value
     *
     * @param appCode     应用编码（必须）
     * @param profileCode 环境编码（必须）
     * @param keys        一个或多个key（必须）
     * @param values      与keys数量对应的value（必须）
     */
    @RequestMapping("/setPropertyValue")
    public EmptyResult setPropertyValues(String appCode, String profileCode, String[] keys, String[] values) {
        if (keys.length != values.length) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "属性key和value数量不相等");
        }
        ManagerAssert.adminOrHaveRelation(appCode);
        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppCode(appCode);
        order.setProfileCode(profileCode);
        for (int i = 0; i < keys.length; i++) {
            SetPropertyValuesOrder.KeyValue keyValue = new SetPropertyValuesOrder.KeyValue();
            keyValue.setKey(keys[i]);
            keyValue.setValue(values[i]);
            order.addKeyValue(keyValue);
        }

        return propertyValueService.setPropertyValues(order);
    }

    /**
     * 删除属性value
     *
     * @param appCode     应用编码（必须）
     * @param key         key（必须）
     * @param profileCode 环境编码（必须）
     */
    @RequestMapping("/deletePropertyValue")
    public EmptyResult deletePropertyValue(String appCode, String key, String profileCode) {
        ManagerAssert.adminOrHaveRelation(appCode);
        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setAppCode(appCode);
        order.setKey(key);
        order.setProfileCode(profileCode);

        return propertyValueService.deletePropertyValue(order);
    }

    /**
     * 查找应用在指定环境的所有属性value
     *
     * @param appCode     应用编码（必须）
     * @param profileCode 环境编码（必须）
     */
    @RequestMapping("/findAppProfilePropertyValue")
    public FindAppProfilePropertyValueResult findAppProfilePropertyValue(String appCode, String profileCode) {
        if (!StringUtils.equals(appCode, ConfigService.COMMON_APP_CODE)) {
            ManagerAssert.adminOrHaveRelation(appCode);
        }
        FindAppProfilePropertyValueOrder order = new FindAppProfilePropertyValueOrder();
        order.setAppCode(appCode);
        order.setProfileCode(profileCode);

        return propertyValueService.findAppProfilePropertyValue(order);
    }

    /**
     * 分页查询属性value
     *
     * @param pageNo      页码（必须）
     * @param pageSize    每页大小（必须）
     * @param appCode     应用编码（可选，有值会进行模糊查询）
     * @param key         key（可选，有值会进行模糊查询）
     * @param profileCode 环境编码（可选，有值会进行模糊查询）
     */
    @RequestMapping("/queryPropertyValue")
    public QueryPropertyValueResult queryPropertyValue(int pageNo, int pageSize, String appCode, String key, String profileCode) {
        ManagerAssert.admin();
        QueryPropertyValueOrder order = new QueryPropertyValueOrder();
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setAppCode(appCode);
        order.setKey(key);
        order.setProfileCode(profileCode);

        return propertyValueService.queryPropertyValue(order);
    }
}
