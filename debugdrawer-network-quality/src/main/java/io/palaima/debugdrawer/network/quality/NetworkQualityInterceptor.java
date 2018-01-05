package io.palaima.debugdrawer.network.quality;

import android.util.Log;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Response;

public class NetworkQualityInterceptor implements Interceptor {

    private static final Double DELAY_VARIANCE = 0.40;

    private final NetworkQualityConfig config;
    private final Random random;

    NetworkQualityInterceptor(NetworkQualityConfig config) {
        this.config = config;
        this.random = new Random();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!config.networkEnabled()) {
            throw new IOException("Mock: No Internet connection");
        }
        sleepDelay();
        if (isFailure()) {
            throw new IOException("Mock: Request failure");
        } else {
            return chain.proceed(chain.request());
        }
    }

    private void sleepDelay() throws IOException {
        final Long delay = calculateDelayMs();
        if (delay > 0) {
            try {
                Log.d("NetworkQuality", "Applying delay of $delay ms");
                Thread.sleep(delay);
            } catch (InterruptedException ie) {
                throw new IOException("Delay interrupted");
            }

        }
    }

    private long calculateDelayMs() {
        final double lowerBound = 1.0f - DELAY_VARIANCE;
        final double upperBound = 1.0f + DELAY_VARIANCE;
        final double bound = upperBound - lowerBound;
        final double delayPercent = lowerBound + random.nextFloat() * bound;
        return (long) (config.delayMs() * delayPercent);
    }

    private boolean isFailure() {
        return random.nextInt(100) < config.errorPercentage();
    }
}
