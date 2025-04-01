package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String location = request.getParameter("location");
//		System.out.println(location);
		
		String api = "0c25145d030fbcb190ec0a3efa6f60d0";
		
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + api;
		
		URL url = new URL(apiUrl);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		StringBuilder responseContent = new StringBuilder();
		
		Scanner scanner = new Scanner(reader);
		
		//Taking input from the reader 
		
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
			
		}
		
		scanner.close();
		
		
		
		
		//Typecasting - parsing the data into JSON 
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString() , JsonObject.class);
//		System.out.println(jsonObject);
		
		long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTimestamp).toString();
		
		double tempratureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int tempratureselsius = (int) (tempratureKelvin - 273.15);
		
		
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		
		
		
		request.setAttribute("date", date);
		request.setAttribute("city", location);
		request.setAttribute("temprature", tempratureselsius);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		
		request.getRequestDispatcher("index.jsp").forward(request , response);
		
	}

}
