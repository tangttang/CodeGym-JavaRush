package editorhtml.actions;

import editorhtml.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RedoAction extends AbstractAction {
    private View view;

    public RedoAction(View view) {
        this.view = view;
    }


    public boolean accept(Object sender) {
        return false;
    }


    public void actionPerformed(ActionEvent e) {
        view.redo();
    }
}
