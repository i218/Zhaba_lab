package org.nmu.j_test.services;


import com.google.gson.Gson;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class TimeService {

    public Date getDateNow() {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request =
                    HttpRequest.newBuilder(URI.create("https://timeapi.io/api/time/current/zone?timeZone=UTC"))
                            .GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new Exception(response.body());
            }

            var gson = new Gson();
            ResponseData responseData = gson.fromJson(response.body(), ResponseData.class);

            String fixedDateTime = responseData.dateTime.replaceAll("(\\.\\d{1,6})\\d*", "$1");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            LocalDateTime localDateTime = LocalDateTime.parse(fixedDateTime, formatter);

            return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

@Data
class ResponseData {
    String dateTime;
}
