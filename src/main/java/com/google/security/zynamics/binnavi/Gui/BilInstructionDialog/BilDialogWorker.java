package com.google.security.zynamics.binnavi.Gui.BilInstructionDialog;

import java.awt.Window;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.google.security.zynamics.binnavi.CUtilityFunctions;
import com.google.security.zynamics.binnavi.Gui.errordialog.NaviErrorDialog;
import com.google.security.zynamics.binnavi.disassembly.INaviCodeNode;
import com.google.security.zynamics.reil.translators.InternalTranslationException;

import edu.cmu.bap.client.BapClient;
import edu.cmu.bap.client.Image;
import edu.cmu.bap.client.InsnsParser;

/**
 * This is a dialog for displaying BIL. We don't want it to block the main
 * GUI thread, so it SwingWorker spawns a separate thread for this Dialog
 * @author rvt
 *
 */
public class BilDialogWorker extends SwingWorker<String, Integer> {
	final Window parent;

	final INaviCodeNode codeNode;

	public BilDialogWorker(final Window parent, final INaviCodeNode codeNode) {
		this.parent = parent;
		this.codeNode = codeNode;
	}

	@Override
	protected String doInBackground() throws Exception {
		BapClient.getInstance().init();
		Image img = BapClient.getInstance().getImage("/home/vagrant/scc32");
		System.out.println("Img resource: " + img.toString());
		System.out.println("Segments: " + img.getSegments().toString());
		//System.out.println("Cheap: " + img.getSegments().get(0).getInsns().toString());
		System.out.println("Main: " + img.findSymbol("main").getInsns());
		String text = InsnsParser.parseBil(img.findSymbol("main").getInsns());
        //System.out.println("First segment: " + img.getSegments().get(0).toString());
        //System.out.println("Insns first sym: " + img.getSegments().get(0).getSymbols().get(0).getIsns().toString());
        //System.out.println("First symbol: " + img.getSegments().get(0).getSymbols().get(0).toString());
		return text;
	}

	@Override
	protected void done() {
		try {
			// get() returns the result of doInBackground. We are guaranteed the result
			// is available in done()
			// show creates a new dialog, sets the text, and makes it visible
			CBilInstructionDialog.show(parent, codeNode, get());
		}
		catch (final InternalTranslationException | InterruptedException | ExecutionException exception) {
			CUtilityFunctions.logException(exception);

			final String message = "E00XXX: " + "Could not show BIL code";
			final String description = CUtilityFunctions.createDescription(
					String.format("BinNavi could not show the BIL code of node at '%X'.", codeNode.getAddress()),
					new String[] { "The node could not be converted to BIL code." },
					new String[] { "You can not fix this problem yourself. Please contact the " + "BinNavi support." }); // TODO

			NaviErrorDialog.show(parent, message, description, exception);
		}
	}

}
