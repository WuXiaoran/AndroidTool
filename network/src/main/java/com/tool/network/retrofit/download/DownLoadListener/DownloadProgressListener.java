package com.tool.network.retrofit.download.DownLoadListener;


/**
 * @作者          吴孝然
 * @创建日期      2019/2/11 10:02
 * @描述          成功回调处理
 **/
public interface DownloadProgressListener {
    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}
