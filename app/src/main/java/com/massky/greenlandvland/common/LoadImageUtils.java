package com.massky.greenlandvland.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
    图片的三级缓存
    1.内存缓存：优先加载，速度快
    2.本地缓存：次优先加载，速度稍快
    3.网络加载：最后加载，速度由网络决定，耗流量
 */
public class LoadImageUtils {
//    private static Bitmap bitmap;
//    private static Bitmap alterBitmap;
    private Context context;
//    private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(1024 * 1024 * 3) {
//        @Override
//        protected int sizeOf(String key, Bitmap value) {
//            return value.getRowBytes() * value.getHeight();//此图片的总字节量
//        }
//    };


    //最大内存的八分之一
    long memoryMax = Runtime.getRuntime().maxMemory() / 8;
    private LruCache<String, Bitmap> lruCache=new LruCache<String, Bitmap>((int) memoryMax){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            int byteCount = value.getRowBytes() * value.getHeight();
            return byteCount;//此图片的总字节量
        }
    };


    private ImageLoaderListener listener;

    public interface ImageLoaderListener {
        void imageLoadOK(Bitmap bitmap, String url, RoundedImageView imageView);
    }

    public LoadImageUtils(Context context, ImageLoaderListener listener) {
        this.context = context;
        this.listener = listener;
    }

    //获取图片
    //1.内存缓存-->本地缓存-->网络加载
    public void getBitmap(String url, RoundedImageView imageView) {
        Bitmap bitmap = null;
        if (url == null || url.length() <= 0) {
            return;
        }
        //内存缓存
        bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        //本地缓存
        bitmap = getBitmapFromCacheFile(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        //网络加载
        getBitmapAsync(url,imageView);
    }
    //从内存缓存中获取
    private Bitmap getBitmapFromCache(String url) {
        Log.e("TAG","内存缓存");
        Bitmap bitmap = lruCache.get(url);
        return bitmap;
    }
    //从本地缓存获取
    private Bitmap getBitmapFromCacheFile(String url) {
        Log.e("TAG","本地缓存");
        Bitmap bitmap = null;
        String name = url.substring(url.lastIndexOf("/") + 1);
        File cacheDir = context.getCacheDir();
        File bitFile = null;
        File[] files = cacheDir.listFiles();
        if (files == null) {
            return bitmap;
        }
        for (int i = 0; i < files.length; i++) {
            if (name.equals(files[i].getName())) {
                bitFile = files[i];
                break;
            }
        }
        if (bitFile == null) {
            return bitmap;
        }
        bitmap = BitmapFactory.decodeFile(bitFile.getAbsolutePath());
        return bitmap;
    }
    //异步加载网络获取图片
    private void getBitmapAsync(String url, RoundedImageView imageView) {
        Log.e("TAG","网络获取");
        ImageAysncTask task = new ImageAysncTask(imageView);
        task.execute(url);
    }

    private class ImageAysncTask extends AsyncTask<String, Integer, Bitmap> {
        String path = "";
        RoundedImageView imageView;

        public ImageAysncTask(RoundedImageView imageView) {
            this.imageView=imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            InputStream ips = null;
            path = params[0];
            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                ips = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(ips);
                bitmap= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight()/6);
                saveBitmapToCacheFile(path, bitmap);
                lruCache.put(path, bitmap);
                Log.d("TAG", "doInBackground()");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ips != null) {
                    try {
                        ips.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            listener.imageLoadOK(bitmap, path, imageView);
        }
    }

    private void saveBitmapToCacheFile(String path, Bitmap bitmap) {
        String name = path.substring(path.lastIndexOf("/") + 1);
        File cacheDir = context.getCacheDir();
        File bitFile = new File(cacheDir, name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bitFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


//    public static void roundPic(Context context, ImageView imageView, int drawable){
//        bitmap= BitmapFactory.decodeResource(context.getResources(),drawable);//要剪裁的图片
//        Bitmap backbg= BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);//圆形模板
//        alterBitmap= Bitmap.createBitmap(backbg.getWidth(),backbg.getHeight(),backbg.getConfig());
//        Canvas canvas=new Canvas(alterBitmap);
//        Paint paint=new Paint();
//        paint.setAntiAlias(true);//抗锯齿
//        canvas.drawBitmap(backbg,new Matrix(),paint);
//        //两张图片相交时
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        bitmap= Bitmap.createScaledBitmap(bitmap,backbg.getWidth(),backbg.getHeight(),true);
//        canvas.drawBitmap(bitmap,new Matrix(),paint);
//        //设置图片
//        imageView.setImageBitmap(alterBitmap);
//    }
}
