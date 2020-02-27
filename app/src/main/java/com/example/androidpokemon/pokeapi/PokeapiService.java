package com.example.androidpokemon.pokeapi;

import com.example.androidpokemon.models.PokemonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonResponse> obtenerListPokemon(@Query("limit") int limit, @Query("offset") int offset);

}
