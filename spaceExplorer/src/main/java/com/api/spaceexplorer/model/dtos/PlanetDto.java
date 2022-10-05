package com.api.spaceexplorer.model.dtos;

import javax.validation.constraints.NotBlank;

public class PlanetDto {

        @NotBlank
        private String planetName;
        @NotBlank
        private int width;
        @NotBlank
        private int height;
        private String newPlanetName;

        public PlanetDto() {
        }

        public PlanetDto(String planetName, int width, int height) {
                this.planetName = planetName;
                this.width = width;
                this.height = height;
        }

        public PlanetDto(String planetName, String newPlanetName) {
                this.planetName = planetName;
                this.newPlanetName = newPlanetName;
        }

        public String getPlanetName() {
                return planetName;
        }

        public int getWidth() {
                return width;
        }

        public int getHeight() {
                return height;
        }

        public String getNewPlanetName() {
                return newPlanetName;
        }
}
