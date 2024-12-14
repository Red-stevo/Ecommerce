package org.codiz.onshop.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.codiz.onshop.configurations.DarajaConfig;
import org.codiz.onshop.service.impl.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MpesaService {

    private  final TokenService tokenService;
    private final DarajaConfig config;
    private final RestTemplate restTemplate;

    public String lipaNaMpesa(String phoneNumber, String amount) {
        String accessToken = tokenService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String timestamp = getTimestamp();
        String password = generatePassword(config.getShortCode(),config.getPassKey(), String.valueOf(new Timestamp(System.currentTimeMillis())));

        Map<String, String> payload = new HashMap<>();
        payload.put("BusinessShortCode", "174379");
        payload.put("Password", password);
        payload.put("Timestamp", timestamp);
        payload.put("TransactionType", "CustomerPayBillOnline");
        payload.put("Amount", amount);
        payload.put("PartyA", phoneNumber);
        payload.put("PartyB", "174379");
        payload.put("PhoneNumber", phoneNumber);
        payload.put("CallBackURL", config.getCallbackUrl());
        payload.put("AccountReference", "Test");
        payload.put("TransactionDesc", "Payment");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                config.getBaseUrl() + "/mpesa/stkpush/v1/processrequest",
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }


    public static String generatePassword(String businessShortCode, String passkey, String timestamp) {
        String rawPassword = businessShortCode + passkey + timestamp;
        return Base64.getEncoder().encodeToString(rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    public static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }


}
