package tina.com.common.downloadback.config;

/**
 * @author: 小民
 * @date: 2017-06-05
 * @time: 18:49
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明:  在使用网络请求时，是否显示进度条
 */

public enum NetDialogConfig{
    //不显示进度条
    UN_LOADING,
    //显示进度条 -> 点击空白出可以取消 -> 放心年轻人  取消自动取消队列
    NORMAL_LOADING,
    //显示进度条 -> 点击空白处 不可取消
    FORBID_LOADING;
}