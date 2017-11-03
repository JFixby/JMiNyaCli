
package org.litecoinpool.miner;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class Miner implements Observer {

	private static final String DEFAULT_URL = "http://127.0.0.1:8332/";
	private static final String DEFAULT_AUTH = "nyatla.1:x";
	private static final long DEFAULT_SCAN_TIME = 5000;
	private static final long DEFAULT_RETRY_PAUSE = 30000;

	private Worker worker;
	private long lastWorkTime;
	private long lastWorkHashes;

	public Miner (final String url, final String auth, final long scanTime, final long retryPause, final int nThread,
		final double throttle) {
		if (nThread < 1) {
			throw new IllegalArgumentException("Invalid number of threads: " + nThread);
		}
		if (throttle <= 0.0 || throttle > 1.0) {
			throw new IllegalArgumentException("Invalid throttle: " + throttle);
		}
		if (scanTime < 1L) {
			throw new IllegalArgumentException("Invalid scan time: " + scanTime);
		}
		if (retryPause < 0L) {
			throw new IllegalArgumentException("Invalid retry pause: " + retryPause);
		}
		try {
			this.worker = new Worker(new URL(url), auth, scanTime, retryPause, nThread, throttle);
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException("Invalid URL: " + url);
		}
		this.worker.addObserver(this);
		final Thread t = new Thread(this.worker);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
		this.log(nThread + " miner threads started");
	}

	private static final DateFormat logDateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");

	public void log (final String str) {
		System.out.println(logDateFormat.format(new Date()) + str);
	}

	@Override
	public void update (final Observable o, final Object arg) {
		final Worker.Notification n = (Worker.Notification)arg;
		if (n == Worker.Notification.SYSTEM_ERROR) {
			this.log("System error");
			System.exit(1);
		} else if (n == Worker.Notification.PERMISSION_ERROR) {
			this.log("Permission error");
			System.exit(1);
		} else if (n == Worker.Notification.AUTHENTICATION_ERROR) {
			this.log("Invalid worker username or password");
			System.exit(1);
		} else if (n == Worker.Notification.CONNECTION_ERROR) {
			this.log("Connection error, retrying in " + this.worker.getRetryPause() / 1000L + " seconds");
		} else if (n == Worker.Notification.COMMUNICATION_ERROR) {
			this.log("Communication error");
		} else if (n == Worker.Notification.LONG_POLLING_FAILED) {
			this.log("Long polling failed");
		} else if (n == Worker.Notification.LONG_POLLING_ENABLED) {
			this.log("Long polling activated");
		} else if (n == Worker.Notification.NEW_BLOCK_DETECTED) {
			this.log("LONGPOLL detected new block");
		} else if (n == Worker.Notification.POW_TRUE) {
			this.log("PROOF OF WORK RESULT: true (yay!!!)");
		} else if (n == Worker.Notification.POW_FALSE) {
			this.log("PROOF OF WORK RESULT: false (booooo)");
		} else if (n == Worker.Notification.NEW_WORK) {
			if (this.lastWorkTime > 0L) {
				final long hashes = this.worker.getHashes() - this.lastWorkHashes;
				final float speed = (float)hashes / Math.max(1, System.currentTimeMillis() - this.lastWorkTime);
				this.log(String.format("%d hashes, %.2f khash/s", hashes, speed));
			}
			this.lastWorkTime = System.currentTimeMillis();
			this.lastWorkHashes = this.worker.getHashes();
		}
	}

	public static void main (final String[] args) {
		ScarabeiDesktop.deploy();
		String url = DEFAULT_URL;
		String auth = DEFAULT_AUTH;
		int nThread = Runtime.getRuntime().availableProcessors();
		double throttle = 1.0;
		long scanTime = DEFAULT_SCAN_TIME;
		long retryPause = DEFAULT_RETRY_PAUSE;

		if (args.length > 0 && args[0].equals("--help")) {
			System.out.println("Usage:  java Miner [URL] [USERNAME:PASSWORD] [THREADS] [THROTTLE] [SCANTIME] [RETRYPAUSE]");
			return;
		}

		if (args.length > 0) {
			url = args[0];
		}
		if (args.length > 1) {
			auth = args[1];
		}
		if (args.length > 2) {
			nThread = Integer.parseInt(args[2]);
		}
		if (args.length > 3) {
			throttle = Double.parseDouble(args[3]);
		}
		if (args.length > 4) {
			scanTime = Integer.parseInt(args[4]) * 1000L;
		}
		if (args.length > 5) {
			retryPause = Integer.parseInt(args[5]) * 1000L;
		}

		try {
			new Miner(url, auth, scanTime, retryPause, nThread, throttle);
		} catch (final Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
