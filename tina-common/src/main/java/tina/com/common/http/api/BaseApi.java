package tina.com.common.http.api;

import tina.com.common.http.net.RxHttpUtils;

/**
 * @author yxc
 * @date 2018/7/19
 */
public class BaseApi<T> {

    protected T service;

    protected T getService(Class<T> serviceclass) {
        service = RxHttpUtils.createApi(serviceclass);
        return service;
    }


}
