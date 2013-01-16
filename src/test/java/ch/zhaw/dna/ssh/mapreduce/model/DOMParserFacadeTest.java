package ch.zhaw.dna.ssh.mapreduce.model;


import static ch.zhaw.dna.ssh.mapreduce.model.ConcreteWebMap.URLKEY;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DOMParserFacadeTest {
	
	@Test
	public void shouldCopeWithNonEndingTags() {
		DomParserFacade facade = new DomParserFacade();
		String[] testTags = new String[]{"p"};
		Map<String, List<String>> vals = facade.extractText("<html><p>foo</p><ul></html>", testTags);
		assertEquals("foo", vals.get("p").get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("p").size());
	}
	
	@Test
	public void getContentOfTag(){
		String[] testTags = new String[]{"h1","h2","a"};
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = facade.extractText("<html><a>foo</a></html>", testTags);
		assertEquals("foo", vals.get("p").get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("p").size());
		
	}
	
	@Test
	public void getEmptyCausedByWrongTag(){
		String testString = "<html><a>Link zur ZHAW</a></html>";
		String[] testTags = new String[]{"h1","h2","p"};
		DomParserFacade localFacade = new DomParserFacade();
		assertEquals(0, localFacade.extractText(testString, testTags));
	}
	
	@Test
	public void getMultpleContentSameTag(){
		String testString = "<html><a>foo</a><a href=\"index.html\">bar</a><html>";
		String[] testTags = new String[]{"h1","h2","a"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals("foo", vals.get("a").get(0));
		assertEquals("bar", vals.get("a").get(1));
		assertEquals("index.html", vals.get(URLKEY).get(0));
		assertEquals(2, vals.size());
		assertEquals(2, vals.get("a").size());
	}
	
	@Test
	public void getNestedContentDifferentTag(){
		String testString = "<html><a><h1>Link zur ZHAW</h1></a></html>";
		String[] testTags = new String[]{"h1","h2","a"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals("Link zur ZHAW", vals.get("a").get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("a").size());
	}
	
	@Test
	public void getNestedContentDifferentTagSplit(){
		String testString = "<html><a>Link zur <h1>ZHAW</h1></a></html>";
		String[] testTags = new String[]{"h1","h2","a"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals("Link zur ZHAW", vals.get("a").get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("a").size());
		
	}
	
	@Test
	public void getNestedContentWrongTagSplit(){
		String testString = "<html><a>Link zur <h1>ZHAW</h1></a></html>";
		String[] testTags = new String[]{"h2","a"};
		DomParserFacade localFacade = new DomParserFacade();
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals("Link zur ZHAW", vals.get("a").get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("a").size());
		
	}
	
	@Test
	public void shouldIncludeUrlsFromNonSearchedTags(){
		String testString = "<html><a href=\"http://www.google.com\">foo</a><h1>bar</h1></html>";
		String[] testTags = new String[]{"h1"};
		DomParserFacade localFacade = new DomParserFacade();
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}
	
	@Test
	public void shouldCopeWithGarbage() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=http://www.google.com\"></a>", vals);
		assertEquals(0, vals.size());
	}
	
	@Test
	public void shouldCopeWithGarbage2() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"http://www.google.com></a>", vals);
		assertEquals(0, vals.size());
	}
	
	@Test
	public void shouldCopeWithGarbage3() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a> href=\"http://www.google.com\"></a>", vals);
		assertEquals(0, vals.size());
	}
	
	@Test
	public void shouldCopeWithGarbage4() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"", vals);
		assertEquals(0, vals.size());
	}

	@Test
	public void shouldCopeWithGarbage5() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"http://www.google.com", vals);
		assertEquals(0, vals.size());
	}

	@Test
	public void shouldCopeWithGarbage6() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("href=\"http://www.google.com\"></a>", vals);
		assertEquals(0, vals.size());
	}

	@Test
	public void shouldIgnoreLocalAnkers() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"#bar\"></a>", vals);
		assertEquals(0, vals.size());
	}

	@Test
	public void shouldIgnoreInexistentHref() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a title=\"foo\"></a>", vals);
		assertEquals(0, vals.size());
	}
	
	@Test
	public void shouldIgnoreHrefInText() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a title=\"foo\">href=\"</a>", vals);
		assertEquals(0, vals.size());
	}
	
	@Test
	public void shouldReadAfterAttribute() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a title=\"foo\" href=\"http://www.google.com\"></a>", vals);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}
	
	@Test
	public void shouldReadBetweenAttribute() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a title=\"foo\" href=\"http://www.google.com\" class=\"meow\"></a>", vals);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}

	@Test
	public void shouldReadBeforeAttribute() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"http://www.google.com\" class=\"meow\"></a>", vals);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}

	@Test
	public void shouldReadAlone() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"http://www.google.com\"></a>", vals);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}
	
	@Test
	public void shouldReadWithSpacesBefore() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a   href=\"http://www.google.com\"></a>", vals);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}

	@Test
	public void shouldReadWithSpacesAfter() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"http://www.google.com\" ></a>", vals);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}

	@Test
	public void shouldReadWithText() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"http://www.google.com\">foo bar</a>", vals);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}
	
	@Test
	public void shouldReadWithConfusingText() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addUrlsToMap("<a href=\"http://www.google.com\">href=\"</a>", vals);
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
	}
	
	@Test
	public void shouldLeaveTaglessInput() {
		DomParserFacade facade = new DomParserFacade();
		assertEquals("foo bar", facade.stripTagsFromContent("foo bar"));
	}
	
	@Test
	public void shouldRemoveOneTag() {
		DomParserFacade facade = new DomParserFacade();
		assertEquals("foo bar", facade.stripTagsFromContent("foo <i>bar"));
	}
	
	@Test
	public void shouldRemoveTwoTag() {
		DomParserFacade facade = new DomParserFacade();
		assertEquals("foo bar", facade.stripTagsFromContent("foo <i>bar</i>"));
	}
	
	@Test
	public void shouldRemoveTagsWithAttribute() {
		DomParserFacade facade = new DomParserFacade();
		assertEquals("foo bar", facade.stripTagsFromContent("foo <i class=\"foo\">bar</i>"));
	}
	
	@Test
	public void shouldRemoveAllEnclosingTag() {
		DomParserFacade facade = new DomParserFacade();
		assertEquals("foo bar", facade.stripTagsFromContent("<i>foo bar</i>"));
	}
	
	@Test
	public void shouldNotAddSpaces() {
		DomParserFacade facade = new DomParserFacade();
		assertEquals("foobar", facade.stripTagsFromContent("foo</i>bar"));
	}
	
	@Test
	public void shouldKeepUrlText() {
		DomParserFacade facade = new DomParserFacade();
		assertEquals("foo bar", facade.stripTagsFromContent("<a href=\"http://www.google.com\">foo bar</a>"));
	}
	
	@Test
	public void shouldAddForNewTag() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addToMap("p", "hello world", vals);
		assertEquals("hello world", vals.get("p").get(0));
		assertEquals(1, vals.get("p").size());
		assertEquals(1, vals.size());
	}
	
	@Test
	public void shouldAddForExistingTags() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		vals.put("p", new LinkedList<String>(Arrays.asList(new String[]{"foo"})));
		facade.addToMap("p", "bar", vals);
		assertEquals("foo", vals.get("p").get(0));
		assertEquals("bar", vals.get("p").get(1));
		assertEquals(2, vals.get("p").size());
		assertEquals(1, vals.size());
	}
	
	@Test
	public void shouldAddUrlAndText() {
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = new HashMap<String, List<String>>();
		facade.addToMap("a", "<p><a href=\"http://www.google.com\">foo</a> bar</p>", vals);
		assertEquals("foo bar", vals.get("a").get(0));
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
		assertEquals(1, vals.get("a").size());
		assertEquals(1, vals.get(URLKEY).size());
		assertEquals(2, vals.size());
	}
}
