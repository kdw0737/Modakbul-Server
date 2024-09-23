package com.modakbul.global.s3.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public String upload(MultipartFile image) {
		//입력받은 이미지 파일이 빈 파일이면 null 반환
		if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
			return null;
			//throw new BaseException(BaseResponseStatus.EMPTY_IMAGE_FILE);
		}
		//uploadImage를 호출하여 S3에 저장된 이미지의 public url을 반환한다.
		return uploadImage(image);
	}

	private String uploadImage(MultipartFile image) {
		validateImageFileExtention(image.getOriginalFilename());
		try {
			return uploadImageToS3(image);
		} catch (IOException e) {
			throw new BaseException(BaseResponseStatus.IO_EXCEPTION_ON_IMAGE_UPLOAD);
		}
	}

	private List<String> uploadImages(List<MultipartFile> images) {
		List<String> uploadedUrls = new ArrayList<>();

		for (MultipartFile image : images) {
			String uploadedUrl = upload(image);
			uploadedUrls.add(uploadedUrl);
		}

		return uploadedUrls;
	}

	private void validateImageFileExtention(String filename) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new BaseException(BaseResponseStatus.NO_FILE_EXTENTION);
		}

		String extention = filename.substring(lastDotIndex + 1).toLowerCase();
		List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

		if (!allowedExtentionList.contains(extention)) {
			throw new BaseException(BaseResponseStatus.INVALID_FILE_EXTENTION);
		}
	}

	private String uploadImageToS3(MultipartFile image) throws IOException {
		String s3FileName =
			UUID.randomUUID().toString().substring(0, 10) + "_" + image.getOriginalFilename(); //변경된 파일 명

		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(image.getContentType());
			metadata.setContentLength(image.getSize());
			PutObjectRequest request = new PutObjectRequest(bucketName, s3FileName, image.getInputStream(), metadata);
			request.withCannedAcl(CannedAccessControlList.AuthenticatedRead);
			amazonS3.putObject(bucketName, s3FileName, image.getInputStream(), metadata); // put image to S3
		} catch (Exception e) {
			throw new BaseException(BaseResponseStatus.PUT_OBJECT_EXCEPTION);
		}

		return amazonS3.getUrl(bucketName, s3FileName).toString();
	}

	public void deleteImageFromS3(String imageAddress) {
		String key = getKeyFromImageAddress(imageAddress);
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
		} catch (Exception e) {
			throw new BaseException(BaseResponseStatus.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}

	private String getKeyFromImageAddress(String imageAddress) {
		try {
			URL url = new URL(imageAddress);
			String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
			return decodingKey.substring(1); // 맨 앞의 '/' 제거
		} catch (MalformedURLException | UnsupportedEncodingException e) {
			throw new BaseException(BaseResponseStatus.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}
}
