package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import main.crawl50;

public class crawl50Test {
	public static void main(String args[]){
		System.out.println("Tests completed successfully");
	}
	@Test
	public  void testingCanonicalization() {
		assertEquals("Here is test for Canonicalizing URL1: ", "http://WebReference.com/html/about.html",
				crawl50.CanonicalizeURL("about.html", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL2: ", "http://WebReference.com/html/tutorial1/",
				crawl50.CanonicalizeURL("tutorial1/", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL3: ", "http://WebReference.com/html/tutorial1/2.html",
				crawl50.CanonicalizeURL("tutorial1/2.html", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL4: ", "http://WebReference.com/",
				crawl50.CanonicalizeURL("/", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL5: ", "http://www.internet.com/",
				crawl50.CanonicalizeURL("//www.internet.com/", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL6: ", "http://WebReference.com/experts/",
				crawl50.CanonicalizeURL("/experts/", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL7: ", "http://WebReference.com/",
				crawl50.CanonicalizeURL("../", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL8: ", "http://WebReference.com/experts/",
				crawl50.CanonicalizeURL("../experts/", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL9: ", "http://WebReference.com/",
				crawl50.CanonicalizeURL("../../../", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL10: ", "http://WebReference.com/html/",
				crawl50.CanonicalizeURL("./", "http://WebReference.com/html/"));

		assertEquals("Here is test for Canonicalizing URL11: ", "http://WebReference.com/html/about.html",
				crawl50.CanonicalizeURL("./about.html", "http://WebReference.com/html/"));
	}


	@Test
	public void testingValiedURL() throws IOException {
		assertEquals("Here is test for Valied URL1: ", false,
				crawl50.validateURL("http://www.example.com/junk"));

		assertEquals("Here is test for Valied URL2: ", true,
				crawl50.validateURL("http://www.example.com"));


	}
}
