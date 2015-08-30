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
package com.google.security.zynamics.binnavi.Gui.ReilInstructionDialog; // TODO own package

import com.google.common.collect.Lists;
import com.google.security.zynamics.binnavi.Gui.Actions.CActionProxy;
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
   * @throws InternalTranslationException Thrown if the instruction could not be converted to BIL
   *         code.
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
  /** No support for now
  private static String reilGraphToText(final BilGraph graph) {
    final List<BilInstruction> reilInstructions = new ArrayList<BilInstruction>();

    for (final BilBlock reilBlock : graph.getNodes()) {
      reilInstructions.addAll(Lists.newArrayList(reilBlock.getInstructions()));
    }

    Collections.sort(reilInstructions, new Comparator<BilInstruction>() {
      @Override
      public int compare(final BilInstruction lhs, final BilInstruction rhs) {
        return (int) (lhs.getAddress().toLong() - rhs.getAddress().toLong());
      }
    });

    final StringBuilder buffer = new StringBuilder();

    for (final BilInstruction reilInstruction : reilInstructions) {
      buffer.append(reilInstruction.toString());
      buffer.append('\n');
    }

    return buffer.toString();
  }
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
   * @throws InternalTranslationException Thrown if the instruction could not be converted to BIL
   *         code.
   */
  public static void show(final Window parent, final INaviCodeNode node)
      throws InternalTranslationException {
    // IMPLEMENT HERE

    // final BilGraph graph = CNodeFunctions.copyBilCode(parent, node);
    final String title = String.format("BIL code of %s", node.getAddress().toHexString());

    // final String text = reilGraphToText(graph);
    StringBuilder text = new StringBuilder();

    for (IInstruction i : node.getInstructions()) {
        byte[] b = i.getData();
        StringBuilder sb = new StringBuilder();
        for (byte bb : b) {
            sb.append(String.format("%02x", bb));
        }
        System.out.println("Hex: " + sb.toString());

        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec("bap-mc " + sb.toString() + " --show-bil --arch=x86");
            p.waitFor();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
                e.printStackTrace();
                // text = "Error: Unable to get BIL code"; // TODO
        }

        System.out.println("Result: " + output.toString());
        text.append(normalize(output.toString()) + "\n");
    }

    final CBilInstructionDialog dialog = new CBilInstructionDialog(parent, title, text.toString());

    GuiHelper.centerChildToParent(parent, dialog, true);

    dialog.setVisible(true);
  }

  /**
   * Shows an instruction dialog.
   *
   * @param parent Parent window used for dialogs.
   * @param instruction The instruction whose BIL code is shown.
   *
   * @throws InternalTranslationException Thrown if the instruction could not be converted to BIL
   *         code.
   */
  /** TODO, per instruction
  public static void show(final Window parent, final INaviInstruction instruction)
      throws InternalTranslationException {
    final BilTranslator<INaviInstruction> translator = new BilTranslator<INaviInstruction>();
    final BilGraph reilGraph = translator.translate(new StandardEnvironment(), instruction);
    final String text = reilGraphToText(reilGraph);
    final String title = String.format("BIL code of '%s'", instruction.toString());

    final CBilInstructionDialog dialog = new CBilInstructionDialog(parent, title, text);

    GuiHelper.centerChildToParent(parent, dialog, true);

    dialog.setVisible(true);
  }
  */

  /**
   * Adds a menu bar to the dialog.
   */
  private void addMenuBar() {
    final JMenu menu = new JMenu("BIL Code");
    final JMenuItem copyItem =
        new JMenuItem(CActionProxy.proxy(new CActionCopyAllReilCode(m_textArea))); // TODO
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
        new CReilInstructionDialogMenu(m_textArea).show(                        // TODO cleanup
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
