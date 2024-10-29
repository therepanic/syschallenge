/*
 * Copyright by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.syschallenge.mainservice.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syschallenge.mainservice.oauth.model.GoogleOAuthIdTokenUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author panic08
 * @since 1.0.0
 */
class GoogleOAuthV4ServiceTest {

    private GoogleOAuthV4Service googleOAuthService;
    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();

        this.googleOAuthService = new GoogleOAuthV4Service(new GoogleOAuthV4Api(restTemplate), new ObjectMapper());
        this.mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testExtractGoogleOAuthIdTokenUser() {
        //contains: {
        //  "iss": "https://accounts.google.com",
        //  "azp": "613917991989-ccheeqfpn4dq1lrcmam52ddlfcvd2fmi.apps.googleusercontent.com",
        //  "aud": "613917991989-ccheeqfpn4dq1lrcmam52ddlfcvd2fmi.apps.googleusercontent.com",
        //  "sub": "104830021709651013572",
        //  "email": "litvitsk10102008@gmail.com",
        //  "email_verified": true,
        //  "at_hash": "VTEx-N8RBIBdsoz4TtSnFA",
        //  "name": "panic",
        //  "picture": "https://lh3.googleusercontent.com/a/ACg8ocLXhqpGh8InYHpd5-b3Es7vh5L5XWDvUUdRxXT--ns3BVD35c6d=s96-c",
        //  "given_name": "panic",
        //  "iat": 1730228618,
        //  "exp": 1730232218
        //}
        String testJwt = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImM4OGQ4MDlmNGRiOTQzZGY1M2RhN2FjY2ZkNDc3NjRkMDViYTM5MWYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2MTM5MTc5OTE5ODktY2NoZWVxZnBuNGRxMWxyY21hbTUyZGRsZmN2ZDJmbWkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2MTM5MTc5OTE5ODktY2NoZWVxZnBuNGRxMWxyY21hbTUyZGRsZmN2ZDJmbWkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ4MzAwMjE3MDk2NTEwMTM1NzIiLCJlbWFpbCI6ImxpdHZpdHNrMTAxMDIwMDhAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJWVEV4LU44UkJJQmRzb3o0VHRTbkZBIiwibmFtZSI6InBhbmljIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0xYaHFwR2g4SW5ZSHBkNS1iM0VzN3ZoNUw1WFdEdlVVZFJ4WFQtLW5zM0JWRDM1YzZkPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6InBhbmljIiwiaWF0IjoxNzMwMjI4NjE4LCJleHAiOjE3MzAyMzIyMTh9.0LhxeHcazCApjGc5WcoFS4FUR5W8FJ5n3K4qa6sT0mLGUPurgffim6ahottM6jw9RwFvqHx6T7tQtpx9OZsumtzOLc6x3O9Ek13_ClK47y83x77q2aW4tqJKw18SfvAVPdqgpPFxQcOpcBlZ6UZ0ldDQyvW61ab9F6JCrFc41T8jzdlDr5oUWgqTXQGtAESgOvxdgWomba2Q0LQBOsBdaEXcwWposhUEYbV6YyRNThZ52AhLlfpX8IW9N5bwxAu1NO7IvWZ4kEQAejcP6Fh3qCIhtOLl6cYXTH6wYn90pSlqtqxLpUWuFgQM5T5bVvLYwfPs5tmBvkhUyDPpoaJX3g";

        String expectedResponse = "{\n" +
                "    \"access_token\": \"test\",\n" +
                "    \"expires_in\": 0,\n" +
                "    \"scope\": \"test\",\n" +
                "    \"token_type\": \"test\",\n" +
                "    \"id_token\": \"" + testJwt + "\"\n" +
                "}";

        mockRestServiceServer.expect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        GoogleOAuthIdTokenUser user = googleOAuthService.extractGoogleOAuthIdTokenUser("test", "test", "test",
                "test", "test");

        assertEquals("104830021709651013572", user.userId());
        assertEquals("litvitsk10102008@gmail.com", user.email());
        assertTrue(user.isEmailVerified());
        assertEquals("panic", user.name());
        assertEquals("panic", user.givenName());
        assertEquals("https://lh3.googleusercontent.com/a/ACg8ocLXhqpGh8InYHpd5-b3Es7vh5L5XWDvUUdRxXT--ns3BVD35c6d=s96-c", user.pictureUrl());
    }
}
