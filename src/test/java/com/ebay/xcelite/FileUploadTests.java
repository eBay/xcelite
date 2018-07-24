package com.ebay.xcelite;

import com.ebay.xcelite.model.UploadData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class FileUploadTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void uploadAndConvertXlsToBean() {
        ClassPathResource resource = new ClassPathResource("UploadData.xlsx");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);

        ResponseEntity<UploadData> responseEntity = restTemplate.postForEntity("/", map, UploadData.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualToComparingFieldByField(uploadData());
    }

    private UploadData uploadData() {
        UploadData uploadData = new UploadData();
        uploadData.setName("Virginia");
        uploadData.setLastName("Adams");
        return uploadData;
    }
}
