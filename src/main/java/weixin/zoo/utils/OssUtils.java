package weixin.zoo.utils;

import com.aliyun.oss.OSSClient;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * Created by viczhang.zhangz on 2017/5/5.
 */
public class OssUtils {

    private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = "LTAIv4EUi6dHexuL";
    private static String accessKeySecret = "qzfLAnbddlKOxa5DTuEy9Z4Au0PMdf";
    private static String bucketName = "zujuguan";

    private static OSSClient ossClient;

    private static OSSClient init(){
        if(ossClient == null){
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        }
        return ossClient;
    }

    public static boolean uploadFile(String filePath, String key){
        OSSClient client = init();
        try {
            InputStream inputStream = new FileInputStream(filePath);
            client.putObject(bucketName, key, inputStream);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            client.shutdown();
        }

        return true;
    }

    public static String getFileUrl(String key){
        OSSClient client = init();
        try {
            Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
            URL url = client.generatePresignedUrl(bucketName,key,expiration);
            return url.toString();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            client.shutdown();
        }

        return null;
    }
}
