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
package com.google.security.zynamics.binnavi.ZyGraph.Menus.Actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.google.security.zynamics.binnavi.ZyGraph.Implementations.CInstructionFunctions;
import com.google.security.zynamics.binnavi.disassembly.INaviCodeNode;


/**
 * Action class used to show the BIL Code
 */
public final class CActionShowBilCodeNode extends AbstractAction {
  /**
   * Used for serialization.
   */
  private static final long serialVersionUID = -9000048574652376838L; // ??

  /**
   * Parent window for dialogs.
   */
  private final Window m_parent;

  /**
   * The node whose BIL code is shown.
   */
  private final INaviCodeNode m_node;

  /**
   * Creates a new action object.
   *
   * @param parent Parent window for dialogs.
   * @param node The node whose BIL code is shown.
   */
  public CActionShowBilCodeNode(final Window parent, final INaviCodeNode node) {
    super("Show BIL code");

    m_parent = parent;
    m_node = node;
  }

  @Override
  public void actionPerformed(final ActionEvent event) {
    CInstructionFunctions.showBilCode(m_parent, m_node); // TODO
  }
}
