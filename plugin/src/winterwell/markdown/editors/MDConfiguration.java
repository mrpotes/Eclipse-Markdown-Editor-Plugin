package winterwell.markdown.editors;

import static winterwell.markdown.editors.MDScanner.MD_COMMENT;
import static winterwell.markdown.editors.MDScanner.MD_EMPHASIS;
import static winterwell.markdown.editors.MDScanner.MD_HEADER;
import static winterwell.markdown.editors.MDScanner.MD_JEKYLL_HEADER;
import static winterwell.markdown.editors.MDScanner.MD_LINK;
import static winterwell.markdown.editors.MDScanner.MD_LIST;
import static winterwell.markdown.preferences.MarkdownPreferencePage.PREF_COMMENT;
import static winterwell.markdown.preferences.MarkdownPreferencePage.PREF_DEFUALT;
import static winterwell.markdown.preferences.MarkdownPreferencePage.PREF_HEADER;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import winterwell.markdown.Activator;

public class MDConfiguration extends TextSourceViewerConfiguration {
	private ColorManager cm;
	private List<SingleTokenScanner> scanners = new ArrayList<SingleTokenScanner>();

	public MDConfiguration(ColorManager colorManager, IPreferenceStore prefStore) {
		super(prefStore);
		this.cm = colorManager;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			MD_COMMENT,
			MD_EMPHASIS,
			MD_HEADER,
			MD_JEKYLL_HEADER,
			MD_LINK,
			MD_LIST
		};
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler pr = new PresentationReconciler();
		addReconciler(pr, IDocument.DEFAULT_CONTENT_TYPE, PREF_DEFUALT, SWT.NORMAL);
		addReconciler(pr, MD_COMMENT, PREF_COMMENT, SWT.NORMAL);
		addReconciler(pr, MD_EMPHASIS, PREF_DEFUALT, SWT.ITALIC);
		addReconciler(pr, MD_HEADER, PREF_HEADER, SWT.BOLD);
		addReconciler(pr, MD_JEKYLL_HEADER, PREF_COMMENT, SWT.ITALIC);
		addReconciler(pr, MD_LINK, PREF_COMMENT, TextAttribute.UNDERLINE);
		addReconciler(pr, MD_LIST, PREF_HEADER, SWT.NORMAL);
		return pr;
	}

	private void addReconciler(PresentationReconciler pr, String contentType, String pref, int fontMod) {
		SingleTokenScanner scanner = new SingleTokenScanner(cm, fPreferenceStore, pref, fontMod);
		scanners.add(scanner);
		DefaultDamagerRepairer ddr = new DefaultDamagerRepairer(scanner);
		pr.setRepairer(ddr, contentType);
		pr.setDamager(ddr, contentType);
	}
	
	void updateColours() {
		for (SingleTokenScanner s: scanners) s.setDefaultReturnToken();
	}

	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		private String pref;
		private IPreferenceStore prefStore;
		private int fontMod;
		private ColorManager cm;

		public SingleTokenScanner(ColorManager cm, IPreferenceStore prefStore, String pref, int fontMod) {
			this.pref = pref;
			this.prefStore = prefStore;
			this.fontMod = fontMod;
			this.cm = cm;
			setDefaultReturnToken();
		}

		private void setDefaultReturnToken() {
			setDefaultReturnToken(new Token(new TextAttribute(cm.getColor(PreferenceConverter.getColor(prefStore, pref)), null, fontMod)));
		}
	}
	
	@SuppressWarnings("unused")
	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		if (true) return super.getTextHover(sourceViewer, contentType);
		// Add hover support for images
		return new MDTextHover();
	}
}


