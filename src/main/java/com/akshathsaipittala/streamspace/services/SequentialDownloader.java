package com.akshathsaipittala.streamspace.services;

import bt.Bt;
import bt.data.Storage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.protocol.crypto.EncryptionPolicy;
import bt.runtime.BtClient;
import bt.runtime.Config;
import com.akshathsaipittala.streamspace.entity.DownloadTask;
import com.akshathsaipittala.streamspace.entity.Movie;
import com.akshathsaipittala.streamspace.repository.MovieRepository;
import com.akshathsaipittala.streamspace.utils.ApplicationConstants;
import com.akshathsaipittala.streamspace.utils.RuntimeHelper;
import com.akshathsaipittala.streamspace.utils.TorrentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SequentialDownloader {

    final RuntimeHelper runtimeHelper;
    final MovieRepository movieRepository;

    public BtClient createSequentialDownloadClient(DownloadTask task, Storage storage) {

        return Bt
                .client()
                .magnet(TorrentUtils.getMagnetUri(task.getTorrentHash()))
                .storage(storage)
                .afterTorrentFetched(torrent -> {
                    String torrentName = torrent.getName();
                    // Set the custom filename for each file in the torrent
                    torrent.getFiles().forEach(file -> {
                        // Set the custom filename for the file
                        file.getPathElements().forEach(fileName -> {
                                    if (fileName.endsWith(".mp4") || fileName.endsWith(".mkv") || fileName.endsWith(".avi")) {
                                        Movie movie = new Movie();
                                        log.info("Size {}", file.getSize());
                                        movie.setContentLength(file.getSize());
                                        movie.setName(fileName);
                                        movie.setSummary(fileName);
                                        movie.setContentMimeType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                                        log.info(runtimeHelper.SPRING_CONTENT_MOVIES_STORE + torrentName + "/" + fileName);
                                        movie.setContentId(runtimeHelper.SPRING_CONTENT_MOVIES_STORE + torrentName + "/" + fileName);
                                        movie.setMovieCode(task.getMovieCode());
                                        movie.setMediaSource(ApplicationConstants.TORRENT);
                                        //movie.setMovieCode(task.getTorrentHash());
                                        movieRepository.save(movie);
                                    }
                                }
                        );
                    });
                })
                .config(new Config() {
                    @Override
                    public EncryptionPolicy getEncryptionPolicy() {
                        return EncryptionPolicy.REQUIRE_ENCRYPTED;
                    }

                    @Override
                    public int getNumOfHashingThreads() {
                        return Runtime.getRuntime().availableProcessors();
                    }

                    /*@Override
                    public int getAcceptorPort() {
                        try {
                            return TorrentUtils.getRandomFreePort();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }*/

                })
                .autoLoadModules()
                .module(dhtModule())
                .sequentialSelector()
                .stopWhenDownloaded()
                .build();
    }

    // enable bootstrapping from public routers
    private DHTModule dhtModule() {
        return new DHTModule(new DHTConfig() {
            @Override
            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });
    }

}
