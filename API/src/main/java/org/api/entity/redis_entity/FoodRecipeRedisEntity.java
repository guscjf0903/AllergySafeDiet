package org.api.entity.redis_entity;

import org.springframework.data.annotation.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.dto.IngredientsDto;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "foodRecipe", timeToLive = 3600)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FoodRecipeRedisEntity {
    @Id
    private String foodName;
    private List<String> ingredients;
}
