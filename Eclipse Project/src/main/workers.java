package main;

import java.awt.*;
import java.text.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

public class workers {

	

	static class GameClock extends JPanel {
		
		SimpleDateFormat timeFormat;
		JLabel timeLabel = new JLabel();
		String time;

		public GameClock(JPanel panel) {

			timeFormat = new SimpleDateFormat("hh:mm:ss a");
			time = timeFormat.format(Calendar.getInstance().getTime());
			timeLabel.setVisible(true);

			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					time = timeFormat.format(Calendar.getInstance().getTime());
					System.out.println(time);
					timeLabel.setText(time);
					panel.add(timeLabel);
					
				}
			};
			timer.scheduleAtFixedRate(task, 0, 1000);
			
		}
	}
	
	class Moves {
		
	}
}