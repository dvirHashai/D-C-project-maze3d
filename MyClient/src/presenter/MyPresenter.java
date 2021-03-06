package presenter;

import java.io.IOException;
import java.util.Observable;

import model.Model;
import view.View;

public class MyPresenter extends MyAbstractPresenter {

	/**
	 * @param m
	 * @param v
	 */
	public MyPresenter(Model m, View v) {
		super(m, v);

	}

	/* (non-Javadoc)
	 * @see presenter.MyAbstractPresenter#update(java.util.Observable, java.lang.Object)
	 */
	//to get the notify from the model or the client and to route it
	@Override
	public void update(Observable o, Object arg) {
		if (o == model) {
			if (arg != null) {
				view.DisplySolution(arg);
			} else {
				String[] s = model.getupdateData();
				view.sendDisplySolution(s);
			}

		}
		if (o == view) {
			String command = view.getCommandRegex();
			String[] msg = view.getUserCommand();
			try {
				CommandsMap.get(command).docommand(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
