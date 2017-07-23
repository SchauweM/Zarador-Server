package com.zarador;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.zarador.engine.task.TaskManager;
import com.zarador.net.packet.Packet;
import com.zarador.world.World;

import java.awt.*;
import java.util.LinkedList;

public class GamePanel extends JFrame {

	private LinkedList<Long> cycleTimes = new LinkedList<>();
	private LinkedList<Packet> slowPackets = new LinkedList<>();
	private long maxCycle = 0;
	private long maxMemory = 0;

	private JPanel contentPane;
	private JLabel cycleTimeLabel;
	private JLabel memoryLabel;
	private JLabel playersOnlineLabel;
	private JLabel maxCycleLabel;
	private JLabel maxMemoryLabel;
	private JLabel tasksLabel;
	private JLabel loginCycleLabel;
	private JLabel logoutCycleLabel;
	private JLabel minigameCycleLabel;
	private JLabel entityUpdateLabel;
	private JLabel taskCycleLabel;
	private JLabel slowPacketsLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				//GamePanel frame = new GamePanel();
				//frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GamePanel() {
		setTitle("Argos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 313);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		playersOnlineLabel = new JLabel("Players Online: -");

		memoryLabel = new JLabel("Memory: -");

		cycleTimeLabel = new JLabel("Cycle times: -");

		maxCycleLabel = new JLabel("Max Cycle: -");

		maxMemoryLabel = new JLabel("Max Memory: -");

		tasksLabel = new JLabel("Tasks: -");

		loginCycleLabel = new JLabel("Login Cycle: -");

		logoutCycleLabel = new JLabel("Logout Cycle: -");

		minigameCycleLabel = new JLabel("Minigame Cycle: -");

		entityUpdateLabel = new JLabel("Entity Update Cycle: -");

		taskCycleLabel = new JLabel("Task Cycle: -");

		slowPacketsLabel = new JLabel("Slow Packets: -");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(playersOnlineLabel)
						.addComponent(memoryLabel).addComponent(cycleTimeLabel).addComponent(maxMemoryLabel)
						.addComponent(maxCycleLabel).addComponent(tasksLabel).addComponent(taskCycleLabel)
						.addComponent(logoutCycleLabel).addComponent(loginCycleLabel).addComponent(minigameCycleLabel)
						.addComponent(entityUpdateLabel).addComponent(slowPacketsLabel))
				.addContainerGap(288, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addComponent(playersOnlineLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(memoryLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(maxMemoryLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(cycleTimeLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(maxCycleLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(tasksLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(taskCycleLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(loginCycleLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(logoutCycleLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(minigameCycleLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(entityUpdateLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(slowPacketsLabel)
						.addContainerGap(20, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}

	public void addCycleTime(long cycleTime) {
		cycleTimes.add(cycleTime);
		if (cycleTimes.size() > 20)
			cycleTimes.poll();

		StringBuilder sb = new StringBuilder();
		sb.append("Cycle times: ");
		for (long cycle : cycleTimes) {
			sb.append(cycle + ", ");
		}
		cycleTimeLabel.setText(sb.toString());
		if (System.currentTimeMillis() - GameServer.getStartTime() > 25000) {
			if (cycleTime > maxCycle) {
				maxCycle = cycleTime;
				maxCycleLabel.setText("Max Cycle: " + maxCycle);
			}
		}
	}

	public void addWorldCycle(long loginCycle, long logoutCycle, long minigameCycle, long entityUpdateCycle) {
		loginCycleLabel.setText("Login Cycle: " + loginCycle);
		logoutCycleLabel.setText("Logout Cycle: " + logoutCycle);
		minigameCycleLabel.setText("Minigame Cycle: " + minigameCycle);
		entityUpdateLabel.setText("Entity Update Cycle: " + entityUpdateCycle);
	}

	public void addTaskCycle(long taskCycle) {
		taskCycleLabel.setText("Task Cycle: " + taskCycle);
	}

	public void addGeneral() {
		playersOnlineLabel.setText("Players Online: " + (World.getPlayers().size() + GameSettings.fakePlayerCount));
		long memory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		memoryLabel.setText("Memory: " + memory / (1024 * 1024) + " Mb");
		if (System.currentTimeMillis() - GameServer.getStartTime() > 25000) {
			if (memory > maxMemory) {
				maxMemory = memory;
				maxMemoryLabel.setText("Max Memory: " + maxMemory / (1024 * 1024) + " Mb");
			}
		}
		tasksLabel.setText("Tasks: " + TaskManager.getTaskAmount());
	}

	public void addPacket(Packet p) {
		slowPackets.add(p);
		if (slowPackets.size() > 5)
			slowPackets.poll();
		StringBuilder sb = new StringBuilder();
		sb.append("Slow Packets: ");
		for (Packet packet : slowPackets) {
			sb.append("[" + packet.getOpcode() + "/" + packet.getTime() + "], ");
			slowPacketsLabel.setText(sb.toString());
		}

	}

	public JLabel getSlowPacketsLabel() {
		return slowPacketsLabel;
	}
}
