/**
 * Copyright winterwell Mathematics Ltd.
 * @author Daniel Winterstein
 * 11 Jan 2007
 */
package winterwell.markdown.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

import winterwell.markdown.Activator;
import winterwell.markdown.preferences.MarkdownPreferencePage;

/**
 * 
 *
 * @author Daniel Winterstein
 */
public class JekyllHeaderRule extends MultiLineRule {
		
	public JekyllHeaderRule(IToken token) {	
		super("---", "---", token);
	}
	

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 * @since 2.0
	 */
	public IToken doEvaluate(ICharacterScanner s, boolean resume) {
		MDScanner scanner = (MDScanner)s;
		if (scanner.getOffset() != 0 || !Activator.getDefault().getPreferenceStore().getBoolean(MarkdownPreferencePage.PREF_JEKYLL)) {
			return Token.UNDEFINED;
		}
		IToken token = super.doEvaluate(scanner, resume);
		return token;
	}
}
