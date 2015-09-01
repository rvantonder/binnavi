/*
Copyright 2015 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.google.security.zynamics.binnavi.Gui.BilInstructionDialog; // TODO own package

import com.google.common.collect.Lists;
import com.google.security.zynamics.binnavi.Gui.Actions.CActionProxy;
import com.google.security.zynamics.binnavi.Gui.BilInstructionDialog.CActionCopyAllBilCode;
import com.google.security.zynamics.binnavi.Gui.BilInstructionDialog.CBilInstructionDialogMenu;
import com.google.security.zynamics.binnavi.ZyGraph.Implementations.CNodeFunctions;
import com.google.security.zynamics.binnavi.disassembly.INaviCodeNode;
import com.google.security.zynamics.binnavi.disassembly.INaviInstruction;
import com.google.security.zynamics.zylib.disassembly.IInstruction; // for InstructionType
//import com.google.security.zynamics.reil.BilBlock;
//import com.google.security.zynamics.reil.BilGraph;
//import com.google.security.zynamics.reil.BilInstruction;
import com.google.security.zynamics.reil.translators.InternalTranslationException;
//import com.google.security.zynamics.reil.translators.BilTranslator;
//import com.google.security.zynamics.reil.translators.StandardEnvironment;
import com.google.security.zynamics.zylib.gui.GuiHelper;

import edu.cmu.bap.client.BapClient;
import edu.cmu.bap.client.Image;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.JSONObject;

/**
 * Dialog used to show the BIL code of a single instruction.
 */
public final class CBilInstructionDialog extends JDialog {
	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = -2412060005212653357L; // ?? TODO

	/**
	 * Text area where the BIL code is shown.
	 */
	private final JTextArea m_textArea;

	/**
	 * Creates a new dialog object.
	 *
	 * @param parent Parent window used for dialogs.
	 * @param title Title of the dialog.
	 * @param text Text to show in the dialog.
	 *
	 * @throws InternalTranslationException Thrown if the instruction could not be converted to BIL code.
	 */
	private CBilInstructionDialog(final Window parent, final String title, final String text)
			throws InternalTranslationException {
		super(parent, title);

		m_textArea = new JTextArea(text);
		m_textArea.setEditable(false);
		m_textArea.setFont(GuiHelper.MONOSPACED_FONT);
		m_textArea.addMouseListener(new PopupListener());

		addMenuBar();

		setLayout(new BorderLayout());

		add(new JScrollPane(m_textArea));

		setSize(500, 300);
	}

	/**
	 * Converts a given BIL graph to its text representation.
	 *
	 * @param graph The BIL graph
	 * @return The text representation of the BIL graph
	 */
	/**
	 * No support for now private static String reilGraphToText(final BilGraph graph) { final List<BilInstruction>
	 * reilInstructions = new ArrayList<BilInstruction>();
	 * 
	 * for (final BilBlock reilBlock : graph.getNodes()) {
	 * reilInstructions.addAll(Lists.newArrayList(reilBlock.getInstructions())); }
	 * 
	 * Collections.sort(reilInstructions, new Comparator<BilInstruction>() {
	 * 
	 * @Override public int compare(final BilInstruction lhs, final BilInstruction rhs) { return (int)
	 *           (lhs.getAddress().toLong() - rhs.getAddress().toLong()); } });
	 * 
	 *           final StringBuilder buffer = new StringBuilder();
	 * 
	 *           for (final BilInstruction reilInstruction : reilInstructions) {
	 *           buffer.append(reilInstruction.toString()); buffer.append('\n'); }
	 * 
	 *           return buffer.toString(); }
	 */

	/**
	 * Normalize BIL out put (strip newlines and braces)
	 */
	public static String normalize(String s) {
		return s.replace("{", "").replace("}", "");
	}

	/**
	 * Shows the BIL instruction dialog.
	 *
	 * @param parent Parent window used for dialogs.
	 * @param node The node whose BIL code is shown.
	 *
	 * @throws InternalTranslationException Thrown if the instruction could not be converted to BIL code.
	 */

	// curl --data "{\"load_file\": {\"url\": \"file:///home/vagrant/coreutils_O0_ls\"}, \"id\": \"25\"}" localhost:8080
	// resp: {"id":"25","resource":"7955"}
	// curl --data "{\"get_resource\": \"7955\", \"id\": \"25\"}" localhost:8080
	// resp:
	// {"id":"25","image":{"links":["mmap:/home/vagrant/coreutils_O0_ls?q=7955&length=337449&offset=0"],"arch":"armv7","entry_point":"40600","addr_size":"32","endian":"LittleEndian()","segments":["8805","7956"]}}
	//
	public static void show(final Window parent, final INaviCodeNode node, String text)
			throws InternalTranslationException {

		final String title = "BIL";
		final CBilInstructionDialog dialog = new CBilInstructionDialog(parent, title, text);

		GuiHelper.centerChildToParent(parent, dialog, true);

		dialog.setVisible(true);
	}

	/**
	 * Shows an instruction dialog.
	 *
	 * @param parent Parent window used for dialogs.
	 * @param instruction The instruction whose BIL code is shown.
	 *
	 * @throws InternalTranslationException Thrown if the instruction could not be converted to BIL code.
	 */
	/**
	 * TODO, per instruction public static void show(final Window parent, final INaviInstruction instruction) throws
	 * InternalTranslationException { final BilTranslator<INaviInstruction> translator = new
	 * BilTranslator<INaviInstruction>(); final BilGraph reilGraph = translator.translate(new StandardEnvironment(),
	 * instruction); final String text = reilGraphToText(reilGraph); final String title =
	 * String.format("BIL code of '%s'", instruction.toString());
	 * 
	 * final CBilInstructionDialog dialog = new CBilInstructionDialog(parent, title, text);
	 * 
	 * GuiHelper.centerChildToParent(parent, dialog, true);
	 * 
	 * dialog.setVisible(true); }
	 */

	/**
	 * Adds a menu bar to the dialog.
	 */
	private void addMenuBar() {
		final JMenu menu = new JMenu("BIL Code");
		final JMenuItem copyItem = new JMenuItem(CActionProxy.proxy(new CActionCopyAllBilCode(m_textArea))); // TODO
		menu.add(copyItem);

		final JMenuBar menuBar = new JMenuBar();

		menuBar.add(menu);

		setJMenuBar(menuBar);
	}

	/**
	 * Listener to show the popup menu in the JTextArea
	 */
	private class PopupListener extends MouseAdapter {
		/**
		 * Shows a context menu after the user has clicked on the text area.
		 *
		 * @param event The mouse event that triggered the popup menu.
		 */
		private void maybeShowPopup(final MouseEvent event) {
			if (event.isPopupTrigger()) {
				new CBilInstructionDialogMenu(m_textArea).show( // TODO cleanup
						event.getComponent(), event.getX(), event.getY());
			}
		}

		@Override
		public void mousePressed(final MouseEvent event) {
			maybeShowPopup(event);
		}

		@Override
		public void mouseReleased(final MouseEvent event) {
			maybeShowPopup(event);
		}
	}
}
