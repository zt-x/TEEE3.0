package com.teee.utils;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class fileUtil {
    /**
     * NIO方式压缩
     */
    public static File fileToZip(String sourceFilePath, String zipFilePath, String fileName) throws IOException {
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        WritableByteChannel writableByteChannel = null;
        File zipFile = new File(zipFilePath + "/" + fileName);
        if (!sourceFile.exists()) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
            return null;
        } else {
            try {
                if (zipFile.exists()) {
                    Files.delete(zipFile.toPath());
                }
                File[] sourceFiles = sourceFile.listFiles();
                if (null == sourceFiles || sourceFiles.length < 1) {
                    System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                    return null;
                } else {
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    writableByteChannel = Channels.newChannel(zos) ;

                    for (File file : sourceFiles) {
                        //创建ZIP实体，并添加进压缩包
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(file);
                        FileChannel fileChannel = fis.getChannel();
                        fileChannel.transferTo(0, file.length() -1,writableByteChannel);
                    }
                }
            } finally {
                //关闭流
                if (null != bis) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (zos != null) {
                    zos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (null != writableByteChannel) {
                    writableByteChannel.close();
                }
            }
        }
        return zipFile;
    }
    public static void delFile(File index){
        if (index.isDirectory()){
            File[] files = index.listFiles();
            for (File in: files) {
                delFile(in);
            }
        }
        index.delete();
        //出现几次删除成功代表有几个文件和文本文件
        System.out.println("删除成功");
    }
}
