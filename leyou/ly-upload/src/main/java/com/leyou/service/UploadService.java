/*
信息:
*/
package com.leyou.service;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private UploadProperties uploadProperties;
//    @Autowired
//    private ThumbImageConfig thumbImageConfig; //缩略策略
   // private static final List<String> ALLOW_TYPE= Arrays.asList("image/jpeg","image/png","image/bmp","image/jpg");
    public String saveImage(MultipartFile file) {
        try {
            //校验文件类型
          if (!uploadProperties.getAllowTypes().contains(file.getContentType())){
              throw new LyException(ExceptionEnums.ERROR_UPLOAD_TYPE);
          }
            //校验文件内容,如果没有图片返回空
            BufferedImage bufferedImage= ImageIO.read(file.getInputStream());
          if (bufferedImage==null){
              throw new LyException(ExceptionEnums.ERROR_UPLOAD_TYPE);
          }
           //FDFS上传
           String extend= StringUtils.substringAfterLast(file.getOriginalFilename(),".");
           StorePath storePath= storageClient.uploadFile(file.getInputStream(),file.getSize(),extend,null);
           return uploadProperties.getBaseUrl()+storePath.getFullPath();
        } catch (IOException e) {
            log.error("[文件上传失败]",e);
            throw new LyException(ExceptionEnums.UPLOAD_FILE_ERROR);
        }
    }
}
