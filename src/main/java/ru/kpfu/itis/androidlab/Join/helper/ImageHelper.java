package ru.kpfu.itis.androidlab.Join.helper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class ImageHelper {

    private Cloudinary cloudinary;

    public Cloudinary getInstance() {
        if (cloudinary == null){
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dnl4u0eua",
                    "api_key", "241562529735436",
                    "api_secret", "zcu8PHeXQRjPAXjUUb8nqIyNzzE"));
        }
        return cloudinary;
    }

    public String uploadImage(MultipartFile file) {
        cloudinary = getInstance();
        String url = null;
        Map uploadResult = null;
        if (file.isEmpty()) {
            return null;
        }
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));

            url = (String) uploadResult.get("url");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return url;
    }

    public boolean deleteImage(String url) {
        cloudinary = getInstance();
        String[] tags = url.split("/");
        String[] publicId = tags[tags.length - 1].split(".");
        Map result;
        try {
            result = cloudinary.api().deleteResources(ObjectUtils.asArray(publicId[0]), ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
