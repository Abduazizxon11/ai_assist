package org.example;

import com.google.gson.Gson;
import org.example.model.ResponseData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GeminiAPIExample {

    static HttpClient client = HttpClient.newHttpClient();
    static Gson gson = new Gson();

    public static void main(String[] args) throws IOException, InterruptedException {

        // JSON request body (the content you want to send to the Gemini API)
        String jsonRequestBody = "{\"contents\":[{\"parts\":[{\"text\":\"Explain how AI works\"}]}]}";

        // Create API request with API key as query parameter
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyA1lyz2u1IaADLJ2RMs9UCIAbZbDKcJDFI"))  // Replace YOUR_API_KEY with the actual key
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))  // Send the JSON body
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Get the response body
        String responseBody = response.body();

        // Parse JSON response
        ResponseData responseData = gson.fromJson(responseBody, ResponseData.class);


        for (ResponseData.Candidate candidate : responseData.getCandidates()) {
            ResponseData.Content content = candidate.getContent();
            List<ResponseData.Part> parts = content.getParts();
            for (ResponseData.Part part : parts) {
                System.out.println(part.getText());  // This will print the text part
            }
        }

        // Print the result

    }
}

// Define a class for the response data
