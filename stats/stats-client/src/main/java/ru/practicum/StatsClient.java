package ru.practicum;

import org.springframework.http.ResponseEntity;
import ru.practicum.model.HitInput;
import org.springframework.web.client.RestTemplate;
import ru.practicum.model.HitOutput;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatsClient {

    private final RestTemplate rest = new RestTemplate();

    public ResponseEntity<HitInput> sendStatsHit(String ip, String app, String uri) {
        var input = new HitInput();
        input.setIp(ip);
        input.setUri(uri);
        input.setApp(app);

        return rest.postForEntity("http://localhost:9090/hit", input, HitInput.class);
    }

    public ResponseEntity<HitOutput> getHits(LocalDateTime startTime,
                                             LocalDateTime endTime,
                                             String uri,
                                             boolean unique) {

        Map<String, Object> parameters = Map.of(
                "startTime", startTime,
                "endTime", endTime,
                "uris", uri,
                "unique", unique
        );
        return rest.getForEntity("http://localhost:9090/stats", HitOutput.class, parameters);
    }
}
