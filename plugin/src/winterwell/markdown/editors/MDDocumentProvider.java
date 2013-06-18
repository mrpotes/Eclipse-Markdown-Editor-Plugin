package winterwell.markdown.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.RuleBasedPartitioner;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import winterwell.markdown.Activator;

public class MDDocumentProvider extends FileDocumentProvider {
	
	private Map<Object,IDocument> documents = new HashMap<Object,IDocument>();

	protected IDocument createEmptyDocument() {
		return new MDDocument();
	}
	
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument d = super.createDocument(element);
		if (d != null && d instanceof MDDocument) {
			MDDocument document = (MDDocument) d;
			MDScanner scanner = new MDScanner();
			IDocumentPartitioner partitioner =
				new RuleBasedPartitioner(
					scanner,
					new String[] {
						IDocument.DEFAULT_CONTENT_TYPE,
						MDScanner.MD_COMMENT,
						MDScanner.MD_EMPHASIS,
						MDScanner.MD_HEADER,
						MDScanner.MD_JEKYLL_HEADER,
						MDScanner.MD_LINK,
						MDScanner.MD_LIST
					});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		IPathEditorInput pathEditorInput = (IPathEditorInput) ((org.eclipse.core.runtime.IAdaptable)element).getAdapter(IPathEditorInput.class);
		if (pathEditorInput != null) {
			documents.put(pathEditorInput.getPath().toOSString(), d);
		}
		return d;
	}
	
	@Override
	public IDocument getDocument(Object element) {
		IDocument document = super.getDocument(element);
		if (document != null) return document;
		IPathEditorInput pathEditorInput = (IPathEditorInput) ((org.eclipse.core.runtime.IAdaptable)element).getAdapter(IPathEditorInput.class);
		return pathEditorInput == null ? null : documents.get(pathEditorInput.getPath().toOSString());
	}
	
	@Override
	protected boolean setDocumentContent(IDocument document, IEditorInput editorInput, String encoding) throws CoreException {
		if (super.setDocumentContent(document, editorInput, encoding)) return true;
		IPathEditorInput pathEditorInput = (IPathEditorInput) ((org.eclipse.core.runtime.IAdaptable)editorInput).getAdapter(IPathEditorInput.class);
		if (pathEditorInput != null) {
			try {
				setDocumentContent(document, new FileInputStream(pathEditorInput.getPath().toFile()), encoding);
			} catch (FileNotFoundException e) {
				throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, "Could not find file", e));
			}
			return true;
		}
		return false;
	}
	
}
