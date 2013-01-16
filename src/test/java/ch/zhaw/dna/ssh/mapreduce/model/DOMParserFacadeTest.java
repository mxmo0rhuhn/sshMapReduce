package ch.zhaw.dna.ssh.mapreduce.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DOMParserFacadeTest {
	
	
	@Test
	public void getContentOfTag(){
		String testString = "<a>Link zur ZHAW</a>";
		String[] testTags = new String[]{"h1","h2","a"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		assertEquals("Link zur ZHAW",localFacade.extractText(testString, testTags));
		
	}
	
	@Test
	public void getEmptyCausedByWrongTag(){
		String testString = "<a>Link zur ZHAW</a>";
		String[] testTags = new String[]{"h1","h2","p"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		assertEquals("",localFacade.extractText(testString, testTags));
		
	}
	
	@Test
	public void getMultpleContentSameTag(){
		String testString = "<a>Link zur ZHAW</a><a href=\"index.html\">Another Test</a>";
		String[] testTags = new String[]{"h1","h2","a"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		assertEquals("Link zur ZHAW Another Test",localFacade.extractText(testString, testTags));
		
	}
	
	@Test
	public void getNestedContentDifferentTag(){
		String testString = "<a><h1>Link zur ZHAW</h1></a>";
		String[] testTags = new String[]{"h1","h2","a"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		assertEquals("Link zur ZHAW",localFacade.extractText(testString, testTags));
		
	}
	
	@Test
	public void getNestedContentDifferentTagSplit(){
		String testString = "<a>Link zur <h1>ZHAW</h1></a>";
		String[] testTags = new String[]{"h1","h2","a"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		assertEquals("Link zur ZHAW",localFacade.extractText(testString, testTags));
		
	}
	
	@Test
	public void getNestedContentWrongTagSplit(){
		String testString = "<a>Link zur <h1>ZHAW</h1></a>";
		String[] testTags = new String[]{"h2","a"};
		
		DomParserFacade localFacade = new DomParserFacade();
		
		assertEquals("Link zur ZHAW",localFacade.extractText(testString, testTags));
		
	}

}
