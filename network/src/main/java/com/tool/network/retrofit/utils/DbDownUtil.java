package com.tool.network.retrofit.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tool.network.retrofit.ToolRetrofit;
import com.tool.network.retrofit.download.DaoMaster;
import com.tool.network.retrofit.download.DaoSession;
import com.tool.network.retrofit.download.DownInfo;
import com.tool.network.retrofit.download.DownInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * @作者          吴孝然
 * @创建日期      2019/2/11 10:27
 * @描述          断电续传 数据库工具类-geendao运用
 **/
public class DbDownUtil {
    private static DbDownUtil db;
    private final static String dbName = "tests_db";
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;


    public DbDownUtil() {
        context= ToolRetrofit.getApplication();
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }


    /**
     * 获取单例
     * @return
     */
    public static DbDownUtil getInstance() {
        if (db == null) {
            synchronized (DbDownUtil.class) {
                if (db == null) {
                    db = new DbDownUtil();
                }
            }
        }
        return db;
    }


    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


    public void save(DownInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.insert(info);
    }

    public void update(DownInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.update(info);
    }

    public void deleteDowninfo(DownInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.delete(info);
    }


    public DownInfo queryDownBy(long Id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownInfoDao.Properties.Id.eq(Id));
        List<DownInfo> list = qb.list();
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<DownInfo> queryDownAll() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        return qb.list();
    }
}
