package com.huawei.test19;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by tWX366549 on 2016/7/19.
 */
public class Util {
    public static boolean isDebug = false;
    public static String FILE_PATH = "lgu+";
    private static String[] coms = {"ps | grep 'eth0' | awk '{print $2}'"};

    private static long lastClickTime;

    public static String createEmergencyFileName(String account) {
        String name = account;
        if (account.contains(".")) {
            name = account.replace(".", "_");
        }
        if (name.contains("@")) {
            name = name.replace("@", "__");
        }
        return name;
    }

    public static String getDownloadDirPath() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        else {
            File apkFile = Environment.getDownloadCacheDirectory();
//            File apkFile = new File(Environment.getDownloadCacheDirectory() + FILE_PATH);
//            if (!apkFile.exists())
//                apkFile.mkdirs();
            chmod(apkFile.getAbsolutePath());
            return apkFile.getAbsolutePath();
        }
    }

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 300) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 修改目录或文件的读写权限
     *
     * @param path
     */
    public static void chmod(String path) {
        ShellUtil.runCommand("chmod 777 " + path);
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static void generateInstall(Context context, File file) {
        // 核心是下面几句代码
        chmod(file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    public static String silenceInstall(String apkPath) {
        String result = ShellUtil.runCommand("pm install -r " + apkPath);
//        ProcessBuilder processBuilder = new ProcessBuilder(args);
//        Process process = null;
//        InputStream errIs = null;
//        InputStream inIs = null;
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            int read = -1;
//            process = processBuilder.start();
//            errIs = process.getErrorStream();
//            while ((read = errIs.read()) != -1) {
//                baos.write(read);
//            }
//            baos.write('\n');
//            inIs = process.getInputStream();
//            while ((read = inIs.read()) != -1) {
//                baos.write(read);
//            }
//            byte[] data = baos.toByteArray();
//            result = new String(data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (errIs != null) {
//                    errIs.close();
//                }
//                if (inIs != null) {
//                    inIs.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (process != null) {
//                process.destroy();
//            }
//        }
        return result;
    }

    public static boolean forceStopProcess(String packageName) throws Exception {
        DataOutputStream os = null;
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("am force-stop " + packageName + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);
            String line = "";
            while ((line = reader.readLine()) != null) {
                Log.i("huawei", line);
            }
            return true;
        } catch (Exception e) {
            final String msg = e.getMessage();
            Log.e("test", "runCommand error: " + msg);
            throw new Exception("kill app failed!");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
//                if (process != null) {
//                    process.destroy();
//                }
            } catch (Exception ignored) {
            }
        }
    }

    public static boolean killProcess(int pid) throws Exception {
        DataOutputStream os = null;
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("kill -9 " + pid + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);
            String line = "";
            while ((line = reader.readLine()) != null) {
                Log.i("huawei", line);
            }
            return true;
        } catch (Exception e) {
            final String msg = e.getMessage();
            Log.e("test", "runCommand error: " + msg);
            throw new Exception("kill app failed!");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
//                if (process != null) {
//                    process.destroy();
//                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 将字符串写入文件
     *
     * @param content
     * @return
     */
    public static void writeLog(String content) throws FileNotFoundException {
        Log.e("huawei", "writeLog content:" + content);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(new File(Constant.LOG_PATH), true);
            outStream.write(content.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("huawei", "e = " + e.getMessage());
            throw new FileNotFoundException(e.getMessage());
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将字符串写入文件
     *
     * @param content
     * @param file    SETTINGS.xml
     * @return
     */
    public static boolean write(String content, File file) throws FileNotFoundException {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            outStream.write(content.getBytes("UTF-8"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("huawei", "e = " + e.getMessage());
            throw new FileNotFoundException(e.getMessage());
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFile(InputStream inputStream, File file) {
        DataInputStream dis = null;

        DataOutputStream dos = null;
        byte buffer[] = new byte[2048];
        int len = 0;
        try {
            dis = new DataInputStream(inputStream);
            dos = new DataOutputStream(new FileOutputStream(file));
            while ((len = dis.read(buffer)) != -1) {
                dos.write(buffer, 0, len);
            }
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                    dis = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dos != null) {
                try {
                    dos.close();
                    dos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean write(InputStream inputStream, File originalFile) {
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(originalFile)));
            String line = "";
            while ((line = br.readLine()) != null) {
//                LogUtil.i("davy", "line=" + line);
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    public static InputStream getLocalFile(Context context, int resId) {
        return context.getResources().openRawResource(resId);
    }


    public static ArrayList<String> getAccountName(Context context) throws Exception {
        ArrayList<String> localAccounts = new ArrayList();
        Properties pro = new Properties();
        InputStream is = context.getAssets().open("config");
        pro.load(is);
        Enumeration em = pro.propertyNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            localAccounts.add(name);
        }
        return localAccounts;
    }

    public static void setEmergencyAccounts() {

    }

    @SuppressWarnings("unchecked")
    public static ArrayList<String> getEmergencyAccounts(Context context) throws Exception {
        Properties pro = new Properties();
        InputStream is = context.getAssets().open("config");
        pro.load(is);
        Enumeration<String> enu = (Enumeration<String>) pro.propertyNames();
        ArrayList<String> accountNames = new ArrayList();
        while (enu.hasMoreElements()) {
            accountNames.add(enu.nextElement());
        }
        return accountNames;
    }

    public static void write(String content, FileOutputStream outputStream) {
        FileOutputStream outStream = null;
        try {
            outputStream.write(content.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("huawei", "e = " + e.getMessage());
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void write(InputStream inputStream, FileOutputStream outputStream) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String line = "";
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
