package com.example.fsoft_shopee_nhom02.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    public Boolean deleteFile(String directory) {
        Map paramsOption = ObjectUtils.asMap("resource_type", "image", "invalidate" , true);
        try {
            cloudinary.uploader().destroy(directory, paramsOption);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //    Upload thanh cong return duong dan chua hinh anh
//    Upload khong thanh cong return -1
    public String uploadFile(MultipartFile file, String imgName, String directory) {
        File uploadedFile = null;

        try {
            if(file == null) {
                return "-1";
            }
            uploadedFile = convertMultiPartToFile(file, imgName);
            String fileDir = directory + "/"
                    + uploadedFile.getName(); //Dir tren cloudinary: Tạo folder id product và file name là id product
            //Option cho cloudinary
            Map paramsOption =
                    ObjectUtils.asMap("resource_type", "auto",
                            "public_id", fileDir);
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    paramsOption);

            uploadedFile.delete(); // Xoa file tao khi push len cloudinary xong
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            uploadedFile.delete(); // Xoa file tao khi push len cloudinary xong
            return "-1";
        }
    }

    private File convertMultiPartToFile(MultipartFile file, String fileName) throws IOException {
        File convFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
