package ch.zhaw.dna.ssh.mapreduce.model;


import static ch.zhaw.dna.ssh.mapreduce.model.ConcreteWebMap.URLKEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DomParserFacadeTest {
	
	@Test
	public void shouldCopeWithNonEndingTags() {
		DomParserFacade facade = new DomParserFacade();
		String[] testTags = new String[]{"P"};
		Map<String, List<String>> vals = facade.extractText("<html><p>foo</p><ul></html>", testTags);
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("P").size());
		assertEquals("foo", vals.get("P").get(0));
	}
	
	@Test
	public void getContentOfTag(){
		String[] testTags = new String[]{"H1","H2","A"};
		DomParserFacade facade = new DomParserFacade();
		Map<String, List<String>> vals = facade.extractText("<html><a>foo</a></html>", testTags);
		assertEquals(1, vals.size());
		assertEquals("foo", vals.get("A").get(0));
		assertEquals(1, vals.get("A").size());
		
	}
	
	@Test
	public void getEmptyCausedByWrongTag(){
		String testString = "<html><a>Link zur ZHAW</a></html>";
		String[] testTags = new String[]{"H1","H2","P"};
		DomParserFacade localFacade = new DomParserFacade();
		assertEquals(0, localFacade.extractText(testString, testTags).size());
	}
	
	@Test
	public void getMultpleContentSameTag(){
		String testString = "<html><a>foo</a><a href=\"index.html\">bar</a><html>";
		String[] testTags = new String[]{"H1","H2","A"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals(2, vals.size());
		assertEquals(2, vals.get("A").size());
		assertTrue(vals.get("A").contains("foo"));
		assertTrue(vals.get("A").contains("bar"));
		assertEquals("index.html", vals.get(URLKEY).get(0));
	}
	
	@Test
	public void getNestedContentDifferentTag(){
		String testString = "<html><a><h1>Link zur ZHAW</h1></a></html>";
		String[] testTags = new String[]{"H1","H2","A"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("A").size());
		assertEquals("Link zur ZHAW", vals.get("A").get(0));
	}
	
	@Test
	public void getNestedContentDifferentTagSplit(){
		String testString = "<html><a>Link zur <h1>ZHAW</h1></a></html>";
		String[] testTags = new String[]{"H1","H2","A"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("A").size());
		assertEquals("Link zur ZHAW", vals.get("A").get(0));
		
	}
	
	@Test
	public void getNestedContentWrongTagSplit(){
		String testString = "<html><a>Link zur <h1>ZHAW</h1></a></html>";
		String[] testTags = new String[]{"H2","A"};
		DomParserFacade localFacade = new DomParserFacade();
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("A").size());
		assertEquals("Link zur ZHAW", vals.get("A").get(0));
	}
	
	@Test
	public void shouldIncludeUrlsFromNonSearchedTags(){
		String testString = "<html><a href=\"http://www.google.com\">foo</a><h1>bar</h1></html>";
		String[] testTags = new String[]{"H2"};
		DomParserFacade localFacade = new DomParserFacade();
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals(1, vals.size());
		assertEquals(1, vals.get(URLKEY).size());
		assertEquals("http://www.google.com", vals.get(URLKEY).get(0));
	}
	
	@Test
	public void shouldCopeWithXmlDefinition() {
		String testString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><html><h1>bar</h1></html>";
		String[] testTags = new String[]{"H1"};
		DomParserFacade localFacade = new DomParserFacade();
		Map<String, List<String>> vals = localFacade.extractText(testString, testTags);
		assertEquals(1, vals.size());
		assertEquals(1, vals.get("H1").size());
		assertEquals("bar", vals.get("H1").get(0));
	}
	
}
