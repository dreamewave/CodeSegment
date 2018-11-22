import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import com.zteits.atms.util.file.FileOperater;
import flex.messaging.util.URLEncoder;

public class DownLoadImgeUtil
{

    /**
     * 生成文件路径
     * @return
     * @throws Exception
     */
    public static String getPath() throws Exception
    {

        int len = DownLoadImgeUtil.class.getResource("").getPath().indexOf("WEB-INF");
        String filepath = DownLoadImgeUtil.class.getResource("").getPath().substring(0, len) + "picTemp";

        FileOperater fo = new FileOperater();
        fo.newFolder(filepath);
        return filepath;
    }

    /**
     * 下载图片
     * @param fileimgpath 文件名称
     * @param imageList 图片路径
     * @return  是否下载成功
     * @throws Exception
     */
    public static int downLoadImge(String fileimgpath, List imageList) throws Exception
    {
        String filerootpath = getPath();
        System.out.println("filerootpath: " + filerootpath);
        String filepath = filerootpath + "\\" + fileimgpath;
        System.out.println("filepath: " + filepath);
        OutputStream os = new FileOutputStream(filepath + ".zip");
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(os));
        HttpClient hc = new HttpClient();
        GetMethod getMethod = null;
        String filename1;
        try
        {
            for(int i = 0; i < imageList.size(); i++)
            {
                System.out.println("======== " + i);
                try
                {
                    String fullURl = (String)imageList.get(i);
                    //现场发布的图片路径，支持中文
                    String strurl = tranformStyle(fullURl.substring(fullURl.lastIndexOf("$http")+1));
                    getMethod = new GetMethod(strurl);
                    int randomNum = (int) (Math.random()*9000+1000);
                    String path;
                    path = fullURl.substring((fullURl.lastIndexOf(".")+1),fullURl.length());
                    // 重命名
                    filename1 = fullURl.substring(0, (fullURl.lastIndexOf("$http")+1))+String.valueOf(randomNum)+"."+path;
                    hc.executeMethod(getMethod);

                    out.putNextEntry(new ZipEntry(filename1));
                    out.write(getMethod.getResponseBody());
//                    out.setEncoding("gbk");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            // 释放连接
            getMethod.releaseConnection();
            out.flush();
            out.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 1;
    }
    /**
     * 下载图片(根据路口分类放在不同文件夹)
     * @param fileimgpath 文件名称
     * @param imageList 图片路径
     * @return  是否下载成功
     * @throws Exception
     */
    public static int downLoadImge1(String fileimgpath, List imageList) throws Exception
    {
        String filerootpath = getPath();
        String filepath = filerootpath + "\\" + fileimgpath;
        OutputStream os = new FileOutputStream(filepath + ".zip");
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(os));
        HttpClient hc = new HttpClient();
        GetMethod getMethod = null;
        String filename1;
        try
        {
            for(int i = 0; i < imageList.size(); i++)
            {
                System.out.println("======== " + i);
                try
                {
                    String fullURl = (String)imageList.get(i);
                    //现场发布的图片路径，支持中文
                    String strurl = tranformStyle(fullURl.substring(fullURl.lastIndexOf("$http")+1));
                    getMethod = new GetMethod(strurl);
                    int randomNum = (int) (Math.random()*9000+1000);
                    String path;
                    path = fullURl.substring((fullURl.lastIndexOf(".")+1),fullURl.length());// .jpg
                    // 重命名
                    filename1 = fullURl.substring(0, (fullURl.lastIndexOf("$http")+1))+String.valueOf(randomNum)+"."+path;
                    String fileCatergory = fullURl.substring(0,fullURl.indexOf("$"));
                    hc.executeMethod(getMethod);
                    File file =new File("\\" + fileCatergory);
                    // 如果文件夹不存在创建文件夹，并且把图片放入文件夹
                    if(!file.exists()){
                        file.mkdir();
                        URL url = new URL(strurl);
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        InputStream is = conn.getInputStream();
                        byte[] buffer=new byte[1024];
                        int len=0;
                        while((len=is.read(buffer))!=-1){
                            os.write(buffer, 0, len);
                        }
                    }
                    out.putNextEntry(new ZipEntry(fileCatergory + "/" + filename1.trim()));
                    out.write(getMethod.getResponseBody());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            // 释放连接
            getMethod.releaseConnection();
            out.flush();
            out.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 对中文字符进行UTF-8编码
     * @param source 要转义的字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String tranformStyle(String source) throws UnsupportedEncodingException
    {
        char[] arr = source.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < arr.length; i++)
        {
            char temp = arr[i];
            if(isChinese(temp))
            {
                sb.append(URLEncoder.encode("" + temp, "UTF-8"));
                continue;
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }
    /**
     * 获取字符的编码值
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static int getValue(char s) throws UnsupportedEncodingException
    {
        String temp = (URLEncoder.encode("" + s, "GBK")).replace("%", "");
        if(temp.equals(s + ""))
        {
            return 0;
        }
        char[] arr = temp.toCharArray();
        int total = 0;
        for(int i = 0; i < arr.length; i++)
        {
            try
            {
                int t = Integer.parseInt((arr[i] + ""), 16);
                total = total * 16 + t;
            }
            catch(NumberFormatException e)
            {
                e.printStackTrace();
                return 0;
            }
        }
        return total;
    }
    /**
     * 判断是不是中文字符
     * @param c
     * @return
     */
    public static boolean isChinese(char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
        {
            return true;
        }
        return false;
    }
}