package com.liu.community.provider;

import com.liu.community.exception.CustomizeErrorCode;
import com.liu.community.exception.CustomizeException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

@Service
public class TencentClooudProvider {
    @Value("${tencentcloud.ufile.secretId}")
    private String secretId;
    @Value("${tencentcloud.ufile.secretKey}")
    private String secretKey;
    @Value("tencentcloud.ufile.path")
    private String path;
    @Value("tencentcloud.ufile.region_name")
    private String region_name;
    @Value("tencentcloud.ufile.bucketName")
    private String bucketName;



    public String upload(MultipartFile file){
        if (file==null){
            return null;
        }
        String generatedFileName;
        String[] filePaths = file.getOriginalFilename().split("\\.");
        if (filePaths.length>1){
            generatedFileName = UUID.randomUUID().toString() + "." +filePaths[filePaths.length-1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int moth = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(region_name);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);



        try {
            // 指定要上传的文件
            File localFile = File.createTempFile("temp", null);
            file.transferTo(localFile);
            // 指定要上传到的存储桶
            // 指定要上传到 COS 上对象键
            String key =  "/" +year + "/" +moth + "/" + day + "/" + generatedFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return path + putObjectRequest.getKey();
        } catch (CosServiceException serverException) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (CosClientException clientException) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
        return null;
    }
}
