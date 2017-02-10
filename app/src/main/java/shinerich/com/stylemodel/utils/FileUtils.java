package shinerich.com.stylemodel.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File工具类
 *
 * @author hunk
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 保存文件
     */
    public static boolean saveFile(byte[] data, String dirPath, String filename) {
        BufferedOutputStream bos = null;
        try {
            File file = createDir(dirPath);
            bos = new BufferedOutputStream(new FileOutputStream(new File(file,
                    filename)));
            bos.write(data, 0, data.length);
            bos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 获取文件
     */
    public static byte[] readFile(String filePath) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                baos = new ByteArrayOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[1024 * 8];
                int c = 0;
                while ((c = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
                }
                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 检查文件
     */
    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /*
     * 删除文件
     */
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        }
    }

    /*
     * 创建目录
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /*
     * 删除该目录
     */
    public static void deleteDir(File dir) {
        deleteDir(dir, true);
    }

    /*
     * 删除该目录
     */
    public static void deleteDir(File dir, boolean isMe) {

        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            // 删除所有文件
            if (file.isFile()) {

                file.delete();
            } else if (file.isDirectory()) {
                // 递规的方式删除文件夹
                deleteDir(file);
            }
        }
        if (isMe) {
            // 删除目录本身
            dir.delete();
        }
    }


    /*
     * 获取目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    // 递归调用继续统计
                    dirSize += file.length();
                    dirSize += getDirSize(file);
                }
            }
        }

        return dirSize;
    }


    /*
     * 获取目录文件个数
     */
    public long getFileCount(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            count = files.length;
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归
                    count = count + getFileCount(file);
                    count--;
                }
            }
        }

        return count;
    }
}
