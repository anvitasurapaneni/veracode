package main;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class crawl50 {

	static boolean valiedfirstpage = true;
	private static Scanner stdin;
	public static void main(String args[]) throws Exception{



		int index = 0;
		//output file
		PrintWriter pw = new PrintWriter("50URLS.txt");
		// final list of URLs // can stop crawling when this size is equal to 50
		LinkedList<String> URLsList= new LinkedList<String>();
		// stores list of URLs to be parsed. we can stop parsing when this becomes empty
		HashSet<String> URLstoParse= new HashSet<String>();

		String seedURL = getURL();
		URLsList.add(seedURL);
		URLstoParse.add(seedURL);
		System.out.println("Started crawling with URL: "+seedURL);

		while ((!URLstoParse.isEmpty()) && URLsList.size() <= 51 ) {
			try {
				String URL_Parent = URLsList.get(index);
				URLstoParse.remove(URL_Parent);
				index ++;
				URL url = new URL(URL_Parent);
				InputStream is = (InputStream) url.openStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String html_line = "";
				boolean script = false;
				boolean oneLineScript = false;
				while((html_line = br.readLine()) != null){
					oneLineScript = false;
					// script will be set to true when the line starts an opening script tag
					// and set to false only when an closing tag is encountered.
					// the URLs are not added to list as long as script is true to avoid URLs from script tags
					// inline scrips are also handled in a similar way
					if(html_line.contains("<script")){
						script = true;
					}
					if(html_line.contains("</script")){
						script = false;
					}
					if(html_line.contains("<script") && html_line.contains("</script")){
						oneLineScript = true;	
					}
					if((!script) && (!oneLineScript)){
						if(URLsList.size() > 51) break;
						// Regular expression for link in href attribute
						Pattern p = Pattern.compile("href=\"(.*?)\"");
						Matcher m = p.matcher(html_line);

						String url_child_temp = null;
						while (m.find()) {
							if(URLsList.size() > 51) break;
							url_child_temp = m.group(1);
							// get the complete URL
							String url_child =CanonicalizeURL(url_child_temp, URL_Parent);
							if(validateURL(url_child) && (!URLsList.contains(url_child))){
								URLsList.add(url_child);
								URLstoParse.add(url_child);
							}
							
						}

					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}
		
		System.out.println("done parsing");
		for(String s: URLsList){
			System.out.println(s);
			//writing to file
			pw.println(s);
		}
		pw.close();
	}

	// asks the user to give a URL until he enters one valid URL
	private static String getURL() {
		// TODO Auto-generated method stub
		if(valiedfirstpage){
			System.out.println("Please Enter a URL");
		}
		if(!valiedfirstpage){
			System.out.println("The previous URL is faulty.Please Enter another URL");
		}
		stdin = new Scanner(new BufferedInputStream(System.in));
		while (stdin.hasNext()) {
			String url_temp = stdin.nextLine();
			try {
				if(validateURL(url_temp)){
					return url_temp;
				}
				else{
					valiedfirstpage = false;
					return getURL();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}



	// checks if the given URL exists
	public static boolean validateURL(String url_temp) throws IOException {
		// TODO Auto-generated method stub
		try{
			URL u = new URL(url_temp);
			HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
			huc.setRequestMethod("GET"); 
			huc.connect(); 
			int StatusCode = huc.getResponseCode();
			if(StatusCode <= 300){
				return true;
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}

	// canonicalises ie convers child URL, either relative or absolute to an
	//absolute URL based on parent URL
	public static String  CanonicalizeURL(String url, String ParentURL) {

		String MyUrl = url;
		String ParUrl = ParentURL;

		String protocol = "http:";
		String domainRootURL = "";

		int protocolindex = ParUrl.indexOf("://");
		protocol = ParUrl.substring(0, protocolindex + 1);


		int domainRootURLindex = ParUrl.indexOf("/", ParUrl.indexOf("://") + 3);
		domainRootURL = ParUrl.substring(0, domainRootURLindex + 1);
		String restOfURL = ParUrl.substring(domainRootURLindex + 1);



		if (MyUrl.startsWith(protocol))
			return MyUrl;

		if (MyUrl.startsWith("//")){
			MyUrl = protocol + MyUrl;
			return MyUrl;
		}

		if (MyUrl.startsWith("/")){
			if(domainRootURL.endsWith("/")){
				domainRootURL = domainRootURL.substring(0, domainRootURL.length() - 1);
			}
			if(MyUrl.startsWith("/")){
				MyUrl = MyUrl.substring(1);
			}
			MyUrl = domainRootURL + "/" + MyUrl;
			return MyUrl;
		}

		if (MyUrl.startsWith("www")){
			MyUrl = protocol + "//" + MyUrl;
			return MyUrl;
		}



		if(MyUrl.startsWith("./")){
			MyUrl = MyUrl.replace("./", "");
			MyUrl = domainRootURL + restOfURL +MyUrl;
			return MyUrl;
		}


		if(MyUrl.startsWith(".")){
			//	String line = MyUrl;
			int noOfDots = MyUrl.substring(0, MyUrl.indexOf("/")).length();
			MyUrl = MyUrl.replaceAll("\\.+/","");

			for(int i =1; i <= noOfDots; i++){
				int index = 0;
				if(restOfURL.contains("/")){
					index = restOfURL.lastIndexOf('/');
				}
				restOfURL = restOfURL.substring(0,index);
			}

			MyUrl = domainRootURL + restOfURL +MyUrl;
			return MyUrl;

		}




		else {
			if(ParUrl.endsWith("/")) MyUrl = ParUrl + MyUrl;
			else MyUrl = ParUrl + "" + MyUrl;
		}



		MyUrl = MyUrl.trim();
		return MyUrl;

	}





}
