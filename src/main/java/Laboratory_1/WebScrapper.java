package Laboratory_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebScrapper {

    public String baseGetRequest() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constants.URL).openConnection();
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
//            System.out.println("Response code: " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

//            System.out.println(response.toString());
            return response.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
