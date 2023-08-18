package io.github.ambrosia.util;

public class PerformanceProfiler {
	static long elapsed = 0;

	public static void start() {
		elapsed = System.nanoTime();
	}

	public static void end() {
		elapsed = System.nanoTime() - elapsed;
	}

	public static long getMillis() {
		return elapsed / 100000;
	}

	public static void run(Runnable runnable) {
		start();
		runnable.run();
		end();
		System.out.println(getMillis());
	}
}
