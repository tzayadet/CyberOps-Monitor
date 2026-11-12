package com.zyanetralys.cyberops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.annotation.PreDestroy;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@RestController
public class CyberOpsBackend {

    private static final int MAX_ACTIVE_STREAMS = 20;
    private static final long STREAM_TIMEOUT_MS = 60_000L;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private final Semaphore activeStreams = new Semaphore(MAX_ACTIVE_STREAMS);

    private static final String[] BOTS = {"NullPtr","CipherWolf","KernelPanic","PhantomRoot","ZeroTrust","DarkNode"};
    private static final String[] ROLES = {"SOC L2","Threat Hunter","Red Team","Reverse Eng","Cloud Sec","IR Lead"};
    private static final String[] COUNTRIES = {"US","CN","RU","IR","KP","DE","ES","BR","JP","AU"};
    private static final String[] ATTACKS = {"DDoS","C2 Beacon","SQLi","XSS","Malware Drop","Zero-Day","Data Exfil"};
    private static final String[] KEYWORDS = {"c2","malware","edr","zero-day","yara","pentest","exploit","ransomware"};

    public static void main(String[] args) {
        SpringApplication.run(CyberOpsBackend.class, args);
    }

    @GetMapping("/api/connect")
    public Map<String, String> connect() {
        return Map.of("sessionId", UUID.randomUUID().toString(), "status", "connected");
    }

    @GetMapping(value = "/api/stream/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable String id) {
        if (id == null || !id.matches("[A-Za-z0-9-]{1,64}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid stream identifier.");
        }
        if (!activeStreams.tryAcquire()) {
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Stream capacity reached. Retry after an active stream closes."
            );
        }

        SseEmitter emitter = new SseEmitter(STREAM_TIMEOUT_MS);
        AtomicBoolean released = new AtomicBoolean(false);
        AtomicReference<ScheduledFuture<?>> taskRef = new AtomicReference<>();

        Runnable cleanup = () -> {
            ScheduledFuture<?> task = taskRef.get();
            if (task != null) {
                task.cancel(true);
            }
            if (released.compareAndSet(false, true)) {
                activeStreams.release();
            }
        };

        emitter.onCompletion(cleanup);
        emitter.onTimeout(() -> {
            cleanup.run();
            emitter.complete();
        });
        emitter.onError(error -> cleanup.run());

        try {
            ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
                if (released.get()) {
                    return;
                }
                try {
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    Map<String, Object> event = random.nextDouble() < 0.65
                        ? generateChat(random)
                        : generateAttack(random);
                    emitter.send(SseEmitter.event().data(event));
                } catch (IOException | IllegalStateException error) {
                    cleanup.run();
                    emitter.completeWithError(error);
                }
            }, 100, 750, TimeUnit.MILLISECONDS);

            taskRef.set(task);
            if (released.get()) {
                task.cancel(true);
            }
        } catch (RejectedExecutionException error) {
            cleanup.run();
            emitter.completeWithError(error);
        }

        return emitter;
    }

    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdownNow();
    }

    private Map<String, Object> generateChat(ThreadLocalRandom random) {
        Map<String, String> bot = Map.of(
            "id", "B" + (random.nextInt(6) + 1),
            "handle", BOTS[random.nextInt(BOTS.length)],
            "role", ROLES[random.nextInt(ROLES.length)]
        );
        String keyword = KEYWORDS[random.nextInt(KEYWORDS.length)];
        Map<String, Object> message = new HashMap<>();
        message.put("text", "scanning " + keyword + " artifacts, updating EDR rules");
        if (random.nextBoolean()) {
            message.put("replyTarget", "@" + BOTS[random.nextInt(BOTS.length)]);
        }
        message.put("kw", keyword);
        return Map.of("type", "chat", "bot", bot, "msg", message, "ts", Instant.now().toString());
    }

    private Map<String, Object> generateAttack(ThreadLocalRandom random) {
        return Map.of(
            "type", "attack",
            "src", COUNTRIES[random.nextInt(4)],
            "tgt", COUNTRIES[4 + random.nextInt(6)],
            "attackType", ATTACKS[random.nextInt(ATTACKS.length)],
            "severity", random.nextDouble() > 0.75 ? "crit" : "high",
            "detected", random.nextBoolean(),
            "progress", 0.0,
            "ts", Instant.now().toString()
        );
    }
}
