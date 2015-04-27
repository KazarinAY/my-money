package kazarin.my_money.gui;

import javax.swing.JFrame;

class FrameHolder {
	private static JFrame frame;

	public static JFrame getFrame() {
		return frame;
	}

	public static void setFrame(JFrame newFrame) {
		frame = newFrame;
	}

}
