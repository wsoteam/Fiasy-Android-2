package com.wsoteam.diet.InApp.properties;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayService {
    private final Map<String, AndroidPublisher> androidPublishers = new HashMap<>();

    private String readCredentialsJson(String packageName) {
        return "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"restfin\",\n" +
                "  \"private_key_id\": \"ca9d68b2826edf43f643c6e6b202b3f59759ae1a\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCTW8rCZLbTO7+1\\nnTYOZWLi8ii5GNNF/wvERZoA2So8d32P5uGRREqjRfdXwjrw7BHWVCCO48OIFcoC\\nDXxYrkXkiggMjX6yxwZq9m9nK45hFpNmoJXl7GLsjdy4TpVTppOx74jFzCz241Cw\\nn9+Xq4+emk530oDhILuNcCCmLJ9X9KDPYtE7pviO1hSq7aajsuxOHXZrpCDKsvvj\\nOPl++QX/lhFaB/97Xq5zRxXsvwdMpj0Hoxartg0vtCCpTMxPZVd5AUrKjGrUaWAu\\nQDHmOI4y1NMkU3vxrNzAL5yWDlovL8rHcjUZtPitai86wOsVmYjczMvjiVlzVJN7\\nkCYzkwFzAgMBAAECggEABIT6MQAZw1y/wngx4jslz0QSD6s9P5ZU7A4ebQw8CWK4\\nzSMNpADuMRfCQETKvfzfhy1JWliN9KyGZ422VJnzttCs8oWcbys/fSRVwlpJoZyp\\npKokLssTNaIcNOLkHY6sVPa9wSND9cZ/AQvGNcajPwAIjBA+t6jjV7Eqry4LLQ1d\\nu7X4oT1WzfH70NxpYmJiCVZDAMns01mDfDrY5cYdWaA47e4MPp+NaxyI9FMOov8G\\nV5jVWO2YArh1Y0yBXUVD2HgaP/HMM3dbfRHYsDF9wjYZFFKJE0QcLR0d36zBU6wZ\\nHFcs+aNVEC+xfB1b+j3DzQQRrxGaCluPJKFm5hh30QKBgQDL3MQbBWtXVjobGld6\\nONQBfAKNLZ6SiGPWawtuHuIXjQnq3W8e6h4jCMeANfssYMO2RoGRAg1lTkYsEHtD\\nkTLSknvfEQ0Kpl/HtZGw3La6caLYIm+0JTGAqd/Lmx5Uw62Mp+88gKioWad0IDJ+\\nOij0X3nu4y+eIOnYwc0YcbzYHQKBgQC5C57sUHlEl4WW4MvjuBUe0hEwLVilRtnd\\nq1Xm/2tbgN3YxpkcFn/3Vd8ZTEm4BGG4F5aTcsXUauwH/K9Q/IZvysM6TTuZlkbc\\n8DunPbQDrl0enZOeTbtd+Bbi9wldwWy+9XH91+0l2WVJxmOelGdnjuqXUO9em1HU\\nbci3uUGqzwKBgBY/UWdhgAVX859hkIpWCUzgK6ccg2GaYMrVg7nQxPus+JgY8jd9\\ndSx9mMf1HCQLq1AJAbrBbpTPpfjJE6qb/d/c/IerZLD01x8EQZH7hDV0kUDlASu3\\no19XKoUduIGnmRjwersvjzhQEGYr3VbJIg8JHFVllZy8Gsj83VT9lI5BAoGAbl/b\\nHJPnsZ07psJRe3QKzQZ6ECWMMZFwVINlStLhg4P7T9kt7I4TKEHFQD673hJnAzq2\\nqx6Tl99rCFyVahYmTjpAdt8ohLUZcaGtw1TPsj5n9uIA8jTlZOidJiY6JP1B0ylu\\nI/6OyrvAad/2/dkBUMjzbuxsyx6UEcSmK8hJvgsCgYEAo+KhuhZQ8HMNhyTIk25R\\nSC8u6u/7++F9BvR+aOMSTpiNTX1+BiCQ0nlJGRxFahklEoReJ0B5ErMrajw6EDm+\\neK7KedYZOto+epQUo/plOOOFJBbDS5R5dXsIe2NJfQgS8Qj/hcgH5vFHfaj53cDZ\\nFIYIY6IbBCRuwqkaVYNiUE4=\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"dablyat@restfin.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"105116951471404214761\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/dablyat%40restfin.iam.gserviceaccount.com\"\n" +
                "}\n";
    }

    private AndroidPublisher getPublisher(String packageName) throws Exception {
        if (!androidPublishers.containsKey(packageName)) {
            String credentialsJson = readCredentialsJson(packageName);
            InputStream inputStream = new ByteArrayInputStream(
                    credentialsJson.getBytes(StandardCharsets.UTF_8));
            HttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();
            GoogleCredential credential = GoogleCredential.fromStream(inputStream)
                    .createScoped(Collections.singleton(
                            AndroidPublisherScopes.ANDROIDPUBLISHER));
            AndroidPublisher.Builder builder = new AndroidPublisher.Builder(
                    transport, JacksonFactory.getDefaultInstance(), credential);
            AndroidPublisher androidPublisher = builder.build();
            androidPublishers.put(packageName, androidPublisher);
        }
        return androidPublishers.get(packageName);
    }

    public ProductPurchase getPurchase(String packageName,
                                       String productId,
                                       String token)
            throws Exception {
        AndroidPublisher publisher = getPublisher(packageName);
        AndroidPublisher.Purchases.Products.Get get = publisher
                .purchases().products().get(packageName, productId, token);
        return get.execute();
    }

    public SubscriptionPurchase getSubscription(String packageName,
                                                String productId,
                                                String token)
            throws Exception {
        AndroidPublisher publisher = getPublisher(packageName);
        AndroidPublisher.Purchases.Subscriptions.Get get = publisher
                .purchases().subscriptions().get(packageName, productId, token);
        return get.execute();
    }
}
