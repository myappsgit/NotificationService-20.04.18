package myapps.notification.service.utils;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduler {

	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
	private static ScheduledFuture<?> scheduler;

	public static void startScheduler() throws IOException{
		if(scheduler == null || scheduler.isCancelled())
			scheduler =  service.scheduleWithFixedDelay(new EmailQueue(), 0, 10, TimeUnit.SECONDS);
	}
	
	public static void stopScheduler(){
		scheduler.cancel(false);
	}

	public static void main(String[] args) throws IOException {
		Scheduler.startScheduler();
	}
}
