package com.littlebird.apigateway.router;

import com.littlebird.apigateway.model.TweetRequest;
import com.littlebird.apigateway.service.PostTweetKafkaService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Component
public class AppRouter {

    @Autowired
    private PostTweetKafkaService postTweetKafkaService;

    @Value("${post.image.local.url}")
    private String mediaurl;

    public Mono<ServerResponse> postTweetHandler(ServerRequest request)  {
        Mono<TweetRequest> postRequestMono = request.bodyToMono(TweetRequest.class);

        log.info("start processing tweet");

        String url = "image";

        return postRequestMono.flatMap(postRequest -> {
            log.info("post tweet handler called with request data: {}", postRequest);
            postRequest.setUrl(url);
            postTweetKafkaService.sendPostTweet(postRequest);
            return ServerResponse.ok().bodyValue("Tweet posted successfully");
        });
    }

    public Mono<Object> processMediaFile(ServerRequest request) {
        try {
            return request.body(BodyExtractors.toMultipartData())
                    .flatMap(map -> map.toSingleValueMap().entrySet().stream().findFirst()
                            .map(entry -> {
                                log.info("started processing media file");
                                MultipartFile multipartFile = (MultipartFile) entry.getValue();

                                if (multipartFile == null || multipartFile.isEmpty()) {
                                    return Mono.empty();
                                }

                                String fileName = multipartFile.getOriginalFilename();
                                Path fileNameAndPath = Paths.get(mediaurl, fileName);

                                try {
                                    Files.write(fileNameAndPath, multipartFile.getBytes());
                                    log.info("Media file processed successfully");

                                    // Convert the file content to String (you may adjust this based on your file content)
                                    String fileContent = new String(Files.readAllBytes(fileNameAndPath));
                                    return Mono.just(fileContent);
                                } catch (Exception e) {
                                    log.error("Failed to process media file", e);
                                    e.printStackTrace();
                                    return Mono.empty();
                                }
                            })
                            .orElse(Mono.empty())
                    );
        } catch (Exception e) {
            log.error("Failed to process media file", e);
            return Mono.empty();
        }
    }


}
