package gc.garcol.apputil.observation;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

public class TracingUtil {

    private static final ThreadLocal<Span> SPAN_HOLDER = new ThreadLocal<>();

    public static void startNewSpan(String name) {
        var span = Tracer.NOOP.nextSpan().name(name);
        SPAN_HOLDER.set(span);
        span.start();
    }

    public static Span getAndRemove() {
        var span = SPAN_HOLDER.get();
        SPAN_HOLDER.remove();
        return span;
    }

    public static void end(Span span) {
        span.end();
    }
}
