package winterwell.markdown;

import static winterwell.markdown.preferences.MarkdownPreferencePage.*;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "winterwell.markdown";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeDefaultPluginPreferences() {
		IPreferenceStore pStore = getPreferenceStore();
		pStore.setDefault(PREF_WORD_WRAP, false);
		pStore.setDefault(PREF_FOLDING, true);
		pStore.setDefault(PREF_TASK_TAGS, true);
		pStore.setDefault(PREF_TASK_TAGS_DEFINED, "TODO,FIXME,??");
		pStore.setDefault(PREF_MARKDOWN_COMMAND, MARKDOWNJ);
		pStore.setDefault(PREF_SECTION_NUMBERS, true);
		pStore.setDefault(PREF_JEKYLL, false);
		PreferenceConverter.setDefault(pStore, PREF_DEFUALT, DEF_DEFAULT);
		PreferenceConverter.setDefault(pStore, PREF_COMMENT, DEF_COMMENT);
		PreferenceConverter.setDefault(pStore, PREF_HEADER, DEF_HEADER);
		PreferenceConverter.setDefault(pStore, PREF_HEADER, DEF_LINK);
	}
}
