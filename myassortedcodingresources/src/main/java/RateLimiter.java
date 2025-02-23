import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {
    private final int capacity;
    private final int refillRate;
    private final ConcurrentHashMap<String, AtomicInteger> buckets;
    private final ScheduledExecutorService scheduler;

    public RateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.buckets = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);

        //scheduler.scheduledAtFixedRate(this :: refillTokens, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::refillTokens, 1, 5, TimeUnit.SECONDS);
    }

    private void refillTokens() {
        for (String clientId : buckets.keySet()) {
            AtomicInteger bucket = buckets.get(clientId);
            if (bucket != null) {
                int newTokens = Math.min(bucket.get() + refillRate, capacity);
                bucket.set(newTokens);
            }
        }
        System.out.println("++++ buckets : " + buckets);
    }

    /**
     * Determines if a request from a specific client is allowed based on the current token count.
     *
     * @param clientId the unique identifier for the client making the request.
     * @return {@code true} if the request is allowed (i.e., there are enough tokens available);
     *         {@code false} if the request is rate-limited (i.e., no tokens are available).
     */
    public boolean allowRequest(String clientId) {
        AtomicInteger bucket = buckets.computeIfAbsent(clientId, k -> new AtomicInteger(capacity));
        while(true) {
            int currentTokens = bucket.get();
            if (currentTokens <= 0) {
                System.out.println("---- buckets : " + buckets);
                return false;
            }
            if(bucket.compareAndSet(currentTokens, currentTokens -1)) {
                System.out.println("***** buckets : " + buckets);
                return true;
            }
        }
    }

    private void shutdown() {
        scheduler.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        // Create a rate limiter with a capacity of 10 tokens and a refill rate of 5 tokens per second
        RateLimiter rateLimiter = new RateLimiter(10, 5);

        // Simulate requests from a client
        String clientId = "client1";
        for (int i = 1; i <= 20; i++) {
            if (rateLimiter.allowRequest(clientId)) {
                System.out.println("Request " + i + ": Allowed");
            } else {
                System.out.println("Request " + i + ": Rate Limited");
            }
            Thread.sleep(200); // Simulate request delay
        }

        // Shutdown the rate limiter
        rateLimiter.shutdown();
    }


}
