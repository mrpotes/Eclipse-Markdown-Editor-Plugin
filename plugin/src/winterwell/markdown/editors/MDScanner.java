/**
 * Copyright winterwell Mathematics Ltd.
 * @author Daniel Winterstein
 * 13 Jan 2007
 */
package winterwell.markdown.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * 
 *
 * @author Daniel Winterstein
 */
public class MDScanner extends RuleBasedScanner {
	
	public static final String MD_LINK = "__link";
	public static final String MD_HEADER = "__header";
	public static final String MD_LIST = "__list";
	public static final String MD_EMPHASIS = "__emphasis";
	public static final String MD_COMMENT = "__comment";
	public static final String MD_JEKYLL_HEADER = "__jekyllHeader";
	
    public MDScanner() {
    	Token emphasis = new Token(MD_EMPHASIS);
		List<IRule> rules = new ArrayList<IRule>(Arrays.asList(
    	           new LinkRule(new Token(MD_LINK)),
    	           new HeaderRule(new Token(MD_HEADER)),
    	           new ListRule(new Token(MD_LIST)),
    	           new EmphasisRule("_", emphasis),
    	           new EmphasisRule("***", emphasis),
    	           new EmphasisRule("**", emphasis),
    	           new EmphasisRule("*", emphasis),
    	           new MultiLineRule("<!--", "-->", new Token(MD_COMMENT)),
    	           new JekyllHeaderRule(new Token(MD_JEKYLL_HEADER))
    			));
        setRules(rules.toArray(new IRule[rules.size()]));
    }
    
    public int getOffset() {
    	return fOffset;
    }
}
