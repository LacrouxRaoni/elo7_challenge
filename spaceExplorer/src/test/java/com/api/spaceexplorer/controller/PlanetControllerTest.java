package com.api.spaceexplorer.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class PlanetControllerTest {

    @Autowired
    private PlanetController controller;

    @BeforeEach
    public void setup(){
        RestAssuredMockMvc.standaloneSetup(this.controller);
    }

    @Test
    void getAllPlanets_shouldReturn200_Ok() {

    }

    @Test
    void getPlanet() {

    }

    @Test
    void postPlanet() {
    }

    @Test
    void putPlanet() {
    }

    @Test
    void deletePlanet() {
    }
}