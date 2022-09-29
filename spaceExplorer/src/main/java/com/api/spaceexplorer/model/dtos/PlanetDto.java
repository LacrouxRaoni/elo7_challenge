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
