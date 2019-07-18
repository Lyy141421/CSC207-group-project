package ActionListeners.ReferenceActionListeners;

import ApplicantStuff.Reference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ReferenceActionListener implements ActionListener {

    private Reference reference;

    ReferenceActionListener(Reference reference) {
        this.reference = reference;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
}
