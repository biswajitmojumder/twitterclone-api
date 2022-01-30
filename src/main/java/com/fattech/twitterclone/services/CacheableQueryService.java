package com.fattech.twitterclone.services;

import com.fattech.twitterclone.repos.FollowRepo;
import com.fattech.twitterclone.repos.HomeFollowingPlayerIdsRedisRepo;
import com.fattech.twitterclone.repos.PlayerFollowerIdsRedisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CacheableQueryService {
    private final HomeFollowingPlayerIdsRedisRepo homeFollowingPlayerIdsRedisRepo;
    private final FollowRepo followRepo;
    private final PlayerFollowerIdsRedisRepo playerFollowerIdsRedisRepo;

    @Autowired
    public CacheableQueryService(HomeFollowingPlayerIdsRedisRepo homeFollowingPlayerIdsRedisRepo,
                                 FollowRepo followRepo, PlayerFollowerIdsRedisRepo playerFollowerIdsRedisRepo) {
        this.homeFollowingPlayerIdsRedisRepo = homeFollowingPlayerIdsRedisRepo;
        this.followRepo = followRepo;
        this.playerFollowerIdsRedisRepo = playerFollowerIdsRedisRepo;
    }

    public List<Long> getHomeFollowingPlayerIds(Long playerId) {
        var resultFromRedis = homeFollowingPlayerIdsRedisRepo.findByPlayerId(playerId);
        if (Objects.isNull(resultFromRedis)) {
            var resultFromDb = followRepo.getHomeFollowingPlayerIds(playerId);
            homeFollowingPlayerIdsRedisRepo.saveByPlayerId(playerId, resultFromDb);
            return resultFromDb;
        }
        return resultFromRedis;
    }

    public void updateHomeFollowingPlayerIds(Long playerId, Long newFollowingPlayerId) {
        var resultFromRedis = homeFollowingPlayerIdsRedisRepo.findByPlayerId(playerId);
        if (Objects.isNull(resultFromRedis)) {
            var resultFromDb = followRepo.getHomeFollowingPlayerIds(playerId);
            homeFollowingPlayerIdsRedisRepo.saveByPlayerId(playerId, resultFromDb);
        } else {
            resultFromRedis.add(newFollowingPlayerId);
            homeFollowingPlayerIdsRedisRepo.saveByPlayerId(playerId, resultFromRedis);
        }
    }

    public void forceDbQueryGetHomeFollowingPlayerIds(Long playerId) {
        var resultFromRedis = homeFollowingPlayerIdsRedisRepo.findByPlayerId(playerId);
        if (!Objects.isNull(resultFromRedis)) {
            homeFollowingPlayerIdsRedisRepo.deleteByPlayerId(playerId);
        }
    }

    public List<Long> getPlayerFollowerIds(Long playerId) {
        var resultFromRedis = playerFollowerIdsRedisRepo.findByPlayerId(playerId);
        if (Objects.isNull(resultFromRedis)) {
            var resultFromDb = followRepo.getFollowerIdsByPlayerId(playerId);
            playerFollowerIdsRedisRepo.saveByPlayerId(playerId, resultFromDb);
            return resultFromDb;
        }
        return resultFromRedis;
    }

    public void updatePlayerFollowerIds(Long playerId, Long newPlayerFollowerId) {
        var resultFromRedis = playerFollowerIdsRedisRepo.findByPlayerId(playerId);
        if (Objects.isNull(resultFromRedis)) {
            var resultFromDb = followRepo.getFollowerIdsByPlayerId(playerId);
            playerFollowerIdsRedisRepo.saveByPlayerId(playerId, resultFromDb);
        } else {
            resultFromRedis.add(newPlayerFollowerId);
            playerFollowerIdsRedisRepo.saveByPlayerId(playerId, resultFromRedis);
        }
    }

    public void forceDbQueryGetPlayerFollowerIds(Long playerId) {
        var resultFromRedis = playerFollowerIdsRedisRepo.findByPlayerId(playerId);
        if (!Objects.isNull(resultFromRedis)) {
            playerFollowerIdsRedisRepo.deleteByPlayerId(playerId);
        }
    }
}
