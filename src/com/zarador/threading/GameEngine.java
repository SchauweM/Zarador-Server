package com.zarador.threading;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import com.zarador.GameServer;
import com.zarador.threading.task.Task;

public final class GameEngine implements Runnable {

	private final BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();
	private final ScheduledExecutorService logicService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("Logic"));
	private final BlockingExecutorService taskService = new BlockingExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("Task")));
	private final ExecutorService workService = Executors.newSingleThreadExecutor(new NamedThreadFactory("Work"));
	private boolean running = false;
	private final Thread thread;

	public GameEngine() {
		thread = new Thread(this, "GameEngine");
	}

	public boolean isRunning() {
		return running;
	}

	public void start() {
		if (running) {
			throw new IllegalStateException("The engine is already running.");
		}
		running = true;
		thread.start();
	}

	@Override
	public void run() {
		try {
			while (running) {
				try {
					this.submitLogic(new LogicTask(tasks.take()));
				} catch (InterruptedException e) {
					continue;
				}
			}
		} finally {
			logicService.shutdown();
			taskService.shutdown();
			workService.shutdown();
		}
	}

	public void stop() {
		if (!running) {
			throw new IllegalStateException("The engine is already stopped.");
		}
		running = false;
		tasks.notify();
	}

	public void pushTask(Task task) {
		if (!running) {
			return;
		}
		if (task == null) {
			GameServer.getLogger().log(Level.WARNING, "Null task submitted");
			return;
		}
		tasks.offer(task);
	}

	public ScheduledFuture<?> scheduleLogic(Runnable runnable, long delay, TimeUnit unit) {
		if (!running) {
			return null;
		}
		return logicService.schedule(new ServerTask(runnable), delay, unit);
	}

	public void submitTask(Runnable runnable) {
		if (!running) {
			return;
		}
		taskService.submit(new ServerTask(runnable));
	}

	public void submitWork(Runnable runnable) {
		if (!running) {
			return;
		}
		workService.submit(new ServerTask(runnable));
	}

	public void submitLogic(Runnable runnable) {
		if (!running) {
			return;
		}
		logicService.submit(new ServerTask(runnable));
	}

	public void waitForPendingParallelTasks() throws ExecutionException {
		taskService.waitForPendingTasks();
	}

	private final class LogicTask implements Runnable {
		private final Task task;

		private LogicTask(Task task) {
			this.task = task;
		}

		@Override
		public void run() {

			task.execute(GameEngine.this);
		}
	}

	private static final class ServerTask implements Runnable {
		private final Runnable runnable;

		private ServerTask(Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void run() {
			try {
				runnable.run();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private static final class NamedThreadFactory implements ThreadFactory {
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		private NamedThreadFactory(String namePrefix) {
			SecurityManager s = System.getSecurityManager();
			this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			this.namePrefix = namePrefix + '-';
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon()) {
				t.setDaemon(false);
			}
			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}
	}
}
